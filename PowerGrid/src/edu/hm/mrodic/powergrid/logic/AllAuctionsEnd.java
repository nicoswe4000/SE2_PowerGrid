package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;

/**
 * Ende aller Auktionen in dieser Runde.
 * @author Nicolas Schweinhuber, IF2B, schweinh@edu.edu.hm.edu
 * @version 2020-04-16
 */

class AllAuctionsEnd implements HotMove {

    /** Das momentan laufende Spiel.*/
    private final OpenGame game;
    /** Properties dieses Zuges.*/
    private Properties allAuctionsEndProperties = new Properties();

    /** Oeffentlicher Konstruktor des Zuges EndAuctions.
     * Erzeugt einen Prototypen (Game ist null).
     */
    AllAuctionsEnd(){
        this.game = null;
        setProperty("type", this.getType().toString());
    }

    /**
     * Privater Konstruktor des Zuges EndAuctions.
     * Erzeugt den echten Zug, kein prototyp.
     * @param game Das momentan laufende Spiel. Nicht null.
     */
    private AllAuctionsEnd(OpenGame game){
        this.game = Objects.requireNonNull(game);
        setProperty("type", this.getType().toString());
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problem = Optional.empty();
        if(game.getPhase() != Phase.PlantBuying)
            problem = Optional.of(Problem.NotNow);
        else if(!allPassed())
            problem = Optional.of(Problem.PlayersRemaining);
        else if(real){
            executeReal();
        }
        return problem;
    }

    /**Hilfsmethode für die run Methode.
     * Führt alle Folgen des Zuges aus, sofern er ein echter Zug ist.
     */
    private void executeReal(){
        if(game.getRound() == 1){
            game.getOpenPlayers().sort(Comparator.naturalOrder());
        }
        if(game.getAuction()==null) {
            final Plant smallestPlant=game.getPlantMarket().getOpenActual().stream().min(Comparator.naturalOrder()).get();
            game.getPlantMarket().removePlant(smallestPlant.getNumber());
        }
        // keine Auktionen ?
        for(OpenPlayer player: game.getOpenPlayers()){
            player.setPassed(false);
        }
        game.setPhase(Phase.ResourceBuying);
        // Auction löschen ?
        game.setAuction(null);
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame actualGame, Optional<OpenPlayer> player) {
        if(this.game != null)
            throw new IllegalStateException("This isnt a Prototype!");
        Set<HotMove> runnableMoves = Set.of();
        if(player.isEmpty()) {
            final HotMove prototype = new AllAuctionsEnd(Objects.requireNonNull(actualGame));
            if(prototype.test().isEmpty())
                runnableMoves = Set.of(prototype);
        }
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.EndAuctions;
    }

    /**Hilfsmethode für die run Methode.
     * Prüft ob alle Spieler an der Reihe waren.
     * @return true, wenn alle an der Reihe waren, sonsnt false
     */
    private boolean allPassed(){
        return game.getOpenPlayers().stream()
                .allMatch(Player::hasPassed);
    }

    @Override
    public void setProperty(String name, String value){
        allAuctionsEndProperties.put(name, value);
    }

    @Override
    public Properties getProperties(){
        return allAuctionsEndProperties;
    }

    @Override
    public String toString(){
        String returnString="Prototype: "+getType().toString();
        if(game!=null)
            returnString=asText();
        return returnString;
    }
}