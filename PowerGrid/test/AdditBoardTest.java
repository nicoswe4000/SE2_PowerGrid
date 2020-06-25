import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**Zusatz Test zu Board und Test zur Veraenderung durch Aufgabe 6.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-25-05
 */
public class AdditBoardTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);

    private final Edition edition=new EditionGermany();
    private final Board sut= factory.newBoard(edition);
    private final OpenBoard openSut= factory.newBoard(edition);
    private final City city= factory.newCity("Entenhausen",4);
    private final OpenCity openCity= factory.newCity("Winterfell",4);

//Allgemeine Tests
    //Grundzustaende test
    @Test
    public void constructionTestCities() {
        Assert.assertEquals(42,sut.getCities().size());
    }

    //CloseRegions und getCities
    @Test (expected = IllegalStateException.class) public void closeClosedRegions(){
        openSut.close();
        openSut.closeRegions(2);
    }

    //Aufgabe 6 ab hier andere Variante von CLose Regions: (nach num Player statt nach remainin regions)
    @Test public void closeRegions1(){
        openSut.closeRegions(2);
        Assert.assertEquals(21,openSut.getCities().size());
        Assert.assertEquals(21,openSut.getOpenCities().size());
    }

    @Test public void closeRegions2(){
        openSut.closeRegions(3);
        Assert.assertEquals(21,openSut.getCities().size());
        Assert.assertEquals(21,openSut.getOpenCities().size());
    }

    @Test public void closeRegions4(){
        openSut.closeRegions(4);
        Assert.assertEquals(28,openSut.getCities().size());
        Assert.assertEquals(28,openSut.getOpenCities().size());
    }

    @Test public void closeRegions5(){
        openSut.closeRegions(5);
        Assert.assertEquals(35,openSut.getCities().size());
        Assert.assertEquals(35,openSut.getOpenCities().size());
    }

    @Test public void closeRegions6(){
        openSut.closeRegions(6);
        Assert.assertEquals(42,openSut.getCities().size());
        Assert.assertEquals(42,openSut.getOpenCities().size());
    }





    @Test (expected = IllegalArgumentException.class)
    public void closeRegions7(){
        openSut.closeRegions(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void closeRegions8(){
        openSut.closeRegions(7);
    }

    //getCities konkreter zu Aufgabe 6
    //unmodifiable getCities
    @Test (expected = UnsupportedOperationException.class)
    public void getCitiesUnmodifiable(){
        Set<City> cities=openSut.getCities();
        cities.add(city);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getCitiesUnmodifiable2(){
        Set<City> cities=sut.getCities();
        cities.add(city);
    }

    @Test
    public void getCitiesmodifiable2(){
        Set<OpenCity> cities=openSut.getOpenCities();
        int compareValue=cities.size();
        cities.add(openCity);
        Assert.assertEquals(compareValue+1,cities.size());
    }

    //find City
    @Test public void openFindCity(){
        Set<OpenCity> cities=openSut.getOpenCities();
        cities.add(openCity);
        Assert.assertEquals(openCity,openSut.findCity("Winterfell"));
    }

    @Test public void openFindCityNull(){
        Assert.assertEquals(null,openSut.findCity("Entenhausen"));
    }

    @Test public void findCityNull(){
        Assert.assertEquals(null,sut.findCity("Entenhausen"));
    }

    @Test public void findCity(){
        Assert.assertEquals("Berlin",sut.findCity("Berlin").getName());
    }

    //Aufgabe 6 im Speziellen:
    @Test
    public void findCityMutable(){
        OpenCity testCity=openSut.findCity("Berlin");
        //+1 da noch eine Verbidung hinzugefuegt wird
        int compareValue=testCity.getConnections().size()+1;
        testCity.getOpenConnections().put(openCity,5);
        Assert.assertEquals(compareValue,testCity.getConnections().size());
    }
    @Test (expected = UnsupportedOperationException.class)
    public void findCityImmutable(){
        City testCity=sut.findCity("Berlin");
        testCity.getConnections().put(openCity,5);
    }

    //close
    @Test (expected = UnsupportedOperationException.class)
    public void closeOpenCity(){
        openSut.close();
        openSut.getOpenCities().add(openCity);
    }

    @Test (expected = IllegalStateException.class)
    public void closeclosedOpenCity(){
        openSut.close();
        openSut.close();
    }

}