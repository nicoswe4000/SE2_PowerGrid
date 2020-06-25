package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**Ein Speil.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-05-07
 */
@SuppressWarnings("DescendantToken")
class MRodicGame implements OpenGame {

    /** Edition dieses Spieles. Nicht null.*/
    private final Edition gameEdition;
    /** Spielplan. Nicht null.*/
    private final OpenBoard gameBoard;
    /** Laufende Runde. Nicht negativ.*/
    private int gameRound;
    /** Spielphase. Nicht null.*/
    private Phase gamePhase;
    /** Alle Spieler. Nicht null.*/
    private final List<OpenPlayer> gamePlayer;
    /** Spielstufe. Nicht negativ.*/
    private int gameLevel;
    /**Kraftstoffmarkt. Nicht null.*/
    private final OpenResourceMarket gameResourceMarket;
    /** Kraftwerksmarkt. Nicht null.*/
    private final OpenPlantMarket gamePlantMarket;
    /** Laufende Auktion.*/
    private OpenAuction gameAuction;
    /** Die Factory dieses Spiel. Nicht null.*/
    private final OpenFactory gameFactory;
    /** Anzahl der bisher ausgeführten Spielzüge. Nicht negativ.*/
    private int gameMoves;
    /**Konstruktor eines neuen Spiels.
     * @param edition Edition des Spieles.
     * @param factory Factory des Spieles.
     */
    MRodicGame (Edition edition,OpenFactory factory){
        gameFactory=Objects.requireNonNull(factory);
        gameEdition=edition;
        gameBoard=gameFactory.newBoard(edition);
        gameRound=0;
        gamePhase=Phase.Opening;
        gamePlayer=new ArrayList<>();
        gameLevel=0;
        gameResourceMarket=Objects.requireNonNull(gameFactory.newResourceMarket(edition));
        gamePlantMarket=Objects.requireNonNull(gameFactory.newPlantMarket(edition));
        gameAuction=null;
        gameMoves=0;
    }


    @Override
    public Edition getEdition() {
        return Objects.requireNonNull(gameEdition);
    }

    @Override
    public OpenBoard getBoard() {
        return gameBoard;
    }

    @Override
    public int getRound() {
        return gameRound;
    }

    @Override
    public void setRound(int round) {
        if(round<0){
            throw new IllegalArgumentException("Game: Round darf nicht negativ sein!");
        }
        gameRound=round;
    }

    @Override
    public Phase getPhase() {
        return Objects.requireNonNull(gamePhase);
    }

    @Override
    public void setPhase(Phase phase) {
        gamePhase=Objects.requireNonNull(phase);
    }

    @Override
    public List<OpenPlayer> getOpenPlayers() {
        //Collections.sort(gamePlayer);
        gamePlayer.sort(Player::compareTo);
        return Objects.requireNonNull(gamePlayer);
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(getOpenPlayers());
    }

    @Override
    public int getLevel() {
        return gameLevel;
    }

    @Override
    public void setLevel(int level) {
        if(level<0){
            throw new IllegalArgumentException("Game:Level darf nicht negativ sein!");
        }
        else{
            gameLevel=level;
        }
    }

    @Override
    public OpenPlantMarket getPlantMarket() {
        return gamePlantMarket;
    }

    @Override
    public OpenResourceMarket getResourceMarket() {
        return gameResourceMarket;
    }

    @Override
    public OpenAuction getAuction() {
        return gameAuction;
    }

    @Override
    public void setAuction(OpenAuction auction) {
        gameAuction=auction;
    }

    @Override
    public OpenPlayer findPlayer(String secret) {
        OpenPlayer returnPlayer=null;
        if(secret!=null){
            returnPlayer=gamePlayer.stream().filter(player->player.hasSecret(secret)).findAny().orElse(null);
        }
        return returnPlayer;
    }

    @Override
    public int getNumMoves() {
        return gameMoves;
    }

    @Override
    public void setNumMoves(int numMoves) {
        if(numMoves<0){
            throw new IllegalArgumentException("Game:NumMoves darf nicht negativ sein!");
        }
        gameMoves=numMoves;
    }

    @Override
    public OpenFactory getFactory() {
        return gameFactory;
    }
}
