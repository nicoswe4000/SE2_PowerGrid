package edu.hm.hofmann.angelina.powergrid.datastore;

import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Board;
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
/*public class MyFactoryTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test
    private final String fqcn = "edu.hm.hofmann.angelina.powergrid.datastore.MyFactory";
    private final Factory factory = Factory.newFactory(fqcn);

    //Bezug zu newCity

    @Test(expected = IllegalArgumentException.class) public void newCityNameNull(){
        City sut = factory.newCity(null, 1);
    }

    @Test(expected = IllegalArgumentException.class) public void newCityNameEmpty(){
        City sut = factory.newCity("", -1);
    }

    @Test(expected = IllegalArgumentException.class) public void newCityRegion0(){
        City sut = factory.newCity("", 0);
    }

    @Test(expected = IllegalArgumentException.class) public void newCityRegionNegativ(){
        City sut = factory.newCity("", -1);
    }

    @Test
    public void newBoardCities() {
        Board sut = factory.newBoard(new EditionGermany());
        assertEquals("Konstanz",sut.findCity("Konstanz").getName());
        assertEquals("Trier",sut.findCity("Trier").getName());
        assertEquals("Würzburg",sut.findCity("Würzburg").getName());
        assertEquals("München",sut.findCity("München").getName());
        assertEquals("Flensburg",sut.findCity("Flensburg").getName());
    }

    @Test public void newBoardConnections1(){
        Board sut = factory.newBoard(new EditionGermany());
        City augsburg= sut.findCity("Augsburg");
        City duisburg= sut.findCity("Duisburg");
        City flensburg= sut.findCity("Flensburg");
        City kiel= sut.findCity("Kiel");
        Assert.assertEquals(6,augsburg.getConnections().size());
        Assert.assertEquals(1,duisburg.getConnections().size());
        Assert.assertEquals(1,flensburg.getConnections().size());
        Assert.assertEquals(3,kiel.getConnections().size());
    }

    @Test public void newBoardConnections2(){
        Board sut = factory.newBoard(new EditionGermany());
        City augsburg= sut.findCity("Augsburg");
        City muenchen= sut.findCity("München");
        City flensburg= sut.findCity("Flensburg");
        City kiel= sut.findCity("Kiel");
        Assert.assertTrue(augsburg.getConnections().containsKey(muenchen));
        Assert.assertTrue(muenchen.getConnections().containsKey(augsburg));
        Assert.assertTrue(flensburg.getConnections().containsKey(kiel));
        Assert.assertTrue(kiel.getConnections().containsKey(flensburg));
    }

    @Test
    public void newCity() {
        // arrange
        City sut = factory.newCity("Entenhausen", 1);
        // act
        // assert
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());

    }

    @Test
    public void newBoard() {
        // arrange
        Board sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        assertFalse(sut.getCities().isEmpty());
    }

 */