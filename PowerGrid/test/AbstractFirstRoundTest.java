import edu.hm.cs.rs.powergrid.datastore.Phase;
        import edu.hm.cs.rs.powergrid.datastore.Player;
        import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
        import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
        import edu.hm.cs.rs.powergrid.logic.Move;
        import edu.hm.cs.rs.powergrid.logic.MoveType;
        import edu.hm.cs.rs.powergrid.logic.Rules;
        import edu.hm.cs.rs.powergrid.logic.move.HotMove;
        import org.junit.Assert;

        import java.util.*;
        import java.util.stream.Collectors;

public abstract class AbstractFirstRoundTest {
    /**
     * secret des zuletzt erzeugten Spielers.
     */
    String latestPlayer;
    /**
     * zuletzt erzeugter HotMove.
     */
    HotMove latestHotMove;

    /**
     * Erzeugt einen HotMove.
     * @param phase aktuelle Spielphase.
     * @param openGame aktuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param type Typ des zu erzeugenden HotMoves.
     * @param secret Secret des ausfuehrenden Spielers.
     * @return hotMove von Typ type des uebergebenen Spielers.
     */
    public HotMove getHotMoveGame(Phase phase, OpenGame openGame, Rules rules, MoveType type,Optional<String> secret){
        openGame.setPhase(phase);
        Set<Move> moves=rules.getMoves(secret);
        Move actual=moves.stream().filter(move->move.getType()==type).findFirst().get();
        latestHotMove=(HotMove) actual;
        return latestHotMove;
    }

    /**
     * Besonderer Aufruf zu getHotMoveGame.
     * Speziell zu startAuction.
     * @param openGame actuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param maxPlayers Maximale SPielerzahl.
     * @param firePlayer index des Spielers, der die AUction aufrufen soll.
     * @param plantnr Kraftwerks Nummer, dessen versteigerung beginnen soll.
     * @return ein HotMove zum Typ StartAuction und Player mit Index firePlayer.
     */
    public HotMove getHotMoveStartAuction(OpenGame openGame, Rules rules,int maxPlayers,int firePlayer,String plantnr){
        Assert.assertSame(Phase.PlantBuying,openGame.getPhase());
        List<OpenPlayer> players=openGame.getOpenPlayers();
        players.stream().forEach(player->player.setPassed(true));
        players.stream().skip(maxPlayers-firePlayer).forEach(player->
        {player.setPassed(false);
            player.setElectro(100);
        });
        latestPlayer=players.get(maxPlayers-firePlayer).getSecret();
        Set <Move> moves=rules.getMoves(Optional.ofNullable(latestPlayer));
        Move beginAuction = moves.stream().filter(move->move.getType()==MoveType.StartAuction).filter(move->move.getProperties().contains(plantnr)).findAny().get();
        Assert.assertSame(MoveType.StartAuction,beginAuction.getType());
        latestHotMove=(HotMove)beginAuction;
        return latestHotMove;
    }

    /**
     * Test zu der Zug-Methode getGame.
     * @param phase aktuelle Phase in der der Zug Startet.
     * @param openGame aktuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param type Typ des zu testenden Zugs.
     * @param secret Secret des fuer den Zug verantwortlichen Spielers.
     */
    public void getGameTest(Phase phase, OpenGame openGame, Rules rules, MoveType type,Optional<String> secret){
        HotMove sut= getHotMoveGame(phase,openGame,rules,type,secret);
        Assert.assertSame(openGame,sut.getGame());
    }

    /**
     * Test des Collectaufrufs mit einem HotMove
     * @throws IllegalStateException Liefert im aufrufenden Test Exeption.
     * @param phase aktuelle Phase in der der Zug Startet.
     * @param openGame aktuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param type Typ des zu testenden Zugs.
     * @param secret Secret des fuer den Zug verantwortlichen Spielers.
     */
    public void collectWithNoPrototypeTest(Phase phase, OpenGame openGame, Rules rules, MoveType type,Optional<String> secret){
        HotMove move=getHotMoveGame(phase,openGame,rules,type,secret);
        move.collect(openGame,Optional.empty());
    }


