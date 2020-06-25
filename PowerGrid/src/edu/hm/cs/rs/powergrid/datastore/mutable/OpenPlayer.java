package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface OpenPlayer extends Player, Checksumed {
    Set<OpenCity> getOpenCities();

    void setElectro(int electro);

    void setPassed(boolean passed);

    @Override default Set<City> getCities() {
        return Collections.unmodifiableSet(getOpenCities());
    }

    @Override default Set<Plant> getPlants() {
        return Collections.unmodifiableSet(getOpenPlants());
    }

    @Override default Bag<Resource> getResources() {
        return getOpenResources().immutable();
    }

    /**
     * Test, ob dieser Spieler das gegebene Geheimis hat.
     * @param secret Ein String.
     * @return true, wenn der String das Geheimnis ist; false ansonsten.
     */
    boolean hasSecret(String secret);

    Set<OpenPlant> getOpenPlants();

    Bag<Resource> getOpenResources();

    @Override default int checksum() {
        return Objects.hash(getColor(), getElectro(), hasPassed(), getResources(),
                            Checksumed.checksumOf(getOpenCities()),
                            Checksumed.checksumOf(getOpenPlants()));
    }

}
