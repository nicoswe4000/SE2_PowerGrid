package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;


/**
 * Sortierung der Spieler.
 * @author Nicolas Schweinhuber, IF2B, schweinh@edu.edu.hm.edu
 * @version 2020-04-16
 */

class OrderThePlayers implements HotMove {

    /** Das momentan laufende Spiel.*/
    private final OpenGame game;
    /** Properties dieses Zuges.*/
    private Properties orderThePlayersProperties = new Properties();

    /** Oeffentlicher Konstruktor des Zuges OrderPlayers.
     * Erzeugt einen Prototypen (Game ist null).
     */
    OrderThePlayers(){
        this.game = null;
        setProperty("type", this.getType().toString());
    }

    /**
     * Privater Konstruktor des Zuges OrderPlayers.
     * Erzeugt den echten Zug, kein prototyp.
     * @param game Das momentan laufende Spiel. Nicht null.
     */
    private OrderThePlayers(OpenGame game){
        this.game = game;
        setProperty("type", this.getType().toString());
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problem = Optional.empty();
        if(game.getPhase() != Phase.PlayerOrdering)
            problem = Optional.of(Problem.NotNow);
        else if(real) {
            game.getOpenPlayers().forEach(player->player.setPassed(false));
            if(game.getRound() == 1){
                RandomSource.make().shufflePlayers(game.getOpenPlayers()); // in Runde 1 hat noch keiner ein Kraftwerk, etc. ,danach schon
            }
            else
                game.getOpenPlayers().sort(Comparator.naturalOrder());
            game.setPhase(Phase.PlantBuying);
        }
        return problem;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame actualGame, Optional<OpenPlayer> actualPlayer) {
        if(this.game != null)
            throw new IllegalStateException("This is not a Prototype!");
        Set<HotMove> runnableMoves = Set.of();
        final HotMove prototype = new OrderThePlayers(Objects.requireNonNull(actualGame));
        if(actualPlayer.isEmpty()&&prototype.test().isEmpty())
            runnableMoves = Set.of(prototype);

        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.OrderPlayers;
    }

    @Override
    public void setProperty(String name, String value){
        orderThePlayersProperties.put(name, value);
    }

    @Override
    public Properties getProperties(){
        return orderThePlayersProperties;
    }

    @Override
    public String toString(){
        String returnString="Prototype: "+getType().toString();
        if(game!=null)
            returnString=asText();
        return returnString;
    }
}