import edu.hm.cs.rs.powergrid.Bag;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version last modified 2020-04-17
 */
public abstract class BagClassTest {

    abstract Bag<Integer> getSut();
    abstract Bag<Integer> getSutIntegerElements(Integer... elements);
    abstract Bag<Integer> getSutIntegerCollection(Collection<? extends Integer> collection);
    abstract Bag<String> getSutString();
    abstract Bag<String> getSutStringElements(String...elements);
    abstract Bag<String> getSutStringCollection(Collection<? extends String> collection);

    Bag<Integer> sut = getSut();

    @Test
    public void addSome() {
        // act
        sut.add(1);
        sut.add(2);
        sut.add(42);
        sut.add(1);
        //  assert
        assertEquals(4, sut.size());  // 4
    }

    @Test
    public void getSome() {
        // arrange
        sut.add(1);
        sut.add(2);
        sut.add(42);
        sut.add(1);
        List<Integer> read = new ArrayList<>();
        // act
        for (int number : sut)
            read.add(number);
        // assert
        Collections.sort(read);
        assertEquals(List.of(1, 1, 2, 42), read);
    }

    @Test
    public void removeSome() {
        // arrange
        sut.add(1);
        sut.add(2);
        sut.add(42);
        sut.add(1);
        // act
        sut.remove(2);
        sut.remove(238);
        // assert
        assertEquals(3, sut.size());
    }
    @Test public void construct() {
        Bag<Integer> sut = getSutIntegerElements(1,2);
        assertEquals(2,sut.size());
    }


    @Test public void construct2() {
        Bag<Integer> sut = getSutIntegerElements(1,2);
        Bag<Integer> sut2 = getSutIntegerCollection(sut);
        assertEquals(2,sut2.size());
    }

    @Test public void construct3() {
        Bag<Integer> sut = getSutIntegerElements(1,2);
        Bag<Integer> sut2 = getSutIntegerCollection(sut);
        sut.add(3);
        assertEquals(2,sut2.size());
    }

    //distingt
    @Test public void distingt() {
        Bag<Integer> sut1 = getSutIntegerElements(1,1,2,2,3,4);
        Set<Integer> sut2= new HashSet<Integer>();
        sut2.add(1);
        sut2.add(2);
        sut2.add(3);
        sut2.add(4);
        assertEquals(sut2,sut1.distinct());
    }

