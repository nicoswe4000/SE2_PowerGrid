package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Player;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;

import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Beginn einer Auktion.
 * @author Nicolas Schweinhuber, IF2B, schweinh@edu.edu.hm.edu
 * @version 2020-04-16
 */

class BeginOfAuction implements HotMove {

    /** Das momentan laufende Spiel.*/
    private final OpenGame game;
    /** Der Spieler, der diesen Zug ausfuehren soll.*/
    private final OpenPlayer player;
    /** Das Kraftwerk, welches in diesem Zug versteigert werden soll.*/
    private final OpenPlant plant;
    /** Properties dieses Zuges.*/
    private Properties beginOfAuctionProperties = new Properties();

    /** Oeffentlicher Konstruktor des Zuges StartAuction.
     * Erzeugt einen Prototypen (Game, Player und Plant sind null).
     */
    BeginOfAuction(){
        this.game = null;
        this.player = null;
        this.plant = null;
        setProperty("type", this.getType().toString());
    }

    /**
     * Privater Konstruktor des Zuges StartAuction.
     * Erzeugt den echten Zug, kein prototyp.
     * @param game Das momentan laufende Spiel. Nicht null.
     * @param player Der Spieler, der den Zug ausfuehren soll. Nicht null.
     * @param plant Das Kraftwerk, welches in dieser Auktion versteigert werden soll. Nicht null.
     */
    private BeginOfAuction(OpenGame game, OpenPlayer player, OpenPlant plant){
        this.game = Objects.requireNonNull(game);
        this.player = Objects.requireNonNull(player);
        this.plant = Objects.requireNonNull(plant);
        setProperty("type", this.getType().toString());
        setProperty("player", player.getColor());
        setProperty("plant", String.valueOf(plant.getNumber()));
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problem = Optional.empty();
        final List<OpenPlayer> notParticipated = new ArrayList<>();
        game.getOpenPlayers().stream().filter(actualPlayer->!actualPlayer.hasPassed()).forEach(notParticipated::add);
        if(game.getPhase() != Phase.PlantBuying)
            problem = Optional.of(Problem.NotNow);
        else {
            problem = conditionChecker();
        }
        if(real && problem.isEmpty()){
            final OpenAuction auction = game.getFactory().newAuction(plant, notParticipated);
            game.setAuction(auction);
            game.getAuction().setPlayer(player);
            game.getAuction().setAmount(plant.getNumber());
            //game.getAuction().getOpenPlayers().remove(player);
            //game.getAuction().getOpenPlayers().set(game.getAuction().getOpenPlayers().size()-1,player);
            game.getAuction().getOpenPlayers().add(game.getAuction().getOpenPlayers().remove(0));
            game.setPhase(Phase.PlantAuction);
        }
        return problem;
    }

    /**Hilfsmethode für die run Methode.
     * Prüft ob alle Bedingungen erfüllt sind.
     * @return Optional mit Problem, eventuell leer.
     */
    private Optional<Problem> conditionChecker(){
        Optional<Problem> check = Optional.empty();
        final Player lastPlayer=game.getPlayers().stream().filter(sut->!sut.hasPassed()).min(Comparator.naturalOrder()).orElse(null);
        if (lastPlayer!=player)
            check = Optional.of(Problem.NotYourTurn);
        else if (!game.getPlantMarket().getActual().contains(plant))
            check = Optional.of(Problem.PlantNotAvailable);
        else if (player.getElectro() < plant.getNumber())
            check=Optional.of(Problem.NoCash);
        return check;
    }


    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame actualGame, Optional<OpenPlayer> actualPlayer) {
        if(this.game != null)
            throw new IllegalStateException("This isnt a Prototype!");
        Set<HotMove> runnableMoves = Set.of();
        if(actualPlayer.isPresent()) {
            runnableMoves= Objects.requireNonNull(actualGame).getPlantMarket()
                    .getOpenActual().stream()
                    .map(openPlant->new BeginOfAuction(Objects.requireNonNull(actualGame), actualPlayer.get(), openPlant))
                    .filter(prototype->prototype.test().isEmpty())
                    .collect(Collectors.toSet());
        }
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.StartAuction;
    }

    @Override
    public void setProperty(String name, String value){
        beginOfAuctionProperties.put(name, value);
    }

    @Override
    public Properties getProperties(){
        return beginOfAuctionProperties;
    }

    @Override
    public String toString(){
        String returnString="Prototype: "+getType().toString();
        if(game!=null)
            returnString=asText();
        return returnString;
    }
}