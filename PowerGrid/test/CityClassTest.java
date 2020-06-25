import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-04-16
 */
public class CityClassTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    //BasisTest Grundfunktionalitaet
    @Test
    public void newCity() {
        // arrange
        OpenCity sut = factory.newCity("Entenhausen", 1);
        // act
        // assert
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());

    }

    //Test zu getName()

    @Test (expected= NullPointerException.class) public void WrongName(){
        OpenCity sut = factory.newCity(null, 1);
    }

    @Test (expected=IllegalArgumentException.class) public void WrongName2(){
        OpenCity sut = factory.newCity("", 1);
    }

    //Tests zu getRegion

    @Test (expected=IllegalArgumentException.class) public void WrongRegion(){
        OpenCity sut = factory.newCity("Entenhausen", 0);
    }

    @Test (expected=IllegalArgumentException.class) public void WrongRegion1(){
        OpenCity sut = factory.newCity("Entenhausen", -1);
    }

    //Tests zu Connect

    @Test public void ConnectCity() {
        // arrange
        OpenCity sut1 = factory.newCity("Entenhausen", 1);
        OpenCity sut2 = factory.newCity("Muenchen", 1);
        OpenCity sut3 = factory.newCity("Babenberg", 2);
        // act
        sut1.connect(sut2,0);
        sut1.connect(sut3,3);
        int region1=sut1.getOpenConnections().get(sut2);
        int region2=sut1.getOpenConnections().get(sut3);
        // assert
        Assert.assertTrue(sut1.getOpenConnections().containsKey(sut2));
        Assert.assertEquals(0,region1);
        Assert.assertEquals(3,region2);
    }

    @Test public void ConnectCityNoChangeInCity2() {
        // arrange
        OpenCity sut1 = factory.newCity("Entenhausen", 1);
        OpenCity sut2 = factory.newCity("Muenchen", 1);
        OpenCity sut3 = sut2;
        // act
        sut1.connect(sut2,0);
        // assert
        Assert.assertEquals(sut3,sut2);
    }

    @Test (expected=NullPointerException.class) public void ConnectToNull(){
        OpenCity sut = factory.newCity("Entenhausen", 2);
        sut.connect(null, 4);
    }

    @Test (expected=IllegalArgumentException.class) public void ConnectToSelf(){
        OpenCity sut = factory.newCity("Entenhausen", 2);
        sut.connect(sut, 4);
    }

    @Test (expected=IllegalArgumentException.class) public void ConnectKostsNegtiv(){
        OpenCity sut1 = factory.newCity("Entenhausen", 2);
        OpenCity sut2 = factory.newCity("Montagu", 2);
        sut1.connect(sut2, -2);
    }

    @Test (expected=IllegalArgumentException.class) public void ConnectExistentConnection(){
        OpenCity sut1 = factory.newCity("Entenhausen", 2);
        OpenCity sut2 = factory.newCity("Montagu", 2);
        sut1.connect(sut2, 2);
        sut1.connect(sut2, 5);
    }

    @Test (expected=IllegalStateException.class) public void ConnectAfterClose(){
        OpenCity sut1 = factory.newCity("Entenhausen", 2);
        OpenCity sut2 = factory.newCity("Montagu", 2);
        sut1.close();
        sut1.connect(sut2, 2);
    }

    //Tests zu get Connections

   @Ignore
   @Test (expected = IllegalStateException.class)public void GetEmptyConnections(){
        OpenCity sut = factory.newCity("doma", 2);
        sut.getOpenConnections();
    }

    @Test public void GetConnectionBeforClose(){
        OpenCity sut1 = factory.newCity("Entenhausen", 2);
        OpenCity sut2 = factory.newCity("Montagu", 2);
        OpenCity sut3 = factory.newCity("Muenchen", 3);
        sut1.connect(sut2, 2);
        sut1.getOpenConnections().put(sut3,7);
        Assert.assertTrue(sut1.getConnections().containsKey(sut3));
    }

    @Test (expected=UnsupportedOperationException.class) public void GetConnectionAfterClose() {
        OpenCity sut1 = factory.newCity("Entenhausen", 2);
        OpenCity sut2 = factory.newCity("Montagu", 2);
        OpenCity sut3 = factory.newCity("Muenchen", 3);
        sut1.connect(sut2, 2);
        sut1.close();
        sut1.getOpenConnections().put(sut3, 7);
    }

    //Tests zu Close

    @Test (expected=IllegalStateException.class) public void CloseClosed(){
        OpenCity sut1 = factory.newCity("Entenhausen", 2);
        sut1.close();
        sut1.close();
    }




}