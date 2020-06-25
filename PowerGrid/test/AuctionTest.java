import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AuctionTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    //System.setProperty("powergrid.factory", "edu.hm.mrodic.powergrid.datastore.MRodicFactory");
    OpenFactory factory = OpenFactory.newFactory(fcqn);

    OpenPlant testPlant = factory.newPlant(1, Plant.Type.Coal, 2, 3);
    OpenPlayer sep = factory.newPlayer("test", "blue");
    OpenPlayer josef = factory.newPlayer("test2", "red");


    @Test
    public void getPlayers() {
        List<OpenPlayer> testList = new ArrayList<>();
        testList.add(sep);
        testList.add(josef);
        OpenAuction sut = factory.newAuction(testPlant, testList);
        Assert.assertEquals(testList, sut.getPlayers());
    }

    @Test (expected = IllegalArgumentException.class)
    public void getPlayersEmpty() {
        List<OpenPlayer> testList = new ArrayList<>();
        OpenAuction sut = factory.newAuction(testPlant, testList);
        testList=sut.getOpenPlayers();
        testList.clear();
        sut.getPlayers();
    }

    @Test
    public void getAmount() {
        List<OpenPlayer> testList = new ArrayList<>();
        testList.add(factory.newPlayer("123","white"));
      OpenAuction sut = factory.newAuction(testPlant, testList);
        Assert.assertEquals(1, sut.getAmount());
    }

    @Test (expected = IllegalArgumentException.class)
    public void getAmountNegative() {
        List<OpenPlayer> testList = new ArrayList<>();
        OpenAuction sut = factory.newAuction(testPlant, testList);
        sut.setAmount(-5);
    }
    @Test (expected = IllegalArgumentException.class)
    public void getAmountSmallerThanPlantNumber() {
        List<OpenPlayer> testList = new ArrayList<>();
        OpenAuction sut = factory.newAuction(testPlant, testList);
        sut.setAmount(testPlant.getNumber()-1);
    }


    @Test
    public void getPlant() {
        List<OpenPlayer> testList = new ArrayList<>();
        testList.add(factory.newPlayer("124","none"));
        OpenAuction sut = factory.newAuction(testPlant, testList);
        Assert.assertEquals(testPlant, sut.getPlant());
    }

    @Test (expected = NullPointerException.class)
    public void nullPlant() {
        List<OpenPlayer> testList = new ArrayList<>();
        OpenAuction sut = factory.newAuction(null, testList);
       sut.getPlant();
    }

    @Test
    public void setPlayer() {
        List<OpenPlayer> testList = new ArrayList<>();
        testList.add(sep);
        OpenAuction sut = factory.newAuction(testPlant, testList);

        Assert.assertEquals(sep, sut.getPlayer());
    }

    @Test (expected = NullPointerException.class)
    public void nullPlayer() {
        List<OpenPlayer> testList = new ArrayList<>();
        OpenAuction sut = factory.newAuction(null, testList);
        sut.setPlayer(null);
        sut.getPlayer();
    }

    @Test
    public void setAmount() {
        List<OpenPlayer> testList = new ArrayList<>();
        testList.add(factory.newPlayer("123","black"));
        OpenAuction sut = factory.newAuction(testPlant, testList);
        sut.setAmount(42);
        Assert.assertEquals(42, sut.getAmount());
    }
    @Test public void samePlants(){
        OpenPlant plant=factory.newPlant(41, Plant.Type.Coal,2,3);
        List<OpenPlayer> players=new ArrayList<>();
        players.add(josef);
        players.add(sep);
        OpenAuction sut1=factory.newAuction(plant,players);
        OpenAuction sut2=factory.newAuction(plant,players);
        OpenAuction sut3=factory.newAuction(testPlant,players);
        Assert.assertTrue(sut1==sut2);
        Assert.assertTrue(sut1!=sut3);

    }
    @Test (expected = IllegalStateException.class) public void playersEmpty(){
        List<OpenPlayer> players=new ArrayList<>();
        players.add(josef);
        players.add(sep);
        OpenPlant plant=factory.newPlant(41, Plant.Type.Coal,2,3);
        OpenAuction sut1=factory.newAuction(plant,players);
        sut1.getOpenPlayers().remove(sep);
        sut1.getOpenPlayers().remove(josef);
        sut1.getOpenPlayers();
    }
    @Test(expected = IllegalArgumentException.class) public void negAmount(){
        List<OpenPlayer> players=new ArrayList<>();
        players.add(josef);
        players.add(sep);
        OpenPlant plant=factory.newPlant(41, Plant.Type.Coal,2,3);
        OpenAuction sut1=factory.newAuction(plant,players);
        sut1.setAmount(-5);
    }
}