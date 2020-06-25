import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
/**
 * @author Mirna Rodic, IF2B, mrodic@hm.edu
 * @version 11.05.2020
 */

public class SichtenAufDatastoreTest {
    //SmokeTest vom Prof.
        //@Rule
        //public final Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

        /** Factory. */
        private final OpenFactory factory;
        private final OpenFactory factory2;
        List<OpenPlayer> playersList;
    OpenPlant plantStandard;

        /** Initialisiert die Factory. */
        public SichtenAufDatastoreTest() throws IOException {
            // TODO: Ersetzen Sie den Wert durch den FQCN Ihrer Factory-Implementierung
            System.setProperty("powergrid.factory", "edu.hm.mrodic.powergrid.datastore.MRodicFactory");
            factory = OpenFactory.newFactory();
            factory2=OpenFactory.newFactory();
            playersList= new ArrayList<>();
            playersList.add(factory.newPlayer("123","one"));
            playersList.add(factory.newPlayer("456","two"));
            playersList.add(factory.newPlayer("789","three"));
            plantStandard=factory.newPlant(99, Plant.Type.Eco,2,30);
        }

        /** Eine Ausgabe. */
        private Edition edition = new EditionGermany();

        @Test public void testFactoryInitialized() {
            assertNotNull("factory exists", factory);
        }

        @Test public void testOpenCityIsModifiable() {
            OpenCity sut = factory.newCity("Duckburg", 1);
            sut.connect(factory.newCity("ja",1),3);
            OpenCity other = factory.newCity("Mouseton", 1);
            sut.getOpenConnections().put(other, 1);
            assertEquals("factory creates mutable cities", 1, (long)sut.getConnections().get(other));
        }

        @Test(expected = UnsupportedOperationException.class) public void testCityIsReadonly() {
            City sut = factory.newCity("Duckburg", 1);
            City other = factory.newCity("Mouseton", 1);
            sut.getConnections().put(other, 1);
        }

        @Test public void testOpenBoardIsModifiable() {
            OpenBoard board = factory.newBoard(edition);
            assertEquals("board holds specified cities",
                    edition.getCitySpecifications().size(),
                    board.getCities().size());
            board.getOpenCities().add(factory.newCity("Duckburg", 1));
            assertTrue("factory creates mutable board", board.findCity("Duckburg") != null);
        }

        @Test(expected = UnsupportedOperationException.class) public void testClosedOpenBoardIsReadonly() {
            OpenBoard board = factory.newBoard(edition);
            board.close();
            board.getOpenCities().add(factory.newCity("Duckburg", 1));
        }

        @Test(expected = UnsupportedOperationException.class) public void testBoardIsReadonly() {
            Board board = factory.newBoard(edition);
            board.getCities().add(factory.newCity("Duckburg", 1));
        }

        @Test(expected = UnsupportedOperationException.class) public void testBoardIsDeeplyReadonly() {
            Board board = factory.newBoard(edition);
            City other = factory.newCity("Mouseton", 1);
            City sut = board.getCities().iterator().next();
            sut.getConnections().put(other, 1);
        }

        @Test public void testMutability() {
            PlantMarket market;
            OpenPlantMarket openMarket;

            Game game = factory.newGame(edition);
            market = game.getPlantMarket();
            // openMarket = game.getPlantMarket(); // Compilerfehler

            OpenGame openGame = factory.newGame(edition);
            market = openGame.getPlantMarket();
            openMarket = openGame.getPlantMarket();
        }

