package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;

import java.util.List;
import java.util.Map;

/**Ein Kraftstoffmarkt.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-05-07
 */
class MRodicResourceMarket implements OpenResourceMarket {
    /** Momentan verfügbare Kraftwerke. Nicht null.*/
    private final Bag<Resource> available;
    /** Die noch nicht im Markt verfügbaren Rohstoffe.*/
    private final Bag<Resource> supply;
    /** Preise der Rohstoffe. Nicht null.*/
    private final Map<Resource, List<Integer>> toCost;

    /**Konstruktor eines Kraftstoffmarktes.
     * @param resourcesAvailable Verfügbare Rohstoffe.
     * @param resourcesSupply  Restlichen Rohstoffe.
     * @param resourceCost Preisliste.
     */
    MRodicResourceMarket(Bag<Resource> resourcesAvailable, Bag<Resource> resourcesSupply, Map<Resource, List<Integer>> resourceCost){
        available=resourcesAvailable;
        supply=resourcesSupply;
        toCost=resourceCost;
    }

    @Override
    public Bag<Resource> getAvailable() {
        return getOpenAvailable().immutable();
    }

    @Override
    public Bag<Resource> getOpenAvailable() {
        return available;
    }

    @Override
    public Bag<Resource> getSupply() {
        return getOpenSupply().immutable();
    }

    @Override
    public Bag<Resource> getOpenSupply() {
        return supply;
    }

    @Override
    public int getPrice(Resource resource) {
        if(!this.getAvailable().contains(resource)) {
            throw new IllegalArgumentException("Diese Resource ist nicht verfuegbar");
        }
        return toCost.get(resource).get(available.count(resource) - 1);
    }
}
