import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.cs.rs.powergrid.logic.*;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

/**Test zur Veraenderung durch Aufgabe 6 neben den Addit-Tests.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-15-05
 */
public class LeavingTheAuctionTest extends AbstractFirstRoundTest {
    //Grundkonstruktion aus dem Smoke7Test von R. Schiedermeier uebernommen
    /**
     * Spielstand.
     */
    private final Game game;

    /**
     * Spielstand.
     */
    private final OpenGame openGame;

    /**
     * Spielregeln.
     */
    private final Rules rules;

    /**
     * Fluchtwert fuer kein Geheimnis.
     */
    private final String NO_SECRET = "";

    /**
     * Maximale Spielerzahl der Edition
     */
    private final int maxPlayers;

    /**
     * Minimale Spilerzahl der Edition
     */
    private final int minPlayers;

    private final List<OpenPlayer> players;

    private final OpenPlant openPlant;

    private final OpenCity openCity;

    private final HotMove leaveAuction;

    private final List<Player> playersInAuction;


    /**
     * Fuehrt ein Runnable mehrmals aus.
     */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public LeavingTheAuctionTest() {
        System.setProperty("powergrid.factory", "edu.hm.mrodic.powergrid.datastore.MRodicFactory");
        System.setProperty("powergrid.rules", "edu.hm.mrodic.powergrid.logic.GameRules");
        // Stellt sicher, dass keine zufaelligen Daten vorkommen
        System.setProperty("powergrid.randomsource", "edu.hm.cs.rs.powergrid.logic.SortingRandomSource");
        openGame = OpenFactory.newFactory().newGame(new EditionGermany());
        openPlant = openGame.getPlantMarket().getOpenHidden().stream().findAny().orElse(null);
        openCity = openGame.getBoard().getOpenCities().stream().findAny().get();
        rules = Rules.newRules(openGame);
        game = openGame;
        //Zu Beginn nur Spielerhinzufuegen moeglich

        maxPlayers = game.getEdition().getPlayersMaximum();
        minPlayers = game.getEdition().getPlayersMinimum();
        //fuegt maxAnzahl Spieler ins Spiel ein
        times.accept(maxPlayers, () -> fireMove(NO_SECRET, MoveType.JoinPlayer));
        players = openGame.getOpenPlayers();

        final HotMove beginAuction = getHotMoveStartAuction(openGame, rules, maxPlayers, 6,"3");
        beginAuction.fire();
        playersInAuction=game.getAuction().getPlayers();
        leaveAuction=getHotMoveGame(Phase.PlantAuction,openGame,rules,MoveType.LeaveAuction,Optional.ofNullable(playersInAuction.get(0).getSecret()));
    }

    //eigene Tests ab hier:
    @Test
    public void basic() {
        Assert.assertSame(Phase.PlantAuction, game.getPhase());
        Assert.assertEquals(maxPlayers, players.size());
        Assert.assertTrue(leaveAuction.isAutoFire());
        Assert.assertFalse(leaveAuction.hasPriority());
        Assert.assertSame(MoveType.LeaveAuction, leaveAuction.getType());
        Assert.assertTrue(leaveAuction.fire().isEmpty());
        Assert.assertEquals(5, playersInAuction.size());
    }

    @Test public void wrongPhase(){
        openGame.setPhase(Phase.Opening);
        Optional<Problem> problem=leaveAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotNow,problem.get());
    }

    @Test public void playerIsTheFirstInList(){
        List<OpenPlayer> playerList=openGame.getAuction().getOpenPlayers();
        OpenPlayer first=playerList.get(0);
        playerList.remove(first);
        playerList.add(first);
        Assert.assertEquals(playerList.get(playerList.size()-1),first);
        Optional<Problem> problem=leaveAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotYourTurn,problem.get());
    }

    @Test public void leavingOfHighestBidder(){
        List<OpenPlayer> playerList=openGame.getAuction().getOpenPlayers();
        openGame.getAuction().setPlayer(playerList.get(0));
        Optional<Problem> problem=leaveAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.TopBidder,problem.get());
    }

    @Test public void removeInBidderList(){
        List<OpenPlayer> playerList=new ArrayList<>();
        playerList.addAll( openGame.getAuction().getOpenPlayers());
        playerList.remove(0);
        Assert.assertNotEquals(playerList,game.getAuction().getPlayers());
        Assert.assertTrue(leaveAuction.fire().isEmpty());
        Assert.assertEquals(playerList,game.getAuction().getPlayers());
    }

    @Test public void getGame(){
        Assert.assertSame(openGame,leaveAuction.getGame());
    }

    @Test public void toStringT(){
        leaveAuction.fire();
        toStringPlayer(Phase.PlantAuction,openGame,rules,playersInAuction.get(0),MoveType.LeaveAuction);
    }

    @Test public void toStringPrototype(){
        toStringTestPrototype(Phase.PlantAuction,openGame,rules,MoveType.LeaveAuction);
    }

    @Test public void getProperties(){
        leaveAuction.fire();
        getProbertiesTestPlayer(openGame,rules,playersInAuction.get(0));
    }


    @Test public void setProperties(){
        leaveAuction.fire();
        setPropertiesTestPlayer(Phase.PlantAuction,openGame,rules,playersInAuction.get(0),MoveType.LeaveAuction);
    }

    @Test (expected = IllegalStateException.class) public void collectMove(){
        leaveAuction.collect(openGame,Optional.ofNullable(players.get(0)));
    }






//Hilfsmethode von R.Schiedermeier: (mit Return)

    /**
     * Fuehrt einen Zug aus, der gelingen muss.
     *
     * @param type          Typ des Zuges.
     * @param secretOrNot   Geheimnis oder Leerstring, wenn keines noetig ist.
     * @param propertyValue Name und Wert einer Properties des gewuenschten Zuges.
     */
    private Optional<Problem> fireMove(String secretOrNot,MoveType type, String... propertyValue) {
        final Optional<String> secret = secretOrNot == NO_SECRET ?
                Optional.empty() :
                Optional.of(secretOrNot);
        final Optional<Move> move = rules.getMoves(secret)
                .stream()
                .filter(amove -> amove.getType() == type)
                .filter(amove -> propertyValue.length == 0 ||
                        amove.getProperty(propertyValue[0]).equals(propertyValue[1]))
                .findAny();
        Optional<Problem> problem=rules.fire(secret, move.get());
        return problem;
    }
}