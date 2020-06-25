import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);

    Edition testEdition = new EditionGermany();

    @Test
    public void getEdition() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(testEdition, sut.getEdition());
    }

    @Test
    public void getBoard() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(factory.newBoard(testEdition), sut.getBoard());
    }

    @Test
    public void getRound() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(0, sut.getRound());
    }

    @Test
    public void setRound() {
        OpenGame sut = factory.newGame(testEdition);
        sut.setRound(5);
        Assert.assertEquals(5, sut.getRound());
    }

    @Test
    public void getPhase() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(Phase.Opening, sut.getPhase());
    }

    @Test (expected = NullPointerException.class)
    public void nullPhase() {
        OpenGame sut = factory.newGame(testEdition);
        sut.setPhase(null);
        sut.getPhase();
    }

    @Test
    public void setPhase() {//100% Coverage ?
        OpenGame sut = factory.newGame(testEdition);
        sut.setPhase(Phase.Opening);
        Assert.assertEquals(Phase.Opening, sut.getPhase());
        sut.setPhase(Phase.PlayerOrdering);
        Assert.assertEquals(Phase.PlayerOrdering, sut.getPhase());
        sut.setPhase(Phase.PlantBuying);
        Assert.assertEquals(Phase.PlantBuying, sut.getPhase());
        sut.setPhase(Phase.PlantAuction);
        Assert.assertEquals(Phase.PlantAuction, sut.getPhase());
        sut.setPhase(Phase.ResourceBuying);
        Assert.assertEquals(Phase.ResourceBuying, sut.getPhase());
        sut.setPhase(Phase.Building);
        Assert.assertEquals(Phase.Building, sut.getPhase());
        sut.setPhase(Phase.PlantOperation);
        Assert.assertEquals(Phase.PlantOperation, sut.getPhase());
        sut.setPhase(Phase.Bureaucracy);
        Assert.assertEquals(Phase.Bureaucracy, sut.getPhase());
        sut.setPhase(Phase.Terminated);
        Assert.assertEquals(Phase.Terminated, sut.getPhase());
    }

    @Test
    public void getPlayers() {
        OpenGame sut = factory.newGame(testEdition);
        OpenPlayer sep = factory.newPlayer("test", "red");
        OpenPlayer josef = factory.newPlayer("test2", "blue");
        sut.getOpenPlayers().add(sep);
        sut.getOpenPlayers().add(josef);
        List<Player> testList = new ArrayList<>();
        testList.add(josef);
        testList.add(sep);
        Assert.assertEquals(testList, sut.getPlayers());
    }

    @Test
    public void getLevel() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(0, sut.getLevel());
    }

    @Test
    public void setLevel() {
        OpenGame sut = factory.newGame(testEdition);
        sut.setLevel(2);
        Assert.assertEquals(2, sut.getLevel());
    }
    @Test(expected = IllegalArgumentException.class)
    public void setRoundException() {
        OpenGame sut = factory.newGame(testEdition);
        sut.setRound(-2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeLevel() {
        OpenGame sut = factory.newGame(testEdition);
        sut.setLevel(-1);
        sut.getLevel();
    }

    @Test
    public void getPlantMarket() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(factory.newPlantMarket(testEdition), sut.getPlantMarket());
    }

    @Test
    public void getResourceMarket() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(factory.newResourceMarket(testEdition), sut.getResourceMarket());
    }

    @Test
    public void setAuction() {
        OpenGame sut = factory.newGame(testEdition);
        OpenPlayer sep = factory.newPlayer("test", "red");
        OpenPlayer josef = factory.newPlayer("test2", "blue");
        sut.getOpenPlayers().add(sep);
        sut.getOpenPlayers().add(josef);
        OpenPlant testPlant = factory.newPlant(1, Plant.Type.Coal, 2, 3);
        OpenAuction testAuction = factory.newAuction(testPlant,sut.getOpenPlayers() );
        sut.setAuction(testAuction);
        Assert.assertEquals(testAuction, sut.getAuction());
    }

    @Test
    public void getNumMoves() {
        Game sut = factory.newGame(testEdition);
        Assert.assertEquals(0, sut.getNumMoves());
    }

    @Test
    public void setNumMoves() {
        OpenGame sut = factory.newGame(testEdition);
        sut.setNumMoves(42);
        Assert.assertEquals(42, sut.getNumMoves());
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeNumMoves() {
        OpenGame sut = factory.newGame(testEdition);
        sut.setNumMoves(-42);
        sut.getNumMoves();
    }

    @Test
    public void getFactory() {
        OpenGame sut = factory.newGame(testEdition);
        Assert.assertEquals(factory, sut.getFactory());
    }

    @Test public void findPlayerNull(){
        OpenGame sut = factory.newGame(testEdition);
        OpenPlayer sep = factory.newPlayer("test", "red");
        OpenPlayer josef = factory.newPlayer("test2", "blue");
        sut.getOpenPlayers().add(sep);
        sut.getOpenPlayers().add(josef);
        Assert.assertEquals(null,sut.findPlayer(null));

    }
    @Test public void findPlayer(){
        OpenGame sut = factory.newGame(testEdition);
        OpenPlayer sep = factory.newPlayer("test", "red");
        OpenPlayer josef = factory.newPlayer("test2", "blue");
        sut.getOpenPlayers().add(sep);
        sut.getOpenPlayers().add(josef);
        Assert.assertEquals(sep,sut.findPlayer("test"));

    }
    @Test (expected = NullPointerException.class) public void editionNull(){
        OpenFactory factory2=OpenFactory.newFactory(fcqn);
        Game sut=factory2.newGame(null);
    }
}