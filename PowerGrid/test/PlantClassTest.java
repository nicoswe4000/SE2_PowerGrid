import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.mrodic.powergrid.MRodicBag;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Angelina Hofmann
 * @version 2020-04-16
 */
public class PlantClassTest {

    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";

    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test public void newPlant() {
        // arrange
        Plant sut = factory.newPlant(3, Plant.Type.Oil, 2, 1);
        // act
        // assert
        Assert.assertEquals(3, sut.getNumber());
        Assert.assertEquals(Plant.Type.Oil, sut.getType());
        Assert.assertEquals(2, sut.getNumberOfResources());
        Assert.assertEquals(1, sut.getCities());
    }

    @Test public void newPlantNull() {
        // arrange
        Plant sut = factory.newPlant(0, Plant.Type.Oil, 0, 1);
        // act
        // assert
        Assert.assertEquals(0, sut.getNumber());
        Assert.assertEquals(Plant.Type.Oil, sut.getType());
        Assert.assertEquals(0, sut.getNumberOfResources());
        Assert.assertEquals(1, sut.getCities());
    }

    //Falsche Angaben
    @Test (expected = IllegalArgumentException.class) public void constructNumberNegativ(){
        Plant sut = factory.newPlant(-1, Plant.Type.Oil, 2, 1);
    }

    @Test (expected = NullPointerException.class) public void constructTypeNull(){
        Plant sut = factory.newPlant(1,null, 2, 1);
    }

    @Test (expected = IllegalArgumentException.class) public void constructResourcesNegativ(){
        Plant sut = factory.newPlant(1, Plant.Type.Oil, -1, 1);
    }

    @Test (expected = IllegalArgumentException.class) public void constructCitiesNegativ(){
        Plant sut = factory.newPlant(1, Plant.Type.Oil, 1, -1);
    }

    //getRessources()
    @Test public void getResourcesEco(){
        Plant sut = factory.newPlant(1, Plant.Type.Eco, 0, 2);
        Assert.assertEquals(1,sut.getResources().size());
    }
    @Test public void getResourcesEco2(){
        OpenPlant sut = factory.newPlant(1, Plant.Type.Eco, 0, 2);
        Set<Bag<Resource>> watedResources=new HashSet<>();
        Bag<Resource> emptyBag = new MRodicBag<>();
        emptyBag=emptyBag.immutable();
        watedResources.add(emptyBag);
        watedResources=Collections.unmodifiableSet(watedResources);
        Assert.assertEquals(watedResources,sut.getResources());
    }

    @Test public void getResourcesOil(){
        Plant sut = factory.newPlant(1, Plant.Type.Oil, 3, 2);
        Set<Bag<Resource>> watedResources=new HashSet<>();
        Bag<Resource> oilBag= new MRodicBag<>();
        oilBag.add(Resource.Oil);
        oilBag.add(Resource.Oil);
        oilBag.add(Resource.Oil);
        oilBag=oilBag.immutable();
        watedResources.add(oilBag);
        watedResources=Collections.unmodifiableSet(watedResources);
        Assert.assertEquals(watedResources,sut.getResources());
    }

    @Test public void getResourcesHybrid(){
        Plant sut = factory.newPlant(1, Plant.Type.Hybrid, 3, 2);
        Set<Bag<Resource>> watedResources=new HashSet<>();
        Bag<Resource> oilBag1= new MRodicBag<>();
        oilBag1.add(Resource.Oil);
        oilBag1.add(Resource.Oil);
        oilBag1.add(Resource.Oil);
        oilBag1=oilBag1.immutable();
        watedResources.add(oilBag1);
        Bag<Resource> oilBag2= new MRodicBag<>();
        oilBag2.add(Resource.Oil);
        oilBag2.add(Resource.Oil);
        oilBag2.add(Resource.Coal);
        oilBag2=oilBag2.immutable();
        watedResources.add(oilBag2);
        Bag<Resource> oilBag3= new MRodicBag<>();
        oilBag3.add(Resource.Oil);
        oilBag3.add(Resource.Coal);
        oilBag3.add(Resource.Coal);
        oilBag3=oilBag3.immutable();
        watedResources.add(oilBag3);
        Bag<Resource> oilBag4= new MRodicBag<>();
        oilBag4.add(Resource.Coal);
        oilBag4.add(Resource.Coal);
        oilBag4.add(Resource.Coal);
        oilBag4=oilBag4.immutable();
        watedResources.add(oilBag4);
        watedResources=Collections.unmodifiableSet(watedResources);
        sut.getResources();
        Assert.assertTrue(sut.getResources().containsAll(watedResources));
    }

    //hasOperates bzw setOperated
    @Test public void hasNotOperated(){
        Plant sut = factory.newPlant(1, Plant.Type.Eco, 0, 2);
        Assert.assertFalse(sut.hasOperated());
    }

    @Test public void setOperated(){
        OpenPlant sut = factory.newPlant(1, Plant.Type.Eco, 0, 2);
        sut.setOperated(true);
        Assert.assertTrue(sut.hasOperated());
    }

}