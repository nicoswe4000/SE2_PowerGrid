import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;

/**
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-16
 */
public class SpielplanTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);
    //von dem Prof.
    @Test
    public void newCity() {
        City sut = factory.newCity("Entenhausen", 1);
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());
    }
    @Test (expected= NullPointerException.class) public void exceptionsCtor1(){
        City city=factory.newCity(null,3);
    }
    @Test (expected = IllegalArgumentException.class) public void exceptionsCtor2(){
        City city=factory.newCity("Muenchen",0);
    }
    @Test (expected = IllegalArgumentException.class) public void exceptionsCtor3(){
        City city=factory.newCity("Muenchen",-5);
    }
    @Test (expected= IllegalArgumentException.class) public void exceptionsCtor4(){
        City city=factory.newCity("",3);
    }
    @Test public void connections(){
        OpenCity city1 = factory.newCity("Muenchen",1);
        OpenCity city2 = factory.newCity("Rosenheim",2);
        OpenCity city3 = factory.newCity("Augsburg",3);
        city1.connect(city2,4);
        city1.connect(city3,5);
        Map<City,Integer> connections=city1.getConnections();
        Assert.assertEquals((Integer) 4,connections.get(city2));
        Assert.assertEquals((Integer)5,connections.get(city3));
    }
    @Test (expected = IllegalStateException.class) public void exceptionsConnect1(){
        OpenCity city1 = factory.newCity("Muenchen",1);
        OpenCity city2 = factory.newCity("Rosenheim",2);
        OpenCity city3 = factory.newCity("Augsburg",3);
        city1.connect(city2,4);
        city1.close();
        city1.connect(city3,5);
    }
    @Test (expected = IllegalArgumentException.class) public void exceptionsConnect2(){
        OpenCity city1 = factory.newCity("Muenchen",1);
        city1.connect(city1,4);
    }
    @Test (expected = NullPointerException.class) public void exceptionsConnect3(){
        OpenCity city1 = factory.newCity("Muenchen",1);
        city1.connect(null,4);
    }
    @Test (expected = IllegalArgumentException.class) public void exceptionsConnect4(){
        OpenCity city1 = factory.newCity("Muenchen",1);
        City city2 = factory.newCity("Rosenheim",2);
        city1.connect(city2,-4);
    }
    @Test (expected = IllegalArgumentException.class) public void exceptionsConnect5(){
        OpenCity city1 = factory.newCity("Muenchen",1);
        City city2 = factory.newCity("Rosenheim",2);
        city1.connect(city2,4);
        city1.connect(city2,4);
    }
    @Test (expected=IllegalStateException.class) public void closeException(){
        City city1 = factory.newCity("Muenchen",1);
        city1.close();
    }
    @Test (expected=UnsupportedOperationException.class) public void close2(){
        OpenCity city=factory.newCity("Muenchen",2);
        City city2=factory.newCity("Augsburg",2);
        city.connect(city2,10);
        city.close();
        Map<City,Integer> connections=city.getConnections();
        connections.remove(city2);
    }
    //von dem Prof.
    @Ignore
    @Test public void newBoard() {
        // arrange
        OpenBoard sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        assertFalse(sut.getCities().isEmpty());
    }
    private final OpenBoard board = factory.newBoard(new EditionGermany());
    @Test (expected=NullPointerException.class) public void exceptionCtor(){
        OpenFactory factory = OpenFactory.newFactory(fqcn);
        Board sut=factory.newBoard(null);
    }
    @Test public void find_get_City1(){
        City city1=board.findCity("Dresden");
        Set<City> cities=board.getCities();
        boolean sut=cities.contains(city1);
        Assert.assertEquals(true,sut);
    }
    @Test public void find_get_City2(){
        City city1=board.findCity("abcdefg");
        Assert.assertEquals(null,city1);
    }
    @Test (expected=UnsupportedOperationException.class) public void getCitiesException1(){
        City city1=board.findCity("Dresden");
        board.close();
        Set<City> cities=board.getCities();
        cities.remove(city1);
    }
    @Test (expected=UnsupportedOperationException.class) public void getCitiesException2(){
        board.close();
        Set<City> cities=board.getCities();
        cities.add(factory.newCity("sut",2));
    }
    @Test public void closeRegions1(){
        City city1=board.findCity("Dresden");
        Set<City> cities1=board.getCities();
        boolean sut1=cities1.contains(city1);
        board.closeRegions(3);
        Set<City> cities2=board.getCities();
        boolean sut2=cities2.contains(city1);
        Assert.assertEquals(true,sut1);
        Assert.assertEquals(false,sut2);
    }
    @Test public void closeRegions2(){
        City city1=board.findCity("Dresden");
        board.closeRegions(3);
        City city2=board.findCity("Frankfurt-Oder");
        Map<City,Integer> connections=city2.getConnections();
        boolean sut=connections.containsKey(city1);
        Assert.assertEquals(false,sut);
    }
    @Test (expected=IllegalStateException.class) public void exceptionCloseRegion1(){
        board.close();
        board.closeRegions(5);
    }
}
