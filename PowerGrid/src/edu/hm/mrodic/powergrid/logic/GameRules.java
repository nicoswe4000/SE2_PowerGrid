package edu.hm.mrodic.powergrid.logic;


import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.*;

/**Implementierng der Spielregeln.
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-05-06
 */

public class GameRules  extends AbstractAllMoves implements Rules{
    /** Momentan laufendes Spiel veraenderlich.*/
    private final OpenGame openGame;
    /**Die Flag für fireWhile().*/
    private boolean flag;
    /**Konstruktor der Spielregeln.
     * @param game Spiel, auf das sich diese regeln beziehen.
     */
    public GameRules(OpenGame game){
        this.openGame=game;
    }

    @Override
    public Game getGame() {
        return openGame;
    }

    /**
     * Hilfsmethode um Hotmoves zu erhalten erweitert getMoves.
     * @param secret aktuelles Geheimnis eines Spielers oder leer
     * @return Set aller moeglichen Zuege
     */
    private Set<HotMove> getHotMoves(Optional<String> secret) {
        Set<HotMove> result=new HashSet<>();
        if(secret.isEmpty() || openGame.findPlayer(secret.get())!=null) {
            final OpenPlayer player = openGame.findPlayer(secret.orElse(null));
            result=getHotMovesPlayer(Optional.ofNullable(player));
        }
        return result;
    }

    /**
     * gibt ein Set an Hotmoves zurueck.
     * @param player moegliche Zuege dieses Spielers
     * @return Set an ausfuehrbaren Zuegen
     */
    private Set<HotMove> getHotMovesPlayer(Optional<OpenPlayer> player) {
        final Set<HotMove> result=new HashSet<>();
        final Set<HotMove> hotMoves = getAllPrototypes();
        hotMoves.forEach(prototype -> result.addAll(prototype.collect(openGame, player)));
        return result;
    }


    @Override
    public Set<Move> getMoves(Optional<String> secret) {
        final Set<Move> result=new HashSet<>();
        final Set<HotMove> hotMoves=getHotMoves(secret);
        result.addAll(hotMoves);
        return result;
    }

    @Override
    public Set<Move> getPrototypes() {
        //waere die Anzahl null koennte kein Spiel gespielt werden
        return new HashSet<>(Objects.requireNonNull(getAllPrototypes()));
    }

    @Override
    public Optional<Problem> fire(Optional<String> secret, Move move) {
        final HotMove hotMove;
        if(move instanceof HotMove){
            hotMove=(HotMove) move;
        }
        else throw new IllegalArgumentException();

        //ist hotMove aus unserem Spiel?
        if(hotMove.getGame()!=this.getGame()){
            throw new IllegalArgumentException();
        }
        final Optional<Problem> problems =hotMove.fire();

        if(problems.isEmpty()){
            fireWhile();
        }
        return problems;
    }

    /**
     * Schleife feuert alle Zuege mit Prioritaet oder falls nur ein Zug moeglich und autoFire ist.
     * Hilfsmethode unterstuetzt fire.
     */
    private void fireWhile(){
        final List<Optional<OpenPlayer>> players=new ArrayList<>();
        openGame.getOpenPlayers().stream().forEach(player->players.add(Optional.ofNullable(player)));
        players.add(Optional.empty());
        for(int playerCounter=0; playerCounter<players.size();playerCounter++){
            setFlag(true);
            while(getFlag()){
                final Optional<OpenPlayer> player=players.get(playerCounter);
                final Set<HotMove> hotMoves=getHotMovesPlayer(players.get(playerCounter));
                playerCounter=fireMove(hotMoves,playerCounter);

            }
        }

    }

    /**
     * Hilfsmethode zum Ausfuehren weiterer automatischer Zuege (bzw mit Prio).
     * @param moves alle moeglichen Zuege des Auctuell betrachteten Spielers oder ohne Spieler.
     * @param numPlayers Anzahl aller Spieler im Spiel.
     * @return Nummer des actuel betrachteten Spielers in getPlayer.
     */
    private int fireMove(Set<HotMove> moves,int numPlayers){
        int playerCounter= numPlayers;
        if(moves.stream().anyMatch(Move::hasPriority)){
            //da die Anzahl groesser 0 ist muss einer da sein falls nicht darf Exeption geworfen werden
            final HotMove hotMove=moves.stream().filter(Move::hasPriority).findAny().get();
            hotMove.fire();
            playerCounter=0;
        }
        else if(moves.size()==1){
            //da die Anzahl gleich 1 ist muss einer da sein falls nicht darf Exeption geworfen werden
            final HotMove singleHotMove=moves.stream().findFirst().get();
            if(singleHotMove.isAutoFire()){
                singleHotMove.fire();
                playerCounter=0;
            }
            else setFlag(false);
        }
        else setFlag(false);
        return playerCounter;
    }

    /**Getter für die Flag.
     * Hilfsmethode für fireWhile().
     * @return true oder false.
     */
    private boolean getFlag(){
        return flag;
    }

    /**Setter für die Flag.
     * Hilfsmethode für fireWhile().
     * @param flag Wert für Flag (boolean).
     */
    private void setFlag(boolean flag){
     this.flag=flag;
    }
}

