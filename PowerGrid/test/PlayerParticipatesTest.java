import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

public class PlayerParticipatesTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final String fcqnrules="edu.hm.mrodic.powergrid.logic.GameRules";
    /*Problems die ich abdecken will:
     * - MaxPlayers
     * - TooFewPlayers
     * - NotNow */

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);
    private final Edition testEdition = new EditionGermany();
    private final OpenGame game = factory.newGame(testEdition);
    private final Rules rules=Rules.newRules(fcqnrules,game);
    //6 player-objects to test the boundaries of max players
    private final OpenPlayer sep = factory.newPlayer("test1", "blue");
    private final OpenPlayer josef = factory.newPlayer("test2", "red");
    private final OpenPlayer peter = factory.newPlayer("test3", "green");
    private final OpenPlayer gustav = factory.newPlayer("test4", "yellow");
    private final OpenPlayer paul = factory.newPlayer("test5", "black");
    private final OpenPlayer jakob = factory.newPlayer("test6", "white");
    private final OpenPlayer gregor = factory.newPlayer("test7", "orange");

    //Test for Arrival of a new Hero
    @Test
    public void testWorks() {
        game.setPhase(Phase.Opening);
        Assert.assertTrue(rules.getPrototypes().stream().map(Move::getType).anyMatch(type->type.equals(MoveType.JoinPlayer)));
    }

    @Test (expected = NullPointerException.class)
    public void testNullGamePlayer() {
        game.setPhase(Phase.Opening);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.JoinPlayer)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        moveHot.collect(null,Optional.of(jakob));
    }
    @Test (expected = RuntimeException.class)
    public void testNotNullCollect() {
        game.setPhase(Phase.Opening);
        final Rules rulesNull = Rules.newRules(fcqnrules,null);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.JoinPlayer)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        Set<HotMove> moves=moveHot.collect(game,Optional.empty());
        for(HotMove sut:moves)
            sut.collect(game,Optional.empty());
    }

    @Test // mit dem neuen genau 6 Spieler
    public void testMaxPlayersPassed() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        game.getOpenPlayers().add(peter);
        game.getOpenPlayers().add(gustav);
        Assert.assertTrue(rules.getMoves(Optional.empty()).stream().map(Move::getType).anyMatch(type->type.equals(MoveType.JoinPlayer)));
    }

    @Test // zu viele Spieler, 7 statt 6
    public void testMaxPlayersFail() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        game.getOpenPlayers().add(peter);
        game.getOpenPlayers().add(gustav);
        game.getOpenPlayers().add(paul);
        game.getOpenPlayers().add(jakob);
        Optional<Move> move=rules.getMoves(Optional.empty()).stream().filter(prototype->prototype.getType().equals(MoveType.JoinPlayer)).findFirst();
        Assert.assertEquals(true,move.isEmpty());
    }

    @Test
    public void fireWorks() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        Optional<Move> move=rules.getMoves(Optional.empty()).stream().filter(prototype->prototype.getType().equals(MoveType.JoinPlayer)).findFirst();
        Assert.assertTrue(rules.fire(Optional.empty(),move.get()).isEmpty());
    }

    @Test // falsche phase
    public void fireNotNow() {
        game.setPhase(Phase.PlayerOrdering);
        Optional<Move> move=rules.getMoves(Optional.empty()).stream().filter(prototype->prototype.getType().equals(MoveType.JoinPlayer)).findFirst();
        Assert.assertEquals(true,move.isEmpty());
    }

    @Test
    public void getGame() {
        game.setPhase(Phase.Opening);
        Optional<Move> move=rules.getPrototypes().stream().filter(prototype->prototype.getType().equals(MoveType.JoinPlayer)).findFirst();
        Assert.assertSame(game, rules.getGame());
    }

    @Test // nur ein spieler, man muss noch einen hinzufügen
    public void collectAddPlayer() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        Assert.assertTrue(rules.getMoves(Optional.empty()).stream().anyMatch(prototype->prototype.getType().equals(MoveType.JoinPlayer)));
    }

    // Tests für die Auswirkungen

    @Test
    public void testSecretColor() {
        game.setPhase(Phase.Opening);
        Optional<Move> sut=rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.JoinPlayer)).findFirst();
        Assert.assertTrue(sut.isPresent());
        rules.fire(Optional.empty(),sut.get());
        Assert.assertFalse(rules.getGame().getPlayers().stream().filter(color -> color.getColor().equals("FF0001")).findFirst().isEmpty());
    }

    //Test for Start of a new Game

    @Test(expected = NullPointerException.class) public void testNonNUll(){
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        moveHot.test();
    }
    @Test public void testCommenceGame(){
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().filter(option->option.getType()==MoveType.CommenceGame).findFirst();
        HotMove moveHot=(HotMove)move.get();
        moveHot.test();
        Assert.assertFalse(Phase.PlayerOrdering==game.getPhase());
    }
    @Test public void testCommenceGame2(){
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().filter(option->option.getType()==MoveType.CommenceGame).findFirst();
        game.setPhase(Phase.ResourceBuying);
        HotMove moveHot=(HotMove)move.get();
        moveHot.run(true);
        Assert.assertFalse(Phase.PlayerOrdering==game.getPhase());
    }
    @Test (expected = RuntimeException.class)
    public void testNotNullCollectStartGame4() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        Set<HotMove> moves=moveHot.collect(game,Optional.of(sep));
        for(HotMove sut:moves)
            sut.collect(game,Optional.of(sep));
    }
    @Test public void prio(){
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        Set<HotMove> moves=moveHot.collect(game,Optional.of(sep));
        boolean prio=false;
        for(HotMove sut:moves)
            prio=sut.hasPriority();
        Assert.assertEquals(false,prio);
    }

    @Test public void prioYes(){
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        game.getOpenPlayers().add(jakob);
        game.getOpenPlayers().add(gustav);
        game.getOpenPlayers().add(gregor);
        game.getOpenPlayers().add(paul);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        Set<HotMove> moves=moveHot.collect(game,Optional.of(sep));
        boolean prio=false;
        for(HotMove sut:moves)
            prio=sut.hasPriority();
        Assert.assertEquals(true,prio);
    }
    @Test public void prioNo(){
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        game.getOpenPlayers().add(jakob);
        game.getOpenPlayers().add(gustav);
        game.getOpenPlayers().add(gregor);
        game.getOpenPlayers().add(paul);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        boolean prio=moveHot.hasPriority();
        Assert.assertEquals(false,prio);
    }
    @Test
    public void testWorks2() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        Assert.assertTrue(rules.getMoves(Optional.of("test1")).stream().anyMatch(step->step.getType().equals(MoveType.CommenceGame)));
    }

    @Test (expected = NullPointerException.class)
    public void testNullGameStart() {
        game.setPhase(Phase.Opening);
        game.setPhase(Phase.Opening);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        Set<HotMove> moves=moveHot.collect(null,Optional.of(jakob));
        //Assert.assertTrue(.getMoves(Optional.of("test1")).stream().anyMatch(step->step.getType().equals(MoveType.CommenceGame)));
    }

    @Test
    public void testTooFewPlayers() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().findAny();
        Assert.assertTrue(move.isEmpty());
    }

    @Test
    public void fireWorks2() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(jakob);
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().findAny();
        Assert.assertTrue(rules.fire(Optional.of("test1"),move.get()).isEmpty());
        Assert.assertEquals(0, rules.getGame().getLevel());
        Assert.assertEquals(1, rules.getGame().getRound());
        Assert.assertEquals(Phase.PlantBuying, rules.getGame().getPhase());
    }

    @Test
    public void fireNotNow2() {
        game.setPhase(Phase.PlayerOrdering);
        Assert.assertEquals(true,rules.getMoves(Optional.of("test1")).isEmpty());
    }

    @Test
    public void notYourTurnTest() {
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        game.setPhase(Phase.Opening);
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        HotMove moveHot=(HotMove)move.get();
        Set<HotMove> moves=moveHot.collect(game,Optional.of(jakob));
        Assert.assertEquals(true, moves.isEmpty()); //gibt keinen Spieler mit secret test2, sollte Problem werfen
    }

    @Test // man kann starten und hinzufügen
    public void collectJoinPlayerStart() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(jakob);
        game.getOpenPlayers().add(josef);
        Optional<MoveType> any=rules.getMoves(Optional.of("test1")).stream().map(Move::getType).filter(type->type.equals(MoveType.CommenceGame)).findAny();
        Assert.assertFalse(any.isEmpty());
    }

    @Test // max spieleranzahl, man kann nur starten
    public void collectStart() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        game.getOpenPlayers().add(peter);
        game.getOpenPlayers().add(gustav);
        game.getOpenPlayers().add(paul);
        game.getOpenPlayers().add(jakob);
        Assert.assertTrue(rules.getMoves(Optional.of("test3")).size()==1);
    }

    @Test // max spieleranzahl, man kann nur starten
    public void collectStartFail() {
        game.setPhase(Phase.Building); //falsche Phase
        Assert.assertTrue(rules.getMoves(Optional.of("test2")).isEmpty());
    }

    // Tests für die Auswirkungen

    @Test
    public void testInitialisePlantMarket() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(paul);
        game.getOpenPlayers().add(gustav);
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().findAny();
        rules.fire(Optional.of("test1"),move.get());
        Assert.assertEquals(4, game.getPlantMarket().getActual().size());//Kraftwerke 3-6 aktueller Markt
        Assert.assertEquals(4, game.getPlantMarket().getFuture().size());//Kraftwerke 7-10 zukünftiger Markt
        Assert.assertEquals(27, game.getPlantMarket().getNumberHidden()); //alle ohne 3-10 und ohne 13
        Assert.assertFalse(game.getPlantMarket().findPlant(13) == null); //nimmt nur aus allPlants geht nicht so gut einzelne raus zu nehmen
    }



    @Test
    public void testMoney() {
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(jakob);
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().findAny();
        rules.fire(Optional.of("test1"),move.get());
        Player testPlayer = game.getOpenPlayers().stream().findAny().get();
        Assert.assertEquals(50, testPlayer.getElectro()); //50 Elektro = Startwert
    }

    @Test (expected = IllegalStateException.class)
    public void testUnmodifiable() { //sollte nach CommenceGame geschlossen sein -> 2 mal close = Exception
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef);
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().filter(option->option.getType().equals(MoveType.CommenceGame)).findFirst();
        rules.fire(Optional.of("test1"),move.get());
        game.getBoard().close();
    }
    @Test
    public void testCloseRegions() {
        game.getOpenPlayers().clear();
        game.setPhase(Phase.Opening);
        game.getOpenPlayers().add(sep);
        game.getOpenPlayers().add(josef); // Spieleranzahl = 2 ==> alles über Region 3 ist weg
        Optional<Move> move=rules.getMoves(Optional.of("test1")).stream().findAny();
        rules.fire(Optional.of("test1"),move.get());
        boolean sut=rules.getGame().getBoard().getCities().stream().noneMatch(city->city.getRegion()>3);
        Assert.assertTrue(sut);
    }
}