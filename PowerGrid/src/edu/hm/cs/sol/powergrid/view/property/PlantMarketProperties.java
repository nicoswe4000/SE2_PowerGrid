package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.PlantMarket;
import java.util.Objects;
import java.util.Properties;

/** Properties eines Kraftwerksmarktes.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class PlantMarketProperties implements PropertySupplier {
    /** Kraftwerksmarkt. */
    private final PlantMarket market;

    /** Generator fuer Properties.
     * @param market Kraftwerksmarkt.
     */
    public PlantMarketProperties(PlantMarket market) {
        this.market = Objects.requireNonNull(market);
    }

    @Override public Properties get() {
        return new PropertyCollector("hidden", market.getNumberHidden())
            .add("actual",
                 market.getActual(),
                 plant -> new PlantProperties(plant).get(),
                 Plant::compareTo)
            .add("future",
                 market.getFuture(),
                 plant -> new PlantProperties(plant).get(),
                 Plant::compareTo)
            .get();
    }

}
