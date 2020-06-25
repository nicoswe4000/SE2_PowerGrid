
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.hm.cs.rs.powergrid.logic.MoveType.CommenceGame;
import static edu.hm.cs.rs.powergrid.logic.MoveType.JoinPlayer;
import static org.junit.Assert.*;

/**
 * Erste Tests fuer eine Rules-Implementierung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-23
 */
public class Smoke7Test {
    /** Spielstand. */
    private final Game game;

    /** Spielregeln. */
    private final Rules sut;

    /** Fluchtwert fuer kein Geheimnis. */
    private final String NO_SECRET = "";

    /** Fuehrt ein Runnable mehrmals aus. */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public Smoke7Test() {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.mrodic.powergrid.datastore.MRodicFactory");
        System.setProperty("powergrid.rules", "edu.hm.mrodic.powergrid.logic.MRodicRules");
        // Stellt sicher, dass keine zufaelligen Daten vorkommen
        System.setProperty("powergrid.randomsource", "edu.hm.cs.rs.powergrid.logic.SortingRandomSource");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    @Test public void checkPrototypes() {
        Set<MoveType> types = sut.getPrototypes().stream().map(Move::getType).collect(Collectors.toSet());
        assertTrue("Prototypen enthalten JoinPlayer", types.contains(JoinPlayer));
        assertTrue("Prototypen enthalten CommenceGame", types.contains(CommenceGame));
    }

    @Test public void prototypesDontTest() {
        for(Move move: sut.getPrototypes())
            try {
                ((HotMove)move).test();
                fail("Prototype akzeptiert test-Aufruf: " + move);
            } catch(RuntimeException e) {
                // required
            }
    }

    @Test public void testGetGame() {
        assertSame("Rules liefern selbes Spiel", game, sut.getGame());
    }

    @Test public void testGetMovesInvalidSecret() {
        // arrange
        // act
        final Set<Move> have = sut.getMoves(Optional.of("invalid"));
        // assert
        System.out.println(have);
        assertTrue("falsches Geheimnis, keine Zuege", have.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class) public void testFireFakeMove() {
        // arrange
        // act
        sut.fire(Optional.of("invalid"), new Move() {
            @Override public MoveType getType() {
                return JoinPlayer;
            }

            @Override public Properties getProperties() {
                throw new UnsupportedOperationException();
            }

        });
    }

    @Test public void testJoinPlayer() {
        // arrange
        // act
        final Set<Move> have = sut.getMoves(Optional.empty());
        final Move move = have.iterator().next();
        // assert
        assertEquals("Ein moeglicher Zug", 1, have.size());
        assertSame("Zug ist Mitspielen", JoinPlayer, move.getType());
    }

    @Test public void testFireJoinPlayer() {
        // arrange
        // act
        final Move move = sut.getMoves(Optional.empty()).iterator().next();
        final Optional<Problem> problem = sut.fire(Optional.empty(), move);
        // assert
        assertTrue("Neuer Spieler akzeptiert", problem.isEmpty());
        assertEquals("Spieler ist als erster und bisher einziger dabei", 1, game.getPlayers().size());
        assertNotNull("Geheimnis des Spielers verfuegbar", game.getPlayers().get(0).getSecret());
        assertNull("Zweiter Abruf verweigert", game.getPlayers().get(0).getSecret());
    }

    @Test public void testCommenceGameTooFewPlayers() {
        // arrange
        times.accept(game.getEdition().getPlayersMinimum() - 1,
                () -> fireMove(JoinPlayer, NO_SECRET));
        // act
        final String secret1 = reapSecrets().get(0);
        Set<Move> moves = sut.getMoves(Optional.of(secret1));
        // assert
        System.out.println(moves);
        assertTrue("Ein Spieler zu wenig fuer den Spielstart", moves.isEmpty());
    }

    @Test public void testCommenceGameMinPlayers() {
        // arrange
        times.accept(game.getEdition().getPlayersMinimum(),
                () -> fireMove(JoinPlayer, NO_SECRET));
        final String secret1 = reapSecrets().get(0);
        // act
        final Set<Move> moves = sut.getMoves(Optional.of(secret1));
        final Move move = moves.iterator().next();
        // assert
        assertSame("Minimale Anzahl Spieler fuer den Start dabei", CommenceGame, move.getType());
    }

    @Test public void testJoinPlayerMaxPlayers() {
        // arrange
        times.accept(game.getEdition().getPlayersMaximum(),
                () -> fireMove(JoinPlayer, NO_SECRET));
        // act
        final Set<Move> moves = sut.getMoves(Optional.empty());
        // assert
        assertTrue("Maximale Anzahl Spieler dabei, keine weiteren erlaubt", moves.isEmpty());
    }

    @Test public void testCommenceGameMaxPlayers() {
        // arrange
        times.accept(game.getEdition().getPlayersMaximum(),
                () -> fireMove(JoinPlayer, NO_SECRET));
        final String secret1 = reapSecrets().get(0);
        // act
        final Set<Move> moves = sut.getMoves(Optional.of(secret1));
        final Move move = moves.iterator().next();
        // assert
        assertSame("Maximale Anzahl Spieler dabei; Spiel laeuft automatisch bis zur Kraftwerksversteigerung", MoveType.StartAuction, move.getType());
    }

    /**
     * Fuehrt einen Zug aus, der gelingen muss.
     * @param type        Typ des Zuges.
     * @param secretOrNot Geheimnis oder Leerstring, wenn keines noetig ist.
     */
    private void fireMove(MoveType type, String secretOrNot) {
        final Optional<String> secret = secretOrNot == NO_SECRET? Optional.empty(): Optional.of(secretOrNot);
        final Optional<Move> move = sut.getMoves(secret)
                .stream()
                .filter(amove -> amove.getType() == type)
                .findAny();
        assertTrue(move.isPresent());
        assertTrue(sut.fire(secret, move.get()).isEmpty());
    }

    /**
     * Sammelt die Geheimnisse aller Spieler ein.
     * @return Liste mit den Geheimnissen der Spieler.
     */
    private List<String> reapSecrets() {
        return game.getPlayers()
                .stream()
                .map(Player::getSecret)
                .collect(Collectors.toList());
    }

}