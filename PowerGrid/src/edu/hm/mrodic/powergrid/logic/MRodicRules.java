package edu.hm.mrodic.powergrid.logic;


import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MRodicRules extends AllMoves implements Rules{
    private OpenGame openGame;

    public MRodicRules(OpenGame game){
        this.openGame=game;
    }

    @Override
    public Game getGame() {
        return openGame;
    }

    private Set<HotMove> getHotMoves(Optional<String> secret) {
        Set<HotMove> result=new HashSet<>();

        if(secret.isEmpty() || openGame.findPlayer(secret.get())!=null){
            Set<HotMove> hotMoves=getAllPrototypes();
            Optional<OpenPlayer> player=Optional.ofNullable(openGame.findPlayer(secret.orElse(null)));
        /*for(HotMove move: hotMoves){
            move.collect(openGame,player);
        }*/
            hotMoves.forEach(prototype->result.addAll(prototype.collect(openGame,player)));
        }
        return result;
    }

    @Override
    public Set<Move> getMoves(Optional<String> secret) {
        Set<Move> result=new HashSet<>();
        Set<HotMove> hotMoves=getHotMoves(secret);
        result.addAll(hotMoves);
        return result;
    }

    @Override
    public Set<Move> getPrototypes() {
        return new HashSet<>(getAllPrototypes());
    }

    @Override
    public Optional<Problem> fire(Optional<String> secret, Move move) {
        HotMove hotMove;
        if(move instanceof HotMove){
            hotMove=(HotMove) move;
        }
        else throw new IllegalArgumentException();

        //ist hotMove aus unserem Spiel?
        if(hotMove.getGame()!=this.getGame()){
            throw new IllegalArgumentException();
        }
        Optional<Problem> problems =hotMove.fire();

        if(problems.isEmpty()){
            boolean flag=true;
            fireWhile(flag,secret);
        }
        return problems;
    }

    private void fireWhile(boolean flag,Optional secret){
        while(flag){
            Set<HotMove> hotMoves=getHotMoves(secret);
            if(hotMoves.stream().anyMatch(Move::hasPriority)){
                //da die Anzahl groesser 0 ist muss einer da sein falls nicht darf Exeption geworfen werden
                hotMoves.stream().filter(Move::hasPriority).findAny().get().fire();
            }
            else if(hotMoves.size()==1){
                //da die Anzahl gleich 1 ist muss einer da sein falls nicht darf Exeption geworfen werden
                final HotMove singleHotMove=hotMoves.stream().findFirst().get();
                if(singleHotMove.isAutoFire()){
                    singleHotMove.fire();
                }
                else flag=false;
            }
            else flag=false;
        }
    }
}