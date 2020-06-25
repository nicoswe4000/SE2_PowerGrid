import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**Zusatz Test zu City und Test zur Veraenderung durch Aufgabe 6.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-25-05
 */
public class AdditCityTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);

    private final Edition edition=new EditionGermany();
    private final OpenBoard openSut= factory.newBoard(edition);
    private final City city= factory.newCity("Entenhausen",4);
    private final City city2= factory.newCity("Peking",1);
    private final OpenCity openCity= factory.newCity("Winterfell",4);

//Allgemeine Tests
    //wrongConstruction Test mit edition=null in UniqueConstruction Test ausgelagert

    //construct Parameter
    @Test (expected = IllegalArgumentException.class)
    public void emptyName() {
        City city=factory.newCity("",1);
    }

    @Test (expected = NullPointerException.class)
    public void nullName() {
        City city=factory.newCity(null,1);
    }

    @Test (expected =IllegalArgumentException.class)
    public void zeroRegion() {
        City city=factory.newCity("SimCity",0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void negativRegion() {
        City city=factory.newCity("Almeria",-1);
    }

    //closeTest
    @Test (expected = IllegalStateException.class)
    public void closeWithoutConnections() {
        city.close();
    }

    @Test (expected = IllegalStateException.class)
    public void closeClose() {
        openCity.connect(city2,5);
        City help=openCity;
        help.close();
        help.close();
    }
    @Test (expected = IllegalStateException.class)
    public void closeClose2() {
        openCity.connect(city2,5);
        openCity.close();
        openCity.close();
    }

    @Test (expected = UnsupportedOperationException.class)
    public void closedConectionsImmutable() {
        openCity.connect(city,5);
        openCity.close();
        city.getConnections().put(openCity,6);
    }
    @Test (expected = UnsupportedOperationException.class)
    public void closedConectionsImmutable2() {
        openCity.connect(city,5);
        openCity.close();
        City help=openCity;
        help.getConnections().put(openCity,6);
    }


    //CloseRegions und getCities
    @Test (expected = IllegalStateException.class) public void closeClosedRegions(){
        openSut.close();
        openSut.closeRegions(2);
    }

    //getName
    @Test
    public void getNameOpen(){
        Assert.assertEquals("Winterfell",openCity.getName());
    }
    @Test
    public void getName(){
        Assert.assertEquals("Entenhausen",city.getName());
    }

    //getRegion
    @Test
    public void getRegionOpen(){
        Assert.assertEquals(4,openCity.getRegion());
    }
    @Test
    public void getRegion(){
        Assert.assertEquals(4,city.getRegion());
    }

    //getConnections+connect
    @Test (expected = IllegalArgumentException.class)
    public void getConectionsNegativCosts(){
        openCity.connect(city,-1);
    }
    @Test (expected = NullPointerException.class)
    public void getConectionsNullCity(){
        openCity.connect(null,1);
    }
    @Test (expected = IllegalArgumentException.class)
    public void getConectionsThisCity(){
        openCity.connect(openCity,1);
    }
    @Test (expected =Exception.class)
    public void getConectionsSameTwice(){
        openCity.connect(city,1);
        openCity.connect(city,1);
    }
    @Test (expected =IllegalStateException.class)
    public void getConectionsClosed(){
        openCity.connect(city,1);
        openCity.close();
        openCity.connect(city2,1);
    }

    @Test
    public void getConnectionsNotEmpty(){
        Assert.assertEquals(true,city.getConnections().isEmpty());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getConnectionsClose(){
        openCity.connect(city,0);
        City help=openCity;
        help.close();
        help.getConnections().put(openCity,5);
    }

    //getConnections speziell Aufgabe 6:
    @Test
    public void getConectionsOpen(){
        Map<City, Integer> connections=new HashMap<>();
        connections.put(city,0);
        openCity.connect(city,0);
        Assert.assertEquals(connections,openCity.getConnections());
    }
    @Test
    public void getConnections(){
        Map<City, Integer> connections=new HashMap<>();
        connections.put(city,0);
        openCity.connect(city,0);
        City help=openCity;
        Assert.assertEquals(connections,help.getConnections());
    }


    @Test (expected = UnsupportedOperationException.class)
    public void getConnectionsImmutability(){
        openCity.connect(city,0);
        openCity.getConnections().put(city2,2);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void getConnectionsImmutability2(){
        openCity.connect(city,0);
        City sut=openCity;
        sut.getConnections().put(city2,2);
    }

    @Test
    public void getConnectionsmutability(){
        Map<City, Integer> connections=new HashMap<>();
        connections.put(city,0);
        connections.put(city2,2);
        openCity.connect(city,0);
        openCity.getOpenConnections().put(city2,2);
        Assert.assertEquals(connections,openCity.getConnections());
    }





}