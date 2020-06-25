import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GermanPlantTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test
    public void newPlant() {
        Plant sut = factory.newPlant(1, Plant.Type.Oil, 2, 3);

        Assert.assertEquals(1, sut.getNumber());
        Assert.assertEquals(Plant.Type.Oil, sut.getType());
        Assert.assertEquals(2, sut.getNumberOfResources());
        Assert.assertEquals(3, sut.getCities());
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeNumber() {
        Plant sut = factory.newPlant(-1, Plant.Type.Oil, 2, 3);
    }

    @Ignore
    @Test (expected = IllegalArgumentException.class)
    public void zeroNumber() {
        Plant sut = factory.newPlant(0, Plant.Type.Oil, 2, 3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeNumberResources() {
        Plant sut = factory.newPlant(1, Plant.Type.Oil, -1, 3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeNumberCities() {
        Plant sut = factory.newPlant(1, Plant.Type.Oil, 2, -1);
    }

    @Test
    public void notOperatedTest() {
        Plant sut = factory.newPlant(1, Plant.Type.Oil, 2, 3);
        Assert.assertFalse(sut.hasOperated());
    }

    @Test
    public void setOperatedTest() {
        OpenPlant sut = factory.newPlant(1, Plant.Type.Oil, 2, 3);
        sut.setOperated(true);
        Assert.assertTrue(sut.hasOperated());
    }

    @Test
    public void compareTo() {
        Plant sut1 = factory.newPlant(1, Plant.Type.Oil, 2, 3);
        Plant sut2 = factory.newPlant(4, Plant.Type.Oil, 5, 6);
        Assert.assertTrue(sut1.compareTo(sut2) < 0);
        Assert.assertTrue(sut2.compareTo(sut1) > 0);
    }
}