import edu.hm.cs.rs.powergrid.EditionGermany;
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
public class BoardClassTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test
    public void newBoard() {
        // arrange
        OpenBoard sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        assertFalse(sut.getCities().isEmpty());
    }

    @Test(expected=NullPointerException.class) public void editionNull(){
        OpenBoard sut = factory.newBoard(null);
    }


    //Test close

    @Test(expected=IllegalStateException.class) public void closeClosedBoard(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        sut.close();
        sut.close();
    }

    @Test(expected=IllegalStateException.class) public void closeCityOfClosedBoard(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        OpenCity city= sut.findCity("Kiel");
        sut.close();
        city.close();
    }

    //Test getCitiies
    @Test public void getCityBeforClose(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        OpenCity city= factory.newCity("Palma",3);
        sut.getOpenCities().add(city);
        Assert.assertTrue(sut.getCities().contains(city));
    }

    @Test (expected = UnsupportedOperationException.class) public void getCityAfterClose(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        OpenCity city= factory.newCity("Palma",3);
        sut.close();
        sut.getOpenCities().add(city);
    }

    //Test find City

    @Test public void findExistendCity(){
        OpenBoard sut = factory.newBoard(new EditionGermany());

        assertEquals("Konstanz",sut.findCity("Konstanz").getName());
        assertEquals("Trier",sut.findCity("Trier").getName());
        assertEquals("Flensburg",sut.findCity("Flensburg").getName());
    }

    @Test public void findUnexistendCity(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        Assert.assertTrue(sut.findCity("Muenchhausen")==null);
    }

    //Test close Regions

    @Test (expected = IllegalStateException.class) public void closeClosedRegions(){
        OpenBoard sut = factory.newBoard(new EditionGermany());
        sut.close();
        sut.closeRegions(2);
    }

}