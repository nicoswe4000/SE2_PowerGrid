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
public class NoResourceTransactionTest extends AbstractFirstRoundTest {
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




    /**
     * Fuehrt ein Runnable mehrmals aus.
     */
    private static BiConsumer<Integer, Runnable> times = (n, runnable) -> IntStream.range(0, n).forEach(__ -> runnable.run());

    /**
     * Initialisiert Factory und Spielregeln.
     */
    public NoResourceTransactionTest() {
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

        openGame.setPhase(Phase.ResourceBuying);

    }

    //eigene Tests ab hier:
    @Test
    public void basic() {
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-1).getSecret());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        Assert.assertSame(Phase.ResourceBuying, game.getPhase());
        Assert.assertEquals(maxPlayers, players.size());
        Assert.assertTrue(buyNoResource.isAutoFire());
        Assert.assertFalse(buyNoResource.hasPriority());
        Assert.assertSame(MoveType.BuyNoResource, buyNoResource.getType());
        Assert.assertTrue(buyNoResource.fire().isEmpty());
    }

    @Test public void wrongPhase(){
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-1).getSecret());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        openGame.setPhase(Phase.Opening);
        Optional<Problem> problem=buyNoResource.fire();
        Assert.assertTrue(problem.isPresent());
        Assert.assertSame(Problem.NotNow,problem.get());
    }

    @Test public void wrongPlayer(){
        players.get(maxPlayers-1).setPassed(true);
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-2).getSecret());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        players.get(maxPlayers-1).setPassed(false);
        Optional<Problem> problem=buyNoResource.fire();
        Assert.assertTrue(buyNoResource.fire().isPresent());
        Assert.assertSame(Problem.NotYourTurn, problem.get());
    }

    @Test public void wasPlayersTurn(){
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-1).getSecret());
        Assert.assertFalse(game.findPlayer(player.get()).hasPassed());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        Assert.assertTrue(buyNoResource.fire().isEmpty());
        Assert.assertTrue(game.findPlayer(player.get()).hasPassed());
    }


    @Test public void getGame()
    {
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-1).getSecret());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        Assert.assertSame(openGame, buyNoResource.getGame());
    }

    @Test public void toStringT(){
        String player=players.get(maxPlayers-1).getSecret();
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,Optional.ofNullable(player));
        buyNoResource.fire();
        toStringPlayer(Phase.PlantAuction,openGame,rules,player,MoveType.BuyNoResource,buyNoResource);
    }

    @Test public void toStringPrototype(){
        toStringTestPrototype(Phase.PlantBuying,openGame,rules,MoveType.BuyNoResource);
    }

    @Test public void getProperties(){
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-1).getSecret());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        buyNoResource.fire();
        getProbertiesTestPlayer(buyNoResource,player.get());
    }


    @Test public void setProperties(){
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-1).getSecret());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        buyNoResource.fire();
        setPropertiesTestPlayer(buyNoResource,player.get());
    }

    @Test (expected = IllegalStateException.class) public void collectMove(){
        Optional<String> player=Optional.ofNullable(players.get(maxPlayers-1).getSecret());
        HotMove buyNoResource =getHotMoveGame(Phase.ResourceBuying,openGame,rules,MoveType.BuyNoResource,player);
        buyNoResource.collect(openGame,Optional.ofNullable(players.get(maxPlayers-1)));
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