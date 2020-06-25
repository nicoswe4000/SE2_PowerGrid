import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import static org.junit.Assert.*;

import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.RandomSource;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Spielt eine minimale erste Runde komplett durch.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-15
 */
public class Round1Test {
    /** Initialisiert Factory und Spielregeln.
     */ {
        // TODO: Fuegen Sie hier Ihre eigenen FQCNs ein.
        System.setProperty("powergrid.factory", "edu.hm.mrodic.powergrid.datastore.MRodicFactory");
        System.setProperty("powergrid.rules", "edu.hm.mrodic.powergrid.logic.GameRules");
        // Schliesst den Zufall aus
        System.setProperty("powergrid.randomsource", "edu.hm.cs.rs.powergrid.logic.FunnyRandomSource");
    }

    private final OpenFactory factory = OpenFactory.newFactory();

    private final OpenGame game = factory.newGame(new EditionGermany() {
        @Override public List<String> getPlayerColors() {
            return List.of("RED", "GREEN", "BLUE", "YELLOW", "BLACK", "PURPLE");
        }
    });

    private final Rules sut = Rules.newRules(game);

    /** Fluchtwert fuer kein Geheimnis. */
    private final String NO_SECRET = "";

    /** Liste der Geheimnisse aller Spieler. */
    private final List<String> secrets = game.getEdition().getPlayerColors().stream()
            .map(RandomSource.make()::babbled)
            .collect(Collectors.toList());

    @Test public void initialGame() {
        assertSame("Vor dem ersten Spieler", Phase.Opening, game.getPhase());
    }

    @Test public void joinPlayer1() {
        fireMove(MoveType.JoinPlayer, NO_SECRET);
        assertSame(Phase.Opening, game.getPhase());
    }

    @Test public void joinPlayer2() {
        joinPlayer1();
        fireMove(MoveType.JoinPlayer, NO_SECRET);
        assertSame(Phase.Opening, game.getPhase());
    }

    @Test public void commenceGame() {
        joinPlayer2();
        fireMove(MoveType.CommenceGame, secrets.get(1));
        assertSame("Spiel gestartet, laeuft bis zur Kraftwerksauktion", Phase.PlantBuying, game.getPhase());
    }

    @Test public void openAuction3Green() {
        commenceGame();
        fireMove(MoveType.StartAuction, secrets.get(1), "plant", "3");
        assertSame("RED steigt aus, kann neue Auktion starten", Phase.PlantBuying, game.getPhase());
        assertEquals("Spieler GREEN ist Erster",
                "GREEN", game.getPlayers().get(0).getColor());
        assertEquals("Spieler RED ist Zweiter",
                "RED", game.getPlayers().get(1).getColor());
    }

    @Test public void openAuction4Red() {
        openAuction3Green();
        fireMove(MoveType.StartAuction, secrets.get(0), "plant", "5");
        assertSame("Spieler 2 kauft Kraftwerk, Rest der Runde laeuft automatisch bis zur naechsten Kraftwerksversteigerung",
                Phase.PlantBuying, game.getPhase());
        assertArrayEquals("Kraftwerke 3 und 5 verkauft",
                new int[] {4, 6, 7, 8}, game.getPlantMarket().getActual().stream().mapToInt(Plant::getNumber).toArray());
        assertArrayEquals("Kraftwerke 3 und 5 verkauft",
                new int[] {9, 10, 13, 32}, game.getPlantMarket().getFuture().stream().mapToInt(Plant::getNumber).toArray());
        assertEquals("Spieler 2 kauft Kraftwerk, Rest der Runde laeuft automatisch durch",
                2, game.getRound());
        assertEquals("Spieler GREEN hat Grundeinkommen erhalten",
                55, game.getPlayers().get(0).getElectro());
        assertEquals("Spieler RED hat Grundeinkommen erhalten",
                57, game.getPlayers().get(1).getElectro());
        assertEquals("Spieler RED jetzt Erster",
                "RED", game.getPlayers().get(0).getColor());
        assertEquals("Spieler GREEN jetzt Zweiter",
                "GREEN", game.getPlayers().get(1).getColor());
    }

    /**
     * Fuehrt einen Zug aus, der gelingen muss.
     * @param type          Typ des Zuges.
     * @param secretOrNot   Geheimnis oder Leerstring, wenn keines noetig ist.
     * @param propertyValue Name und Wert einer Properties des gewuenschten Zuges.
     */
    private void fireMove(MoveType type, String secretOrNot, String... propertyValue) {
        final Optional<String> secret = secretOrNot == NO_SECRET?
                Optional.empty():
                Optional.of(secretOrNot);
        final Optional<Move> move = sut.getMoves(secret)
                .stream()
                .filter(amove -> amove.getType() == type)
                .filter(amove -> propertyValue.length == 0 ||
                        amove.getProperty(propertyValue[0]).equals(propertyValue[1]))
                .findAny();
        assertTrue(move.isPresent());
        assertTrue(sut.fire(secret, move.get()).isEmpty());
    }
}
