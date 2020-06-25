package edu.hm.cs.rs.powergrid;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** * ABC zur einfacheren Implementierung konkreter Bagklassen.
 * Die Methoden dieser Klasse kommen ohne konkrete Datenstrukturen aus und sind deshalb nicht
 * besonders effizient.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-22
 * @param <E> Typ der Elemente.
 */
public abstract class AbstractBag<E> extends AbstractCollection<E> implements Bag<E> {
    @Override public int size() {
        return distinct().stream()
                .mapToInt(this::count)
                .sum();
    }

    @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            /** Iterator ueber unterschiedliche Elementsorten. */
            private final Iterator<E> iterator = distinct().iterator();

            /** Aktuelle Elementsorte. */
            private E element;

            /** Anzahl Exemplare der aktuelle Elementsorte, die dieser Iterator noch liefern kann.
             * 0, wenn die naechste Sorte an der Reihe ist.
             */
            private int remaining;

            @Override public boolean hasNext() {
                if(remaining > 0) // noch Exemplare der aktuellen Sorte uebrig?
                    return true;
                if(iterator.hasNext()) {    // aktuelle Sorte verbraucht, weiter mit der naechsten
                    element = iterator.next();
                    remaining = count(element);
                    assert remaining > 0;
                    return true;
                }
                return false;
            }

            @Override public E next() {
                if(remaining == 0)
                    hasNext();  // wenn hasNext false liefert, ist das Ergebnis undefiniert
                remaining--;
                return element;
            }

        };
    }

    @Override public Bag<E> add(E element, int times) {
        IntStream.range(0, requireNotNegative(times)).forEach(__ -> add(element));
        return this;
    }

    @Override public Bag<E> add(Bag<? extends E> that) {
        addAll(that);
        return this;
    }

    @Override public boolean contains(Bag<E> that) {
        if(this == that)
            return true;
        return that.distinct()
                .stream()
                .allMatch(element -> count(element) >= that.count(element));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override public boolean equals(Object anything) {
        // Warnung rawtypes: instanceof mit Pattern Matching von Java 14 erlaubt noch keine Typargumente.
        // Ein Wildcard wuerde nichts nutzen, weil weder diese Methode noch die
        // aufgerufene Methode contains vom Typparameter abhaengen.
        if(anything instanceof Bag) {
            Bag bag = (Bag) anything;
            // Warnung unchecked: Aufruf der generische Methode contains mit einem Rawtype-Argument.
            // Der Aufruf ist sicher, weil contains den Typparameter nicht nutzen kann.
            return contains(bag) && bag.contains(this);
        }
        return false;
    }

    /** Implementiert eine Hashfunktion, die unabhaengig von der Reihenfolge der Elemente ist
     * und keine temporaere Containerdatenstruktur braucht.
     * Diese Implementierung ist nicht besonders gut, aber billig.
     * @return Hashcode des Inhaltes.
     * @see https://en.wikipedia.org/wiki/Tabulation_hashing fuer andere Algorithmen.
     */
    @Override public int hashCode() {
        return distinct().stream()
                .map(element -> Objects.hash(element, count(element)))
                .reduce(0, (a, b) -> a ^ b);
    }

    /** Diese Methode ist abstrakt, weil die aus AbstractCollection ererbte Implementierung
     * Iterator.remove braucht.
     * Der Iterator dieser Klasse kann remove ohne Kenntnis einer konkreten Datenstruktur nicht
     * implementieren.
     * Die konkrete Bagklasse muss clear daher neu implementieren.
     */
    @Override public abstract void clear();

    @Override public Bag<E> remove(Bag<E> that) {
        if(!contains(that))
            throw new NoSuchElementException();
        if(this == that)
            clear();
        else
            that.forEach(this::remove);
        return this;
    }

    @Override public String toString() {
        final Stream<E> distinct = distinct().stream();
        final Stream<String> token = size() < 8 // Bis 8 Elemente alle einzeln auflisten
                ? distinct
                .flatMap(element -> Stream.generate(() -> element)
                        .limit(count(element)))
                .map(Object::toString)
                : distinct // darueber mit Multiplikator
                .map(element -> (count(element) > 1 ? count(element) + "x" : "") + element);
        return token.collect(Collectors.joining(", ", "(", ")"));
    }

    @Override
    public Bag<E> remove(Object element, int times) {
        IntStream.range(0, requireNotNegative(times)).forEach(__ -> remove(element));
        return this;
    }

    private static int requireNotNegative(int number) {
        if(number < 0)
            throw new IllegalArgumentException("at least 0 required: " + number);
        return number;
    }

}
