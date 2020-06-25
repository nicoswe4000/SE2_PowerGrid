import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-04-16
 */
public class FactoryMethodsTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    // Factory Tests:
    //Player, Plant, City werden versucht zwei mal anzulegen

    @Test
    public void newCity() {
        OpenCity sut = factory.newCity("Baum", 1);
        OpenCity sut2 = factory.newCity("Vogel", 1);
        sut.connect(sut2, 5);
        City sut3 = factory.newCity("Baum", 1);
        Assert.assertEquals(1, sut3.getConnections().size());
    }

    @Test
    public void newCity2() {
        OpenCity sut1 = factory.newCity("Baum", 1);
        OpenCity sut2 = factory.newCity("Baum", 1);
        Assert.assertTrue(sut1 == sut2);
    }

    @Test
    public void newPlayer() {
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("defg", "red");
        Assert.assertTrue(sut2.hasSecret("abc"));
    }

    @Test
    public void newPlayer2() {
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("defg", "red");
        Assert.assertTrue(sut1 == sut2);
    }

    @Test
    public void newPlant() {
        OpenPlant sut1 = factory.newPlant(5, Plant.Type.Eco, 0, 3);
        OpenPlant sut2 = factory.newPlant(5, Plant.Type.Eco, 0, 7);
        Assert.assertEquals(3, sut2.getCities());
    }

    @Test
    public void newPlant2() {
        OpenPlant sut1 = factory.newPlant(5, Plant.Type.Eco, 0, 3);
        OpenPlant sut2 = factory.newPlant(5, Plant.Type.Eco, 0, 7);
        Assert.assertTrue(sut1 == sut2);
    }

    //natueriche Ornung implementieren


    @Test
    public void orderCity() {
        OpenCity sut1 = factory.newCity("Baum", 1);
        OpenCity sut2 = factory.newCity("Vogel", 1);
        Assert.assertTrue(sut1.compareTo(sut2) < 0);
    }

    @Test
    public void orderCity2() {
        OpenCity sut1 = factory.newCity("Baum", 1);
        OpenCity sut2 = factory.newCity("Vogel", 1);
        Assert.assertTrue(sut2.compareTo(sut1) > 0);
    }

    @Test
    public void orderCity3() {
        OpenCity sut1 = factory.newCity("Baum", 1);
        OpenCity sut2 = factory.newCity("Baum", 2);
        Assert.assertEquals(0, sut2.compareTo(sut1));
    }

    //Player nach anzahl Staedte fallend
    @Test
    public void orderPlayer() {
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("defg", "blue");
        OpenCity toAdd = factory.newCity("Baum", 2);
        sut1.getOpenCities().add(toAdd);
        Assert.assertTrue(sut2.compareTo(sut1) > 0);
    }

    @Test
    public void orderPlayer2() {
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("abc", "blue");
        OpenCity toAdd = factory.newCity("Baum", 2);
        sut1.getOpenCities().add(toAdd);
        Assert.assertTrue(sut1.compareTo(sut2) < 0);
    }

    //Player nach Farbe
    @Test
    public void orderPlayer3() {
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("abc", "blue");
        Assert.assertTrue(sut1.compareTo(sut2) > 0);
        Assert.assertTrue(sut2.compareTo(sut1) < 0);
    }

    @Test
    public void orderPlayer4() {
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("kmn", "red");
        Assert.assertEquals(0, sut1.compareTo(sut2));
    }

    //Player nach Kraftwerknr
    @Test
    public void orderPlayer5() {
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Eco, 0, 3);
        OpenPlant plant2 = factory.newPlant(2, Plant.Type.Eco, 0, 3);
        OpenPlant plant3 = factory.newPlant(3, Plant.Type.Eco, 0, 3);
        OpenPlant plant4 = factory.newPlant(4, Plant.Type.Eco, 0, 3);
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("abc", "blue");
        sut1.getOpenPlants().add(plant1);
        sut1.getOpenPlants().add(plant2);
        sut1.getOpenPlants().add(plant3);
        sut2.getOpenPlants().add(plant4);
        Assert.assertTrue(sut1.compareTo(sut2) > 0);
    }

    @Test
    public void orderPlayer6() {
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Eco, 0, 3);
        OpenPlant plant2 = factory.newPlant(2, Plant.Type.Eco, 0, 3);
        OpenPlant plant3 = factory.newPlant(3, Plant.Type.Eco, 0, 3);
        OpenPlant plant4 = factory.newPlant(4, Plant.Type.Eco, 0, 3);
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("abc", "blue");
        OpenCity toAdd1 = factory.newCity("Haus", 2);
        OpenCity toAdd2 = factory.newCity("Baum", 2);
        sut1.getOpenCities().add(toAdd1);
        sut2.getOpenCities().add(toAdd2);
        sut2.getOpenPlants().add(plant1);
        sut1.getOpenPlants().add(plant2);
        sut1.getOpenPlants().add(plant3);
        sut2.getOpenPlants().add(plant4);

        Assert.assertTrue(sut2.compareTo(sut1) < 0);
    }

    @Test
    public void orderPlayer7() {
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Eco, 0, 3);
        OpenPlant plant2 = factory.newPlant(4, Plant.Type.Eco, 0, 3);
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("abc", "blue");
        sut2.getOpenPlants().add(plant1);
        sut2.getOpenPlants().add(plant2);

        Assert.assertTrue(sut2.compareTo(sut1) < 0);
    }

    @Test
    public void orderPlayer8() {
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Eco, 0, 3);
        OpenPlant plant2 = factory.newPlant(4, Plant.Type.Eco, 0, 3);
        OpenPlayer sut1 = factory.newPlayer("abc", "red");
        OpenPlayer sut2 = factory.newPlayer("abc", "blue");
        sut1.getOpenPlants().add(plant1);
        sut1.getOpenPlants().add(plant2);

        Assert.assertTrue(sut2.compareTo(sut1)  >0);
    }

    //Plant Ordnung

    @Test
    public void orderPlant() {
        Plant sut1 = factory.newPlant(5, Plant.Type.Eco, 0, 3);
        Plant sut2 = factory.newPlant(5, Plant.Type.Eco, 0, 7);
        Assert.assertEquals(0, sut1.compareTo(sut2));
    }

    @Test
    public void orderPlant1() {
        Plant sut1 = factory.newPlant(5, Plant.Type.Eco, 0, 3);
        Plant sut2 = factory.newPlant(4, Plant.Type.Eco, 0, 7);
        Assert.assertTrue(sut1.compareTo(sut2) > 0);
    }

    @Test
    public void orderPlant2() {
        Plant sut1 = factory.newPlant(1, Plant.Type.Eco, 0, 3);
        Plant sut2 = factory.newPlant(5, Plant.Type.Eco, 0, 7);
        Assert.assertTrue(sut1.compareTo(sut2) < 0);
    }

}



