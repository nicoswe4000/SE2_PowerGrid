package edu.hm.cs.sol.powergrid.view.property;

import edu.hm.cs.rs.powergrid.datastore.Game;
import java.util.Objects;
import java.util.Properties;

/** Properties eines Spiels.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public class GameProperties implements PropertySupplier {
    /** Spiel. */
    private final Game game;

    /** Generator fuer Properties.
     * @param game Spiel.
     */
    public GameProperties(Game game) {
        this.game = Objects.requireNonNull(game);
    }

    @Override public Properties get() {
        return new PropertyCollector("edition", game.getEdition().getClass().getSimpleName(),
                                     "level", game.getLevel(),
                                     "phase", game.getPhase(),
                                     "round", game.getRound())
                .add("resourcemarket", new ResourceMarketProperties(game.getResourceMarket()).get())
                .add("plantmarket", new PlantMarketProperties(game.getPlantMarket()).get())
                .add("player",
                     game.getPlayers(),
                     player -> new PlayerProperties(player).get())
                .add("board", new BoardProperties(game.getBoard()).get())
                .add("auction", game.getAuction() == null?
                        new Properties():
                        new AuctionProperties(game.getAuction()).get())
                .get();
    }

}
