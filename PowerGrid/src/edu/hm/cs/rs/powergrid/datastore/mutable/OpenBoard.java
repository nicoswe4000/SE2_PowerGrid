package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import java.util.Collections;
import java.util.Set;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface OpenBoard extends Board, Checksumed {
    void closeRegions(int numPlayers);

    Set<OpenCity> getOpenCities();

    /**
     * Sucht eine Stadt.
     * @param name Name.
     * @return Stadt mit dem Namen oder null, wenn es keine gibt.
     */
    @Override default OpenCity findCity(String name) {
        return getOpenCities()
                .stream()
                .filter(city -> city.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    @Override default Set<City> getCities() {
        return Collections.unmodifiableSet(getOpenCities());
    }

    void close();

    @Override default int checksum() {
        return Checksumed.hash(getOpenCities().stream().mapToInt(OpenCity::checksum));
    }
}
