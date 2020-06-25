package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;

import java.util.*;

/**
 * Der Spielplan.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-16
 */

class MRodicBoard implements OpenBoard {
    /** Zustand des Spielplans. Geschlossen oder offen. True nach dem ersten close-Aufruf.*/
    private boolean closed;
    /** Menge der Staedte auf dem Spielplan. Nicht null. Vielleich leer. */
    private Set<OpenCity> cities;
    /** Edition des Spiels. Nicht null.*/
    private final Edition edition;

    /**
     * Konstruktor des Spielplans.
     * @throws NullPointerException wenn Edition null ist.
     * @param edition Edition des Spiels.
     * @param cities Staedte auf diesem Spielplan.
     */
    MRodicBoard(Edition edition,Set<OpenCity> cities) {
        this.cities = new HashSet<>(cities);
        this.closed=false;
        this.edition=edition;
        connectCities();
    }

    /**
     * Sucht eine Stadt.
     * @param name Name.
     * @return Stadt mit dem Namen oder null, wenn es keine mit diesem Namen gibt.
     */
    @Override
    public OpenCity findCity(String name) {
        return cities.stream().filter(city->city.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Entfernt alle Staedte bis auf die zur Spielerzahl gehoerenden.
     * Loescht auch alle Verbindungen von und zu entfernten Staedten.
     * @param numPlayers Staedte in Regionen zu der gegebenen Spielerzahl.
     *                  Staedte darueber verschwinden.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     * @throws IllegalArgumentException wenn die Anzahl der Spieler kleiner 0 ist.
     * @throws IllegalArgumentException wenn die Anzahl der Spieler groesser ist als erlaubt.
     * @see MRodicBoard#cityRemover
     */
     @Override public void closeRegions(int numPlayers) {
         if(numPlayers<0){
             throw new IllegalArgumentException("Anzahl der Spieler muss groesser 0 sein");
         }
         final List<Integer> locationsToPlayers =edition.getRegionsUsed();
         //Setze immer eins Groesser als maximaler Index
         if(numPlayers>locationsToPlayers.size()-1){
             throw new IllegalArgumentException("Die Anzahl der Spieler ist groesser als erlaubt");
         }
         final int remaining=locationsToPlayers.get(numPlayers);
         closeRegionsHelp(remaining);
     }

    /**
     * Entfernt alle Staedte mit einer Region ueber der Grenze.
     * Loescht auch alle Verbindungen von und zu entfernten Staedten.
     * @param remaining Staedte in Regionen bis zu dieser Nummer bleiben bestehen.
     *                  Staedte darueber verschwinden.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     * @see MRodicBoard#cityRemover
     */
    private void closeRegionsHelp(int remaining) {
        if(closed) {
            throw new IllegalStateException();
        }
        else{
            final Set<OpenCity> citiesToRemove=new HashSet<>();
            cities.stream().filter(city->city.getRegion()<=remaining).forEach(city->cityRemover(city.getOpenConnections(),remaining));
            cities.stream().filter(city -> city.getRegion()>remaining).forEach(citiesToRemove::add);
            cities.removeAll(citiesToRemove);
        }
    }

    @Override
    public Set<OpenCity> getOpenCities() {
        return cities;
    }

    /**
     * Entfernt alle Verbindungen dieser Stadt zu den Staedten in den Regionen über die gegebener Nummer.
     * @param remaining Staedte in Regionen bis zu dieser Nummer bleiben bestehen.
     *                  Staedte darueber verschwinden.
     * @param connections Die Verbindungen einer Stadt zu allen anderen.
     */
    private void cityRemover(Map<City, Integer> connections, int remaining){

        final Set<Map.Entry<City, Integer>> entries = new HashSet<>(connections.entrySet());
        connections.clear();
        entries.stream().filter(entry->entry.getKey().getRegion()<=remaining).forEach(entry->connections.put(entry.getKey(),entry.getValue()));
    }

    /**
     * Menge aller Staedte.
     * @return Staedte. Nicht null.
     * Veraenderlich bis zum ersten close-Aufruf. Dann unveraenderlich.
     */
    @Override
    public Set<City> getCities() {
        return Collections.unmodifiableSet(getOpenCities());
    }

    /**
     * Schliesst diesen Spielplan und alle Staedte darauf.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     */
    @Override
    public void close() {
        if(closed) {
            throw new IllegalStateException("This Board is closed");
        }
        else {
            closed = true;
            cities = Collections.unmodifiableSet(cities);
            cities.forEach(City::close);

        }
    }

    /**Hilfsmethode.
     * Verbindet alle Staedte dieses Spielplans.     *
     */
    private void connectCities(){
        for(String row : edition.getCitySpecifications()) {
            final String[] informations = row.split("[\\s]+");
            final OpenCity from = this.findCity(informations[0]);
            OpenCity toCity;
            for (int indexOfInformation = 2; indexOfInformation < informations.length; indexOfInformation += 2) {
                final int cost;
                final String name;
                name = informations[indexOfInformation];
                toCity = this.findCity(name);
                cost = Integer.parseInt(informations[indexOfInformation + 1]);
                //es wird eine Verbindung in beide Richtungen erzeugt.
                from.connect(toCity, cost);
                toCity.connect(from, cost);
            }
        }
    }
}
