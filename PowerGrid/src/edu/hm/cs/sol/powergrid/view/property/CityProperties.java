package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.City;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/** Properties einer Stadt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class CityProperties implements PropertySupplier {
    /** Stadt. */
    private final City city;

    /** Generator fuer Properties.
     * @param city Stadt.
     */
    public CityProperties(City city) {
        this.city = Objects.requireNonNull(city);
    }

    @Override public Properties get() {
        return new PropertyCollector("name", city.getName(),
                                     "region", city.getRegion())
                .add("connection",
                     city.getConnections().entrySet(),
                     connection -> new PropertyCollector("to", connection.getKey().getName(),
                                                         "cost", connection.getValue())
                             .get(),
                     Comparator.comparing(Map.Entry<City, Integer>::getKey))
                .get();
    }

}
