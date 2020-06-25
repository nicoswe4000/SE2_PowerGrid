package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface OpenGame extends Game, Checksumed {
    @Override OpenBoard getBoard();

    void setLevel(int level);

    void setPhase(Phase phase);

    List<OpenPlayer> getOpenPlayers();

    @Override default List<Player> getPlayers() {
        return Collections.unmodifiableList(getOpenPlayers());
    }

    void setAuction(OpenAuction auction);

    @Override OpenPlantMarket getPlantMarket();

    @Override OpenResourceMarket getResourceMarket();

    void setRound(int round);

    @Override OpenAuction getAuction();

    default OpenPlayer findPlayer(String secret) {
        return getOpenPlayers().stream()
                .filter(player -> player.hasSecret(secret))
                .findAny()
                .orElse(null);
    }

    OpenFactory getFactory();

    void setNumMoves(int numMoves);

    @Override default int checksum() {
        return Objects.hash(getLevel(),
                            getPhase(),
                            Checksumed.checksumOf(getOpenPlayers()),
                            getAuction() == null? 0: getAuction().checksum(),
                            getPlantMarket().checksum(),
                            getResourceMarket().checksum(),
                            getRound());
    }
}
