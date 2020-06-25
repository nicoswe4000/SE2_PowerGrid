import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.ResourceMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;
import org.junit.Assert;
import org.junit.Test;

public class ResourceMarketTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    //private final Factory factory = Factory.newFactory(fcqn);

    @Test
    public void getAvailable() {
        OpenFactory factory=OpenFactory.newFactory(fcqn);
        Edition edition =new EditionGermany();
        ResourceMarket sut = factory.newResourceMarket(edition);
        Assert.assertEquals(24, sut.getAvailable().count(Resource.Coal));
        Assert.assertEquals(18, sut.getAvailable().count(Resource.Oil));
        Assert.assertEquals(6, sut.getAvailable().count(Resource.Garbage));
        Assert.assertEquals(2, sut.getAvailable().count(Resource.Uranium));
    }

    @Test
    public void getSupply() {
        OpenFactory factory=OpenFactory.newFactory(fcqn);
        OpenResourceMarket sut = factory.newResourceMarket(new EditionGermany());
        int sizeOil=sut.getSupply().count(Resource.Oil);
        sut.getOpenSupply().add(Resource.Coal);
        sut.getOpenSupply().add(Resource.Oil);
        Assert.assertEquals(sizeOil+1, sut.getSupply().count(Resource.Oil));
        Assert.assertEquals(1, sut.getSupply().count(Resource.Coal));
    }

    @Test
    public void getPrice() {
        OpenFactory factory=OpenFactory.newFactory(fcqn);
        ResourceMarket sut = factory.newResourceMarket(new EditionGermany());
        OpenFactory factory2=OpenFactory.newFactory(fcqn);
        OpenResourceMarket sut2 = factory2.newResourceMarket(new EditionGermany());
        sut2.getOpenAvailable().remove(Resource.Coal,3);
        Assert.assertNotEquals(sut.getPrice(Resource.Coal), sut2.getPrice(Resource.Coal));
    }
}