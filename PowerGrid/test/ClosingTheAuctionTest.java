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
public class ClosingTheAuctionTest extends AbstractFirstRoundTest {
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

    private final HotMove closeAuction;

    private final Optional<String> playerHelp;



    /**
     * Fuehrt ein Runnable mehrmals aus.
     */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public ClosingTheAuctionTest() {
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
        //fuegt 2 Spieler ins Spiel ein
        times.accept(2, () -> fireMove(NO_SECRET, MoveType.JoinPlayer));
        players = openGame.getOpenPlayers();
        playerHelp=Optional.ofNullable(players.get(0).getSecret());
        HotMove commence=getHotMoveGame(Phase.Opening,openGame,rules,MoveType.CommenceGame,playerHelp);
        rules.fire(playerHelp,commence);
        final HotMove beginAuction = getHotMoveStartAuction(openGame, rules, 2, 1,"4");
        beginAuction.fire();
        List<Player> playersInAuction=game.getAuction().getPlayers();
        closeAuction=getHotMoveGame(Phase.PlantAuction,openGame,rules,MoveType.CloseAuction,Optional.empty());
    }

    //eigene Tests ab hier:
    @Test
    public void basic() {
        Assert.assertSame(Phase.PlantAuction, game.getPhase());
        Assert.assertEquals(2, players.size());
        Assert.assertTrue(closeAuction.isAutoFire());
        Assert.assertFalse(closeAuction.hasPriority());
        Assert.assertSame(MoveType.CloseAuction, closeAuction.getType());
        Assert.assertTrue(closeAuction.fire().isEmpty());
    }

    @Test public void wrongPhase(){
        openGame.setPhase(Phase.Opening);
        Optional<Problem> problem=closeAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotNow,problem.get());
    }

    @Test public void betListSizeBigger(){
        openGame.getAuction().getOpenPlayers().add(openGame.findPlayer(playerHelp.get()));
        Optional<Problem> problem=closeAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.AuctionRunning,problem.get());
    }

    @Test public void removePlant(){
        Assert.assertNotNull(game.getPlantMarket().findPlant(4));
        Assert.assertTrue(closeAuction.fire().isEmpty());
        Assert.assertNull(game.getPlantMarket().findPlant(4));
    }

    @Test public void playerGetPlant(){
        Player player=openGame.getAuction().getPlayer();
        Plant plant=game.getPlantMarket().findPlant(4);
        Assert.assertEquals(0,player.getPlants().size());
        Assert.assertTrue(closeAuction.fire().isEmpty());
        Assert.assertEquals(1,player.getPlants().size());
        Assert.assertSame(plant,player.getPlants().stream().findFirst().get());
    }

    @Test public void playerGotToPay(){
        OpenPlayer player=openGame.getAuction().getPlayer();
        final int amount=player.getElectro();
        Assert.assertTrue(closeAuction.fire().isEmpty());
        Assert.assertEquals(amount-4,player.getElectro());
    }

    @Test public void wasPlayerTurn(){
        OpenPlayer player=openGame.getAuction().getPlayer();
        Assert.assertFalse(player.hasPassed());
        Assert.assertTrue(closeAuction.fire().isEmpty());
        Assert.assertTrue(player.hasPassed());
    }

    @Test public void newPhase(){
        Assert.assertTrue(closeAuction.fire().isEmpty());
        Assert.assertSame(Phase.PlantBuying,game.getPhase());
    }

    @Test public void getGame(){
        Assert.assertSame(openGame,closeAuction.getGame());
    }

    @Test public void toStringT(){
        toStringTestHotMove(closeAuction);
    }

    @Test public void toStringPrototype(){
        toStringTestPrototype(Phase.PlantAuction,openGame,rules,MoveType.CloseAuction);
    }

    @Test public void getProperties(){
        getPropertiesTest(closeAuction);
    }


    @Test public void setProperties(){
        setPropertiesTest(closeAuction);
    }

    @Test (expected = IllegalStateException.class) public void collectMove(){
        closeAuction.collect(openGame,Optional.ofNullable(players.get(0)));
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