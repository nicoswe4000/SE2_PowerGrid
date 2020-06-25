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


/**Test zur Veraenderung durch Aufgabe 6 neben den Addit-Tests.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-15-05
 */
public class BeginOfAuctionTest extends AbstractFirstRoundTest {
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

    private final HotMove beginAuction;


    /**
     * Fuehrt ein Runnable mehrmals aus.
     */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public BeginOfAuctionTest() {
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
        times.accept(maxPlayers, () -> fireMove(NO_SECRET, MoveType.JoinPlayer));
        players = openGame.getOpenPlayers();
        beginAuction = getHotMoveStartAuction(openGame, rules, maxPlayers, 3,"3");
    }

    //eigene Tests ab hier:
    @Test
    public void basic() {
        Assert.assertSame(Phase.PlantBuying, game.getPhase());
        Assert.assertEquals(maxPlayers, players.size());
        Assert.assertSame(MoveType.StartAuction, beginAuction.getType());
        Assert.assertFalse(beginAuction.hasPriority());
        Assert.assertTrue(beginAuction.isAutoFire());
    }



    @Test
    public void firstPlayerNorPassed() {
        openGame.getOpenPlayers().get(maxPlayers - 3).setPassed(true);
        Optional<Problem> problem = beginAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotYourTurn, problem.get());
    }

    @Test
    public void plantNotInMarket() {
        Set<OpenPlant> actualPlants = openGame.getPlantMarket().getOpenActual();
        actualPlants.clear();
        openGame.getPlantMarket().getOpenHidden().stream().limit(4).forEach(plant -> actualPlants.add(plant));
        Assert.assertEquals(4, openGame.getPlantMarket().getOpenActual().size());
        Optional<Problem> problem = beginAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.PlantNotAvailable, problem.get());
    }


    @Test
    public void playerGotNoCash() {
        openGame.getOpenPlayers().get(maxPlayers - 3).setElectro(0);
        Optional<Problem> problem = beginAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NoCash, problem.get());
    }

    @Test
    public void newPhase() {
        Optional<Problem> problem = beginAuction.fire();
        Assert.assertTrue(problem.isEmpty());
        Assert.assertSame(Phase.PlantAuction, game.getPhase());
    }

    @Test public void wrongPhase(){
        openGame.setPhase(Phase.Opening);
        Optional<Problem> problem = beginAuction.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotNow, problem.get());
    }

    @Test
    public void correctBetList() {
        Optional<Problem> problem = beginAuction.fire();
        List<Player> compare = List.of(game.getPlayers().get(maxPlayers - 3), game.getPlayers().get(maxPlayers - 2), game.getPlayers().get(maxPlayers - 1));
        Assert.assertTrue(problem.isEmpty());
        Assert.assertEquals(3, game.getAuction().getPlayers().size());
        Assert.assertTrue(game.getAuction().getPlayers().containsAll(compare));
    }

    @Test
    public void correctBetList2() {
        HotMove beginAuction2 = getHotMoveStartAuction(openGame, rules, maxPlayers, 2,"3");
        Optional<Problem> problem = beginAuction2.fire();
        List<Player> compare = List.of(game.getPlayers().get(maxPlayers - 2), game.getPlayers().get(maxPlayers - 1));
        Assert.assertTrue(problem.isEmpty());
        Assert.assertEquals(2, game.getAuction().getPlayers().size());
        Assert.assertTrue(game.getAuction().getPlayers().containsAll(compare));
    }

    @Test
    public void correctMostBetPerson() {
        HotMove beginAuction2 = getHotMoveStartAuction(openGame, rules, maxPlayers, 2,"3");
        Optional<Problem> problem = beginAuction2.fire();
        Assert.assertTrue(problem.isEmpty());
        Assert.assertEquals(players.get(maxPlayers - 2), game.getAuction().getPlayer());
    }

    @Test
    public void correctMostBetPerson2() {
        HotMove beginAuction2 = getHotMoveStartAuction(openGame, rules, maxPlayers, 1,"3");
        Optional<Problem> problem = beginAuction2.fire();
        Assert.assertTrue(problem.isEmpty());
        Assert.assertEquals(players.get(maxPlayers - 1), game.getAuction().getPlayer());
    }

    @Test
    public void highestBitIsNumberOfPlant() {
        Assert.assertTrue(beginAuction.fire().isEmpty());
        Assert.assertSame(Phase.PlantAuction,game.getPhase());
        Assert.assertEquals(3,game.getAuction().getAmount());
    }

    @Test
    public void highestBitIsNumberOfPlant2() {
        HotMove beginAuction2=getHotMoveStartAuction(openGame, rules, maxPlayers, 5,"6");
        Assert.assertTrue(beginAuction2.fire().isEmpty());
        Assert.assertSame(Phase.PlantAuction,game.getPhase());
        Assert.assertEquals(6,game.getAuction().getAmount());
    }

    @Test public void correctPlant(){
        Assert.assertTrue(beginAuction.fire().isEmpty());
        Plant plant=game.getPlantMarket().findPlant(3);
        Assert.assertSame(plant,game.getAuction().getPlant());
    }

    @Test
    public void correctPlant2() {
        HotMove beginAuction2=getHotMoveStartAuction(openGame, rules, maxPlayers, 5,"6");
        Assert.assertTrue(beginAuction2.fire().isEmpty());
        Plant plant=game.getPlantMarket().findPlant(6);
        Assert.assertSame(plant,game.getAuction().getPlant());;
    }

    @Test public void firstPlayeraAtListEnd(){
        Assert.assertTrue(beginAuction.fire().isEmpty());
        Player first=players.get(maxPlayers-3);
        Assert.assertSame(first,game.getAuction().getPlayer());
        Assert.assertSame(first,game.getAuction().getPlayers().get(game.getAuction().getPlayers().size()-1));
    }

    @Test public void getGame(){
        Assert.assertSame(openGame,beginAuction.getGame());
    }

    @Test public void toStringT(){
        String compare="StartAuction{plant=3, player=8000F6}";
        Assert.assertEquals(compare,beginAuction.toString());
    }

    @Test public void toStringPrototype(){
        toStringTestPrototype(Phase.PlayerOrdering,openGame,rules,MoveType.StartAuction);
    }


    @Test public void getProperties(){
        getProbertiesTestAuction(openGame, rules, maxPlayers, 4,"3");
    }

    @Test public void setProperties(){
        setPropertiesTestAuction(openGame, rules, maxPlayers, 2,"3");
    }

    @Test (expected = IllegalStateException.class) public void collectMove(){
        beginAuction.collect(openGame,Optional.ofNullable(players.get(0)));
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