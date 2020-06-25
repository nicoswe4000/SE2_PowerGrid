import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Auction;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.mrodic.powergrid.MRodicBag;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**Zusatz Test zur Auction und Test zur Veraenderung durch Aufgabe 6.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-25-05
 */
public class AdditAuctionTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);

    private final OpenPlant testPlant = factory.newPlant(42, Plant.Type.Eco, 0, 3);
    private final OpenPlayer pink=factory.newPlayer("pink","pink");
    private final OpenPlayer red=factory.newPlayer("red","red");

//Allgemeine Tests (haben noch Probleme im Code Aufgedeckt)
    //construct Test
    @Test public void constructValues() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(pink);
        players.add(red);
        Auction sut=factory.newAuction(testPlant,players);
        Assert.assertEquals(42,sut.getAmount());
        Assert.assertEquals(pink,sut.getPlayer());
    }
    @Test (expected = NullPointerException.class)
    public void wrongConstructValuePlant() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        Auction sut=factory.newAuction(null,players);
    }
    @Test (expected = NullPointerException.class)
    public void wrongConstructValuePlayerNull() {
        Auction sut=factory.newAuction(testPlant,null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void wrongConstructValuePlayerEmpty() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        Auction sut=factory.newAuction(testPlant,players);
    }

    @Test (expected = NullPointerException.class)
    public void OpenWrongConstructValuePlayerNull() {
        OpenAuction sut=factory.newAuction(testPlant,null);
    }

    @Test (expected = Exception.class)
    public void OpenWrongConstructValuePlayerEmpty() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        factory.newAuction(testPlant,players);
    }

    //getPlayers Test
    @Test
    public void getPlayersBasic() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        players.add(pink);
        Auction sut = factory.newAuction(testPlant,players);
        Assert.assertEquals(players, sut.getPlayers());
    }

    @Test
    public void getOpenPlayersBasic() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        players.add(pink);
        OpenAuction sut = factory.newAuction(testPlant,players);
        Assert.assertEquals(players, sut.getOpenPlayers());
    }

    @Test
    public void getPlayersBasicmutable() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        players.add(pink);
        OpenAuction sut = factory.newAuction(testPlant,players);
        Assert.assertEquals(players, sut.getOpenPlayers());
    }


    @Test (expected = IllegalStateException.class)
    public void getPlayersBasicEmpty() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        sut.getOpenPlayers().clear();
        sut.getPlayers();
    }

    @Test (expected = IllegalStateException.class)
    public void getOpenPlayersBasicEmpty() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        sut.getOpenPlayers().clear();
        sut.getOpenPlayers();
    }

//getPlayersTest speziell fuer Aufteilung in Aufgabe 6

    @Test (expected = UnsupportedOperationException.class)
    public void getPlayersImmutable() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        Auction sut = factory.newAuction(testPlant,players);
        List<Player> immutablePlayers=sut.getPlayers();
        immutablePlayers.add(pink);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getOpenPlayersImmutable() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        List<Player> immutablePlayers=sut.getPlayers();
        immutablePlayers.add(pink);
    }

    @Test
    public void getOpenPlayersMutable() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        List<OpenPlayer> mutablePlayers=sut.getOpenPlayers();
        int compareValue=mutablePlayers.size()+1;
        mutablePlayers.add(pink);
        Assert.assertEquals(compareValue,mutablePlayers.size());
    }

    //set/getPlayer
    @Test
    public void setPlayer() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        sut.setPlayer(pink);
        Assert.assertEquals(pink,sut.getPlayer());
    }

    @Test (expected = NullPointerException.class)
    public void setWrongPlayer() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        sut.setPlayer(null);
    }

    //set Amount
    @Test
    public void setAmount() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        sut.setAmount(1);
        Assert.assertEquals(1,sut.getAmount());
    }

    @Test (expected = Exception.class)
    public void setWrongAmount3() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        sut.setAmount(-1);
    }
    @Test
    public void setWrongAmount4() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        sut.setAmount(0);
        Assert.assertEquals(0,sut.getAmount());
    }

    //getPlant
    @Test public void clearAuction(){
        OpenGame game=factory.newGame(new EditionGermany());
        game.setAuction(null);
        Assert.assertEquals(null,game.getAuction());
    }

    //spezieller zu Aufgabe 6 getPlant:
    @Test
    public void getPlantOpen() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        Assert.assertEquals(testPlant,sut.getPlant());
    }
    @Test
    public void getPlant() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        Auction sut = factory.newAuction(testPlant,players);
        Assert.assertEquals(testPlant,sut.getPlant());
    }
    @Test (expected = UnsupportedOperationException.class)
    public void getPlantOpenAddResources() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        OpenAuction sut = factory.newAuction(testPlant,players);
        Bag<Resource> bag=new MRodicBag<Resource>();
        bag.add(Resource.Oil);
        sut.getPlant().getResources().add(bag);
    }
    @Test (expected = UnsupportedOperationException.class)
    public void getPlantAddResources() {
        List<OpenPlayer> players = new ArrayList<OpenPlayer>();
        players.add(red);
        Auction sut = factory.newAuction(testPlant,players);
        Bag<Resource> bag=new MRodicBag<Resource>();
        bag.add(Resource.Oil);
        sut.getPlant().getResources().add(bag);
    }

}