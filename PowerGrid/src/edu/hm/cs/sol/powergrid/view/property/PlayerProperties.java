package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import java.util.Objects;
import java.util.Properties;

/** Properties eines Spielers.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class PlayerProperties implements PropertySupplier {
    /** Spieler. */
    private final Player player;

    /** Generator fuer Properties.
     * @param player Spieler.
     */
    public PlayerProperties(Player player) {
        this.player = Objects.requireNonNull(player);
    }

    @Override public Properties get() {
        return new PropertyCollector("color", player.getColor(),
                                     "electro", player.getElectro(),
                                     "passed", player.hasPassed())
                .add("plant",
                     player.getPlants(),
                     plant -> new PropertyCollector("number", plant.getNumber()).get(),
                     Plant::compareTo)
                .add("resource",
                     player.getResources(),
                     resource -> new PropertyCollector("type", resource.name()).get(),
                     Resource::compareTo)
                .add("city",
                     player.getCities(),
                     city -> new PropertyCollector("name", city.getName()).get(),
                     City::compareTo)
                .get();
    }

}
