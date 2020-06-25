import com.sun.source.tree.AssertTree;
import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**Zusatz Test zu Game und Test zur Veraenderung durch Aufgabe 6.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-20-05
 */
public class AdditGameTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);

    private final OpenPlant testPlant = factory.newPlant(42, Plant.Type.Eco, 0, 3);
    private final OpenPlayer pink=factory.newPlayer("pink","pink");
    private final OpenPlayer red=factory.newPlayer("red","red");
    private final Edition edition=new EditionGermany();
    private final Board board= factory.newBoard(edition);
    private final OpenCity openCity= factory.newCity("Winterfell",4);
    private final Game game= factory.newGame(edition);
    private final OpenGame openGame= factory.newGame(edition);

//Allgemeine Tests
    //getBoard
    @Test
    public void getBoard() {
        Assert.assertTrue(board==game.getBoard());
    }

    @Test
    public void getOpenBoard() {
        Assert.assertTrue(board==openGame.getBoard());
    }

    //getBoard speziell Aufgabe 6:
    @Test
    public void getOpenBoard2() {
        int compareValalue=openGame.getBoard().getOpenCities().size()+1;
        openGame.getBoard().getOpenCities().add(openCity);
        Assert.assertEquals(compareValalue,openGame.getBoard().getOpenCities().size());
    }
    @Test (expected = UnsupportedOperationException.class)
    public void getBoard1() {
        game.getBoard().getCities().add(openCity);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getBoard2() {
        openGame.getBoard().getCities().add(openCity);
    }

    //getRound
    @Test
    public void getRound() {
        Assert.assertEquals(0,game.getRound());
    }
    @Test
    public void getOpenRound() {
        Assert.assertEquals(0,openGame.getRound());
    }

    //getPhase
    @Test
    public void getPhase() {
        Assert.assertEquals(Phase.Opening,game.getPhase());
    }
    @Test
    public void getOpenPhase() {
        Assert.assertEquals(Phase.Opening,openGame.getPhase());
    }

    //getLevel
    @Test
    public void getLevel() {
        Assert.assertEquals(0,game.getLevel());
    }
    //getPlayers
    @Test
    public void getPlayers() {
        Assert.assertFalse(game.getPlayers()==null);
    }

    @Test
    public void getOpenPlayers() {
        Assert.assertFalse(openGame.getPlayers()==null);
    }

    @Test
    public void getPlayersSort() {
        openGame.getOpenPlayers().add(pink);
        openGame.getOpenPlayers().add(red);
        pink.getOpenPlants().add(testPlant);
        pink.getOpenCities().add(openCity);
        List<Player> compareValue=new ArrayList<Player>();
        compareValue.add(pink);
        compareValue.add(red);
        Assert.assertEquals(compareValue,openGame.getPlayers());
    }

    //getPlayers speziell Aufgabe 6
    @Test (expected = UnsupportedOperationException.class)
    public void getOpenPlayersImmutable() {
        openGame.getPlayers().add(pink);
    }

    @Test
    public void getOpenPlayersAdd() {
        openGame.getOpenPlayers().add(pink);
        Assert.assertEquals(1,openGame.getPlayers().size());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getPlayersAddImmutable() {
        game.getPlayers().add(pink);
    }

    //getAuction speziell Aufgabe 6:
    @Test
    public void getOpenAuction() {
        List<OpenPlayer> players=new ArrayList<OpenPlayer>();
        players.add(red);
        openGame.setAuction(factory.newAuction(testPlant,players));
        openGame.getAuction().getOpenPlayers().add(pink);
        Assert.assertEquals(2,openGame.getAuction().getPlayers().size());
    }
    @Test (expected = UnsupportedOperationException.class)
    public void getAuction() {
        List<OpenPlayer> players=new ArrayList<OpenPlayer>();
        players.add(red);
        openGame.setAuction(factory.newAuction(testPlant,players));
        Game sut=openGame;
        sut.getAuction().getPlayers().add(pink);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getAuction2() {
        List<OpenPlayer> players=new ArrayList<OpenPlayer>();
        players.add(red);
        openGame.setAuction(factory.newAuction(testPlant,players));
        openGame.getAuction().getPlayers().add(pink);
    }

    //getPlantMarket speziell Aufgabe 6
    @Test
    public void getOpenPlantmarket() {
        openGame.getPlantMarket().removePlant(30);
        Assert.assertEquals(41,openGame.getPlantMarket().getNumberHidden());
    }
    @Test (expected = UnsupportedOperationException.class)
    public void getPlantmarket() {
        game.getPlantMarket().getActual().add(testPlant);
    }

    //getResourceMarket speziell Aufgabe 6
    @Test
    public void getOpenResourceMarket() {
        int compareValue=openGame.getResourceMarket().getOpenSupply().size();
        openGame.getResourceMarket().getOpenSupply().add(Resource.Uranium);
        Assert.assertEquals(compareValue+1,openGame.getResourceMarket().getOpenSupply().size());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getResourceMarket() {
        game.getResourceMarket().getSupply().add(Resource.Uranium);
    }

    //findPlayer speziell Aufgabe 6
    @Test
    public void findOpenPlayer() {
        openGame.getOpenPlayers().add(pink);
        openGame.findPlayer("pink").getOpenPlants().add(testPlant);
        Assert.assertEquals(1,openGame.findPlayer("pink").getOpenPlants().size());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void findPlayer() {
        openGame.getOpenPlayers().add(pink);
        Game sut=openGame;
        //hier besteht nur die Option getPlants, getOpenPlants ist nicht mgl
        sut.findPlayer("pink").getPlants().add(testPlant);
    }



}