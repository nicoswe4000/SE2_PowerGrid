package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;

/**Zug vom Typ UpdatePlantMarket.
 * @author Rodic Mirna, mrodic@hm.edu
 * @version 2020-06-10
 */
class RestorePlantMarket implements HotMove {
    /** Dieses Spiel.*/
    private final OpenGame game;
    /** Zustand des Stapels der Kraftwerke. Leer=true.*/
    private  boolean isHiddenEmpty;
    /**Attribute von diesem Zug.*/
    private Properties properties=new Properties();

    /**Privater Konstruktor.
     * Kein Prototyp.
     * @param game Dieses Spiel. Nicht null.
     */
    private RestorePlantMarket(OpenGame game){
        this.game=game;
        isHiddenEmpty=game.getPlantMarket().getNumberHidden()==0;
        setProperty("type",getType().toString());
    }

    /** Öffentlicher Konstruktor.
     * Erzeugt ein Prototyp, setzt alle Attribute gleich null.
     */
    RestorePlantMarket(){
        this.game=null;
        setProperty("type",getType().toString());
    }
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        final Optional<Problem> problems=allConditionsFit();
        if(real&&problems.isEmpty()){
            final Set<OpenPlant> actualAndFuture=new TreeSet<>();
            actualAndFuture.addAll(game.getPlantMarket().getOpenFuture());
            actualAndFuture.addAll(game.getPlantMarket().getOpenActual());
            game.getPlantMarket().getOpenFuture().clear();
            game.getPlantMarket().getOpenActual().clear();
            if(!isHiddenEmpty) {
                actualAndFuture.add(game.getPlantMarket().getOpenHidden().get(0));
                game.getPlantMarket().getOpenHidden().remove(0);
            }
            actualAndFuture.stream()
                    .limit(game.getEdition().getActualPlants(game.getLevel()))
                    .forEach(plant->game.getPlantMarket().getOpenActual().add(plant));
            actualAndFuture.stream().skip(game.getEdition().getActualPlants(game.getLevel()))
                    //.limit(game.getEdition().getFuturePlants((game.getLevel()-1)))
                    .forEach(plant->game.getPlantMarket().getOpenFuture().add(plant));

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
        final HotMove prototype=new RestorePlantMarket(Objects.requireNonNull(actualGame));
        if(prototype.test().isEmpty())
            runnableMoves=Set.of(prototype);
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.UpdatePlantMarket;
    }

    /**Hilfsmethode für die allConditionsFit Methode.
     * Prüft ob Kraftwerke in dem aktuellen und/oder zukünftigem Markt fehlen.
     * @return true, wenn Kraftwerke fehlen, sonst false.
     */
    private boolean someMissing(){
        final Edition edition=game.getEdition();
        return game.getPlantMarket().getOpenActual().size()<edition.getActualPlants(game.getLevel())
                || game.getPlantMarket().getOpenFuture().size()<edition.getFuturePlants(game.getLevel());
    }

    /** Hilfsmethode für die run Methode.
     * Prüft ob alle Bedingungen erfüllt sind.
     * @return Optional mit Problem, eventuell leer.
     */
    private Optional<Problem> allConditionsFit(){
        Optional<Problem> problems=Optional.empty();
        final List<Phase> phases=List.of(Phase.PlantBuying,Phase.Building,Phase.Bureaucracy);
        final boolean noneMatch = phases.stream().noneMatch(phase->phase==game.getPhase());
        if(noneMatch)
            problems=Optional.of(Problem.NotNow);
        else if(!someMissing())
            problems=Optional.of(Problem.PlantMarketFine);
        else if(isHiddenEmpty && game.getPlantMarket().getOpenFuture().size()==0)
            problems=Optional.of(Problem.NoPlants);
        return problems;
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
