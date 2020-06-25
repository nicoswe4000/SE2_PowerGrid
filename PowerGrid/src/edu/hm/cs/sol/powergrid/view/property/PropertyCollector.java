package edu.hm.cs.sol.powergrid.view.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

/**
 * Baut Propertylisten zusammen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-12
 */
public class PropertyCollector {
    /** Diese Properties erweitern die Methoden. */
    private final Properties properties = new Properties();

    /** Ein leerer Sammler fuer Properties. */
    public PropertyCollector() {
    }

    /** Ein Sammler mit gegebenen Properties.
     * @param namesAndValues Paarweise Namen und Werte von Properties, mit denen dieser Sammler startet.
     * Das erste Argument muss ein String sein, das zweite kann irgendetwas sein.
     * Eine gerade Anzahl.
     */
    public PropertyCollector(Object... namesAndValues) {
        add(namesAndValues);
    }

    /**
     * Fuegt mehrere Properties ein.
     * @param namesAndValues Paarweise Namen und Werte.
     * Das erste Argument muss ein String sein, das zweite kann irgendetwas sein.
     * Eine gerade Anzahl.
     * @return Dieser Sammler.
     */
     PropertyCollector add(Object... namesAndValues) {
        if(namesAndValues.length % 2 != 0)
            throw new IllegalArgumentException("odd number of args: " + Arrays.toString(namesAndValues));
        for(int index = 0; index < namesAndValues.length; index += 2)
            setProperty((String)namesAndValues[index],
                        namesAndValues[index + 1].toString());
        return this;
    }

    /**
     * Fuegt eine Reihe indexierter Properties "name.index" ein.
     * Der Index beginnt bei 0.
     * Fuegt so viele fuehrenden Nullen ein, dass die Propertynamen aufsteigend sortieren.
     * @param <T> Typ der Objekte, deren Properties die Methode aufnimmt.
     * @param name Name, an den die Methode den Index anfuegt.
     * @param collection Sammlung von Objekten. Die Reihenfolge bleibt.
     * @param toProperties Funktion, die die Properties eines Objektes liefert.
     * @return Dieser Sammler.
     */
     <T> PropertyCollector add(String name,
                                     Collection<T> collection,
                                     Function<T, Properties> toProperties) {
        int index = 0;
        final String numberString = Integer.toString(collection.size());
        setProperty(name + ".num", numberString);
        final String indexFormat = "%0" + numberString.length() + 'd';
        for(T element: collection) {
            final Properties props = toProperties.apply(element);
            for(String key: props.stringPropertyNames())
                setProperty(name + '.' + String.format(indexFormat, index) + '.' + key,
                            props.getProperty(key));
            index++;
        }
        return this;
    }

    /**
     * Fuegt untergeordnete Properties ein.
     * @param name Name, die den Subproperties vorsteht..
     * @param props Properties, die dazu kommen.
     * @return Dieser Sammler.
     */
    public PropertyCollector add(String name, Properties props) {
        for(String key: props.stringPropertyNames())
            setProperty(name + '.' + key,
                        props.getProperty(key));
        return this;
    }

    /**
     * Fuegt eine Reihe indexierter Properties "name.index" ein.
     * Der Index beginnt bei 0.
     * Fuegt so viele fuehrenden Nullen ein, dass die Propertynamen aufsteigend sortieren.
     * @param <T> Typ der Objekte, deren Properties die Methode aufnimmt.
     * @param name Name, an den die Methode den Index anfuegt.
     * @param collection Sammlung von Objekten.
     * @param toProperties Funktion, die die Properties eines Objektes liefert.
     * @param comparator Legt die Reihenfolge der Objekte fest.
     * @return Dieser Sammler.
     */
    public <T> PropertyCollector add(String name,
                                     Collection<T> collection,
                                     Function<T, Properties> toProperties,
                                     Comparator<T> comparator) {
        final List<T> list = new ArrayList<>(collection);
        Collections.sort(list, comparator);
        return add(name, list, toProperties);
    }

    /** Liefert die Einzelheiten des Spiels als Properties.
     * @return Properties.
     */
    public Properties get() {
        return properties;
    }

    private void setProperty(String key, String value) {
        if(properties.containsKey(key))
            throw new IllegalArgumentException("double defined property: " + key);
        properties.setProperty(key, value);
    }

}
