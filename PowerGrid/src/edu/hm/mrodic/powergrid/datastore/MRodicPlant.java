package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.mrodic.powergrid.MRodicBag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Ein Kraftwerk.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-16
 */
class MRodicPlant implements OpenPlant {
    /** Zustand des Kraftwerks. True wenn das Kraftwerk Strom produziert hat.*/
    private boolean didOperate;
    /** Eindeutige Nummer (ID) des Kraftwerks. Nicht negativ.*/
    private final int plantNumber;
    /** Typ/Art des Kraftwerks. Nicht null.*/
    private final Type plantType;
    /** Anzahl der Rohstoffe, die das Kraftwerk verbrennen muss. Nicht negativ.*/
    private final int necessaryResources;
    /** Anzahl der Staedte, die das Kraftwerk versorgen kann. Ect positiv.*/
    private final int numberOfCities;
    /** Menge aller Kombinationen der Kraftstoffen, die das Kraftwerk verbrennen kann. Nicht null und nicht leer.*/
    private final Set<Bag<Resource>> bagResources;

    /**
     * Konstruktor eines Kraftwerkes.
     * @param number eindeutige Identifikationsnummer des Kraftwerkes. Nicht negativ.
     * @param type Typ des Kraftwerkes. Nicht null.
     * @param resources die Anzahl der Kraftstoffe, die das Kraftwerk braucht. Nicht negativ.
     * @param cities die Anzahl der Staedte, die das Kraftwerk versorgen kann. Echt positiv.
     * @throws IllegalArgumentException wenn die Nummer des Kraftwerkes kleiner  0 ist
     * @throws NullPointerException wenn der Typ des Kraftwerkes null ist.
     * @throws IllegalArgumentException wenn die Anzahl der Kraftstoffe kleiner 0 ist.
     * @throws IllegalArgumentException wenn die Anzahl der Staedte kleiner-gleich 0 ist.
     */
    MRodicPlant(int number, Type type, int resources, int cities){
        didOperate =false;
        this.plantNumber=number;
        this.plantType = Objects.requireNonNull(type,"Typ must be non null");
        if(resources<0) {
            throw new IllegalArgumentException("Number of resources must be >= 0");
        }
        else {
            this.necessaryResources = resources;
        }
        if(cities>0) {
            this.numberOfCities = cities;
        }
        else {
            throw new IllegalArgumentException("Number of cities must be > 0");
        }
        this.bagResources=decideToConstruct();
    }

    /**
     * Eindeutige Nummer.
     * @return Nummer. Nicht negativ.
     */
    @Override
    public int getNumber() {
        return plantNumber;
    }

    /**
     * Anzahl Staedte, die dieses Kraftwerk mit Strom versorgen kann.
     * @return Anzahl Staedte. Nicht negativ.
     */
    @Override
    public int getCities() {
        return numberOfCities;
    }

    /**
     * Anzahl Rohstoffe, die dieses Kraftwerk braucht, egal welcher Art.
     * @return Anzahl Rohstoffe. Nicht negativ.
     */
    @Override
    public int getNumberOfResources() {
        return necessaryResources;
    }

    @Override
    public Type getType() {
        return plantType;
    }

    /**
     * Test, ob dieses Kraftwerk Strom produziert hat.
     * @return true genau dann, wenn dieses Kraftwerk gelaufen ist.
     */
    @Override
    public boolean hasOperated() {
        return didOperate;
    }

    /**
     * Legt fest, ob  dieses Kraftwerk Strom produziert hat.
     * @param operated true genau dann, wenn dieses Kw Strom produziert hat.
     */
    @Override
    public void setOperated(boolean operated) {
        this.didOperate =operated;
    }

