package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Eine Stadt auf dem Spielplan.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-16
 */
 class MRodicCity implements OpenCity {
    /** Name der Stadt. Nicht null und nicht leer. */
    private  final String cityName;
    /** Gebiet in dem sich die Stadt befindet. Echt positiv.*/
    private  final int cityArea;
    /** Zustand der Stadt. Geschlossen oder offen. True nach dem ersten close-Aufruf.*/
    private boolean closed;
    /** Verbindungen mit den anderen Staedten (andere Stadt und die Kosten der Verbindung). Nicht null und nicht leer.*/
    private Map <City,Integer> connections ;
    /** Zustand der Stadt. Verbunden oder nicht. True nach dem ersten connect-Aufruf.*/
    private boolean connected;

    /**
     * Konstruktor einer Stadt.
     * @param name Name der Stadt. Nicht null und nicht leer.
     * @param area Gebiet in dem sich die Stadt befindet. Ect positiv.
     * @throws NullPointerException wenn der Name null ist.
     * @throws IllegalArgumentException wenn der Name leer ist.
     * @throws IllegalArgumentException wenn das Gebiet kleiner-gleich 0 ist.
     */
    MRodicCity(String name, int area){
        connected=false;
        if(area>0) {
            this.cityArea = area;
        }
        else {
            throw new IllegalArgumentException("Name mustn't be empty and area must be >0");
        }
        this.cityName=name;
        closed=false;
        connections=new HashMap<>();
    }

    /**
     * Name der Stadt.
     * @return Name. Nicht leer und nicht null.
     */
    @Override
    public String getName() {
        return cityName;
    }

    /**
     * Gebiet, in dem die Stadt liegt.
     * @return Gebiet. Wenigstens 1.
     */
    @Override
    public int getRegion() {
        return cityArea;
    }

    /**
     * Verbindungen zu anderen Staedten.
     * Veraenderlich bis zum ersten close-Aufruf, dann unveraenderlich.
     * @return Verbindungen. Nicht null und nicht leer.
     * Jeder Eintrag bildet eine andere Stadt auf die Verbindungskosten dort hin ab.
     */
    @Override
    public Map<City, Integer> getConnections() {
        return Collections.unmodifiableMap(getOpenConnections());
    }

    /**
     * Verbindet diese Stadt mit einer anderen.
     * Nur vor dem ersten close-Aufruf erlaubt.
     * @param toCity   Eine andere Stadt. Nicht null, nicht diese.
     * @param cost Verbindungskosten. Nicht negativ.
     * @throws IllegalStateException    wenn diese Stadt geschlossen ist.
     * @throws IllegalArgumentException wenn es schon eine Verbindung zu toCity gibt.
     */
    @Override
    public void connect(City toCity, int cost) {
        connected=true;
        if(closed){
            throw new IllegalStateException("This City is closed");
        }
        else {
            if(cost<0 || toCity==this) {
                throw new IllegalArgumentException("toCity mustn't be this City and cost must be >=0");
            }
            if(connections.containsKey(toCity) ) {
                throw new IllegalArgumentException("This connection exists already");
            }
            final Integer costObject= cost;
            connections.put(Objects.requireNonNull(toCity),costObject);
        }
    }

    @Override
    public Map<City, Integer> getOpenConnections() {
        return connections;
    }

    /**
     * Schliesst die Verbindungen dieser Stadt ab.
     * connect-Aufrufe sind nicht mehr erlaubt, dafuer getConnections.
     * @throws IllegalStateException wenn die Stadt geschlossen ist.
     */
    @Override
    public void close() {
        if(closed){
            throw new IllegalStateException("This City is closed");
        }
        else if(connected){
            closed = true;
            connections = Collections.unmodifiableMap(connections);
        }
        else {
            throw new IllegalStateException("This City isn´t connected yet");
        }
    }

    /**
     * Natuerliche Ordnung: Name, alphabetisch.
     * Vergleicht diese Stadt mit der anderen bezüglich des Namens in der alphabetischen Reihenfolge.
     * Name ist der Schluesselattribut der City-Objekte. Es werden die Namen nach den Stelle im Alphabet verglichen.
     * @param that Andere Stadt. Nicht null.
     * @throws NullPointerException wenn that null ist.
     * @return 0 heißt this==that; groesser 0 heißt this ist nach that; kleiner 0 heißt this ist vor that.
     */
    @Override
    public int compareTo(City that) {
        final String nameThat=Objects.requireNonNull(that).getName();
        return this.cityName.compareToIgnoreCase(nameThat);
    }
}