package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;


/**
 * kein Kauf von Ressourcen.
 * @author Nicolas Schweinhuber, IF2B, schweinh@edu.edu.hm.edu
 * @version 2020-04-16
 */

class NoResourceTransaction implements HotMove {

    /** Das momentan laufende Spiel.*/
    private final OpenGame game;
    /** Der Spieler, der diesen Zug ausfuehren soll.*/
    private final OpenPlayer player;
    /** Properties dieses Zuges.*/
    private Properties properties = new Properties();

    /** Oeffentlicher Konstruktor des Zuges BuyNoResource.
     * Erzeugt einen Prototypen (Game und Player sind null).
     */
    NoResourceTransaction(){
        this.game = null;
        this.player = null;
        setProperty("type", this.getType().toString());
    }

    /**
     * Privater Konstruktor des Zuges BuyNoResource.
     * Erzeugt den echten Zug, kein prototyp.
     * @param game Das momentan laufende Spiel. Nicht null.
     * @param player Der Spieler, der den Zug ausfuehren soll. Nicht null.
     */
    private NoResourceTransaction(OpenGame game, OpenPlayer player){
        this.game = Objects.requireNonNull(game);
        this.player = Objects.requireNonNull(player);
        setProperty("type", this.getType().toString());
        setProperty("player", player.getColor());
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problem = Optional.empty();
        final Player lastPlayer=game.getPlayers().stream().filter(sut->!sut.hasPassed()).max(Comparable::compareTo).orElse(null);
        if(game.getPhase() != Phase.ResourceBuying)
            problem = Optional.of(Problem.NotNow);
        else if(lastPlayer!=player)
            problem = Optional.of(Problem.NotYourTurn);
        else if(real){
            player.setPassed(true);
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
            throw new IllegalStateException("This isnt a Prototype!");
        Set<HotMove> runnableMoves = Set.of();
        if(actualPlayer.isPresent()) {
            final HotMove prototype = new NoResourceTransaction(Objects.requireNonNull(actualGame), actualPlayer.get());
            if (prototype.test().isEmpty())
                runnableMoves = Set.of(prototype);
        }
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.BuyNoResource;
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