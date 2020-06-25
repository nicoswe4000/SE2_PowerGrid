import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Rules;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;


/**
 * Testklasse fue die Aufgabe8.
 * @author Rodic Mirna, mrodic@hm.edu
 * @version 2020-06-09
 */
public class ZuegeRodicTest {
    /**Das Spiel.*/
    private final OpenGame game;
    /**Spielregeln.*/
    private  Rules rules ;
    /**Spieler.*/
    private List<OpenPlayer> players=new ArrayList<>();
    /**Zug zum Testen.*/
    private Optional<Move> optionalMove =Optional.empty();
    private final OpenFactory factory;
    private final String fcqnFactory="edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final String fcqnRules="edu.hm.mrodic.powergrid.logic.GameRules";
    public ZuegeRodicTest(){

        factory=OpenFactory.newFactory(fcqnFactory);
        game=factory.newGame(new EditionGermany());
        game.setLevel(0);
        game.setRound(1);
        rules =Rules.newRules(fcqnRules,game);
        /*OpenPlayer player1=factory.newPlayer("secret1","player1");
        OpenPlayer player2=factory.newPlayer("secret2","player2");
        OpenPlayer player3=factory.newPlayer("secret3","player3");*/
        players.add(factory.newPlayer("secret1","player1"));
        players.add(factory.newPlayer("secret2","player2"));
        players.add(factory.newPlayer("secret3","player3"));
    }
    private HotMove findHotMove(MoveType type){
        Optional<Move> move=rules.getPrototypes().stream().filter(option->option.getType()==type).findFirst();
        return (HotMove) move.get();
    }
    //Tests fuer EndResourceBuying:erb

