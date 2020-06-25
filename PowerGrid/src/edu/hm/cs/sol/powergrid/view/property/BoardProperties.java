package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import java.util.Comparator;
import java.util.Objects;
import java.util.Properties;

/** Properties eines Spielplans.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class BoardProperties implements PropertySupplier {
    /** Spielplan. */
    private final Board board;

    /** Generator fuer Properties.
     * @param board Spieplan.
     */
    public BoardProperties(Board board) {
        this.board = Objects.requireNonNull(board);
    }

    @Override public Properties get() {
        return new PropertyCollector()
            .add("city",
                 board.getCities(),
                 city -> new CityProperties(city).get(),
                 Comparator.comparing(City::getRegion).thenComparing(City::getName))
            .get();
    }
}
