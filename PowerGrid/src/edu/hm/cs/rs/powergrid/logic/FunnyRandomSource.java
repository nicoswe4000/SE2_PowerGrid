
package edu.hm.cs.rs.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Deterministische Zufallsentscheidungen fuer Tests.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-06
 */
public class FunnyRandomSource implements RandomSource {
    /** Tauscht Zehner- und Einerstellen einer Zahl.
     * Verwirft die anderen.
     * @param number Eine Zahl.
     * @return Zahl mit getauschten Zehner- und Einerstellen.
     */
    private static int flipDigits(int number) {
        return number%10*10 + number/10;
    }

    @Override public void shufflePlants(List<OpenPlant> plants) {
        Collections.sort(plants, Comparator.comparing(plant -> flipDigits(plant.getNumber())));
    }

    @Override public void shufflePlayers(List<OpenPlayer> players) {
        Collections.sort(players, Comparator.comparing(OpenPlayer::getColor));
    }

    @Override public String babbled(String base) {
        return Integer.toString(Math.abs(base.hashCode())%1_000);
    }
}
