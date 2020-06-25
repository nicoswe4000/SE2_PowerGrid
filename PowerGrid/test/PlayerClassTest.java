import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.mrodic.powergrid.MRodicBag;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Angelina Hofmann
 * @version 2020-04-16
 */
public class PlayerClassTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test public void newPlayer() {
        // arrange
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "red");
        // act
        // assert
        assertTrue(sut.hasSecret("hush - don't tell!"));
        Assert.assertEquals("red",sut.getColor()) ;
    }


    //Test falsche Angabe
    @Test (expected = NullPointerException.class) public void constructColorNull(){
        Player sut = factory.newPlayer("hush - don't tell!", null);
    }

    //Grundangaben passen
    //getCities
    @Test public void basics(){
        Player sut = factory.newPlayer("keks42!", "blue");
        Assert.assertTrue(sut.getCities().isEmpty());
        Assert.assertTrue(sut.getPlants().isEmpty());
        Assert.assertTrue(sut.getResources().isEmpty());
    }

    //Grundlagen veraenderlich
    @Test public void basicsChange(){
        OpenPlayer sut = factory.newPlayer("keks42!", "blue");
        OpenCity city = factory.newCity("Entenhausen", 2);
        OpenPlant plant = factory.newPlant(1, Plant.Type.Eco, 0, 2);
        Bag<Resource> resource= new MRodicBag<>();
        resource.add(Resource.Oil);

        sut.getOpenCities().add(city);
        sut.getOpenPlants().add(plant);
        sut.getOpenResources().add(resource);


        Assert.assertTrue(sut.getCities().contains(city));
        Assert.assertEquals(1,sut.getCities().size());
        Assert.assertTrue(sut.getPlants().contains(plant));
        Assert.assertEquals(1,sut.getPlants().size());
        Assert.assertTrue(sut.getResources().contains(resource));
        Assert.assertEquals(1,sut.getResources().size());
    }

    //Test setElectro()
    @Test public void setElectro(){
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "blue");
        sut.setElectro(1);
        Assert.assertEquals(1,sut.getElectro());
    }

    @Test (expected = IllegalArgumentException.class) public void setElectroNeg(){
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "blue");
        sut.setElectro(-1);
    }

    @Test public void setElectroNull(){
        OpenPlayer sut = factory.newPlayer("hush - don't tell!", "blue");
        sut.setElectro(0);
        Assert.assertEquals(0,sut.getElectro());
    }

    //Test getSecret
    @Test public void getSecret(){
        Player sut = factory.newPlayer("keks42!", "blue");
        Assert.assertEquals("keks42!",sut.getSecret());
    }

    @Test public void getSecretTwice(){
        Player sut = factory.newPlayer("keks42!", "blue");
        sut.getSecret();
        Assert.assertEquals(null,sut.getSecret());
    }

    //Test hasSecret()
    @Test public void hasSecret(){
        OpenPlayer sut = factory.newPlayer("keks42!", "blue");
        Assert.assertFalse(sut.hasSecret("Kruemel"));
        Assert.assertTrue(sut.hasSecret("keks42!"));
    }

    //hasPassed und setPassed
    @Test public void hasNotPassed(){
        Player sut = factory.newPlayer("keks42!", "blue");
        Assert.assertFalse(sut.hasPassed());
    }

    @Test public void hasPassed(){
        OpenPlayer sut = factory.newPlayer("keks42!", "blue");
        sut.setPassed(true);
        Assert.assertTrue(sut.hasPassed());
    }
}