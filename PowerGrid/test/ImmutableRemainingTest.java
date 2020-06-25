import com.sun.source.tree.AssertTree;
import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import edu.hm.mrodic.powergrid.MRodicBag;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**Test zur Veraenderung durch Aufgabe 6 neben den Addit-Tests.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-15-05
 */
public class ImmutableRemainingTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);

    private final OpenPlayer pink=factory.newPlayer("pink","pink");
    private final OpenPlayer red=factory.newPlayer("red","red");
    private final Edition edition=new EditionGermany();
    private final Board board= factory.newBoard(edition);
    private final City city= factory.newCity("Entenhausen",4);
    private final OpenCity openCity= factory.newCity("Winterfell",4);
    private final OpenPlantMarket plantMarket= factory.newPlantMarket(edition);
    private final Plant plant= factory.newPlant(4, Plant.Type.Hybrid,2,3);
    private final OpenPlant openPlant= factory.newPlant(6, Plant.Type.Hybrid,2,3);
    private final OpenResourceMarket openResourceMarket= factory.newResourceMarket(edition);


/*
Auction, Bord, City und Game wurden bereits in den jeweiligen Addit-Klasse-Test getestet,
es fehlen noch Plantmaket,Player und ResourceMarket, die anderen Klassen werden von den
bisher geschriebenen Tests abgedeckt
 */

//PlantMarket
    @Test (expected = UnsupportedOperationException.class)
    public void immutableActualPlants(){
        Set<Plant> plants=plantMarket.getActual();
        plants.add(plant);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableActualPlants2(){
        PlantMarket pMarket=plantMarket;
        Set<Plant> plants=plantMarket.getActual();
        plants.add(openPlant);
    }

    @Test
    public void mutableActualPlants(){
        Set<OpenPlant> plants=plantMarket.getOpenActual();
        int size=plants.size();
        plants.add(openPlant);
        Assert.assertEquals(size+1,plants.size());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void mutableActualPlantsImmutable(){
        Set<OpenPlant> plants=plantMarket.getOpenActual();
        //hier ist sicher ein Plant enthlaten
        OpenPlant testPlant=plantMarket.getOpenHidden().stream().findAny().orElse(null);
        Bag<Resource> bag= new MRodicBag<>(Resource.Oil);
        testPlant.getResources().add(bag);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableFuturePlants(){
        Set<Plant> plants=plantMarket.getFuture();
        plants.add(plant);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableFuturePlants2(){
        PlantMarket pMarket=plantMarket;
        Set<Plant> plants=plantMarket.getFuture();
        plants.add(openPlant);
    }

    @Test
    public void mutableFuturePlants(){
        Set<OpenPlant> plants=plantMarket.getOpenFuture();
        int size=plants.size();
        plants.add(openPlant);
        Assert.assertEquals(size+1,plants.size());
    }

    //Player
    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetCities(){
        Set<City> cities=red.getCities();
        cities.add(city);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetCities2(){
        Player player=pink;
        Set<City> cities=pink.getCities();
        cities.add(city);
    }

    @Test
    public void mutableGetCities(){
        Set<OpenCity> cities=pink.getOpenCities();
        int size=cities.size();
        cities.add(openCity);
        Assert.assertEquals(size+1,cities.size());
    }

    @Test
    public void mutableGetCities2(){
        Set<OpenCity> cities=pink.getOpenCities();
        cities.add(openCity);
        OpenCity sut=cities.stream().findFirst().orElse(null);
        int size=sut.getOpenConnections().size();
        sut.getOpenConnections().put(openCity,4);
        Assert.assertEquals(size+1,cities.size());
    }
    @Test (expected = UnsupportedOperationException.class)
    public void mutableGetCities3(){
        Set<City> cities=pink.getCities();
        cities.add(board.findCity("Berlin"));
        //sicher nicht leer
        City sut=cities.stream().findFirst().orElse(null);
        //Berlin hat sicher Connections
        sut.getConnections().put(openCity,4);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetPlants(){
        Set<Plant> plants=red.getPlants();
        plants.add(plant);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetPlants2(){
        Player player=pink;
        Set<Plant> plants=pink.getPlants();
        plants.add(plant);
    }

    @Test
    public void mutableGetPlants(){
        Set<OpenPlant> plants=pink.getOpenPlants();
        int size=plants.size();
        plants.add(openPlant);
        Assert.assertEquals(size+1,plants.size());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void mutableGetPlantsImmutable(){
        Set<OpenPlant> plants=pink.getOpenPlants();
        plants.add(openPlant);
        //Sicher eines enthalten
        OpenPlant sut= plants.stream().findAny().orElse(null);
        sut.getResources().add(new MRodicBag<>(Resource.Oil));
    }


    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetResources(){
        Bag<Resource> resources=red.getResources();
        resources.add(Resource.Uranium);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutablegetResources2(){
        Player player=pink;
        Bag<Resource> resources=pink.getResources();
        resources.add(Resource.Uranium);
    }

    @Test
    public void mutablegetResources(){
        Bag<Resource> resources=pink.getOpenResources();
        int size=resources.size();
        resources.add(Resource.Uranium);
        Assert.assertEquals(size+1,resources.size());
    }

//ResourceMarket
    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetAvailable(){
        Bag<Resource> resources=openResourceMarket.getAvailable();
        resources.add(Resource.Uranium);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetAvailable2(){
        ResourceMarket resourceMarket=openResourceMarket;
        Bag<Resource> resources=resourceMarket.getAvailable();
        resources.add(Resource.Uranium);
    }

    @Test
    public void mutableGetAvailable(){
        Bag<Resource> resources=openResourceMarket.getOpenAvailable();
        int size=resources.size();
        resources.add(Resource.Uranium);
        Assert.assertEquals(size+1,resources.size());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetSupply(){
        Bag<Resource> resources=openResourceMarket.getSupply();
        resources.add(Resource.Uranium);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void immutableGetSupply2(){
        ResourceMarket resourceMarket=openResourceMarket;
        Bag<Resource> resources=resourceMarket.getSupply();
        resources.add(Resource.Uranium);
    }

    @Test
    public void mutableGetSupply(){
        Bag<Resource> resources=openResourceMarket.getOpenSupply();
        int size=resources.size();
        resources.add(Resource.Uranium);
        Assert.assertEquals(size+1,resources.size());
    }

    //fehlender Test:
    @Test (expected = IllegalArgumentException.class)
    public void mutableGetPriceNonExisting(){
        Bag<Resource> resources=openResourceMarket.getOpenAvailable();
        resources.clear();
        openResourceMarket.getPrice(Resource.Coal);
    }






}