    /**
     * Test der ToStringMethode des uebergebenen Zugs.
     * @param hotMove Zug.
     */
    public void toStringTestHotMove(HotMove hotMove){
        Move noHotMove=hotMove;
        String testString= hotMove.getType()+"{}";
        Assert.assertEquals(testString,hotMove.toString());
        Assert.assertEquals(testString,noHotMove.toString());
    }

    /**
     *ToString Test des Prototyps zu einem Zug.
     * @param phase aktuelle Phase in der der Zug Startet.
     * @param openGame aktuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param type Typ des zu testenden Zugs.
     */
    public void toStringTestPrototype(Phase phase, OpenGame openGame, Rules rules, MoveType type){
        Move move=getPrototypeMove(rules,type);
        String testString="Prototype: "+ movetoString(move);
        String sting=move.toString();
        Assert.assertEquals(testString,sting);
    }

    /**
     * Test zu get Properties.
     * @param hotMove zu testender Zug.
     */
    public void getPropertiesTest(HotMove hotMove){
        Properties properties=new Properties();
        properties.setProperty("type", hotMove.getType().toString());
        Assert.assertEquals(properties,hotMove.getProperties());
    }


    /**
     * set Properties Test.
     * @param hotMove zu testender Zug.
     */
    public void setPropertiesTest(HotMove hotMove){
        Properties properties=new Properties();
        properties.setProperty("type", hotMove.getType().toString());
        properties.put("apfel", "banane");
        hotMove.setProperty("apfel","banane");
        Assert.assertEquals(properties,hotMove.getProperties());
    }

    /**
     * ToString test, wenn der Zug einen Spieler erfordert.
     * @param phase aktuelle Phase in der der Zug Startet.
     * @param openGame aktuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param type Typ des zu testenden Zugs.
     * @param player den Zug betreffender Spieler.
     */
    public void toStringPlayer(Phase phase,OpenGame openGame, Rules rules, Player player,MoveType type){
        latestPlayer=player.getSecret();
        latestHotMove= getHotMoveGame(phase,openGame,rules,type,Optional.ofNullable(latestPlayer));
        String compare=type.toString()+"{player="+latestPlayer.substring(0,latestPlayer.length()-1)+"}";
        Assert.assertEquals(compare,latestHotMove.toString());
    }

    /**
     *zweite Variante des toStringPlayer.
     * einfacher bei manchen Zuegen.
     * ohne neues Erzeugen eines Zugs.
     * @param phase aktuelle Phase in der der Zug Startet.
     * @param openGame aktuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param type Typ des zu testenden Zugs.
     * @param player den Zug betreffender Spieler.
     * @param hotMove zu Testender Zug.
     */
    public void toStringPlayer(Phase phase,OpenGame openGame, Rules rules, String player,MoveType type,HotMove hotMove){
        String compare=type.toString()+"{player="+player.substring(0,player.length()-1)+"}";
        Assert.assertEquals(compare,hotMove.toString());
    }


    /**
     * Test von getPrototype.
     * @param rules Spielregeln.
     * @param type Zug Typ.
     * @return Prototyp eines Zugs.
     */
    public Move getPrototypeMove(Rules rules,MoveType type){
        return rules.getPrototypes().stream().filter(prototype-> prototype.getType()==type).findAny().get();
    }

    /**
     * Test der Tostring Methode.
     * Variation, die nur einen Move, keinen player oder Plant benoetigt.
     * @param Move zu testender Zug.
     * @return Move als String.
     */
    public String movetoString(Move Move){
        Properties properties=Move.getProperties();
        return Move.getType().name()
                + properties.stringPropertyNames().stream()
                .filter(name -> !name.equals("type"))
                .sorted()
                .map(name -> name + '=' + properties.getProperty(name))
                .collect(Collectors.joining(", "));
    }

    /**
     * Variation der toStringMethode mit Player.
     * @param player ausfuerender Spieler.
     * @return String des Zugs.
     */
    public Optional<String> getOptionalPlayerSecret(OpenPlayer player){
        return Optional.ofNullable(player.getSecret());
    }

