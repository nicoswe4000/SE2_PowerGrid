import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.cs.rs.powergrid.logic.*;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

/**Test zur Veraenderung durch Aufgabe 6 neben den Addit-Tests.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-15-05
 */
public class OrderPlayersTest extends AbstractFirstRoundTest {
    //Grundkonstruktion aus dem Smoke7Test von R. Schiedermeier uebernommen
    /** Spielstand. */
    private final Game game;

    /** Spielstand. */
    private final OpenGame openGame;

    /** Spielregeln. */
    private final Rules rules;

    /** Fluchtwert fuer kein Geheimnis. */
    private final String NO_SECRET = "";

    /**Maximale Spielerzahl der Edition*/
    private final int maxPlayers;

    /**Minimale Spilerzahl der Edition*/
    private final int minPlayers;

    private final List<OpenPlayer> players;

    private final OpenPlant openPlant;

    private final OpenCity openCity;

    /** Fuehrt ein Runnable mehrmals aus. */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public OrderPlayersTest() {
        System.setProperty("powergrid.factory", "edu.hm.mrodic.powergrid.datastore.MRodicFactory");
        System.setProperty("powergrid.rules", "edu.hm.mrodic.powergrid.logic.GameRules");
        // Stellt sicher, dass keine zufaelligen Daten vorkommen
        System.setProperty("powergrid.randomsource", "edu.hm.cs.rs.powergrid.logic.SortingRandomSource");
        openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        openPlant=openGame.getPlantMarket().getOpenHidden().stream().findAny().orElse(null);
        openCity=openGame.getBoard().getOpenCities().stream().findAny().get();
        rules = Rules.newRules(openGame);
        game = openGame;
        //Zu Beginn nur Spielerhinzufuegen moeglich
        Set<MoveType> types = rules.getPrototypes().stream().map(Move::getType).collect(Collectors.toSet());
        maxPlayers=game.getEdition().getPlayersMaximum();
        minPlayers=game.getEdition().getPlayersMinimum();
        //fuegt 2 Spieler ins Spiel ein
        times.accept(minPlayers,()->fireMove(NO_SECRET,MoveType.JoinPlayer));
        players=openGame.getOpenPlayers();
    }

    //eigene Tests ab hier:

    //Testparameter ueberpruefen
    @Test public void basic(){
        Assert.assertSame(Phase.Opening,game.getPhase());
        Assert.assertEquals(minPlayers,players.size());
        Assert.assertTrue(MoveType.OrderPlayers.isAutoFire());
        Assert.assertFalse(MoveType.OrderPlayers.hasPriority());
    }

    @Test public void wrongPhase(){
        HotMove hotMove=getHotMoveGame(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers,Optional.empty());
        openGame.setPhase(Phase.Opening);
        Optional<Problem> problem=hotMove.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotNow,problem.get());
    }

    @Test public void orderFirstRound(){
        List<OpenPlayer> comparePlayers=new ArrayList<>();
        comparePlayers.addAll(players);
        RandomSource.make().shufflePlayers(comparePlayers);
        //orderPlayers startet dannach automatisch
        fireMove(players.stream().findAny().get().getSecret(),MoveType.CommenceGame);
        Assert.assertEquals(comparePlayers,game.getPlayers());
    }

    @Test public void orderCities(){
        final List<OpenPlayer> buildPlayers=players;
        RandomSource.make().shufflePlayers(buildPlayers);
        OpenPlayer most=players.get(1);
        most.getOpenCities().add(openCity);
        final List<OpenPlayer> comparePlayers=List.of(buildPlayers.get(1),buildPlayers.get(0));
        openGame.setRound(2);
        HotMove order=getHotMoveGame(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers,Optional.empty());
        Optional<Problem> problem=order.fire();
        Assert.assertTrue(problem.isEmpty());
    }

    @Ignore
    @Test public void orderCities2(){
        final List<OpenPlayer> buildPlayers=new ArrayList<>();
        buildPlayers.addAll(players);
        OpenPlayer most=players.get(1);
        most.getOpenCities().add(openCity);
        final List<OpenPlayer> comparePlayers=List.of(buildPlayers.get(1),buildPlayers.get(0));
        Assert.assertNotEquals(comparePlayers,game.getPlayers());
    }


    @Test public void changeOfPhase(){
        fireMove(players.stream().findAny().get().getSecret(),MoveType.CommenceGame);
        //Phase PlayerOrdering wird Automatisch nach CommanceGame Ausgefuehrt und von PlantBuying Abgeloest
        Assert.assertSame(Phase.PlantBuying,game.getPhase());
    }

    @Test public void itsAllPlayersTurnAgain(){
        players.stream().forEach(player->player.setPassed(true));
        players.stream().forEach(player->Assert.assertTrue(player.hasPassed()));
        fireMove(players.stream().findAny().get().getSecret(),MoveType.CommenceGame);
        players.stream().forEach(player->Assert.assertFalse(player.hasPassed()));
    }

    @Test public void getGame(){
       getGameTest(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers,Optional.empty());
    }

    @Test public void toStringT(){
        toStringTestHotMove(getHotMoveGame(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers,Optional.empty()));
    }

    @Test public void toStringPrototype(){
        toStringTestPrototype(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers);
    }

    @Test public void getProperties(){
        getPropertiesTest(getHotMoveGame(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers,Optional.empty()));
    }

    @Test public void setProperties(){
        setPropertiesTest(getHotMoveGame(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers,Optional.empty()));
    }

    @Test (expected = IllegalStateException.class) public void collectMove(){
        collectWithNoPrototypeTest(Phase.PlayerOrdering,openGame,rules,MoveType.OrderPlayers,Optional.empty());
    }








//Hilfsmethode von R.Schiedermeier: (mit Return)
    /**
     * Fuehrt einen Zug aus, der gelingen muss.
     * @param type        Typ des Zuges.
     * @param secretOrNot Geheimnis oder Leerstring, wenn keines noetig ist.
     */
    private Optional<Problem> fireMove(String secretOrNot,MoveType type) {
        final Optional<String> secret = secretOrNot == NO_SECRET? Optional.empty(): Optional.of(secretOrNot);
        final Optional<Move> move = rules.getMoves(secret)
                .stream()
                .filter(amove -> amove.getType() == type)
                .findAny();
        assertTrue(move.isPresent());
        return rules.fire(secret, move.get());
    }






}