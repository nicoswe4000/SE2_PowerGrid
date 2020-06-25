package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

/**
 * Hinzufuegen eines neuen Spielers.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-16
 */
class JoinNewPlayer implements HotMove{
    /** Momentan laufendes Spiel.*/
    private final OpenGame game;
    /**Attribute dieses Spiels.*/
    private Properties properties=new Properties();

    /**
     * Oeffentlicher Konstruktor des Zuges JoinPlayer.
     * Erzeugt einen Prototyp (game=null).
     */
    JoinNewPlayer(){
        this.game=null;
        setProperty("type",getType().toString());
    }

    /**
     * Privater Konstruktor des Zuges JoinPlayer.
     * Erzeugt den echten Zug, kein Prototyp.
     * @param game Das Spiel, das gerade laeuft. Nicht null.
     */
    private JoinNewPlayer(OpenGame game){
        this.game=game;
        setProperty("type",getType().toString());
    }
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problem=Optional.empty();
        if(game.getPhase()!=Phase.Opening)
            problem=Optional.of(Problem.NotNow);
        else if(game.getPlayers().size()>=game.getEdition().getPlayersMaximum())
            problem=Optional.of(Problem.MaxPlayers);
        else if(real){
            final String color=game.getEdition().getPlayerColors().get(game.getPlayers().size());
            final String secret= RandomSource.make().babbled(color);
            final OpenPlayer player=game.getFactory().newPlayer(secret,color);
            game.getOpenPlayers().add(player);
        }
        return problem;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame givenGame, Optional<OpenPlayer> player) {
        //ausfiltern wenn secret nicht leer ist!!!
        if(this.game!=null)
            throw new IllegalStateException("This is not a prototype.");
        Set<HotMove> runnableMoves=Set.of();
        final HotMove prototype=new JoinNewPlayer(Objects.requireNonNull(givenGame));
        if(player.isEmpty() && prototype.test().isEmpty())
            runnableMoves = Set.of(prototype);

        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.JoinPlayer;
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
