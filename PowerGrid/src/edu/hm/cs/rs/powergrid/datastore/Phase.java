package edu.hm.cs.rs.powergrid.datastore;

/**
 * Phasen, die ein Spiel durchlaueft.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-02-26
 */
public enum Phase {
    /** Spieler aufnehmen. Das Spiel laeuft noch nicht. */
    Opening,
    /** Beginn einer Runde: Spieler ordnen. */
    PlayerOrdering,
    /** Kraftwerke erwerben. */
    PlantBuying,
    /** Ein Kraftwerk versteigern. */
    PlantAuction,
    /** Rohstoffe kaufen. */
    ResourceBuying,
    /** Staedte anschliessen. */
    Building,
    /** Kraftwerke betreiben und Staedte mit Strom versorgen. */
    PlantOperation,
    /** Einkommen erhalten und Rohstoffmarkt auffuellen. */
    Bureaucracy,
    /** Spiel beendet. */
    Terminated;

    /**
     * Test, ob das Spiel laeuft.
     * @return true genau dann, wenn das Spiel laeuft.
     */
    public boolean isRunning() {
        return this != Opening && this != Terminated;
    }
}
