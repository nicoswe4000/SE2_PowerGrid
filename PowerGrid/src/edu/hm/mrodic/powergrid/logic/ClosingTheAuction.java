package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

/**
 * Schließung einer Auktion.
 * @author Nicolas Schweinhuber, IF2B, schweinh@edu.edu.hm.edu
 * @version 2020-04-16
 */

class ClosingTheAuction implements HotMove {

    /** Das momentan laufende Spiel.*/
    private final OpenGame game;
    /** Properties dieses Zuges.*/
    private Properties properties = new Properties();

    /** Oeffentlicher Konstruktor des Zuges CloseAuction.
     * Erzeugt einen Prototypen (Game ist null).
     */
    ClosingTheAuction(){
        this.game = null;
        setProperty("type", this.getType().toString());

    }

    /**
     * Privater Konstruktor des Zuges CloseAuction.
     * Erzeugt den echten Zug, kein prototyp.
     * @param game Das momentan laufende Spiel. Nicht null.
     */
    private ClosingTheAuction(OpenGame game){
        this.game = Objects.requireNonNull(game);
        setProperty("type", this.getType().toString());

    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problem = Optional.empty();
        if(game.getPhase() != Phase.PlantAuction)
            problem = Optional.of(Problem.NotNow);
        else if(game.getAuction().getOpenPlayers().size() > 1)
            problem = Optional.of(Problem.AuctionRunning);
        else if(real){
            final OpenPlant plant = game.getAuction().getPlant();
            final OpenPlayer player = game.getAuction().getPlayer();
            game.getPlantMarket().removePlant(plant.getNumber());
            player.getOpenPlants().add(plant);
            player.setElectro(player.getElectro() - game.getAuction().getAmount());
            player.setPassed(true);
            game.setPhase(Phase.PlantBuying);
        }
        return problem;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame actualGame, Optional<OpenPlayer> player) {
        if(this.game != null)
            throw new IllegalStateException("This isnt a Prototype!");
        Set<HotMove> runnableMoves = Set.of();
        final HotMove prototype = new ClosingTheAuction(Objects.requireNonNull(actualGame));
        if(player.isEmpty() && prototype.test().isEmpty())
            runnableMoves = Set.of(prototype);

        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.CloseAuction;
    }

    @Override
    public void setProperty(String name, String value){
        properties.put(name, value);
    }

    @Override
    public Properties getProperties(){
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