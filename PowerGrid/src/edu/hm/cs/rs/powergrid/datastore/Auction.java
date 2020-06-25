package edu.hm.cs.rs.powergrid.datastore;

import java.util.List;

/**
 * Eine laufende Kraftwerks-Versteigerung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-03-09
 */
public interface Auction {
    /**
     * Spieler mit dem momentan hoechsten Gebot.
     * @return Spieler. Nicht null.
     */
    Player getPlayer();

    /**
     * Alle Spieler, die an dieser Auktion beteiligt sind.
     * Der erste Spieler in der Liste gibt das naechste Gebot ab.
     * @return Spielerliste. Nicht leer. Unveraenderlich.
     */
    List<Player> getPlayers();

    /**
     * Betrag des momentan hoechsten Gebotes.
     * @return Anzahl Elektro. Wenigstens so viel wie die Kraftwerksnummer.
     */
    int getAmount();

    /**
     * Versteigertes Kraftwerk.
     * @return Kraftwerk. Nicht null.
     */
    Plant getPlant();
}
