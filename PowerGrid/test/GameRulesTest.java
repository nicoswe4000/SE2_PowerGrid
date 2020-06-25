
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.EditionUSA;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static edu.hm.cs.rs.powergrid.logic.MoveType.*;


/**
 * Test der Rules Implementierung
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version last modified 2020-06-01
 */
public class GameRulesTest {
    //Grundkonstruktion aus dem Smoke7Test von R. Schiedermeier uebernommen
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
    public GameRulesTest() {
        System.setProperty("powergrid.factory", "edu.hm.mrodic.powergrid.datastore.MRodicFactory");
        System.setProperty("powergrid.rules", "edu.hm.mrodic.powergrid.logic.GameRules");
        // Stellt sicher, dass keine zufaelligen Daten vorkommen
        System.setProperty("powergrid.randomsource", "edu.hm.cs.rs.powergrid.logic.SortingRandomSource");
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        sut = Rules.newRules(openGame);
        game = openGame;
    }

    //eigene Tests ab hier:

    //Konstruktion
    @Test public void constructor(){
        Assert.assertSame(game,sut.getGame());
    }

    //Test zu get Prototypes
    @Test public void getPrototypes(){
        //Aktuell nur 2 Zuege implementiert muss bei den Folgenden Aufgaben angepasst werden
        Assert.assertEquals(15,sut.getPrototypes().size());
    }

    @Test public void getPrototypesByName(){
        Set<MoveType> prototypes = sut.getPrototypes().stream().map(Move::getType).collect(Collectors.toSet());
        //Aktuell nur 2 Zuege implementiert muss bei den Folgenden Aufgaben angepasst werden
        Set<MoveType> actuelTypes=Set.of(JoinPlayer,CommenceGame,BuyNoResource,ConnectNoCity,StartAuction,EndResourceBuying,EndBuilding,CloseAuction,LeaveAuction,UpdatePlantMarket,OperateNoPlant,SupplyElectricity,OrderPlayers,EndAuctions,TurnOver);
        Assert.assertEquals(actuelTypes,prototypes);
    }

    //Tests zu getMoves

    @Test public void getMovesStart(){
        //noch kein Speiler vorhanden
        Optional<String> secret=Optional.empty();
        Set<MoveType> possibleMoves = sut.getMoves(secret).stream().map(Move::getType).collect(Collectors.toSet());
        Assert.assertEquals(1,possibleMoves.size());
    }

    @Test public void getMovesWrongSecret(){
        //noch kein Speiler vorhanden
        Optional<String> secret=Optional.of("Hallo");
        Set<MoveType> possibleMoves = sut.getMoves(secret).stream().map(Move::getType).collect(Collectors.toSet());
        Assert.assertTrue(possibleMoves.isEmpty());
    }


//TODO fehlt Test Kein Move fuer einen Spieler

    @Test public void getMovesPlayerCommenceGame(){
        //einen Spieler hinzufuegen
        final Optional<String> secret=Optional.empty();
        Set<Move> possibleMoves=sut.getMoves(secret);
        Move myMove=possibleMoves.stream().findAny().get();
        //Muss einer enthalten sein
        times.accept(2,()->sut.fire(secret,possibleMoves.stream().findAny().get()));
        final Optional<String> secretPlayer=Optional.ofNullable(game.getPlayers().stream().findFirst().get().getSecret());
        Set<MoveType>possibleTypes = sut.getMoves(secretPlayer).stream().map(Move::getType).collect(Collectors.toSet());
        //muss in den naechsten Schritten eventuell angepasst werden
        Set<MoveType> actuelTypes=Set.of(CommenceGame);
        Assert.assertEquals(actuelTypes,possibleTypes);
    }
    //Tests zu
    @Test public void returnFireSecretEmpty(){
        Set<Move> possibleMoves=sut.getMoves(Optional.empty());
        Move myMove=possibleMoves.stream().findAny().get();
        Move newPlayer=possibleMoves.stream().findAny().get();
        times.accept(game.getEdition().getPlayersMaximum(),()->sut.fire(Optional.empty(),newPlayer));
        Assert.assertEquals(game.getEdition().getPlayersMaximum(),game.getPlayers().size());
    }


    @Test (expected = IllegalArgumentException.class)
    public void fireNoMove(){
        sut.fire(Optional.empty(), new Move() {
            @Override
            public MoveType getType() {
                return CommenceGame;
            }
        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void fireNullMove(){
        sut.fire(Optional.empty(),null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void fireWrongGame(){
        final OpenGame openGame = OpenFactory.newFactory().newGame(new EditionUSA());
        Rules sut2 = Rules.newRules(openGame);
        Game game2 = openGame;
        Move move=sut2.getMoves(Optional.empty()).stream().findAny().get();
        sut.fire(Optional.empty(),move);
    }

    @Test public void returnProblems(){
        Set<Move> possibleMoves=sut.getMoves(Optional.empty());
        Move newPlayer=possibleMoves.stream().findAny().get();
        times.accept(game.getEdition().getPlayersMinimum(),()->sut.fire(Optional.empty(),newPlayer));
        Optional<String> secret= Optional.of(game.getPlayers().stream().findAny().get().getSecret());
        possibleMoves=sut.getMoves(secret);
        Move commenceGame=possibleMoves.stream().filter(move->move.getType()==CommenceGame).findAny().get();
        sut.fire(secret,commenceGame);
        Optional<Problem> problem=sut.fire(Optional.empty(),newPlayer);

        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotNow,problem.get());
    }
    //fuehrt So lange weiter aus bis nichts mehr mgl
    @Test public void firePriority(){
        Set<Move> possibleMoves=sut.getMoves(Optional.empty());
        Move newPlayer=possibleMoves.stream().findAny().get();
        times.accept(game.getEdition().getPlayersMaximum()-1,()->sut.fire(Optional.empty(),newPlayer));
        sut.fire(Optional.empty(),newPlayer);
        Assert.assertEquals(Phase.PlantBuying,game.getPhase());
    }













}