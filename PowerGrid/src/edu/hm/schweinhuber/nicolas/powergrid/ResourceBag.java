package edu.hm.schweinhuber.nicolas.powergrid;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.*;

public class ResourceBag<E> extends AbstractCollection<E> implements Bag<E> {

    private Collection<E> elements = new ArrayList<E>();

    private boolean readonly;

    public ResourceBag(){
        elements = new ArrayList<E>();
    }

    public ResourceBag(Collection<? extends E> collection){
        for(E element: collection)
            privateAdd(element);
    }

    private ResourceBag(ResourceBag<E> that, boolean readonly){
        this.elements = Collections.unmodifiableCollection(that.elements);
        this.readonly = readonly;
    }

    @SafeVarargs @SuppressWarnings("varargs") public ResourceBag(E... elements) {
        this(Arrays.asList(elements));
    }

    @Override
    public boolean add(E element) {
        return  privateAdd(element);
    }

    private boolean privateAdd(E element){
        boolean added;
        added = elements.add(element);
        return added;
    }

    @Override
    public Bag<E> add(E element, int times) {
        if(times <= 0)
            throw new IllegalArgumentException("times mustn't be 0 or less");
        else
            for(int counter = times; counter > 0; counter--)
                elements.add(element);
        return this;
    }

    @Override
    public Bag<E> add(Bag<? extends E> that) {
        Iterator<? extends E> cursor = that.iterator();
        while(cursor.hasNext())
            elements.add(cursor.next());
        return this;
    }

    @Override
    public int count(E element) {
        int number = 0;
        for(E cursor:elements) {
            if (cursor == null) {
                if (element == null)
                    number++;
            }
            else if(cursor.equals(element))
                number++;
        }
        return number;
    }

    @Override
    public boolean contains(Bag<E> that) {
       boolean contained = true;
       for(E element : Objects.requireNonNull(that)){
           if(!elements.contains(element))
               contained = false;
       }
       return contained;
    }

    @Override
    public Bag<E> remove(Bag<E> that) throws NoSuchElementException {
        Iterator<E> cursor = Objects.requireNonNull(that).iterator();
        if(this.size()<that.size())
            throw new NoSuchElementException("bag this is smaller than bag that");
        else{
            while (cursor.hasNext())
                elements.remove(cursor.next());
        }
        return this;
    }

    @Override
    public boolean remove(Object element) {
        return elements.remove(element);
    }

    @Override
    public Bag<E> remove(Object element, int times) {
        if(times <= 0)
            throw new IllegalArgumentException("times mustn't be 0 or less");
        else{
            for(int cursor = times; cursor > 0; cursor--)
                this.remove(element);
        }
        return this;
    }

    @Override
    public Bag<E> immutable() {
        return new ResourceBag<>(this, true);
    }

    @Override
    public Set<E> distinct() {
        return new HashSet<E>(elements);
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override public boolean equals (Object object){
        ResourceBag<?> that;
        boolean equal = false;
        if(object != null && object.getClass() == this.getClass()){
            that = (ResourceBag<?>) object;
            equal = this.getElements().containsAll(that.getElements());
        }
        return equal;
    }

    private Collection <?> getElements(){
        return elements;
    }

    @Override public int hashCode(){
        int hashCode = this.size();
        Set<E> elements = this.distinct();
        for(E element: elements){
            hashCode += element.hashCode()*this.count(element);
        }
        return Objects.hash(hashCode, readonly);
    }

    @Override public String toString(){
        return elements.toString();
    }
}