        @Test
        public void testCollectionMutability() {
            List<? extends Player> players;
            List<OpenPlayer> openPlayers;

            Game game = factory.newGame(edition);
            players = game.getPlayers();
            // players.add(null);                      // Laufzeitfehler
            // openPlayers = game.getPlayers();        // Compilerfehler

            OpenGame openGame = factory.newGame(edition);
            players = openGame.getOpenPlayers();
            players.add(null);                          // ok
            openPlayers = openGame.getOpenPlayers();    // ok
            openPlayers.add(null);                      // ok
        }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Beispieltests aus den Angaben
        @Test public void bsp1(){
            OpenPlantMarket writable = factory.newPlantMarket(edition);
            writable.findPlant(3).setOperated(true);
            Assert.assertEquals(true,writable.findPlant(3).hasOperated());
        }
    @Test (expected = UnsupportedOperationException.class)public void bsp3(){
        OpenGame writable = factory.newGame(edition);
        writable.getPlayers().remove(0);
    }
    @Test (expected = UnsupportedOperationException.class)public void bsp4(){
        OpenGame writable = factory.newGame(edition);
        Game readonly = writable;
        readonly.getPlayers().remove(0);
    }


//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //eigene Tests für die Open Sicht
    @Test (expected = IllegalStateException.class) public void OpenCityClose(){
        OpenCity sut=factory.newCity("MRodic",5);
        sut.close();
        sut.close();
    }
    @Test (expected = IllegalStateException.class) public void OpenCityClosedConnect(){
        OpenCity sut=factory.newCity("MRodic",5);
        sut.close();
        sut.connect(factory.newCity("abc",1),2);
    }
    @Test public void OpenCityConnect(){
        OpenCity sut=factory.newCity("abc",5);
        OpenCity sut2=factory.newCity("def",1);
        sut.connect(sut2,2);
        Assert.assertEquals(true,sut.getConnections().containsKey(sut2));

    }
    @Test public void OpenCityGetConnections(){
        OpenCity sut=factory.newCity("abc",5);
        sut.connect(factory.newCity("done",1),5);
        OpenCity sut2=factory.newCity("xyz",1);
        sut.getOpenConnections().put(sut2,6);
        Assert.assertEquals(true,sut.getConnections().containsKey(sut2));

    }
    @Test public void OpenAuctionGetPlayers(){
            OpenPlant plant=factory.newPlant(1, Plant.Type.Eco,2,4);
            edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction sut=factory.newAuction(plant,playersList);
            sut.getOpenPlayers().add(factory.newPlayer("159","four"));
            Assert.assertEquals(true,sut.getPlayers().contains(factory.newPlayer("159","four")));
    }
    @Test public void OpenAuctionGetSetPlayer(){
        OpenPlant plant=factory.newPlant(1, Plant.Type.Eco,2,4);
        edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction sut=factory.newAuction(plant,playersList);
        sut.setPlayer(factory.newPlayer("159","four"));
        Assert.assertEquals(factory.newPlayer("159","four"),sut.getPlayer());
    }
    @Test public void OpenAuctionGetPlayer(){
        OpenPlant plant=factory.newPlant(1, Plant.Type.Eco,2,4);
        edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction sut=factory.newAuction(plant,playersList);
        OpenPlayer player=factory.newPlayer("147","five");
        sut.getOpenPlayers().add(player);
        sut.setPlayer(player);
        OpenPlayer sut2 = sut.getPlayer();
        sut2.setElectro(40);
        Assert.assertEquals(40,sut2.getElectro());
    }
    @Test public void OpenAuctionGetPlant(){
        OpenPlant plant=factory.newPlant(1, Plant.Type.Eco,2,4);
        edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction sut=factory.newAuction(plant,playersList);
        Assert.assertEquals(true,sut.getPlant()==plant);
    }
    @Test public void OpenAuctionSetAmount(){
        OpenPlant plant=factory.newPlant(1, Plant.Type.Eco,2,4);
        OpenAuction sut=factory.newAuction(plant,playersList);
        sut.setAmount(100);
        Assert.assertEquals(100,sut.getAmount());
    }
    @Test public void OpenBoardGetCities(){
            OpenBoard sut=factory.newBoard(edition);
            Assert.assertEquals(true,sut.getOpenCities().add(factory.newCity("six",8)));
    }
    @Test public void OpenBoardCloseRegions(){
        OpenBoard sut=factory.newBoard(edition);
        OpenCity city=sut.findCity("Stuttgart");
        sut.closeRegions(2);
        Assert.assertEquals(false,sut.getCities().contains(city));
    }
    @Test public void OpenBoardFindCity(){
        OpenBoard sut=factory.newBoard(edition);
        OpenCity city=sut.findCity("Flensburg");
        OpenCity city2=factory.newCity("idontknow",1);
       city.connect(city2,3);
       Assert.assertEquals(true,city.getConnections().containsKey(city2));
    }
    @Test(expected = IllegalStateException.class) public void OpenBoardClose(){
            OpenBoard board =factory.newBoard(edition);
            board.close();
            board.close();
    }
    @Test public void OpenPlantSetOperated(){
            OpenPlant sut=factory.newPlant(60, Plant.Type.Eco,4,3);
            sut.setOperated(true);
            Assert.assertEquals(true,sut.hasOperated());
    }
    @Test public void OpenResourceMGetAvailable(){
            OpenResourceMarket sut=factory.newResourceMarket(edition);
            Assert.assertEquals(true,sut.getOpenAvailable().add(Resource.Coal));
    }
    @Test public void OpenResourceMGetSupply(){
            OpenResourceMarket sut=factory.newResourceMarket(edition);
            Assert.assertEquals(true,sut.getOpenSupply().add(Resource.Garbage));
    }
    @Test public void OpenPlayerGetCities(){
            OpenPlayer sut=factory.newPlayer("abcd","none");
            Assert.assertEquals(true,sut.getOpenCities().add(factory.newCity("probe",4)));
    }
    @Test public void OpenPlayerGetPlants(){
        OpenPlayer sut=factory.newPlayer("abcd","none");
        Assert.assertEquals(true,sut.getOpenPlants().add(factory.newPlant(66, Plant.Type.Hybrid,2,4)));
    }
    @Test public void OpenPlayerGetResources(){
        OpenPlayer sut=factory.newPlayer("abcd","none");
        Assert.assertEquals(true,sut.getOpenResources().add(Resource.Uranium));
    }
    @Test public void OpenPlayerHasSecret(){
        OpenPlayer sut=factory.newPlayer("abcd","none");
        Assert.assertEquals(true,sut.hasSecret("abcd"));
        Assert.assertEquals(false,sut.hasSecret("1234"));
    }
    @Test public void OpenPlayerSetElectro(){
        OpenPlayer sut=factory.newPlayer("abcd","none");
        sut.setElectro(101);
        Assert.assertEquals(101,sut.getElectro());
    }
    @Test public void OpenPlayerSetPassed(){
        OpenPlayer sut=factory.newPlayer("abcd","none");
        Assert.assertEquals(false,sut.hasPassed());
        sut.setPassed(true);
        Assert.assertEquals(true,sut.hasPassed());
    }
    @Test public void OpenPlantMfindPlant(){
            OpenPlantMarket sut=factory.newPlantMarket(edition);
            OpenPlant plant=sut.findPlant(5);
            plant.setOperated(true);
            Assert.assertEquals(true,plant.hasOperated());
    }
    @Test public void OpenPlantMRemovePlant(){
            OpenPlantMarket sut =factory.newPlantMarket(edition);
            sut.removePlant(5);
            Assert.assertEquals(null,sut.findPlant(5));
    }
    @Test public void OpenPlantMGetActual(){
        OpenPlantMarket sut =factory.newPlantMarket(edition);
        Assert.assertEquals(true,sut.getOpenActual().add(factory.newPlant(202, Plant.Type.Eco,2,3)));
    }
    @Test public void OpenPlantMGetFuture(){
        OpenPlantMarket sut =factory.newPlantMarket(edition);
        Assert.assertEquals(true,sut.getOpenFuture().add(factory.newPlant(81, Plant.Type.Garbage,1,1)));
        OpenPlant plant=factory.newPlant(6, Plant.Type.Garbage,1,1);
        Assert.assertEquals(true,sut.findPlant(6)==plant);
    }
    @Test public void OpenPlantMGetHidden(){
        OpenPlantMarket sut =factory.newPlantMarket(edition);
        Assert.assertEquals(true,sut.getOpenActual().add(factory.newPlant(81, Plant.Type.Eco,2,3)));
    }
    @Test public void OpenGameGetBoard(){
            OpenGame sut=factory.newGame(edition);
            OpenBoard board=factory.newBoard(edition);
            Assert.assertEquals(true, sut.getBoard()==board);
    }
    @Test public void OpenGameGetPlayers(){
            OpenGame sut=factory.newGame(edition);
            Assert.assertEquals(true,sut.getOpenPlayers().add(factory.newPlayer("123456","test")));
    }
    @Test public void OpenGameGetPlantM(){
            OpenGame sut=factory.newGame(edition);
            sut.getPlantMarket().getOpenActual().add(factory.newPlant(81, Plant.Type.Eco,2,3));
            Assert.assertEquals(factory.newPlant(81, Plant.Type.Eco,2,3),sut.getPlantMarket().removePlant(81));
    }
    @Test public void OpenGameGetResourceM(){
            OpenGame sut=factory.newGame(edition);
            Assert.assertEquals(true,sut.getResourceMarket().getOpenSupply().remove(Resource.Uranium));
    }
    @Test public void OpenGameGetFactory(){
            OpenGame sut=factory.newGame(edition);
            Assert.assertEquals(true,sut.getFactory()==factory);
    }
    @Test public void OpenGameGetSetAuction(){
            OpenGame sut=factory.newGame(edition);
            OpenPlant plant=factory.newPlant(77, Plant.Type.Hybrid,1,2);
            edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction auction=factory.newAuction(plant,playersList);
            sut.setAuction(auction);
            Assert.assertEquals(true,sut.getAuction()==auction);
    }
    @Test public void OpenGameSetLevel(){
            OpenGame sut=factory.newGame(edition);
            sut.setLevel(3);
            Assert.assertEquals(3,sut.getLevel());
    }
    @Test public void OpenGameSetPhase(){
            OpenGame sut=factory.newGame(edition);
            sut.setPhase(Phase.Building);
            Assert.assertEquals(Phase.Building,sut.getPhase());
    }
    @Test public void OpenGameSetRound(){
            OpenGame sut=factory.newGame(edition);
            sut.setRound(5);
            Assert.assertEquals(5,sut.getRound());
    }
    @Test public void OpenGameSetNumMoves(){
            OpenGame sut=factory.newGame(edition);
            sut.setNumMoves(40);
            Assert.assertEquals(40,sut.getNumMoves());
    }
    @Test public void OpenGameFindPlayer(){
            OpenGame sut=factory.newGame(edition);
        sut.getOpenPlayers().add(factory.newPlayer("123456","test"));
            Assert.assertEquals(true, sut.getOpenPlayers().remove(sut.findPlayer("123456"))); //siehe test gameGetPlayers
    }


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //eigene Tests für readOnly View
    @Test (expected =UnsupportedOperationException.class) public void auctionGetPlayers(){
            OpenAuction sut=factory2.newAuction(plantStandard,playersList);
            sut.getPlayers().add(factory.newPlayer("123","white"));
    }
    @Test (expected = UnsupportedOperationException.class) public void auctionGetPlayer(){
        edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction sut=factory2.newAuction(plantStandard,playersList);
        sut.setPlayer(factory2.newPlayer("123","highest"));
        OpenAuction sut2=sut;
        sut2.getPlayer().getCities().add(factory.newCity("Probe",1));
    }
    @Test (expected =UnsupportedOperationException.class) public void auctionGetPlant(){
        OpenAuction sut=factory2.newAuction(plantStandard,playersList);
        sut.getPlant().getResources().add(null);
    }
    @Test (expected = UnsupportedOperationException.class) public void boardFindCity(){
            Board sut=factory2.newBoard(edition);
            sut.findCity("Essen").getConnections().clear();
    }
    @Test (expected = UnsupportedOperationException.class) public void boardGetCities(){
        Board sut=factory2.newBoard(edition);
        sut.getCities().clear();
    }
    @Test(expected =UnsupportedOperationException.class) public void cityGetConnections(){
            City sut=factory2.newCity("MRodic",3);
            sut.getConnections().put(factory2.newCity("none",2),5);
    }
    @Test(expected =IllegalStateException.class) public void cityClose(){
        City sut=factory2.newCity("MRodic",3);
        sut.close();
        sut.close();
    }
    @Test public void gameGetEdition(){
            Game sut=factory2.newGame(edition);
            Assert.assertEquals(true,sut.getEdition()==edition);
    }
    @Test (expected = UnsupportedOperationException.class) public void gameGetBoard(){
            Game sut=factory2.newGame(edition);
            sut.getBoard().getCities().clear();
    }
    @Test (expected = UnsupportedOperationException.class) public void gameGetPlayers(){
        Game sut=factory2.newGame(edition);
        sut.getPlayers().add(factory2.newPlayer("1234","mrodic"));
    }
    @Test (expected = UnsupportedOperationException.class) public void gameGetPlantM(){
        Game sut=factory2.newGame(edition);
        sut.getPlantMarket().getActual().clear();
    }
    @Test public void gameGetPlantMHidden(){
        Game sut=factory2.newGame(edition);
        Assert.assertEquals(edition.getPlantSpecifications().size(),sut.getPlantMarket().getNumberHidden());
    }
    @Test (expected = UnsupportedOperationException.class) public void gameGetResourceM(){
        Game sut=factory2.newGame(edition);
        sut.getResourceMarket().getAvailable().add(Resource.Oil);
    }
    @Test (expected = UnsupportedOperationException.class) public void gameGetAuction(){
            OpenGame sut2=factory2.newGame(edition);
            sut2.setAuction(factory2.newAuction(plantStandard,playersList));
        Game sut=sut2;
        sut.getAuction().getPlayers().clear();
    }
    @Test (expected = UnsupportedOperationException.class) public void gameFindPlayer(){
        OpenGame sut2=factory2.newGame(edition);
        OpenPlayer player=factory2.newPlayer("111","player");
        sut2.getPlayers().add(player);
        Game sut=sut2;
        sut.findPlayer("111").getCities().add(factory2.newCity("exception",2));
    }
    @Test (expected=UnsupportedOperationException.class) public void plantMGetActual(){
            PlantMarket sut=factory2.newPlantMarket(edition);
            sut.getActual().add(plantStandard);
    }
    @Test (expected=UnsupportedOperationException.class) public void plantMGetFuture(){
        PlantMarket sut=factory2.newPlantMarket(edition);
        sut.getFuture().add(plantStandard);
    }
    @Test public void plantMFindPlantNull(){
        PlantMarket sut=factory2.newPlantMarket(edition);
        Assert.assertEquals(null,sut.findPlant(75));
    }
    @Test (expected=UnsupportedOperationException.class) public void plantMFindPlant(){
        PlantMarket sut=factory2.newPlantMarket(edition);
        sut.findPlant(20).getResources().clear();
    }
    @Test (expected = UnsupportedOperationException.class) public void playerGetCities(){
            Player sut=factory2.newPlayer("secret","sut");
            sut.getCities().add(factory2.newCity("gehtNicht",2));
    }
    @Test (expected = UnsupportedOperationException.class) public void playerGetPlants(){
        Player sut=factory2.newPlayer("secret","sut");
        sut.getPlants().add(plantStandard);
    }
    @Test (expected = UnsupportedOperationException.class) public void playerGetResources(){
        Player sut=factory2.newPlayer("secret","sut");
        sut.getResources().add(Resource.Garbage);
    }
    @Test(expected = UnsupportedOperationException.class) public void resourceMGetAvailable(){
            ResourceMarket sut=factory2.newResourceMarket(edition);
            sut.getAvailable().clear();
    }
    @Test(expected = UnsupportedOperationException.class) public void resourceMGetSupply(){
        ResourceMarket sut=factory2.newResourceMarket(edition);
        sut.getSupply().add(Resource.Uranium);
    }
}
