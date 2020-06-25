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
import java.util.stream.Stream;


/**Test zur Veraenderung durch Aufgabe 6 neben den Addit-Tests.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-15-05
 */
public class AllAuctionsEndTest extends AbstractFirstRoundTest {
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

    private final HotMove endAuctions;



    /**
     * Fuehrt ein Runnable mehrmals aus.
     */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public AllAuctionsEndTest() {
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
        players.stream().forEach(player->player.setPassed(true));
        endAuctions=getHotMoveGame(Phase.PlantBuying,openGame,rules,MoveType.EndAuctions,Optional.empty());
    }

    //eigene Tests ab hier:
    @Test
    public void basic() {
        Assert.assertSame(Phase.PlantBuying, game.getPhase());
        Assert.assertEquals(maxPlayers, players.size());
        Assert.assertTrue(endAuctions.isAutoFire());
        Assert.assertFalse(endAuctions.hasPriority());
        Assert.assertSame(MoveType.EndAuctions, endAuctions.getType());
        Assert.assertTrue(endAuctions.fire().isEmpty());
    }

    @Test public void wrongPhase(){
        openGame.setPhase(Phase.Opening);
        Optional<Problem> problem=endAuctions.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotNow,problem.get());
    }

    @Test public void wasAllPlayersTrun(){
        players.get(0).setPassed(false);
        Optional<Problem> problem=endAuctions.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.PlayersRemaining,problem.get());
    }

    //TODO wenn nicht erste Runde??

    @Test public void sortPlayerToPlants1(){
        List<OpenPlayer> compare= new ArrayList<>();
        Stream.iterate(maxPlayers-1,n->n>=0,n->n-1).forEach(n->compare.add(players.get(n)));
        //System.out.println(players);
        //den Spielern 1 bis max wird in ja ein Kraftwerk 10 bis 10+max in umgekehrter Reihenfolge zugewiesen
        //Kraftwerks nummer muss bei anderen Editionen vllt angepasst werden
        Stream.iterate(maxPlayers-1,n->n>=0,n->n-1)
                .forEach(n->players.get(n).getOpenPlants().add(openGame.getPlantMarket().findPlant(40-maxPlayers+n)));
        endAuctions.fire();
        Assert.assertEquals(compare,openGame.getOpenPlayers());
    }
    @Test public void sortPlayerToPlants2(){
        openGame.setRound(2);
        List<OpenPlayer> compare= new ArrayList<>();
        Stream.iterate(maxPlayers-1,n->n>=0,n->n-1).forEach(n->compare.add(players.get(n)));
        //System.out.println(players);
        //den Spielern 1 bis max wird in ja ein Kraftwerk 10 bis 10+max in umgekehrter Reihenfolge zugewiesen
        //Kraftwerks nummer muss bei anderen Editionen vllt angepasst werden
        Stream.iterate(maxPlayers-1,n->n>=0,n->n-1)
                .forEach(n->players.get(n).getOpenPlants().add(openGame.getPlantMarket().findPlant(40-maxPlayers+n)));
        endAuctions.fire();
        Assert.assertEquals(compare,openGame.getOpenPlayers());
    }

    @Test public void noAuction(){
        Assert.assertTrue(endAuctions.fire().isEmpty());
        Assert.assertNull(game.getPlantMarket().findPlant(3));
    }

    @Test public void allPlayersAgain(){
        Assert.assertTrue(endAuctions.fire().isEmpty());
        players.stream().forEach(player->Assert.assertFalse(player.hasPassed()));
    }

    @Test public void changePhase(){
        Assert.assertTrue(endAuctions.fire().isEmpty());
        Assert.assertSame(Phase.ResourceBuying,game.getPhase());
    }

    @Test public void deleateAuction(){
        HotMove beginAuction = getHotMoveStartAuction(openGame, rules, maxPlayers, 3,"4");
        Assert.assertTrue(beginAuction.fire().isEmpty());
        players.stream().forEach(player->player.setPassed(true));
        openGame.setPhase(Phase.PlantBuying);
        Assert.assertNotNull(game.getAuction());
        Assert.assertTrue(endAuctions.fire().isEmpty());
        Assert.assertNull(game.getAuction());
    }


    @Test public void getGame(){
        Assert.assertSame(openGame,endAuctions.getGame());
    }

    @Test public void toStringT(){
        toStringTestHotMove(endAuctions);
    }

    @Test public void toStringPrototype(){
        toStringTestPrototype(Phase.PlantBuying,openGame,rules,MoveType.EndAuctions);
    }

    @Test public void getProperties(){
        getPropertiesTest(endAuctions);
    }


    @Test public void setProperties(){
        setPropertiesTest(endAuctions);
    }

    @Test (expected = IllegalStateException.class) public void collectMove(){
        endAuctions.collect(openGame,Optional.ofNullable(players.get(0)));
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