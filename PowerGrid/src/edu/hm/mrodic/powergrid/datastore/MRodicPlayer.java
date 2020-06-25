package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.mrodic.powergrid.MRodicBag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Ein Spieler.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-16
 */
 class MRodicPlayer implements OpenPlayer {
     /** Farbe (ID) des Spielers. Nicht null.*/
    private final String uniqueColor;
    /** Geheimnis (Passwort) des Spielers. Nicht null.*/
    private final String privateSecret;
    /** Zustand des Spielers. True genau dann, wenn der Spieler an der Reihe war.*/
    private boolean didPass;
    /** Vermoegen/Kaufkraft/Geld des Spielers. Nicht negativ.*/
    private int electroContingent;
    /** Rohstoffe, die der Spieler in seinen Kraftwerken lagert. Nicht null. */
    private final Bag<Resource> resourcesContingent =new MRodicBag<>();//PASST ES SO?? ICH HABE KEINE ANDEREN INFOS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /** Die Staedte, die dieser Spieler an sein Netz angeschlossen hat. Nicht null.*/
    private final Set<OpenCity> connectedCities;
    /** Die Kraftwerke im Besitz des Spielers. Nicht null.*/
    private final Set<OpenPlant> plants;
   /** Zustand des Geheimnisses. True nach dem ersten getSecret-Aufruf.*/
    private boolean secretGiven;

    /**
     * Konstruktor eines Spielers.
     * Alle Collections werden leer erzeugt, das Vermoegen auf 0 und boolean Objektvariablen auf false gesetzt.
     * @param secret Das Geheimnis (Passwort) des Spielers. Nicht null.
     * @param color Die Farbe (ID) des Spielers. Nicht null.
     * @throws NullPointerException wenn das Gehemnis null ist.
     * @throws NullPointerException wenn die Farbe null ist.
     */
    MRodicPlayer(String secret, String color){
        this.privateSecret = Objects.requireNonNull(secret,"Secret musst be non null");
        this.uniqueColor=color;
        didPass =false;
        electroContingent =0;
        secretGiven=false;
        connectedCities=new HashSet<>();
        plants=new HashSet<>();
    }

    /**
     * Farbe dieses Spielers.
     * @return Farbe. Nicht null.
     */
    @Override
    public String getColor() {
        return uniqueColor;
    }

    /**
     * Staedte, die dieser Spieler an sein Netz angeschlossen hat.
     * @return Menge der Staedte. Nicht null.
     */
    @Override
    public Set<City> getCities() {
        return Collections.unmodifiableSet(getOpenCities());
    }

    /**
     * Die Kraftwerke dieses Spielers.
     * @return Menge der Kraftwerke. Nicht null.
     */
    @Override
    public Set<Plant> getPlants() {
        return Collections.unmodifiableSet(getOpenPlants());
    }

    /**
     * Die Rohstoffe, die der Spieler in seinen Kraftwerken lagert.
     * @return  Rohstoffe. Nicht null.
     */
    @Override
    public Bag<Resource> getResources() {
        return getOpenResources().immutable();
    }

    /**
     * Das Vermoegen.
     * @return Anzahl Elektro. Nicht negativ.
     */
    @Override
    public int getElectro() {
        return electroContingent;
    }


    @Override
    public Set<OpenCity> getOpenCities() {
        return connectedCities;
    }

    /**
     * Legt das Vermoegen neu fest.
     * @param electro Anzahl Elektro. Nicht negativ.
     */
    @Override
    public void setElectro(int electro) {
        if(electro <0) {
            throw new IllegalArgumentException("Electro must be positiv!");
        }
        else {
            this.electroContingent = electro;
        }
    }

    /**
     * Test, ob der Spieler schon an der Reihe war.
     * @return true genau dann, wenn der Spieler an der Reihe war.
     */
    @Override
    public boolean hasPassed() {
        return didPass;
    }

    @Override
    public void setPassed(boolean passed) {
        this.didPass= passed;
    }

    /**
     * Liefert das Geheimnis dieses Spielers.
     * Nur der erste Aufruf liefert das Geheimnis.
     * Der zweite und alle weiteren liefern null.
     * @return Geheimnis oder null ab dem zweiten Aufruf.
     */
    @Override
    public String getSecret() {
        String secretReturn=null;
        if(!secretGiven) {
            secretReturn = privateSecret;
            secretGiven = true;
        }
        return secretReturn;
    }

    /** Test, ob dieser Spieler das gegebene Geheimis hat.
     * @param secret Ein String.
     * @return true, wenn der String das Geheimnis ist; false ansonsten.
     */
    @Override
    public boolean hasSecret(String secret) {
        boolean isSecret=false;
        if(secret.equals(this.privateSecret)) {
            isSecret = true;
        }
        return isSecret;
    }

    @Override
    public Set<OpenPlant> getOpenPlants() {
        return plants;
    }

    @Override
    public Bag<Resource> getOpenResources() {
        return resourcesContingent;
    }

    /**
     * Natuerliche Ordnung: Anzahl angeschlossener Städte, fallend; bei gleicher Anzahl Städte: Nummer des größten Kraftwerkes, fallend; ohne Kraftwerke: Farbe, alphabetisch.
     * Vergleicht diesen Spieler mit dem anderen bezüglich der oben genannten Attribute.
     * Farbe ist der Schluesselattribut der Player-Objekte. Farben werden nach den Stelle im Alphabet verglichen.
     * @param that Anderer Spieler. Nicht null.
     * @throws NullPointerException wenn that null ist.
     * @return Compare Wert Null bei Gleichheit Negativ, falls kleiner, positiv falls groesser.
     */
    @Override
    public int compareTo(Player that) {
        final int citiesThat=Objects.requireNonNull(that).getCities().size();
        final String colorThat=that.getColor();
        int compared=citiesThat-connectedCities.size();
        if(compared==0) {
            //nur wahr, wenn beide 0, 2 Spieler koennen nicht das selbe Kraftwerk besitzen
            if (biggestPlantNumber(that)==biggestPlantNumber(this)) {
                compared = this.uniqueColor.compareToIgnoreCase(colorThat);
            }
            else {
                compared = biggestPlantNumber(that) - biggestPlantNumber(this);
            }

        }
        return compared;
    }

    /**
     * Hilfsmethode für die compareTo-Methode.
     * Rechnet das größte Kraftwerk des Spielers that aus.
     * @param that Eins Spieler. Nicht null (wird in compareTo geprüft).
     * @return die Nummer des größten Kraftwerks.
     */
    private int biggestPlantNumber(Player that){
        int biggestThis=0;
        for(Plant plant: that.getPlants()){
            if(plant.getNumber()>biggestThis) {
                biggestThis = plant.getNumber();
            }
        }
        return biggestThis;
    }
}