    //add
    @Test public void addTimes() {
        Bag<Integer> sut1 = getSutIntegerElements(1);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,2,2,2,3,3);
        sut1.add(2,4);
        sut1.add(3,2);
        assertEquals(7,sut1.size());
        assertEquals(sut2,sut1);
    }

    @Test (expected = UnsupportedOperationException.class) public void addTimesImmutable() {
        Bag<Integer> sut1 = getSutIntegerElements(1);
        Bag<Integer> sut2=sut1.immutable();
        sut2.add(3,5);
    }

    @Test public void addBag() {
        Bag<Integer> sut1 = getSutIntegerElements(0,3,3,5,5,6);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,2,2,2,3,3);
        Bag<Integer> sut3 = getSutIntegerElements(0,1,2,2,2,2,3,3,3,3,5,5,6);
        sut2.add(sut1);
        assertEquals(13,sut2.size());
        assertEquals(sut3,sut2);
    }

    @Test public void addBagTimesNull() {
        Bag<Integer> sut = getSutIntegerElements(1);
        sut.add(3,0);
        assertEquals(1,sut.size());
    }

    @Test (expected = IllegalArgumentException.class) public void addBagTimesNegative() {
        Bag<Integer> sut = getSutIntegerElements(1);
        sut.add(3,-1);
    }

    @Test (expected = UnsupportedOperationException.class) public void addBagImmutable() {
        Bag<Integer> sut1 = getSutIntegerElements(1);
        Bag<Integer> sut2 = getSutIntegerElements(1);
        Bag<Integer> sut3=sut1.immutable();
        sut3.add(sut2);
    }

    //remove
    @Test public void removeBag() {
        Bag<Integer> sut1 = getSutIntegerElements(0,3,3,5,5,6);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,2,2,2,3,3);
        Bag<Integer> sut3 = getSutIntegerElements(0,1,2,2,2,2,3,3,3,3,5,5,6);
        sut3.remove(sut1);
        assertEquals(7,sut2.size());
        assertEquals(sut2,sut3);
    }

    @Test public void removeTimesBag() {
        Bag<Integer> sut = getSutIntegerElements(0,1,2,2,2,2,3,3,3,3,5,5,6);
        Bag<Integer> sut2 = getSutIntegerElements(0,3,3,3,6);
        sut.remove(1,1);
        sut.remove(2,20);
        sut.remove(3,1);
        sut.remove(5,2);
        assertEquals(sut2,sut);
        assertEquals(5,sut.size());
    }

    @Test public void removeTimes0Bag() {
        Bag<Integer> sut1 = getSutIntegerElements(0,3,3,3,6);
        Bag<Integer> sut2 = getSutIntegerElements(0,3,3,3,6);
        sut1.remove(3,0);
        Assert.assertEquals(sut2,sut1);
        assertEquals(5,sut1.size());
    }

    @Test (expected = IllegalArgumentException.class) public void removeTimesNegative() {
        Bag<Integer> sut = getSutIntegerElements(0,1,2,2);
        sut.remove(2,-1);
    }

    @Test (expected = NoSuchElementException.class) public void removeBiggerBag() {
        Bag<Integer> sut1 = getSutIntegerElements(1,2);
        Bag<Integer> sut2 = getSutIntegerElements(0,1,2,2);
        sut1.remove(sut2);
    }

    @Test (expected = UnsupportedOperationException.class) public void removeBagImmutable() {
        Bag<Integer> sut1 = getSutIntegerElements(1);
        Bag<Integer> sut2 = getSutIntegerElements(1);
        Bag<Integer> sut3=sut1.immutable();
        sut3.remove(sut2);
    }

    @Test (expected = UnsupportedOperationException.class) public void removeTimes() {
        Bag<Integer> sut1 = getSutIntegerElements(3,3,3,3,3,3,3,3,2);
        Bag<Integer> sut2=sut1.immutable();
        Bag<Integer> sut3 = getSutIntegerElements(3,3,3,3,3,2);
        sut2.remove(3,3);
        assertEquals(6, sut2.size());
        assertEquals(sut3, sut2);
    }

    @Test (expected = UnsupportedOperationException.class) public void removeMoreTimes() {
        Bag<Integer> sut1 = getSutIntegerElements(3,3,3,3,3,3,3,3,2);
        Bag<Integer> sut2=sut1.immutable();
        Bag<Integer> sut3 = getSutIntegerElements(2);
        sut2.remove(3,30);
        assertEquals(1, sut2.size());
        assertEquals(sut3, sut2);
    }

    @Test (expected = UnsupportedOperationException.class) public void removeTimesImmutable() {
        Bag<Integer> sut1 = getSutIntegerElements(3,3,3,3,3,3,3,3,2);
        Bag<Integer> sut2=sut1.immutable();
        sut2.remove(3,5);
    }

    //count()

    @Test public void count() {
        Bag<Integer> sut1 = getSutIntegerElements(0,3,3,5,5,6);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,2,2,2,3,3);
        Bag<Integer> sut3 = getSutIntegerElements(0,1,2,2,2,2,3,3,3,3,5,5,6);
        Bag<Integer> sut4 = getSutIntegerElements(0);
        Bag<Integer> sut5 = getSutIntegerElements((Integer)null);
        assertEquals(2,sut1.count(3));
        assertEquals(4,sut2.count(2));
        assertEquals(1,sut3.count(6));
        assertEquals(0,sut4.count(1));
        assertEquals(1,sut4.count(0));
    }

    @Test public void countNull() {
        Bag<Integer> sut = getSutIntegerElements((Integer)null,3,5,5);
        assertEquals(2,sut.count(5));
        assertEquals(1,sut.count((Integer)null));
    }


    //immutable()
    @Test public void sightAdd() {
        sut.add(1);
        sut.add(2);
        Bag<Integer> sutImmu= sut.immutable();
        sut.add(3);
        assertEquals(sutImmu, sut);
    }

    @Test (expected = UnsupportedOperationException.class) public void immutableSightAdd() {
        sut.add(1);
        sut.add(2);
        Bag<Integer> sutImmu= sut.immutable();
        sutImmu.add(3);
    }

    @Test public void SightRemove() {
        sut.add(1);
        sut.add(2);
        Bag<Integer> sutImmu= sut.immutable();
        sut.remove(2);
        assertEquals(sutImmu, sut);

    }

    @Test (expected = UnsupportedOperationException.class) public void immutableSightRemove() {
        sut.add(1);
        sut.add(2);
        Bag<Integer> sutImmu= sut.immutable();
        sutImmu.remove(1);
    }

    //equals
    @Test public void equals2Strings() {
        Bag<String> sut1 = getSutString();
        Bag<String> sut2 = getSutString();
        sut1.add("abc");
        sut2.add("abc");
        assertTrue(sut1.equals(sut2));
    }

    @Test public void notEquals2Strings() {
        Bag<String> sut1 = getSutString();
        Bag<String> sut2 = getSutString();
        sut1.add("abc");
        sut2.add("abcx");
        assertFalse(sut1.equals(sut2));
    }

    @Test public void notEqualsStingInteger() {
        Bag<String> sut1 = getSutString();
        Bag<Integer> sut2 = getSut();
        sut1.add("abc");
        sut2.add(1);
        assertFalse(sut1.equals(sut2));
    }

    @Test public void equals2Integers() {
        Bag<Integer> sut1 = getSut();
        Bag<Integer> sut2 = getSut();
        sut1.add(42);
        sut2.add(42);
        assertTrue(sut1.equals(sut2));
    }

    @Test public void notEquals2Integers() {
        Bag<Integer> sut1 = getSut();
        Bag<Integer> sut2 = getSut();
        sut1.add(42);
        sut2.add(1);
        assertFalse(sut1.equals(sut2));
    }

    @Test public void equalsNull() {
        Bag<String> sut1 = getSutString();
        Bag<String> sut2 = getSutString();
        sut1.add((String)null);
        sut2.add((String)null);
        assertTrue(sut1.equals(sut2));
    }

    @Test public void notEqualsNullString() {
        Bag<String> sut1 = getSutString();
        Bag<String> sut2 = getSutString();
        sut1.add("abc");
        sut2.add((String)null);
        assertFalse(sut1.equals(sut2));
    }

    @Test public void equals2NullDiffrentTyp() {
        Bag<String> sut1 = getSutString();
        Bag<Integer> sut2 = getSut();
        sut1.add((String)null);
        sut2.add((Integer)null);
        assertTrue(sut1.equals(sut2));
    }
    //zusatzTests:
    @Test public void contains() {
        Bag<Integer> sut1 = getSutIntegerElements(1,2,1,3);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,1,1,3);
        assertFalse(sut1.contains(sut2));
    }

    @Test public void contains2() {
        Bag<Integer> sut = getSutIntegerElements(1,2,1,3);
        assertTrue(sut.contains(sut));
    }

  //(1,2,1,1,3).contains(1,2,3,1,3) sollte false liefern => fixen
    @Test public void contains3(){
        Bag<Integer> sut1 = getSutIntegerElements(1,2,1,1,3);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,3,1,3);
        assertFalse(sut1.contains(sut2));
    }

    @Test public void contains4(){
        Bag<Integer> sut1 =getSutIntegerElements(1,2,1,1,3);
        Bag<Integer> sut2 = getSutIntegerElements(1,2);
        assertTrue(sut1.contains(sut2));
    }

    @Test public void contains5(){
        Bag<Integer> sut1 = getSutIntegerElements(1,2,1,1,3);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,(Integer)null);
        assertFalse(sut1.contains(sut2));
    }

    @Test public void contains6(){
        Bag<String> sut1 = getSutStringElements("hallo","welt","Duke","Linux");
        Bag<String> sut2 = getSutStringElements("hallo","welt","Duke");
        assertTrue(sut1.contains(sut2));
    }

    @Test public void contains7(){
        Bag<String> sut1 = getSutStringElements("hallo","welt","Duke","Linux");
        Bag<String> sut2 = getSutStringCollection(sut1);
        assertTrue(sut1.contains(sut2));
    }


    @Test public void remove() {
        Bag<Integer> sut = getSutIntegerElements(1);
        sut.remove(sut);
        assertEquals(0,sut.size());
    }

    @Test public void remove2() {
        Bag<Integer> sut = getSutIntegerElements(1,2,3,4,(Integer)null);
        Bag<Integer> compare = getSutIntegerElements(1,2,3,4);
        Bag<Integer> sut2 = getSutIntegerElements((Integer)null);
        sut.remove(sut2);
        assertEquals(compare,sut);
    }


    @Test (expected = NoSuchElementException.class) public void removeExeption() {
        Bag<Integer> sut1 = getSutIntegerElements(1,2,1,1,3);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,1,1,4);
        sut1.remove(sut2);
    }

    @Test  public void equalsDifferentType(){
        Bag<Integer> sut1 = getSutIntegerElements(1,2,1,1,3);
        assertFalse(sut1.equals("Hello"));
    }

    @Test public void doubleRemove(){
        Bag<Integer> sut1 = getSutIntegerElements(1,2,1,1,3);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,1);
        Bag<Integer> sut3 = getSutIntegerElements(1,3);
        Bag<Integer> want = getSut();
        Bag<Integer> temp=sut1.remove(sut2);
        temp.remove(sut3);
        assertEquals(want,temp);
    }

    @Test public void doubleRemov2(){
        Bag<Integer> sut1 = getSutIntegerElements(1,2,1,1,3);
        Bag<Integer> sut2 = getSutIntegerElements(1,2,1);
        Bag<Integer> sut3 = getSutIntegerElements(1,3);
        Bag<Integer> want = getSut();
        assertEquals(want,sut1.remove(sut2).remove(sut3));
    }
    @Test public void containsNull() {
        Bag<String> sut1 = getSutString();
        Bag<String> sut2 = getSutString();
        sut1.add((String)null);
        sut2.add((String)null);
        Assert.assertEquals(true,sut1.contains(sut2));
    }
    @Test public void sizeNull(){
        Bag<Integer> bag= getSut();
        bag.add((Integer)null);
        Assert.assertEquals(1,bag.size());

    }
    @Test public void equalsAlmost(){
        Bag<Integer> sut = getSutIntegerElements(0,1,2,2,2,2,3,3,3,3,5,5,6);
        Bag<Integer> sut2 = getSutIntegerElements(0,1,2,2,2,2,3,3,3,3,5,6);
        Assert.assertEquals(false,sut.equals(sut2));
    }
    @Test public void containsItself(){
        Bag<String> sut1 = getSutString();
        sut1.add((String)null);
        Assert.assertEquals(true,sut1.contains(sut1));
    }




    
}