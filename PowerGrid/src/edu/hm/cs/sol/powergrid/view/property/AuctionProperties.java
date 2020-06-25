package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.Auction;
import java.util.Objects;
import java.util.Properties;

/** Properties einer Kraftwerksauktion.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class AuctionProperties implements PropertySupplier {
    /** Die Auktion. */
    private final Auction auction;

    /** Generator fuer Properties.
     * @param auction Auktion.
     */
    public AuctionProperties(Auction auction) {
        this.auction = Objects.requireNonNull(auction);
    }

    @Override public Properties get() {
        return new PropertyCollector("plant", auction.getPlant().getNumber(),
                                     "amount", auction.getAmount(),
                                     "top.player", auction.getPlayer().getColor())
                .add("player",
                     auction.getPlayers(),
                     player -> new PropertyCollector("color", player.getColor()).get())
                .get();
    }

}