    @Test public void erbFire(){
        game.setPhase(Phase.ResourceBuying);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().forEach(player->player.setPassed(true));
        optionalMove =rules.getMoves(Optional.of("secret2")).stream().filter(option->option.getType().equals(MoveType.EndResourceBuying)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.of("secret2"), optionalMove.get()).isEmpty());
    }
    @Test (expected = NullPointerException.class) public void erbRunPrototype(){
        HotMove move=findHotMove(MoveType.EndResourceBuying);
        move.test();
    }
    @Test  (expected = RuntimeException.class)public void erbCollectNoPrototype(){
        game.setPhase(Phase.ResourceBuying);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().forEach(player->player.setPassed(true));
        optionalMove =rules.getMoves(Optional.of("secret2")).stream().filter(option->option.getType().equals(MoveType.EndResourceBuying)).findFirst();
        HotMove move=(HotMove) optionalMove.get();
        move.collect(game,Optional.of(players.get(1)));
    }
    @Test public void erbFireFalsePhase(){
        game.setPhase(Phase.PlayerOrdering);
        HotMove move=findHotMove(MoveType.EndResourceBuying);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(0))).isEmpty());
    }
    @Test public void erbFirePlayersRemaining(){
        game.setPhase(Phase.ResourceBuying);
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().get(0).setPassed(false);
        HotMove move=findHotMove(MoveType.EndResourceBuying);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(0))).isEmpty());

    }
    @Test public void erbImpact1(){
        erbFire();
        Assert.assertEquals(rules.getGame().getPhase(), Phase.PlantBuying);
    }
    @Test public void erbImpact2(){
        boolean sut=rules.getGame().getPlayers().stream().noneMatch(Player::hasPassed);
        Assert.assertTrue(sut);
    }


    //Tests fuer ConnectNoCity:cnc
    @Test public void cncFire(){
        game.setPhase(Phase.Building);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(false));
        players.get(1).setPassed(true);
        game.getOpenPlayers().addAll(players);
        optionalMove =rules.getMoves(Optional.of("secret3")).stream().filter(option->option.getType().equals(MoveType.ConnectNoCity)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.of("secret3"), optionalMove.get()).isEmpty());
    }
    @Test (expected = NullPointerException.class) public void cncRunPrototype(){
        HotMove move=findHotMove(MoveType.ConnectNoCity);
        move.test();
    }
    @Test (expected = IllegalStateException.class) public void cncCollectNoPrototype(){
        game.setPhase(Phase.Building);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(false));
        players.get(1).setPassed(true);
        game.getOpenPlayers().addAll(players);
        optionalMove =rules.getMoves(Optional.of("secret3")).stream().filter(option->option.getType().equals(MoveType.ConnectNoCity)).findFirst();
        HotMove move=(HotMove) optionalMove.get();
        move.collect(game,Optional.of(players.get(1)));
    }
    @Test public void cncFireFalsePhase(){
        game.setPhase(Phase.Opening);
        HotMove move=findHotMove(MoveType.ConnectNoCity);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(0))).isEmpty());
    }

    @Test public void cncFireAllPassed(){
        game.setPhase(Phase.Building);
        game.getOpenPlayers().forEach(player->player.setPassed(true));
        HotMove move=findHotMove(MoveType.ConnectNoCity);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(1))).isEmpty());
    }

    @Test public void cncFireMoreThanOneNotPassed(){
        game.setPhase(Phase.Building);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().forEach(player->player.setPassed(false));
        game.getOpenPlayers().get(2).setPassed(true);
        game.getOpenPlayers().get(0).getOpenPlants().add(game.getPlantMarket().findPlant(44));
        HotMove move=findHotMove(MoveType.ConnectNoCity);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(0))).isEmpty());
        Assert.assertEquals(false,move.collect(game,Optional.of(players.get(1))).isEmpty());
    }
    @Test public void cncImpact1(){
        cncFire();
        Assert.assertEquals(Phase.PlantBuying,game.getPhase());
       // Assert.assertFalse(rules.getGame().getPlayers().get(0).hasPassed());
        Assert.assertFalse(rules.getGame().getPlayers().get(1).hasPassed());
        Assert.assertFalse(rules.getGame().getPlayers().get(2).hasPassed());
    }


    //Tests fuer EndBuilding:eb
    @Test public void ebFire(){
        game.setPhase(Phase.Building);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(true));
        game.getOpenPlayers().addAll(players);
        optionalMove =rules.getMoves(Optional.of("secret1")).stream().filter(option->option.getType().equals(MoveType.EndBuilding)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.of("secret2"), optionalMove.get()).isEmpty());
    }
    @Test (expected = NullPointerException.class) public void ebRunPrototype(){
        HotMove move=findHotMove(MoveType.EndBuilding);
        move.test();
    }
    @Test (expected = RuntimeException.class) public void ebCollectNoPrototype(){
        game.setPhase(Phase.Building);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(true));
        game.getOpenPlayers().addAll(players);
        optionalMove =rules.getMoves(Optional.of("secret1")).stream().filter(option->option.getType().equals(MoveType.EndBuilding)).findFirst();
        HotMove move=(HotMove) optionalMove.get();
        move.collect(game,Optional.of(players.get(0)));
    }
    @Test public void ebFireFalsePhase(){
        game.setPhase(Phase.ResourceBuying);
        HotMove move=findHotMove(MoveType.EndBuilding);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(0))).isEmpty());
    }

    @Test public void ebFireNotAllPassed(){
        game.setPhase(Phase.Building);
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().get(1).setPassed(false);
        HotMove move=findHotMove(MoveType.EndBuilding);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(0))).isEmpty());
    }
    @Test public void ebImpact1(){
        ebFire();
        Assert.assertEquals(Phase.PlantBuying,rules.getGame().getPhase());
    }
    @Test public void ebImpact2(){
        boolean sut=rules.getGame().getPlayers().stream().noneMatch(Player::hasPassed);
        Assert.assertTrue(sut);
    }


    //Tests fuer OperateNoPlant:onp
    @Test public void onpFire(){
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(false));
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().get(0).getOpenPlants().add(game.getPlantMarket().findPlant(44));
        optionalMove =rules.getMoves(Optional.of("secret1")).stream().filter(option->option.getType().equals(MoveType.OperateNoPlant)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.of("secret1"), optionalMove.get()).isEmpty());
    }
    @Test (expected = NullPointerException.class) public void onpRunPrototype(){
        HotMove move=findHotMove(MoveType.OperateNoPlant);
        move.test();
    }
    @Test (expected = RuntimeException.class) public void onpCollectNoPrototype(){
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(false));
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().get(0).getOpenPlants().add(game.getPlantMarket().findPlant(44));
        optionalMove =rules.getMoves(Optional.of("secret1")).stream().filter(option->option.getType().equals(MoveType.OperateNoPlant)).findFirst();
        HotMove move=(HotMove) optionalMove.get();
        move.collect(game,Optional.of(players.get(1)));
    }
    @Test public void onpFireFalsePhase(){
        game.setPhase(Phase.Building);
        HotMove move=findHotMove(MoveType.OperateNoPlant);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(0))).isEmpty());
    }

    @Test public void onpFireNotFirst(){
        game.setPhase(Phase.PlantOperation);
        HotMove move=findHotMove(MoveType.OperateNoPlant);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(1))).isEmpty());
    }
    @Test public void onpImpact1(){
        onpFire();
        Assert.assertFalse(rules.getGame().getPlayers().get(1).hasPassed());
    }
    @Test public void onpimpact2(){
        onpFire();
        players.sort(Comparator.naturalOrder());
        Assert.assertEquals(players.stream().filter(sut->!sut.hasPassed()).findFirst().get(),game.getOpenPlayers().stream().filter(sut->!sut.hasPassed()).min(Comparator.naturalOrder()).get());
        //optionalMove =rules.getMoves(Optional.of("secret2")).stream().filter(option->option.getType().equals(MoveType.OperateNoPlant)).findFirst();
        //Assert.assertEquals(true, optionalMove.isPresent());
        //Assert.assertTrue(rules.fire(Optional.of("secret2"), optionalMove.get()).isEmpty());
    }


    //Tests fuer SupplyElectricity:se
    @Test public void seFire(){
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(true));
        game.getOpenPlayers().addAll(players);
        optionalMove =rules.getMoves(Optional.of("secret1")).stream().filter(option->option.getType().equals(MoveType.SupplyElectricity)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.of("secret1"), optionalMove.get()).isEmpty());
    }
    @Test (expected = NullPointerException.class) public void seRunPrototype(){
        HotMove move=findHotMove(MoveType.SupplyElectricity);
        move.test();
    }
    @Test (expected = RuntimeException.class) public void seCollectNoPrototype(){
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(true));
        game.getOpenPlayers().addAll(players);
        optionalMove =rules.getMoves(Optional.of("secret1")).stream().filter(option->option.getType().equals(MoveType.SupplyElectricity)).findFirst();
        HotMove move=(HotMove) optionalMove.get();
        move.collect(game,Optional.of(players.get(2)));
    }
    @Test public void seFireFalsePhase(){
        game.setPhase(Phase.Terminated);
        HotMove move=findHotMove(MoveType.SupplyElectricity);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(2))).isEmpty());
    }

    @Test public void seFireNotAllPassed(){
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().addAll(players);
        game.getOpenPlayers().get(1).setPassed(false);
        HotMove move=findHotMove(MoveType.SupplyElectricity);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(2))).isEmpty());
    }

    @Test public void seImpact1(){
        game.setPhase(Phase.PlantOperation);
        game.getOpenPlayers().clear();
        players.forEach(player->player.setPassed(true));
        //Player1 hat noch ein Kraftwerk (cncFireMoreThanOneNotPassed)
        players.get(0).getOpenPlants().add(game.getPlantMarket().findPlant(3));
        players.get(0).getOpenPlants().add(game.getPlantMarket().findPlant(22));
        players.get(1).getOpenPlants().add(game.getPlantMarket().findPlant(7));
        players.stream().limit(2).forEach(player-> player.getOpenPlants().forEach(plant->plant.setOperated(true)));
        game.getOpenPlayers().addAll(players);
        optionalMove =rules.getMoves(Optional.of("secret1")).stream().filter(option->option.getType().equals(MoveType.SupplyElectricity)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        rules.fire(Optional.of("secret1"), optionalMove.get());
        Assert.assertEquals(33,rules.getGame().getPlayers().get(0).getElectro());
        Assert.assertEquals(22,rules.getGame().getPlayers().get(1).getElectro());
        Assert.assertEquals(10,rules.getGame().getPlayers().get(2).getElectro());
    }
    @Test public void seImpact2(){
        boolean sut1=players.get(0).getOpenPlants().stream().noneMatch(Plant::hasOperated);
        boolean sut2=players.get(1).getOpenPlants().stream().noneMatch(Plant::hasOperated);
        Assert.assertTrue(sut1);
        Assert.assertTrue(sut2);
    }
    @Test public void seImpect3(){
        boolean sut=rules.getGame().getPlayers().stream().noneMatch(Player::hasPassed);
        Assert.assertTrue(sut);
    }

    @Test public void seImpact4(){
        seFire();
        Assert.assertEquals(Phase.PlantBuying,rules.getGame().getPhase());
    }


    //Tests fuer TurnOver:to
    @Test public void toFire(){
        game.setPhase(Phase.Bureaucracy);
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenFuture().add(game.getPlantMarket().findPlant(7));
        game.getPlantMarket().getOpenFuture().add(game.getPlantMarket().findPlant(10));
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.TurnOver)).findFirst();
        Assert.assertEquals(50,rules.getGame().getResourceMarket().getAvailable().size());
        Assert.assertEquals(34,rules.getGame().getResourceMarket().getSupply().size());
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.empty(), optionalMove.get()).isEmpty());
    }
    @Test (expected = NullPointerException.class) public void toRunPrototype(){
        HotMove move=findHotMove(MoveType.TurnOver);
        move.test();
    }
    @Test (expected = RuntimeException.class) public void toCollectNoPrototype(){
        game.setPhase(Phase.Bureaucracy);
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenFuture().add(game.getPlantMarket().findPlant(7));
        game.getPlantMarket().getOpenFuture().add(game.getPlantMarket().findPlant(10));
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.TurnOver)).findFirst();
        HotMove move=(HotMove) optionalMove.get();
        move.collect(game,Optional.of(players.get(2)));
    }
    @Test public void toFireFalsePhase(){
        game.setPhase(Phase.Building);
        HotMove move=findHotMove(MoveType.TurnOver);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(2))).isEmpty());
    }

    @Test public void toImpact1(){
        toFire();
        game.setPhase(Phase.Building);
        Assert.assertEquals(54,rules.getGame().getResourceMarket().getAvailable().size());
        Assert.assertEquals(30,rules.getGame().getResourceMarket().getSupply().size());
    }
    @Test public void toImpact2(){
        Assert.assertFalse(rules.getGame().getPlantMarket().getFuture().contains(game.getPlantMarket().findPlant(10)));
    }
    @Test public void toImpact3(){
        game.setPhase(Phase.Bureaucracy);
        game.setLevel(2);
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenActual().add(game.getPlantMarket().findPlant(3));
        game.getPlantMarket().getOpenActual().add(game.getPlantMarket().findPlant(5));
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.TurnOver)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        rules.fire(Optional.empty(), optionalMove.get());
        Assert.assertEquals(null,game.getPlantMarket().findPlant(3));
    }
    @Test public void toImpact4(){
        toFire();
        Assert.assertEquals(2,rules.getGame().getRound());
    }
    @Test public void toImpact5(){
        toFire();
        Assert.assertEquals(Phase.PlantBuying,rules.getGame().getPhase());
    }


    //Tests fuer UpdatePlantMarket:upm

    @Test public void upmFirePlantBuying(){
        game.setPhase(Phase.PlantBuying);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().addAll(players);
        //TurnOver musste Kraftwerke entfernt haben
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.empty(), optionalMove.get()).isEmpty());
    }
    @Test (expected = NullPointerException.class) public void upmRunPrototype(){
        HotMove move=findHotMove(MoveType.UpdatePlantMarket);
        move.test();
    }
    @Test (expected = RuntimeException.class) public void upmCollectNoPrototype(){
        game.setPhase(Phase.PlantBuying);
        game.getOpenPlayers().clear();
        game.getOpenPlayers().addAll(players);
        //TurnOver musste Kraftwerke entfernt haben
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        HotMove move=(HotMove) optionalMove.get();
        move.collect(game,Optional.of(players.get(0)));
    }

    @Test public void upmFireBuilding(){//<----------------!!!!
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenActual().clear();
        game.getPlantMarket().getOpenFuture().clear();
        game.setLevel(1);
        int numHidden=game.getPlantMarket().getNumberHidden();
        game.setPhase(Phase.Building);
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.empty(), optionalMove.get()).isEmpty());
        Assert.assertEquals(8,rules.getGame().getPlantMarket().getActual().size()+rules.getGame().getPlantMarket().getFuture().size());
        //Assert.assertEquals(numHidden-8-1,rules.getGame().getPlantMarket().getNumberHidden());
    }

    @Test public void upmFireBureaucracy(){
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenFuture().clear();
        game.setPhase(Phase.Bureaucracy);
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        Assert.assertTrue(optionalMove.isPresent());
        Assert.assertTrue(rules.fire(Optional.empty(), optionalMove.get()).isEmpty());
    }

    @Test public void upmFireFalsePhase(){
        game.getPlantMarket().getOpenFuture().clear();
        game.setPhase(Phase.Bureaucracy);
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        game.setPhase(Phase.ResourceBuying);
        HotMove move=findHotMove(MoveType.UpdatePlantMarket);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(2))).isEmpty());
        Assert.assertFalse(rules.fire(Optional.empty(),optionalMove.get()).isEmpty());
    }

    @Test public void upmAllThere(){
        game.setPhase(Phase.Bureaucracy);
        Stream.of(11,12,13,14).forEach(number->game.getPlantMarket().getOpenActual().add(game.getPlantMarket().findPlant(number)));
        Stream.of(15,16,17,18).forEach(number->game.getPlantMarket().getOpenFuture().add(game.getPlantMarket().findPlant(number)));
        HotMove move=findHotMove(MoveType.UpdatePlantMarket);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(2))).isEmpty());
    }

    @Test public void upmNoLeft(){
        game.setPhase(Phase.Bureaucracy);
        game.getPlantMarket().getOpenFuture().clear();
        game.getPlantMarket().getOpenActual().clear();
        game.getPlantMarket().getOpenHidden().clear();
        //move=rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        HotMove move=findHotMove(MoveType.UpdatePlantMarket);
        Assert.assertEquals(true,move.collect(game,Optional.of(players.get(2))).isEmpty());
    }

    @Test public void upmNoLeftFuture(){
        game.setPhase(Phase.Bureaucracy);
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenFuture().clear();
        game.getPlantMarket().getOpenActual().clear();
        HotMove move=findHotMove(MoveType.UpdatePlantMarket);
        Assert.assertEquals(false,move.collect(game,Optional.of(players.get(2))).isEmpty());
    }

    @Test public void upmNoLeftHidden(){
        game.setPhase(Phase.Bureaucracy);
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenActual().clear();
        game.getPlantMarket().getOpenHidden().stream()
                .limit(4)
                .forEach(number->game.getPlantMarket().getOpenFuture().add(number));
        game.getPlantMarket().getOpenHidden().clear();
        HotMove move=findHotMove(MoveType.UpdatePlantMarket);
        Assert.assertEquals(false,move.collect(game,Optional.of(players.get(2))).isEmpty());
    }
    @Test public void upmImpact1(){
        game.getOpenPlayers().addAll(players);
        game.setPhase(Phase.PlantBuying);
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        rules.fire(Optional.empty(),optionalMove.get());
        rules.fire(Optional.empty(),optionalMove.get());
        rules.fire(Optional.empty(),optionalMove.get());
        List<Plant> market= new ArrayList<>(game.getPlantMarket().getActual());
        Assert.assertTrue(market.get(0).compareTo(market.get(1))<=0);
        Assert.assertTrue(market.get(1).compareTo(market.get(2))<=0);
        Assert.assertTrue(market.get(2).compareTo(market.get(3))<=0);
    }

    @Test public void upmImpact2(){
        game.getOpenPlayers().addAll(players);
        game.setPhase(Phase.PlantBuying);
        game.setLevel(1);
        game.getPlantMarket().getOpenFuture().clear();
        game.getPlantMarket().getOpenActual().clear();
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        for(int counter=0;counter<8;counter++)
            rules.fire(Optional.empty(), optionalMove.get());
        List<Plant> market= new ArrayList<>(game.getPlantMarket().getFuture());
        //System.out.println(game.getPlantMarket().getOpenFuture());
        Optional<Plant> plant=game.getPlantMarket().getFuture().stream().max(Comparator.naturalOrder());
        Assert.assertEquals(plant.get().getNumber(),market.get(market.size()-1).getNumber());
    }

    @Test public void upmImpact3(){
        game.setPhase(Phase.Bureaucracy);
        game.getOpenPlayers().addAll(players);
        game.getPlantMarket().getOpenActual().clear();
        game.getPlantMarket().getOpenHidden().stream()
                .limit(4)
                .forEach(number->game.getPlantMarket().getOpenFuture().add(number));
        game.getPlantMarket().getOpenHidden().clear();
        optionalMove =rules.getMoves(Optional.empty()).stream().filter(option->option.getType().equals(MoveType.UpdatePlantMarket)).findFirst();
        rules.fire(Optional.empty(), optionalMove.get());
        Assert.assertEquals(4,game.getPlantMarket().getOpenActual().size());
        Assert.assertTrue(game.getPlantMarket().getOpenFuture().isEmpty());
    }
    @Test public void setPropertyNameNull(){
        int sum =0;
        for(Move move: rules.getPrototypes()) {
            try {
                ((HotMove) move).setProperty(null, "abc");
            } catch (NullPointerException e) {
                sum++;
            }
        }
        Assert.assertTrue(sum==rules.getPrototypes().size());
    }
    @Test public void setPropertyValueNull(){
        int sum=0;
        for(Move move: rules.getPrototypes()) {
            try {
                ((HotMove) move).setProperty("type", null);
            } catch (NullPointerException e) {
                sum++;
            }
        }
        Assert.assertTrue(sum==rules.getPrototypes().size());
    }
    @Test public void getProperties(){
        game.getOpenPlayers().addAll(players);
        game.setAuction(factory.newAuction(game.getPlantMarket().findPlant(21),players));
        Set<Move> moves=new HashSet<>();
        for(Phase phase:Phase.values()){
            game.setPhase(phase);
            game.getOpenPlayers().forEach(player->player.setPassed(true));
            moves.addAll(rules.getMoves(Optional.empty()));
            moves.addAll(rules.getMoves(Optional.of("secret2")));
            game.getOpenPlayers().get(1).setPassed(false);
            moves.addAll(rules.getMoves(Optional.of("secret2")));
        }
        //System.out.println(moves.size());
        for(Move move:moves)
            Assert.assertTrue(move.getProperties().size()>=1);
    }
    @Test public void toStringTest(){
        game.getOpenPlayers().addAll(players);
        game.setAuction(factory.newAuction(game.getPlantMarket().findPlant(21),players));
        Set<Move> moves=new HashSet<>();
        for(Phase phase:Phase.values()){
            game.setPhase(phase);
            game.getOpenPlayers().forEach(player->player.setPassed(true));
            moves.addAll(rules.getMoves(Optional.empty()));
            moves.addAll(rules.getMoves(Optional.of("secret2")));
            game.getOpenPlayers().get(1).setPassed(false);
            moves.addAll(rules.getMoves(Optional.of("secret2")));
        }
        for(Move move:moves){
            String[] first=move.toString().split("\\{");
            String[] second=first[1].split("}");
            if(second.length==0)
                Assert.assertTrue(move.getProperties().size()==1);
            else {
                String[] third = second[0].split(", ");
                Assert.assertTrue(third.length == move.getProperties().size()-1);
            }
        }
    }
    @Test public void toStringPrototypeTest(){
        for(Move move:rules.getPrototypes()){
            Assert.assertTrue(move.toString().split(":").length==2);
        }
    }
    @Test public void getGamePrototype(){
        try {
            for (Move move : rules.getPrototypes())
                ((HotMove) move).getGame();
        }catch (NullPointerException e){

        }
    }
    @Test public void getGame(){
        game.getOpenPlayers().addAll(players);
        game.setAuction(factory.newAuction(game.getPlantMarket().findPlant(21),players));
        Set<Move> moves=new HashSet<>();
        for(Phase phase:Phase.values()){
            game.setPhase(phase);
            game.getOpenPlayers().forEach(player->player.setPassed(true));
            moves.addAll(rules.getMoves(Optional.empty()));
            moves.addAll(rules.getMoves(Optional.of("secret2")));
            game.getOpenPlayers().get(1).setPassed(false);
            moves.addAll(rules.getMoves(Optional.of("secret2")));
        }
        for(Move move:moves){
            Assert.assertTrue(((HotMove)move).getGame()==game);
        }
    }

}
