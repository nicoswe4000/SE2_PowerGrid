package edu.hm.cs.rs.powergrid;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/** Eine Tuete, deren Elemente keine bestimmte Reihenfolge haben.null und Duplikate
 * (mehrere Exemplare des gleichen Elementes) sind erlaubt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-21
 * @param <E> Type der Elemente.
 */
public interface Bag<E> extends Collection<E> {
    /** Eine unveraenderliche Sicht auf diese Tuete.
     * Diese Methode kopiert keine Elemente.
     * Referenzen auf dieses Original bleiben veraenderlich.
     * @return Sicht, die keine Aenderungen erlaubt.
     */
    Bag<E> immutable();

    /** Anzahl Elemente in dieser Tuete.
     * @return Anzahl. Nicht negativ.
     */
    @Override
    int size();

    /** Liefert in einer beliebigen Reihenfolge alle Elemente in dieser Tuete.
     * Elemente tauchen mehrmals auf, wenn diese Tuete mehrere Exemplare enthaelt.
     * @return Ein Iterator.
     */
    @Override
    Iterator<E> iterator();

    /** Die Menge der unterschiedlichen Elemente.
     * @return Die unterschiedlichen Elemente. Nicht null. Eventuell leer.
     */
    Set<E> distinct();

    @Override
    boolean add(E element);

    /** Fuegt mehrere Exemplare eines Element mehrmals ein.
     * @param element Element.
     * @param times Anzahl Exemplare. Nicht negativ.
     * @return Diese Tuete, eventuell mit mehr Elementen als vorher.
     * @throws UnsupportedOperationException wenn diese Tuete unveraenderlich ist.
     * @throws IllegalArgumentException wenn die Anzahl negativ ist.
     */
    Bag<E> add(E element, int times);

    /** Fuegt alle Elemente der anderen Tuete in diese ein.
     * @param that Eine Tuete. Nicht null.
     * @return Diese Tuete, eventell mit mehr Elementen als vorher.
     * @throws UnsupportedOperationException wenn diese Tuete unveraenderlich ist.
     */
    Bag<E> add(Bag<? extends E> that);

    /** Anzahl Exemplare eines Elementes.
     * @param element Gesuchtes Element.
     * @return Anzahl Exemplare. Nicht negativ.
     * 0, wenn das Element nicht in dieser Tuete vorkommt.
     */
    int count(E element);

    /** Test, ob alle Elemente einer anderen Tuete auch in dieser vorkommen.
     * @param that Eine Tuete.
     * @return true genau dann, wenn diese Tuete von jedem Element in that
     * gleich viele oder mehr Exemplare enthaelt.
     */
    boolean contains(Bag<E> that);

    /** Testet, ob anything ebenfalls eine Tuete ist und die gleiche Auswahl und Anzahl
     * Elemente enthaelt.
     * @param anything Eine Referenz.
     * @return true genau dann, wenn anything eine gleiche Tuete mit dem gleichen Inhalt ist.
     */
    @Override
    boolean equals(Object anything);

    @Override
    int hashCode();

    /** Entfernt aus dieser Tuete alle Elemente, die eine andere Tuete enthaelt.
     * Wenn this und that gleiche Tueten sind, ist diese Tuete nachher leer.
     * @param that Eine Tuete.
     * @return Diese Tuete, eventuell mit weniger Elementen als vorher.
     * @throws NoSuchElementException wenn diese Tuete nicht genug Elemente enthaelt.
     * @throws UnsupportedOperationException wenn diese Tuete unveraenderlich ist.
     */
    Bag<E> remove(Bag<E> that) throws NoSuchElementException;

    @Override
    boolean remove(Object element);

    /** Entfernt mehrere Exemplare eines bestimmten Elementes aus dieser Tuete.
     * Wenn es nicht genug Exemplare gibt, entfernt die Methode so viele wie moeglich.
     * @param element Ein Element.
     * @param times Anzahl Exemplare, die die Methode hoechstens loescht.
     * Nicht negativ.
     * @return Diese Tuete, eventuell mit weniger Elementen als vorher.
     * @throws UnsupportedOperationException wenn diese Tuete unveraenderlich ist.
     * @throws IllegalArgumentException wenn die Anzahl negativ ist.
     */
    Bag<E> remove(Object element, int times);

}
