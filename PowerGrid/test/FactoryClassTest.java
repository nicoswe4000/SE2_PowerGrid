import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-04-16
 */
public class FactoryClassTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    //Bezug zu newCity

    @Test(expected = NullPointerException.class) public void newCityNameNull(){
        OpenCity sut = factory.newCity(null, 1);
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
        OpenBoard sut = factory.newBoard(new EditionGermany());
        assertEquals("Konstanz",sut.findCity("Konstanz").getName());
        assertEquals("Trier",sut.findCity("Trier").getName());
        assertEquals("Flensburg",sut.findCity("Flensburg").getName());
    }

    @Test public void newBoardConnections1(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        OpenCity augsburg= sut.findCity("Augsburg");
        OpenCity duisburg= sut.findCity("Duisburg");
        OpenCity flensburg= sut.findCity("Flensburg");
        OpenCity kiel= sut.findCity("Kiel");
        Assert.assertEquals(6,augsburg.getConnections().size());
        Assert.assertEquals(1,duisburg.getConnections().size());
        Assert.assertEquals(1,flensburg.getConnections().size());
        Assert.assertEquals(3,kiel.getConnections().size());
    }

    @Test public void newBoardConnections2(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        OpenCity augsburg= sut.findCity("Augsburg");
        OpenCity muenchen= sut.findCity("M\u00FCnchen");
        OpenCity flensburg= sut.findCity("Flensburg");
        OpenCity kiel= sut.findCity("Kiel");
        Assert.assertTrue(augsburg.getConnections().containsKey(muenchen));
        Assert.assertTrue(muenchen.getConnections().containsKey(augsburg));
        Assert.assertTrue(flensburg.getConnections().containsKey(kiel));
        Assert.assertTrue(kiel.getConnections().containsKey(flensburg));
    }

    @Test
    public void newCity() {
        // arrange
        OpenCity sut = factory.newCity("Entenhausen", 1);
        // act
        // assert
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());

    }

    @Test
    public void newBoard() {
        // arrange
        OpenBoard sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        assertFalse(sut.getCities().isEmpty());
    }
}