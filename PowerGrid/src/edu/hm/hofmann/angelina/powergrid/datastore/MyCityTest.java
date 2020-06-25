package edu.hm.hofmann.angelina.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * @author A. Hofmann, angelina.hofmann@hm.edu
 * @version 2020-03-27
 */
/*public class MyCityTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final String fqcn = "edu.hm.hofmann.angelina.powergrid.datastore.MyFactory";
    private final Factory factory = Factory.newFactory(fqcn);

    //BasisTest Grundfunktionalität
    @Test
    public void newCity() {
        // arrange
        City sut = factory.newCity("Entenhausen", 1);
        // act
        // assert
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());

    }

    //Test zu getName()

    @Test (expected=IllegalArgumentException.class) public void WrongName(){
        City sut = factory.newCity(null, 1);
    }

    @Test (expected=IllegalArgumentException.class) public void WrongName2(){
        City sut = factory.newCity("", 1);
    }

    //Tests zu getRegion

    @Test (expected=IllegalArgumentException.class) public void WrongRegion(){
        City sut = factory.newCity("Entenhausen", 0);
    }

    @Test (expected=IllegalArgumentException.class) public void WrongRegion1(){
        City sut = factory.newCity("Entenhausen", -1);
    }

    //Tests zu Connect

    @Test public void ConnectCity() {
        // arrange
        City sut1 = factory.newCity("Entenhausen", 1);
        City sut2 = factory.newCity("Muenchen", 1);
        City sut3 = factory.newCity("Babenberg", 2);
        // act
        sut1.connect(sut2,0);
        sut1.connect(sut3,3);
        int region1=sut1.getConnections().get(sut2);
        int region2=sut1.getConnections().get(sut3);
        // assert
        Assert.assertTrue(sut1.getConnections().containsKey(sut2));
        Assert.assertEquals(0,region1);
        Assert.assertEquals(3,region2);
    }

    @Test public void ConnectCityNoChangeInCity2() {
        // arrange
        City sut1 = factory.newCity("Entenhausen", 1);
        City sut2 = factory.newCity("Muenchen", 1);
        City sut3 = sut2;
        // act
        sut1.connect(sut2,0);
        // assert
        Assert.assertEquals(sut3,sut2);
    }

    @Test (expected=IllegalArgumentException.class) public void ConnectToNull(){
        City sut = factory.newCity("Entenhausen", 2);
        sut.connect(null, 4);
    }

    @Test (expected=IllegalArgumentException.class) public void ConnectToSelf(){
        City sut = factory.newCity("Entenhausen", 2);
        sut.connect(sut, 4);
    }

    @Test (expected=IllegalArgumentException.class) public void ConnectKostsNegtiv(){
        City sut1 = factory.newCity("Entenhausen", 2);
        City sut2 = factory.newCity("Montagu", 2);
        sut1.connect(sut2, -2);
    }

    @Test (expected=IllegalArgumentException.class) public void ConnectExistentConnection(){
        City sut1 = factory.newCity("Entenhausen", 2);
        City sut2 = factory.newCity("Montagu", 2);
        sut1.connect(sut2, 2);
        sut1.connect(sut2, 5);
    }

    @Test (expected=IllegalStateException.class) public void ConnectAfterClose(){
        City sut1 = factory.newCity("Entenhausen", 2);
        City sut2 = factory.newCity("Montagu", 2);
        sut1.close();
        sut1.connect(sut2, 2);
    }

    //Tests zu get Connections

    @Test (expected=IllegalArgumentException.class) public void GetEmptyConnections(){
        City sut = factory.newCity("Entenhausen", 2);
        sut.getConnections();
    }

    //toDo test fur connections=Null schreiben

    @Test public void GetConnectionBeforClose(){
        City sut1 = factory.newCity("Entenhausen", 2);
        City sut2 = factory.newCity("Montagu", 2);
        City sut3 = factory.newCity("Muenchen", 3);
        sut1.connect(sut2, 2);
        sut1.getConnections().put(sut3,7);
        Assert.assertTrue(sut1.getConnections().containsKey(sut3));
    }

    @Test (expected=UnsupportedOperationException.class) public void GetConnectionAfterClose() {
        City sut1 = factory.newCity("Entenhausen", 2);
        City sut2 = factory.newCity("Montagu", 2);
        City sut3 = factory.newCity("Muenchen", 3);
        sut1.connect(sut2, 2);
        sut1.close();
        sut1.getConnections().put(sut3, 7);
    }

    //Tests zu Close

    @Test (expected=IllegalStateException.class) public void CloseClosed(){
        City sut1 = factory.newCity("Entenhausen", 2);
        sut1.close();
        sut1.close();
    }

 */