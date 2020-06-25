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

/**Zug vom Typ EndResourceBuying.
 * @author Rodic Mirna, mrodic@hm.edu
 * @version 2020-06-10
 */
class CloseResourceMarket implements HotMove {
    /** Dieses Spiel.*/
    private final OpenGame game;
    /**Attribute vom diesem Zug.*/
    private Properties properties=new Properties();

    /** Privater Konstruktor.
     * Kein Prototyp.
     * @param game Dieses Spiel. Nicht null.
     */
    private CloseResourceMarket(OpenGame game){
        this.game=game;
        setProperty("type",getType().toString());
    }

    /** Öffentlicher Konstruktor.
     * Erzeugt einen Protoypen, setzt alle Attribute gleich null.
     */
    CloseResourceMarket(){
        this.game=null;
        setProperty("type",getType().toString());
    }
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problems=Optional.empty();
        final boolean noneRemaining=game.getPlayers().stream().allMatch(Player::hasPassed);
        if(game.getPhase()!= Phase.ResourceBuying)
            problems=Optional.of(Problem.NotNow);
        else if(!noneRemaining)
            problems=Optional.of(Problem.PlayersRemaining);
        else if(real){
            game.setPhase(Phase.Building);
            game.getOpenPlayers().forEach(player->player.setPassed(false));
        }
        return problems;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame actualGame, Optional<OpenPlayer> player) {
        if(this.game!=null)
            throw new IllegalStateException("This isn't a prototype");
        Set<HotMove> runnableMoves=Set.of();
        final HotMove prototype=new CloseResourceMarket(Objects.requireNonNull(actualGame));
        if(prototype.test().isEmpty())
            runnableMoves=Set.of(prototype);
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.EndResourceBuying;
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
