package edu.hm.mrodic.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Optional;
import java.util.Set;

public class ArrivalOfANewHero implements HotMove {

    @Override
    public Optional<Problem> run(boolean real) {
        return Optional.empty();
    }

    @Override
    public OpenGame getGame() {
        return null;
    }

    @Override
    public Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player) {
        return null;
    }

    @Override
    public MoveType getType() {
        return null;
    }
}
