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

/**Zug vom Typ ConnectNoCity.
 * @author Rodic Mirna, mrodic@hm.edu
 * @version 2020-06-10
 */
class SkipConnectingCity implements HotMove {
    /** Spieler, der den Zug ausführen möchte.*/
    private final OpenPlayer player;
    /**Dieses Spiel.*/
    private final OpenGame game;
    /**Attribute von diesem Zug.*/
    private Properties properties=new Properties();

    /** Privater Konstruktor.
     * Kein Prototyp.
     * @param game Dieses Spiel. Nicht null.
     * @param player Der Spieler. Nicht null.
     */
    private SkipConnectingCity(OpenGame game, OpenPlayer player){
        this.game=game;
        this.player=player;
        setProperty("type",getType().toString());
        setProperty("player",player.getColor());
    }

    /**Öffentlicher Konstruktor.
     * Erzeugt einen Prototyp, setzt alle Attribute gleich null.
     */
    SkipConnectingCity(){
        this.game=null;
        this.player=null;
        setProperty("type",getType().toString());
    }
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problems=Optional.empty();
        final Player lastPlayer=game.getPlayers().stream().filter(sut->!sut.hasPassed()).max(Comparable::compareTo).orElse(null);
        if(game.getPhase()!= Phase.Building)
            problems=Optional.of(Problem.NotNow);
        else if(lastPlayer!=this.player)
            problems=Optional.of(Problem.NotYourTurn);
        else if(real )
            player.setPassed(true);
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
        if(actualPlayer.isPresent()){
            final HotMove prototype=new SkipConnectingCity(Objects.requireNonNull(actualGame),actualPlayer.get());
            if(prototype.test().isEmpty())
                runnableMoves=Set.of(prototype);
        }
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.ConnectNoCity;
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