    /**
     * Variation get Properties mit Kraftwerk und Spieler.
     * @param openGame aktuelles Spiel.
     * @param rules Spielregeln.
     * @param maxPlayers Maximale SPielerzahl.
     * @param firePlayer ausfuehrender Spieler.
     * @param plantnr Kraftwerksnummer.
     * @return Properties des Zugs.
     */
    public Properties getProbertiesTestAuction(OpenGame openGame, Rules rules, int maxPlayers, int firePlayer, String plantnr){
        Properties properties=new Properties();
        properties.setProperty("plant",plantnr);
        properties.setProperty("type","StartAuction");
        HotMove beginAuction=getHotMoveStartAuction(openGame, rules, maxPlayers, firePlayer,plantnr);
        properties.setProperty("player", latestPlayer.substring(0,latestPlayer.length()-1));
        Assert.assertEquals(properties,beginAuction.getProperties());
        return properties;
    }

    /**
     * Variation set Properties mit Kraftwerk und Spieler.
     * @param openGame aktuelles Spiel.
     * @param rules Spielregeln.
     * @param maxPlayers Maximale SPielerzahl.
     * @param firePlayer ausfuehrender Spieler.
     * @param plantnr Kraftwerksnummer.
     */
    public void setPropertiesTestAuction(OpenGame openGame, Rules rules,int maxPlayers,int firePlayer,String plantnr){
        Properties properties= getProbertiesTestAuction(openGame,rules,maxPlayers,firePlayer,plantnr);
        latestHotMove.setProperty("Banane","Elefant");
        properties.setProperty("Banane","Elefant");
        Assert.assertEquals(properties,latestHotMove.getProperties());
    }

    /**
     * Variation getProberties mit Player.
     * @param openGame aktuell laufendes Spiel.
     * @param rules Spielregeln.
     * @param player ausfuehrender Spieler.
     * @return Properties des Zugs.
     */
    public Properties getProbertiesTestPlayer(OpenGame openGame, Rules rules,Player player){
        Properties properties=new Properties();
        latestPlayer=player.getSecret();
        HotMove actHotMove= getHotMoveGame(Phase.PlantAuction,openGame,rules,MoveType.LeaveAuction,Optional.ofNullable(latestPlayer));
        properties.setProperty("type", actHotMove.getType().toString());
        properties.setProperty("player", latestPlayer.substring(0,latestPlayer.length()-1));
        Assert.assertEquals(properties,actHotMove.getProperties());
        return properties;
    }

    /**
     *Variation des getProperties mit Spieler.
     * erzeugt den Zug nicht.
     * @param actHotMove zu testender Zug.
     * @param secret Geheimnis des zugehoerigen Spielers.
     * @return Properties des Zugs.
     */
    public Properties getProbertiesTestPlayer(HotMove actHotMove,String secret){
        Properties properties=new Properties();
        properties.setProperty("type", actHotMove.getType().toString());
        properties.setProperty("player", secret.substring(0,secret.length()-1));
        Assert.assertEquals(properties,actHotMove.getProperties());
        return properties;
    }

    /**
     *Variation des getProperties mit Spieler.
     * erzeugt den Zug nicht.
     * @param phase Spielphase.
     * @param openGame laufendes Spiel.
     * @param rules Spielregeln.
     * @param player auszufuehrender Spieler.
     * @param type Zugtyp.
     */
    public void setPropertiesTestPlayer(Phase phase,OpenGame openGame, Rules rules,Player player,MoveType type){
        Properties properties=getProbertiesTestPlayer(openGame,rules,player);
        latestHotMove.setProperty("Banane","Elefant");
        properties.setProperty("Banane","Elefant");
        Assert.assertEquals(properties,latestHotMove.getProperties());
    }

    /**
     *Variation des getProperties mit Spieler.
     * erzeugt den Zug nicht.
     * @param actHotMove zu Testender Zug
     * @param secret Geheimnis des zugehoerigen Spielers.
     */
    public void setPropertiesTestPlayer(HotMove actHotMove,String secret){
        Properties properties=getProbertiesTestPlayer(actHotMove,secret);
        actHotMove.setProperty("Banane","Elefant");
        properties.setProperty("Banane","Elefant");
        Assert.assertEquals(properties,actHotMove.getProperties());
    }


}

