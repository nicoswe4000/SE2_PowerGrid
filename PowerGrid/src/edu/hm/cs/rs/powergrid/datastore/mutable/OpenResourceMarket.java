package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.ResourceMarket;
import java.util.Objects;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface OpenResourceMarket extends ResourceMarket, Checksumed {
    @Override default Bag<Resource> getAvailable() {
        return getOpenAvailable().immutable();
    }

    Bag<Resource> getOpenAvailable();

    @Override default Bag<Resource> getSupply() {
        return getOpenSupply().immutable();
    }

    Bag<Resource> getOpenSupply();

    @Override default int checksum() {
        return Objects.hash(getAvailable(), getSupply());
    }
}
