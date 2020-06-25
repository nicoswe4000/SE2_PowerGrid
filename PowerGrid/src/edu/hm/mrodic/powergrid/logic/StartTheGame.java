package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;

/**
 * Spiel starten.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-05-06
 */
class StartTheGame implements HotMove {
    /** Das momentan laufende Spiel.*/
    private final OpenGame game;
    /** Der Spieler, der diesen Zug ausfuehren soll.*/
    private final OpenPlayer player;
    /**Attribute von diesem Zug.*/
    private Properties properties=new Properties();

    /** Oeffentlicher Kontruktor des Zuges CommenceGame.
     * Erzeugt einen Prototypen (Game und Player sind null).
     */
    StartTheGame(){
        game=null;
        player=null;
        setProperty("type",getType().toString());
    }

    /**
     * Privater Konstruktor des Zuges CommenceGame.
     * Erzeugt den echten Zug, kein prototyp.
     * @param givenGame Das momentan laufende Spiel. Nicht null.
     * @param givenPlayer Der Spieler, der den Zug ausfuehren soll. Nicht null.
     */
    private StartTheGame(OpenGame givenGame, OpenPlayer givenPlayer){
        this.game=givenGame;
        this.player=givenPlayer;
        setProperty("type",getType().toString());
        setProperty("player",player.getColor());
    }
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        final Optional<Problem> problem=allConditionsFit();
        if(real && problem.isEmpty()){
            game.setLevel(0);
            game.setRound(1);
            game.setPhase(Phase.PlayerOrdering);
            initializePlantMarket(game.getPlantMarket());
            game.getBoard().closeRegions(game.getPlayers().size());
            game.getOpenPlayers().forEach(openPlayer->openPlayer.setElectro(game.getEdition().getInitialElectro()));
            game.getBoard().close();
        }
        return problem;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame givenGame, Optional<OpenPlayer> openPlayer) {
        if(this.game!=null)
            throw new IllegalStateException("This isnt a prototype");
        Set<HotMove> runnableMoves= Set.of();
        if(openPlayer.isPresent()) {
            final HotMove prototype = new StartTheGame(Objects.requireNonNull(givenGame), openPlayer.get());
            if (prototype.test().isEmpty())
                runnableMoves = Set.of(prototype);
        }
        return runnableMoves;

    }

    @Override
    public MoveType getType() {
        return MoveType.CommenceGame;
    }

    /**Hilfsmethode zum Initialisieren vom Kraftwerksmarkt.
     * Initialisiert den Kraftwerksmarkt nach den vorgegebenen Regeln.
     * @param market Der Kraftwerksmarkt (der Stapel, den man vorbereiten/initialisieren soll).
     */
    private void initializePlantMarket(OpenPlantMarket market){
        final List<Integer> numbers=List.of(3,4,5,6,7,8,9,10,13);
        final int numberSeperator=4;
        numbers.stream().limit(numberSeperator).map(market::findPlant).forEach(plant->{market.getOpenActual().add(plant);
                                        market.getOpenHidden().remove(plant);});
        numbers.stream().skip(numberSeperator).limit(numberSeperator).map(market::findPlant).forEach(plant->{market.getOpenFuture().add(plant);
                                        market.getOpenHidden().remove(plant);});
        final OpenPlant thirteen=market.findPlant(numbers.get(numbers.size()-1));
        market.getOpenHidden().remove(thirteen);
        RandomSource.make().shufflePlants(market.getOpenHidden());
        final List<OpenPlant> copyOfPlants=new ArrayList<>(market.getOpenHidden());
        copyOfPlants.stream()
                .limit(game.getEdition().getPlayersPlantsInitiallyRemoved().get(game.getPlayers().size()))
                .forEach(plant->market.getOpenHidden().remove(plant));
        market.getOpenHidden().add(0,thirteen);
        market.getOpenHidden().add(market.getNumberHidden(),game.getFactory().newPlant(0, Plant.Type.Level3,0,1));

    }

    @Override
    public boolean hasPriority() {
        boolean prio=getType().hasPriority();
        if(game!=null && game.getPlayers().size()==getGame().getEdition().getPlayersMaximum())
            prio=true;
        return prio;
    }

    /**Hilfsmethode für die run Methode.
     * Prüft ob alle Bedingungen erfüllt sind.
     * @return Optional mit Problem, eventuell leer.
     */
    private Optional<Problem> allConditionsFit(){
        Optional<Problem> problem =Optional.empty();
        if(game.getPhase()!= Phase.Opening)
            problem=Optional.of(Problem.NotNow);
        else if(!game.getOpenPlayers().contains(player))
            problem=Optional.of(Problem.NotYourTurn);
        else if(game.getPlayers().size()<game.getEdition().getPlayersMinimum())
            problem=Optional.of(Problem.TooFewPlayers);
        return problem;
    }

    @Override
    public void setProperty(String name, String value) {
        properties.setProperty(Objects.requireNonNull(name),Objects.requireNonNull(value));
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public String toString(){
        String returnString="Prototype: "+getType().toString();
        if(game!=null)
            returnString=asText();
        return returnString;
    }
}
