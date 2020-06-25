import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-16
 */
public class PlayerAndPlantTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);
    //von dem Prof.
    @Test public void newPlant() {
        // arrange
        Plant plant = factory.newPlant(3, Plant.Type.Oil, 2, 1);
        // act
        // assert
        Assert.assertEquals(3, plant.getNumber());
        Assert.assertEquals(Plant.Type.Oil, plant.getType());
        Assert.assertEquals(2, plant.getNumberOfResources());
        Assert.assertEquals(1, plant.getCities());
    }

    @Test (expected=IllegalArgumentException.class) public void CtorPlant1(){
        Plant plant=factory.newPlant(-3, Plant.Type.Coal,3,4);
    }
    @Test (expected=NullPointerException.class) public void CtorPlant2(){
        Plant plant=factory.newPlant(5, null,3,4);
    }
    @Test (expected=IllegalArgumentException.class) public void CtorPlant3(){
        Plant plant=factory.newPlant(5, Plant.Type.Coal,-3,4);
    }
    @Test (expected=IllegalArgumentException.class) public void CtorPlant4(){
        Plant plant=factory.newPlant(5, Plant.Type.Coal,3,0);
    }
    @Test (expected=IllegalArgumentException.class) public void CtorPlant5(){
        Plant plant=factory.newPlant(5, Plant.Type.Coal,3,-8);
    }
    @Test public void getNumber(){
        Plant plant=factory.newPlant(5, Plant.Type.Coal,3,5);
        Assert.assertEquals(5,plant.getNumber());
    }
    @Test public void getCities(){
        Plant plant=factory.newPlant(5, Plant.Type.Coal,3,2);
        Assert.assertEquals(2,plant.getCities());
    }
    @Test public void getNumberResources(){
        Plant plant=factory.newPlant(5, Plant.Type.Coal,3,2);
        Assert.assertEquals(3,plant.getNumberOfResources());
    }
    @Test public void getType(){
        Plant plant=factory.newPlant(5, Plant.Type.Coal,3,2);
        Assert.assertEquals(Plant.Type.Coal,plant.getType());
    }
    @Test public void operated(){
        OpenPlant plant=factory.newPlant(5, Plant.Type.Coal,3,2);
        boolean sut1=plant.hasOperated();
        plant.setOperated(true);
        boolean sut2=plant.hasOperated();
        plant.setOperated(false);
        boolean sut3=plant.hasOperated();
        Assert.assertEquals(false,sut1);
        Assert.assertEquals(true,sut2);
        Assert.assertEquals(false,sut3);
    }



    //von dem Prof.
    @Test public void newPlayer() {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        // act
        // assert
        assertTrue(sut.hasSecret("hush - don't tell!"));
    }
    @Test public void hasColor(){
        OpenPlayer player=factory.newPlayer("12345","blue");
        Assert.assertEquals("blue",player.getColor());
    }
    @Test (expected=NullPointerException.class) public void CtorPlayer1(){
        Player player=factory.newPlayer(null,"red");
    }
    @Test (expected=NullPointerException.class) public void CtorPlayer2(){
        Player player=factory.newPlayer("12345",null);
    }
    @Test public void getCitiesPlayer(){
        Player player=factory.newPlayer("12345","red");
        boolean sut=player.getCities().isEmpty();
        Assert.assertEquals(true,sut);
    }
    @Test public void getPLantsPlayer(){
        Player player=factory.newPlayer("12345","red");
        boolean sut=player.getPlants().isEmpty();
        Assert.assertEquals(true,sut);
    }

    @Test public void electro(){
        OpenPlayer player=factory.newPlayer("12345","red");
        Assert.assertEquals(0,player.getElectro());
        player.setElectro(5);
        Assert.assertEquals(5,player.getElectro());
    }
    @Test (expected=IllegalArgumentException.class) public void electroException(){
        OpenPlayer player=factory.newPlayer("12345","red");
        player.setElectro(-5);
    }
    @Test public void passed(){
        OpenPlayer player=factory.newPlayer("12345","red");
        Assert.assertEquals(false, player.hasPassed());
        player.setPassed(true);
        Assert.assertEquals(true,player.hasPassed());
    }
    @Test public void secret1(){
        Player player=factory.newPlayer("12345","red");
        player.getSecret();
        Assert.assertEquals(null,player.getSecret());
    }
    @Test public void secret2(){
        OpenPlayer player=factory.newPlayer("12345","red");
        Assert.assertEquals(true,player.hasSecret("12345"));
    }
    @Test public void secret3(){
        OpenPlayer player=factory.newPlayer("12345","red");
        Assert.assertEquals(false,player.hasSecret("1234"));
    }

}
