import edu.hm.cs.rs.powergrid.Bag;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
/**
 * @author Mirna Rodic, IF2B, mrodic@hm.edu
 * @author Angelina Hofmann, IF2N, hofman22@hm.deu
 * @version 2020-04-17
 */
public abstract class BagTest {
    abstract Bag<Integer> getSut();
    abstract Bag<Integer> getSutIntegerElements(Integer... elements);
    abstract Bag<Integer> getSutIntegerCollection(Collection<? extends Integer> collection);

    // weitere Tests sind auch in der Testklasse "PlayerAndPlantTest" vorhanden.
    @Test public void ctor1UndSize(){
        Bag<Integer> bag = getSut();
        Assert.assertEquals(0,bag.size());
    }
    @Test public void ctor2UndSize(){
        Bag<Integer> bag = getSutIntegerElements(4,5,6);
        Assert.assertEquals(3,bag.size());
    }
    @Test public void ctor3UndSize(){
        Bag<Integer> bag1 = getSutIntegerElements(4,5,6);
        Bag<Integer> bag2 = getSutIntegerCollection(bag1);
        Assert.assertEquals(3,bag2.size());
    }
    @Test (expected = UnsupportedOperationException.class) public void immutable(){
        Bag<Integer> bag = getSutIntegerElements(4,5,6);
        Bag<Integer> bagneu=bag.immutable();
        bagneu.add(0);
    }
    @Test public void distinct1(){
        Bag<Integer> bag = getSutIntegerElements(4,5,5,5,6,6);
        Set<Integer> elements=bag.distinct();
        Assert.assertEquals(3,elements.size());
    }
    @Test public void distinct2(){
        Bag<Integer> bag = getSutIntegerElements(6,6,6);
        Set<Integer> elements=bag.distinct();
        Assert.assertEquals(1,elements.size());
    }
    @Test public void distinct3(){
        Bag<Integer> bag = getSutIntegerElements(7,6,8);
        Set<Integer> elements=bag.distinct();
        Assert.assertEquals(3,elements.size());
    }
    @Test public void add1(){
        Bag<Integer> bag = getSutIntegerElements(7,6,8);
        boolean sut=bag.add(9);
        Assert.assertEquals(true,sut);
    }
    @Test public void add2(){
        Bag<Integer> bag = getSutIntegerElements(7,6,8);
        bag.add(8);
        Assert.assertEquals(4,bag.size());
    }
    @Test public void add3(){
        Bag<Integer> bag = getSut();
        bag.add(3,5);
        Assert.assertEquals(5,bag.size());
    }
    @Test public void add4(){
        Bag<Integer> bag1 = getSut();
        Bag<Integer> bag2 = getSutIntegerElements(2,4,5);
        bag1.add(bag2);
        Assert.assertEquals(3,bag1.size());
    }
    @Test public void count1(){
        Bag<Integer> bag = getSut();
        Assert.assertEquals(0,bag.count(2));
    }
    @Test public void count2(){
        Bag<Integer> bag = getSutIntegerElements(2,2,2);
        Assert.assertEquals(3,bag.count(2));
    }
    @Test public void count3(){
        Bag<Integer> bag = getSutIntegerElements(2,2,1,2,5,4,1,1);
        Assert.assertEquals(3,bag.count(1));
    }
    @Test public void contained1(){
        Bag<Integer> bag1 = getSutIntegerElements(2);
        Bag<Integer> bag2 = getSutIntegerElements(2,4,5);
        boolean sut = bag2.contains(bag1);
        Assert.assertEquals(true,sut);
    }
    @Test public void contained2(){
        Bag<Integer> bag1 = getSutIntegerElements(3,8);
        Bag<Integer> bag2 = getSutIntegerElements(2,4,5);
        boolean sut = bag2.contains(bag1);
        Assert.assertEquals(false,sut);
    }
    @Test public void contained3(){
        Bag<Integer> bag2 = getSutIntegerElements(2,4,5);
        boolean sut = bag2.contains(bag2);
        Assert.assertEquals(true,sut);
    }
    @Test public void remove1(){
        Bag<Integer> bag = getSutIntegerElements(7,6,8);
        boolean sut=bag.remove(7);
        Assert.assertEquals(true,sut);
        Assert.assertEquals(2,bag.size());
    }
    @Test public void remove2(){
        Bag<Integer> bag = getSutIntegerElements(7,6,8,8);
        bag.remove(8,1);
        Assert.assertEquals(3,bag.size());
    }
    @Test public void remove2_1(){
        Bag<Integer> bag = getSutIntegerElements(7,6,8,8);
        bag.remove(8,2);
        Assert.assertEquals(2,bag.size());
    }
    @Test public void remove2_2(){
        Bag<Integer> bag =getSutIntegerElements(7,6,8,8);
        bag.remove(8,0);
        Assert.assertEquals(4,bag.size());
    }
    @Test (expected = NoSuchElementException.class) public void remove3(){
        Bag<Integer> bag1 = getSutIntegerElements(5);
        Bag<Integer> bag2 = getSutIntegerElements(2,4,5);
        bag1.remove(bag2);
    }
    @Test public void remove3_1(){
        Bag<Integer> bag1 = getSutIntegerElements(2,4,5,9,3);
        Bag<Integer> bag2 = getSutIntegerElements(2,4,5);
        bag1.remove(bag2);
        Assert.assertEquals(2,bag1.size());
    }
    @Test (expected=UnsupportedOperationException.class) public void removeException(){
        Bag<Integer> bag = getSutIntegerElements(7,6,8,8);
        Bag<Integer> bagneu=bag.immutable();
        bagneu.remove(8,2);
    }
    @Test public void equals1(){
        Bag<Integer> bag1= getSutIntegerElements(1,2,3);
        Bag<Integer> bag2=getSutIntegerCollection(bag1);
        Assert.assertEquals(true,bag1.equals(bag2));
    }
    @Test public void equals2(){
        Bag<Integer> bag1= getSutIntegerElements(1,2,3);
        Bag<Integer> bag2= getSutIntegerElements(1,2,3);
        Assert.assertEquals(true,bag1.equals(bag2));
    }
    @Test public void equals3(){
        Bag<Integer> bag1= getSutIntegerElements(1,2,3);
        Bag<Integer> bag2= getSutIntegerElements(1,2,8);
        Assert.assertEquals(false,bag1.equals(bag2));
    }
    @Test public void equals4(){
        Bag<Integer> bag1= getSut();
        Bag<Integer> bag2= getSutIntegerElements(1,2,8);
        Assert.assertEquals(false,bag1.equals(bag2));
    }
    @Test public void equals5(){
        Bag<Integer> bag1= getSut();
        Bag<Integer> bag2= getSut();
        Assert.assertEquals(true,bag1.equals(bag2));
    }
    @Test public void toString1(){
        Bag<Integer> bag1= getSut();
        String sut="[]";
        Assert.assertEquals(sut,bag1.toString());
    }
    @Test public void toString2(){
        Bag<Integer> bag1= getSutIntegerElements(1,2,3);
        String sut="[1, 2, 3]";
        Assert.assertEquals(sut,bag1.toString());
    }
    @Test public void hashCode1(){
        Bag<Integer> bag1= getSut();
        Bag<Integer> bag2= getSut();
        boolean sut =bag1.hashCode()==bag2.hashCode();
        Assert.assertEquals(true,sut);
    }
    @Test public void hashCode2(){
        Bag<Integer> bag1= getSutIntegerElements(1,2,3);
        Bag<Integer> bag2= getSutIntegerElements(1,2,3);
        boolean sut =bag1.hashCode()==bag2.hashCode();
        Assert.assertEquals(true,sut);
    }
    @Test public void hashCode3(){
        Bag<Integer> bag1= getSut();
        Bag<Integer> bag2= getSutIntegerElements(1,2,3);
        boolean sut =bag1.hashCode()==bag2.hashCode();
        Assert.assertEquals(false,sut);
    }
    @Test public void hashCode4(){
        Bag<Integer> bag1= getSutIntegerElements(4,5,6);
        Bag<Integer> bag2= getSutIntegerElements(1,2,3);
        boolean sut =bag1.hashCode()==bag2.hashCode();
        Assert.assertEquals(false,sut);
    }
    @Test public void immutable1(){
        Bag<Integer> bag1= getSutIntegerElements(4,5,6);
        Bag<Integer> bag2=bag1.immutable();
        Assert.assertEquals(false,bag1==bag2);
    }
    @Test public void immutable2(){
        Bag<Integer> bag1= getSutIntegerElements(4,5,6);
        Bag<Integer> bag2=bag1.immutable();
        Assert.assertEquals(true,bag1.equals(bag2));
    }
    @Ignore @Test public void immutable3(){
        Bag<Integer> bag1= getSutIntegerElements(4,5,6);
        Bag<Integer> bag2=bag1.immutable();
        Assert.assertEquals(false,bag1.hashCode()==bag2.hashCode());
    }
    @Test public void immutable4(){
        Bag<Integer> bag1= getSutIntegerElements(4,5,6);
        Bag<Integer> bag2= getSutIntegerElements(4,5,6);
        Assert.assertEquals(true,bag1.hashCode()==bag2.hashCode());
    }
    @Test (expected=IllegalArgumentException.class) public void addException(){
        Bag<Integer> bag1= getSutIntegerElements(4,5,6);
        bag1.add(5,-8);
    }
}
