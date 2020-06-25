package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**Hilfsklasse zu Rules.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-05-06
 */

abstract class AbstractSettings {
    /**Set aller im Spiel bisher vergebenen Geheimnisse.*/
    private final static Set<Optional<String>> secrets= new HashSet<>();

    public void setSecrets(Optional<String> newSecret) {
        secrets.add(newSecret);
    }

    /**Getter der Geheimnisse im Spiel ohne zuordnung zu Spielern.
     * @return unveraenderliches Set der Geheimnisse.
     * */
    public Set<Optional<String>> getSecrets() {
        final Set<Optional<String>> returnSecrets=new HashSet<>();
        returnSecrets.addAll(secrets);
        return returnSecrets;
    }


    /**
     * Liefert alle verfuegbaren Spielzuege.
     * @return Set aller im Spiel implementierten Moves als HotMoves.
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