    /**
     * Rohstoffsammlungen, mit denen dieses Kraftwerk laufen kann.
     * * Wenn das Kw nichts braucht, enthaelt die Menge eine leere Sammlung als einziges Element,
     * das heisst {[]}.
     * * Wenn das Kw nur eine Rohstoffsorte akzeptiert, hat die Menge ein Element,
     * beispielsweise {[Coal=3]}.
     * * Wenn das Kw verschiedene Sorten akzeptiert, enthaelt die Menge alle zulaessigen Kombinationen,
     * beispielsweise {[Coal=2], [Coal, Oil], [Oil=2]}.
     * @return Verschiedene Rohstoffsammlungen. Nicht null und nicht leer.
     * Menge und Elemente unveraenderlich.
     */
    @Override
    public Set<Bag<Resource>> getResources() {
        return bagResources;
    }

    /** Rohstoffsammlungen, mit denen dieses Kraftwerk laufen kann.
     * Entscheidet ob die Rohstoffsamlung leer ist oder konstruiert werden soll.
     * Hat eine Hilfsmethode.
     * @see MRodicPlant#constructResources
     * @return Unveraenderliche Rohstoffsammlungen. Nicht null.
     */
    private Set<Bag<Resource>> decideToConstruct(){
        Set<Bag<Resource>> constructingSet=new HashSet<>();
        if(necessaryResources==0){
            final Bag<Resource> bag=new MRodicBag<>();
            constructingSet.add(bag.immutable());
            constructingSet=Collections.unmodifiableSet(constructingSet);
        }
        else {
            constructingSet = constructResources();
        }
        return constructingSet;
    }
    /**
     * Hilfsmethode zum Konstruieren der Rohstoffsammlungen.
     * * Wenn das Kw nur eine Rohstoffsorte akzeptiert, hat die Menge ein Element,
     * beispielsweise {[Coal, Coal, Coal]}.
     * * Wenn das Kw verschiedene Sorten akzeptiert, enthaelt die Menge alle zulaessigen Kombinationen,
     * beispielsweise {[Coal, Coal], [Coal, Oil], [Oil, Oil]}.
     * In dieser Methode wird eine private Hilfsmethode decideToConstruct() benutzt, die allein dafuer da ist, die Berechnung der Bags zu erleichtern und schneller zu machen.
     * Menge und Elemente unveraenderlich.
     * @return Verschiedene unveraenderliche Rohstoffsammlungen. Nicht null und nicht leer.
     */

    private Set<Bag<Resource>> constructResources() {
        final Set<Resource> possibleTyps =plantType.getResources();
        Set<Bag<Resource>> myResources=new HashSet<>();


        for(int elementsInBag=0;elementsInBag<necessaryResources;elementsInBag++) {
            final Set<Bag<Resource>> constructingResources=new HashSet<>();
            for(Resource actualResource: possibleTyps) {
                if(myResources.isEmpty()) {
                    final Bag<Resource> emptyBag = new MRodicBag<>();
                    emptyBag.add(actualResource);
                    constructingResources.add(emptyBag.immutable());
                }
                else{
                    for(Bag<Resource> bag: myResources){
                        final Bag<Resource> constructingBag= new MRodicBag<>();
                        constructingBag.addAll(bag);
                        constructingBag.add(actualResource);
                        constructingResources.add(constructingBag.immutable());
                    }
                }
            }
            myResources=constructingResources;
        }
        return Collections.unmodifiableSet(myResources);
    }

    /**
     * Natuerliche Ordnung: Nummer, aufsteigend.
     * Vergleicht dieses Kraftwerk mit dem anderen bezüglich des Nummer.
     * Nummer ist der Schlüsselattribut der Plant-Objekte.
     * @param that Anderes Kraftwerk. Nicht null.
     * @throws NullPointerException wenn that null ist.
     * @return Compare Wert Null bei Gleichheit Negativ, falls kleiner, positiv falls groesser.
     */
    @Override
    public int compareTo(Plant that) {
        final int numberThat=Objects.requireNonNull(that).getNumber();
        return this.plantNumber-numberThat;
    }
}
