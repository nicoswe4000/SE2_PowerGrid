package edu.hm.mrodic.powergrid;

import edu.hm.cs.rs.powergrid.AbstractBag;
import edu.hm.cs.rs.powergrid.Bag;

import java.util.*;


/**
 * Eine Tuete, deren Elemente keine bestimmte Reihenfolge haben.
 * null und Duplikate sind erlaubt.
 * Wenn die Tuete unveraenderlich ist, sind keine Aufrufe von add und remove erlaubt.
 * @param <E> der Objekttyp des Baginhalts
 * @author Mirna Rodic, IF2B, mrodic@.hm.edu
 * @author Angelia Hofmann, IF2B, hofman22@hm.edu
 * @version 2020-04-16
 */
public class MRodicBag<E> extends AbstractBag<E>{
    /**Die Elemente der Tuete. "Der Speicher".*/
    private Collection<E> bagElements =new ArrayList<>();

    /**
     * Konstruktor einer leeren Bag.
     *
     */
    public MRodicBag(){
        bagElements =new ArrayList<>();
    }
    /**
     * Konstruktor einer Bag mit dem gleichen Inhalt wie die Collection.
     * @param collection eine Collection (Bag).
     */
   public MRodicBag(Collection<? extends E> collection){
       for(E element:collection) {
           privateAdd(element);
       }
    }

    /**
     * Konstruktor einer Bag mit dem gleichen Inhalt wie das gegebene Array.
     * @param elements ein Vararg.
     */
    @SafeVarargs @SuppressWarnings("varargs") public MRodicBag(E... elements) {
        this(Arrays.asList(elements));
    }
    private MRodicBag(MRodicBag<E> that){
        this.bagElements =Collections.unmodifiableCollection(that.bagElements);
    }

    /**
     * Erzeugt unveraenderliche Sicht auf Bag.
     * @return Selben Bag nur in unveraenderlich.
     */
    @Override
    public Bag<E> immutable() {
        return new MRodicBag<>(this);
    }

    /**
     * Erzeugt Set aus Bag Elementen.
     * @return getrennte Elemente.
     */
    @Override
    public Set<E> distinct() {
        return new HashSet<>(bagElements);
    }
    /**
     * Fuegt das Element der Bag einmal hinzu.
     * @param element das Element, was hinzugefuegt werden soll. Kann auch null sein.
     * @return true genau dann, wenn das Element erfolgreich hinzugefuegt wurde, ansonsten false.
     * @throws UnsupportedOperationException wenn diese Tuete unveraenderlich ist.
     */
    @Override public boolean add(E element){
        return privateAdd(element);
    }

    /**Hilfsmethode für add.
     * Fuegt das Element der Tuete hinzu.
     * @param element Element zum Hinzufuegen.
     * @return True genau dann, wenn das Element hinzugefuegt wurde, sonst false.
     */
    private boolean privateAdd(E element) {
        final boolean added;
        added= bagElements.add(element);
        return added;
    }
    /** Anzahl Exemplare eines Elementes.
     * @param element Gesuchtes Element.
     * @return Anzahl Exemplare. Nicht negativ.
     * 0, wenn das Element nicht in dieser Tuete vorkommt.
     */
    @Override
    public int count(E element) {
        int number=0;
        for(E cursor: bagElements){
            if(cursor==null){
                if (element==null) {number++;}
            }
            else if(cursor.equals(element)) {
                number++;
            }
        }
        return number;
    }
    /**
     * Entfernt das Element der Bag einmal.
     * @param element das Element, was entfernt werden soll. Kann auch null sein.
     * @return true genau dann, wenn das Element erfolgreich entfernt wurde, ansonsten false.
     * @throws UnsupportedOperationException wenn diese Tuete unveraenderlich ist.
     */
    @Override
    public boolean remove(Object element) {
        return bagElements.remove(element);
    }

    /**
     * Leert den Inhalt der Tuete.
     */
    @Override
    public void clear() {
        bagElements.clear();
    }
    /**
     * Speichert den Inhalt der Tuete als ein String.
     * In dem Beispiel mit den Rohstoffen fuer die Kraftwerke, wuerde ein Bag mit 2 mal Kohle folgendermassen ausschauen:
     * [Coal, Coal] .
     * @return lesbare Darstellung des Inhalts dieser Tuete.
     */
    @Override public String toString() {
        return bagElements.toString();
    }
}
