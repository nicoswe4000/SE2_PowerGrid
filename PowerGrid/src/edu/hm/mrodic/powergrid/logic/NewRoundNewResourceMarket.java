package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;

/** Zug vom Typ TurnOver.
 * @author Rodic Mirna, mrodic@hm.edu
 * @version 2020-06-10
 */
class NewRoundNewResourceMarket implements HotMove {
    /** Dieses Spiel.*/
    private final OpenGame game;
    /**Attribute von diesem Zug.*/
    private Properties properties=new Properties();

    /** Privater Konstruktor.
     * Kein Prototyp.
     * @param game Dieses Spiel. Nicht null.
     */
    private NewRoundNewResourceMarket(OpenGame game){
        this.game=game;
        setProperty("type",getType().toString());
    }

    /**Öffentlicher Konstruktor.
     * Erzeugt einen Prototyp, setzt alle Attribute gleich null.
     */
    NewRoundNewResourceMarket(){
        this.game=null;
        setProperty("type",getType().toString());
    }
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(this.game);
        Optional<Problem>problems= Optional.empty();
        if(game.getPhase()!= Phase.Bureaucracy)
            problems=Optional.of(Problem.NotNow);
        else if(real){
            initializeResourceMarket();
            plantAccordingToLevel(game.getLevel());
            game.setRound(game.getRound()+1);
            game.setPhase(Phase.PlayerOrdering);
        }
        return problems;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame actualGame, Optional<OpenPlayer> actualPlayer) {
        if(this.game!=null)
            throw new IllegalStateException("This isn't a prototype.");
        Set<HotMove> runnableMoves=Set.of();
        final HotMove prototype=new NewRoundNewResourceMarket(Objects.requireNonNull(actualGame));
        if(prototype.test().isEmpty())
            runnableMoves=Set.of(prototype);
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.TurnOver;
    }

    /**Hilfsmethode für die run Methode.
     * Erneuert und füllt den Kraftstoffmarkt auf.
     */
    private void initializeResourceMarket(){
        final Map<Resource, List<List<Integer>>> resourceToAdd=game.getEdition().getResourcePlayersToSupply();
        final List<Resource>resources=new ArrayList<>(Arrays.asList(Resource.values()));
        for(Resource resource:resources) {
            int howMuch = resourceToAdd.get(resource).get(game.getPlayers().size()).get(game.getLevel());
            while (howMuch > 0 && game.getResourceMarket().getOpenSupply().remove(resource)) {
                game.getResourceMarket().getOpenAvailable().add(resource);
                howMuch--;
            }
        }
    }

    /**Hilfsmethode für die run Methode.
     *Erneuert den Kraftwerksmarkt, abhänigi von der Stufe des Spieles.
     * @param level Die Stufe des Spieles.
     */
    private void plantAccordingToLevel(int level){
        if(level<2){
            final OpenPlant plant=game.getPlantMarket().getOpenFuture().stream().max(Comparator.naturalOrder()).orElse(null);
            if(plant!=null) {
                game.getPlantMarket().getOpenFuture().remove(plant);
                game.getPlantMarket().getOpenHidden().add(game.getPlantMarket().getNumberHidden(), plant);
            }
        }
        else{
            final OpenPlant plant=game.getPlantMarket().getOpenActual().stream().min(Comparator.naturalOrder()).orElse(null);
            game.getPlantMarket().removePlant(plant.getNumber());
        }

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
