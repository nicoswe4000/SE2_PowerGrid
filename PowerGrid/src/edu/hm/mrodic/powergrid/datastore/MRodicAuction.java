package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**Eine Auktion.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-05-07
 */
class MRodicAuction implements OpenAuction {
    /** Spieler mit dem hoechsten Angebot.*/
    private OpenPlayer mostBit;
    /** Alle an der Auktion beteiligten Spieler. Nicht null.*/
    private List<OpenPlayer> actuelAktionators;
    /** Hoechstes Angebot. Mindestens die Zahl des Kraftwerkes.*/
    private int actualBit;
    /** Kraftwerk das verkauft wird.*/
    private final OpenPlant plantToSell;

    /**Konstruktor einer Auktion.
     * @param player Alle beteiligten Spieler.
     * @param plant Das Kraftwerk zum Verkaufen.
     */
    MRodicAuction (List<OpenPlayer> player, OpenPlant plant){
        actuelAktionators= new ArrayList<>(player);
        actualBit=plant.getNumber();
        plantToSell=plant;
        mostBit=actuelAktionators.get(0);
    }
    @Override
    public OpenPlayer getPlayer() {
        return Objects.requireNonNull(mostBit);
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(getOpenPlayers());
    }

    /**Getter der an der Auktionbeteiligten Spieler.
     *Open, also veraenderlich
     * @return Liste der Spieler, veraenderlich
     */
    public List<OpenPlayer> getOpenPlayers() {
        if(actuelAktionators.isEmpty()){
            throw new IllegalStateException("In der Liste der Spieler muss mindestens ein Spieler sein (der Hoechtbietende)");
        }
        return actuelAktionators;
    }

    @Override
    public int getAmount() {
        return actualBit;
    }

    @Override
    public OpenPlant getPlant() {
        return Objects.requireNonNull(plantToSell);
    }

    /**Setter des Hoechstbietenden.
     * @param player Aktuell Hoechstbietender.
     * Setzt den Hoechstbietenden auf player.
     */
    public void setPlayer(OpenPlayer player) {
        mostBit=Objects.requireNonNull(player);
    }

    /**Setter des Hoechstgebots.
     * @param amount neues Hoechstgebot.
     * Setzt den Hoechstgebot auf amount
     * @throws IllegalArgumentException wenn das Gebot negativ ist .
     */
    public void setAmount(int amount) {
        if(amount<0){
            throw new IllegalArgumentException("Gebot darf nicht negativ sein");
        }
        actualBit=amount;
    }
}
