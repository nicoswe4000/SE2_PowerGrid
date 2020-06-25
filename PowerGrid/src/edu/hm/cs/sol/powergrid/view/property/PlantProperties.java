package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.Plant;
import java.util.Objects;
import java.util.Properties;

/** Properties eines Kraftwerks.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class PlantProperties implements PropertySupplier {
    /** Kraftwerk. */
    private final Plant plant;

    /** Generator fuer Properties.
     * @param plant Kraftwerk.
     */
    public PlantProperties(Plant plant) {
        this.plant = Objects.requireNonNull(plant);
    }

    @Override public Properties get() {
        return new PropertyCollector("number", plant.getNumber(),
                                     "type", plant.getType(),
                                     "cities", plant.getCities(),
                                     "resources", plant.getNumberOfResources(),
                                     "operated", plant.hasOperated())
                .get();
    }

}
