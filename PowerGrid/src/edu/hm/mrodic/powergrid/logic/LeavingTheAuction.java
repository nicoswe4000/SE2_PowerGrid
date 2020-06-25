package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;


/**
 * Verlassen einer Auktion.
 * @author Nicolas Schweinhuber, IF2B, schweinh@edu.edu.hm.edu
 * @version 2020-04-16
 */

class LeavingTheAuction implements HotMove {

    /** Das momentan laufende Spiel.*/
    private final OpenGame game;
    /** Der Spieler, der diesen Zug ausfuehren soll.*/
    private final OpenPlayer player;
    /** Properties dieses Zuges.*/
    private Properties properties = new Properties();

    /** Oeffentlicher Konstruktor des Zuges LeaveAuction.
     * Erzeugt einen Prototypen (Game und Player sind null).
     */
    LeavingTheAuction(){
        this.game = null;
        this.player = null;
        setProperty("type", this.getType().toString());
    }

    /**
     * Privater Konstruktor des Zuges LeaveAuction.
     * Erzeugt den echten Zug, kein prototyp.
     * @param game Das momentan laufende Spiel. Nicht null.
     * @param player Der Spieler, der den Zug ausfuehren soll. Nicht null.
     */
    private LeavingTheAuction(OpenGame game, OpenPlayer player){
        this.game = Objects.requireNonNull(game);
        this.player = Objects.requireNonNull(player);
        setProperty("type", this.getType().toString());
        setProperty("player", player.getColor());
    }

    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(game);
        Optional<Problem> problem = Optional.empty();
        List<OpenPlayer> notParticipated=new ArrayList<>();
        if(game.getPhase() != Phase.PlantAuction)
            problem = Optional.of(Problem.NotNow);
        else {
            notParticipated = game.getAuction().getOpenPlayers();
            if (player != notParticipated.get(0))//<-------------------------------------------????????????!!!!!!!!!!!!!!
                problem = Optional.of(Problem.NotYourTurn);
            else if (player == game.getAuction().getPlayer())
                problem = Optional.of(Problem.TopBidder);
        }
        executeReal(problem, real);
        return problem;
    }

    /**Hilfsmethode für die run Methode.
     * Führt bei einem realen Zug alle Folgen des Zuges aus.
     * @param runProblem Optional mit Problemen, kann leer sein.
     * @param runReal boolean der angibt, ob der Zug tatsächlich ausgeführt werden soll.
     */
    private void executeReal(Optional<Problem> runProblem, boolean runReal){
      if(runReal && runProblem.isEmpty())
          game.getAuction().getOpenPlayers().remove(player);
    } // hab keine andere Möglichkeit gefunden die Komplexität auseinander zu ziehen, als so

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
            final HotMove prototype = new LeavingTheAuction(Objects.requireNonNull(actualGame), actualPlayer.get());
            if(prototype.test().isEmpty())
                runnableMoves = Set.of(prototype);
        }
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.LeaveAuction;
    }

    @Override
    public void setProperty(String name, String value){
        properties.put(name, value);
    }

    @Override
    public Properties getProperties(){
        return properties;
    }

    @Override
    public String toString(){
        String returnString="Prototype: "+getType().toString();
        if(game!=null)
            returnString=asText();
        return returnString;
    }
}