package edu.hm.cs.rs.powergrid.datastore;

import java.util.Set;

/**
 * Der Spielplan.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-02-19
 */
public interface Board {
    /**
     * Sucht eine Stadt.
     * @param name Name.
     * @return Stadt mit dem Namen oder null, wenn es keine mit diesem Namen gibt.
     */
    City findCity(String name);

    /**
     * Alle Staedte.
     * @return Staedte. Nicht null, nicht leer, unveraenderlich.
     */
    Set<City> getCities();
}
