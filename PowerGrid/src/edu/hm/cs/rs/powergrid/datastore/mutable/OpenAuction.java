package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.datastore.Auction;
import edu.hm.cs.rs.powergrid.datastore.Player;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface OpenAuction extends Auction, Checksumed {
    @Override default List<Player> getPlayers() {
        return Collections.unmodifiableList(getOpenPlayers());
    }

    List<OpenPlayer> getOpenPlayers();

    void setPlayer(OpenPlayer player);

    void setAmount(int amount);

    @Override OpenPlayer getPlayer();

    @Override OpenPlant getPlant();


    @Override default int checksum() {
        return Objects.hash(getPlant(),
                            getOpenPlayers(),
                            getPlayer(),
                            getAmount());
    }
}
