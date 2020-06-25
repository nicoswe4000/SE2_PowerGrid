package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Set;

/**ABC zur einfachen Implementierung der getPrototypes Methode.
 * @author Hofmann Angelina
 */
abstract class AbstractAllMoves {
    /**Prototypen.
     * Hilfsmethode um alle Prototypen zu sammeln.
     * @return Menge aller prototypen. Nicht null und nicht leer.
     */
    public Set<HotMove> getAllPrototypes(){
        return Set.of(new JoinNewPlayer(),
                new StartTheGame(),
                new AllAuctionsEnd(),
                new AllEarnMoney(),
                new BeginOfAuction(),
                new CloseResourceMarket(),
                new ClosingTheAuction(),
                new EndOfBuilding(),
                new LeavingTheAuction(),
                new NewRoundNewResourceMarket(),
                new NoResourceTransaction(),
                new OrderThePlayers(),
                new RestorePlantMarket(),
                new SkipConnectingCity(),
                new SkipPlantOperating()
                );
    }
}
