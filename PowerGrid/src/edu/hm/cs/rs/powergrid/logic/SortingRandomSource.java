
package edu.hm.cs.rs.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import java.util.Collections;
import java.util.List;

/**
 * Deterministische Zufallsentscheidungen fuer Tests.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-06
 */
public class SortingRandomSource implements RandomSource {
    @Override public void shufflePlants(List<OpenPlant> plants) {
        Collections.sort(plants);
    }

    @Override public void shufflePlayers(List<OpenPlayer> players) {
        Collections.sort(players);
    }

    @Override public String babbled(String base) {
        return base + 'X';
    }
}
