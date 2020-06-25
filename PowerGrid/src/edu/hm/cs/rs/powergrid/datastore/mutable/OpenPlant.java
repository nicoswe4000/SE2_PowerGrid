package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.datastore.Plant;
import java.util.Objects;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-02-22
 */
public interface OpenPlant extends Plant, Checksumed {
    void setOperated(boolean operated);

    @Override default int checksum() {
        return Objects.hash(getNumber(), hasOperated());
    }
}
