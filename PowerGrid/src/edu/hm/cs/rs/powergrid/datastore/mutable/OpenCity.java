package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.datastore.City;
import java.util.Collections;
import java.util.Map;

/**
 * Eine veraenderliche Stadt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface OpenCity extends City, Checksumed {
    /**
     * Verbindet diese Stadt mit einer anderen.
     * @param to   Eine andere Stadt. Nicht null, nicht diese und kein frueherer Aufruf.
     * @param cost Verbindungskosten. Nicht negativ.
     * @throws IllegalStateException wenn die Stadt geschlossen ist.
     */
    void connect(City to, int cost);

    Map<City, Integer> getOpenConnections();

    @Override default Map<City, Integer> getConnections() {
        return Collections.unmodifiableMap(getOpenConnections());
    }

    /**
     * Schliesst die Verbindungen dieser Stadt ab.
     * connect-Aufrufe sind nicht mehr erlaubt, dafuer getConnections.
     * @throws IllegalStateException wenn die Stadt geschlossen ist.
     * @see OpenCity#connect(City, int)
     * @see OpenCity#getConnections()
     */
    void close();
}
