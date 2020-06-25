package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.ResourceMarket;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/** Properties eines Rohstoffmarktes.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class ResourceMarketProperties implements PropertySupplier {
    /** Rohstoffmarkt. */
    private final ResourceMarket market;

    /** Generator fuer Properties.
     * @param market Rohstoffmarkt.
     */
    public ResourceMarketProperties(ResourceMarket market) {
        this.market = Objects.requireNonNull(market);
    }

    @Override public Properties get() {
        return new PropertyCollector()
            .add("available",
                 List.of(Resource.values()),
                 resource -> new PropertyCollector()
                    .add("resource", resource,
                         "amount", market.getAvailable().count(resource))
                    .get(),
                 Resource::compareTo)
            .add("supply",
                 List.of(Resource.values()),
                 resource -> new PropertyCollector()
                    .add("resource", resource,
                         "amount", market.getSupply().count(resource))
                    .get(),
                 Resource::compareTo)
            .get();
    }

}
