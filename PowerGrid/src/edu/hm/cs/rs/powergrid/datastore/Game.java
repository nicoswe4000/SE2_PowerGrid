package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.Observable;
import java.util.List;

/**
 * Ein Spiel.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-03
 */
public interface Game extends Observable {
    /**
     * Ausgabe des Spieles.
     * @return Ausgabe. nicht null.
     */
    Edition getEdition();

    Board getBoard();

    /**
     * Rundennummer.
     * Zaehlt am Beginn jeder Runde hoch.
     * @return Runde, in der das Spiel gerade ist. Bei Spielbeginn 0. Nicht negativ.
     */
    int getRound();

    /**
     * Spielphase.
     * @return Phase. Nicht null.
     */
    Phase getPhase();

    /**
     * Liste der Spieler. mit dem besten zuerst.
     * Geordnet nach fallender Anzahl angeschlossener Staedte und bei gleicher Anzahl
     * nach fallender Nummer des groessten Kraftwerks.
     * @return Spieler. Nicht null.
     */
    List<Player> getPlayers();

    /**
     * Index der Spielstufe.
     * @return Stufe, 0-based (0 = Stufe 1, ...). Nicht negativ.
     */
    int getLevel();

    PlantMarket getPlantMarket();

    ResourceMarket getResourceMarket();

    Auction getAuction();

    /**
     * Sucht einen Spieler mit dem gegebenen Geheimnis.
     * Diese Methode kehrt eventuell nicht sofort zurueck, sondern braucht eine Weile.
     * @param secret Ein String.
     * @return Spieler mit diesem Geheimnis oder null, wenn es keinen gibt.
     */
    Player findPlayer(String secret);

    /**
     * Anzahl der bisher ausgefuehrten Spielzuege.
     * @return Anzahl Zuege. Bei Spielbeginn 0. Nicht negativ.
     */
    int getNumMoves();
}
