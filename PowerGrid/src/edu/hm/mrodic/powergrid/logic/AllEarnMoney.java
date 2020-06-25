package edu.hm.mrodic.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Problem;
import edu.hm.cs.rs.powergrid.logic.move.HotMove;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**Zug des Typs SupplyElectricity.
 * @author Rodic Mirna, mrodic@hm.edu
 * @version 2020-06-10
 */
class AllEarnMoney implements HotMove {
    /** Dieses Spiel.*/
    private final OpenGame game;
    /**Attrubute von diesem Zug.*/
    private Properties properties=new Properties();

    /**Privater Konstruktor.
     * Kein Prototyp.
     * @param game Dieses Speil. Nicht null.
     */
    private AllEarnMoney(OpenGame game){
        this.game=game;
        setProperty("type",getType().toString());
    }

    /**Öffentlicher Konstruktor.
     * Setzt alle Attribute gleich null. Erzeugt einen Prototyp.
     */
    AllEarnMoney(){
        this.game=null;
        setProperty("type",getType().toString());
    }
    @Override
    public Optional<Problem> run(boolean real) {
        Objects.requireNonNull(this.game);
        Optional<Problem> problems=Optional.empty();
        final boolean noneRemaining=game.getPlayers().stream().allMatch(Player::hasPassed);
        if(game.getPhase()!= Phase.PlantOperation)
            problems=Optional.of(Problem.NotNow);
        else if(!noneRemaining)
            problems=Optional.of(Problem.PlayersRemaining);
        else if(real){
            game.getOpenPlayers().forEach(player->{final int runedPlants = player.getOpenPlants().stream().filter(Plant::hasOperated).collect(Collectors.toList()).size();
                                      final int income=game.getEdition().getPoweredCitiesIncome().get(runedPlants);
                                      player.setElectro(player.getElectro()+income);});
            game.getOpenPlayers().forEach(player->{player.getOpenPlants().forEach(plant->plant.setOperated(false));
                                                   player.setPassed(false);});
            game.setPhase(Phase.Bureaucracy);
        }
        return problems;
    }

    @Override
    public OpenGame getGame() {
        return Objects.requireNonNull(game);
    }

    @Override
    public Set<HotMove> collect(OpenGame actualGame, Optional<OpenPlayer> player) {
        if(this.game!=null)
            throw new IllegalStateException("This isn't a prototype.");
        Set<HotMove> runnableMoves=Set.of();
        final HotMove prototype=new AllEarnMoney(Objects.requireNonNull(actualGame));
        if(prototype.test().isEmpty())
            runnableMoves=Set.of(prototype);
        return runnableMoves;
    }

    @Override
    public MoveType getType() {
        return MoveType.SupplyElectricity;
    }

    @Override
    public void setProperty(String name, String value) {
        properties.setProperty(Objects.requireNonNull(name),Objects.requireNonNull(value));
    }

    @Override
    public Properties getProperties() {
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
