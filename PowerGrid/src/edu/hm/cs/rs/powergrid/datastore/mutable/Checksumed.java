package edu.hm.cs.rs.powergrid.datastore.mutable;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Stellt einen Code zur Verfuegung, der sich bei jeder Aenderung
 * im Objekt oder einem mittelbar daraus referenzierten Objekt aendert.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface Checksumed {
    default int checksum() {
        return hashCode();
    }

    static int hash(IntStream numbers){
        return numbers.reduce(17, (a, b) -> 31*a + b);
    }

    static int checksumOf(Collection<? extends Checksumed> collection){
        return hash(collection.stream().mapToInt(Checksumed::checksum));
    }
}
