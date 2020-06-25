import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.PlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlantMarketTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    //private final Factory factory = Factory.newFactory(fcqn);

    OpenPlant test1 = OpenFactory.newFactory(fcqn).newPlant(1, Plant.Type.Coal, 2, 3);
    OpenPlant test2 = OpenFactory.newFactory(fcqn).newPlant(41, Plant.Type.Oil, 5, 6);

    @Test
    public void getActual() {
        OpenFactory factory = OpenFactory.newFactory(fcqn);
        OpenPlantMarket sut = factory.newPlantMarket(new EditionGermany());
        sut.getOpenActual().add(test1);
        sut.getOpenActual().add(test2);
        Set<Plant> testSet = new HashSet<>();
        testSet.add(test1);
        testSet.add(test2);
        Assert.assertEquals(testSet, sut.getActual());
    }

    @Test
    public void removePlant() {
        OpenFactory factory = OpenFactory.newFactory(fcqn);
        OpenPlantMarket sut = factory.newPlantMarket(new EditionGermany());
        sut.getOpenActual().add(test1);
        sut.getOpenActual().add(test2);
        sut.removePlant(1);
        Set<Plant> testSet = new HashSet<>();
        testSet.add(test2);
        Assert.assertEquals(testSet, sut.getActual());
    }

    @Test
    public void getFuture() {
        OpenFactory factory = OpenFactory.newFactory(fcqn);
        OpenPlantMarket sut = factory.newPlantMarket(new EditionGermany());
        sut.getOpenFuture().add(test1);
        sut.getOpenFuture().add(test2);
        Set<Plant> testSet = new HashSet<>();
        testSet.add(test1);
        testSet.add(test2);
        Assert.assertEquals(testSet, sut.getFuture());
    }

    @Test
    public void zeroFuture() {
        OpenFactory factory = OpenFactory.newFactory(fcqn);
        PlantMarket sut = factory.newPlantMarket(new EditionGermany());
        Set<Plant> emptySet = new HashSet<>();
        Assert.assertEquals(emptySet, sut.getFuture());
    }

    @Test
    public void getNumberHidden() {
        OpenFactory factory = OpenFactory.newFactory(fcqn);
        PlantMarket sut = factory.newPlantMarket(new EditionGermany());
        Assert.assertEquals(42, sut.getNumberHidden());
    }

    @Test
    public void findPlant() {
        OpenFactory factory = OpenFactory.newFactory(fcqn);
        OpenPlantMarket sut = factory.newPlantMarket(new EditionGermany());
        sut.getOpenActual().add(test1);
        sut.getOpenHidden().add(test2);
        Assert.assertEquals(test1, sut.findPlant(1));
        Assert.assertEquals(test2, sut.findPlant(41));
    }

    @Test
    public void getHidden() {
        OpenFactory factory = OpenFactory.newFactory(fcqn);
        OpenPlantMarket sut = factory.newPlantMarket(new EditionGermany());
        sut.getOpenHidden().add(test1);
        sut.getOpenHidden().add(test2);
        List<Plant> testList = new ArrayList<>();
        testList.add(test1);
        testList.add(test2);
        Assert.assertTrue(sut.getOpenHidden().containsAll(testList));
    }
}