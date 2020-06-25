import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import org.junit.Assert;
import org.junit.Test;

public class GermanCityTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test
    public void getNameTest() {
        City sut = factory.newCity("munich", 1);
        Assert.assertEquals("munich", sut.getName());
    }

    @Test
    public void getRegionTest() {
        City sut = factory.newCity("munich", 1);
        Assert.assertEquals(1, sut.getRegion());
    }

    @Test (expected = NullPointerException.class)
    public void constructorFail1() {
        City sut = factory.newCity(null, 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorFail2() {
        City sut = factory.newCity("", 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorFail3() {
        City sut = factory.newCity("munich", 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorFail4() {
        City sut = factory.newCity("munich", -1);
    }

    @Test
    public void connect() {
        OpenCity munich = factory.newCity("munich", 1);
        OpenCity berlin = factory.newCity("berlin", 2);
        OpenCity frankfurt = factory.newCity("frankfurt", 3);

        munich.connect(berlin, 10);
        munich.connect(frankfurt, 20);
        int munichBerlinCost = munich.getOpenConnections().get(berlin);
        int munichFrankfurtCost = munich.getOpenConnections().get(frankfurt);

        Assert.assertTrue(munich.getConnections().containsKey(berlin));
        Assert.assertTrue(munich.getConnections().containsKey(frankfurt));

        Assert.assertEquals(10, munichBerlinCost);
        Assert.assertEquals(20, munichFrankfurtCost);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void closedGetConnections() {
        OpenCity munich = factory.newCity("munich", 1);
        OpenCity berlin = factory.newCity("berlin", 2);
        City frankfurt = factory.newCity("frankfurt", 3);
        munich.connect(berlin, 10);
        munich.close();
        munich.getOpenConnections().put(frankfurt, 20);
    }

    @Test (expected = NullPointerException.class)
    public void nullConnect() {
        OpenCity sut = factory.newCity("munich", 1);
        sut.connect(null, 10);
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativeConnect() {
        OpenCity sut = factory.newCity("munich", 1);
        OpenCity test = factory.newCity("berlin", 2);
        sut.connect(test, -1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void selfConnect() {
        OpenCity sut = factory.newCity("munich", 1);
        sut.connect(sut, 10);
    }

    @Test (expected = IllegalArgumentException.class)
    public void existingConnect() {
        OpenCity sut = factory.newCity("munich", 1);
        OpenCity test = factory.newCity("berlin", 2);
        sut.connect(test, 10);
        sut.connect(test, 10);
    }

    @Test (expected = IllegalStateException.class)
    public void closedConnect() {
        OpenCity sut = factory.newCity("munich", 1);
        OpenCity test = factory.newCity("berlin", 2);
        sut.close();
        sut.connect(test, 10);
    }

    @Test
    public void compareTo() {
        OpenCity munich = factory.newCity("munich", 1);
        OpenCity berlin = factory.newCity("berlin", 2);

        Assert.assertTrue(munich.compareTo(berlin) > 0);
        Assert.assertTrue(berlin.compareTo(munich) < 0);
    }
}