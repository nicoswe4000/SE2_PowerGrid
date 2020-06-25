package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;

/**
 * Der Rohstoffmarkt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-03-15
 */
public interface ResourceMarket {
    /**
     * Kaeufliche Rohstoffe.
     * @return Im Markt verfuegbare Rohstoffmengen.
     */
    Bag<Resource> getAvailable();

    /**
     * Rohstoffe, die momentan im Vorrat liegen und nicht im Markt verfuegbar sind.
     * @return Rohstoffmengen im Vorrat.
     */
    Bag<Resource> getSupply();

    /**
     * Preis eines Rohstoffes, abhaengig vom aktuellen Angebot.
     * @param resource Rohstoff.
     * @return Preis in Elektro. Nicht negativ.
     * @throws IllegalArgumentException wenn der Rohstoff nicht im Angebot ist.
     */
    int getPrice(Resource resource);
}
