package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import java.util.Set;

/**
 * Ein Spieler.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-07
 */
public interface Player extends Comparable<Player>  {
    /**
     * Farbe dieses Spielers.
     * @return Farbe. Nicht null.
     */
    String getColor();

    /**
     * Staedte, die dieser Spieler an sein Netz angeschlossen hat.
     * @return Unveraenderliche Menge der Staedte. Nicht null.
     */
    Set<City> getCities();

    /**
     * Das Vermoegen.
     * @return Anzahl Elektro. Nicht negativ.
     */
    int getElectro();

    /**
     * Spieler war schon an der Reihe.
     * @return true genau dann, wenn der Spieler an der Reihe war.
     */
    boolean hasPassed();

    /**
     * Die Kraftwerke dieses Spielers.
     * @return Unveraenderliche Menge der Kraftwerke. Nicht null.
     */
    Set<Plant> getPlants();

    /**
     * Die Rohstoffe, die der Spieler in seinen Kraftwerken lagert.
     * @return Unveranederliche Rohstoffe. Nicht null.
     */
    Bag<Resource> getResources();

    /**
     * Liefert das Geheimnis dieses Spielers.
     * Nur der erste Aufruf liefert das Geheimnis.
     * Der zweite und alle weiteren liefern null.
     * @return Geheimnis oder null ab dem zweiten Aufruf.
     */
    String getSecret();
}
