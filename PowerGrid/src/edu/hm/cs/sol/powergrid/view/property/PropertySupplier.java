package edu.hm.cs.sol.powergrid.view.property;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/** Stellt ein Objekt als Properties zur Verfuegung.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public interface PropertySupplier {
    /** Liefert die Properties des Objektes.
     * @return Properties.
     */
    Properties get();

    /** Properties als Map mit sortierten Schluesseln.
     * @param properties Properties.
     * @return Map mit alphabetisch sortierten Schluesseln.
     */
    static Map<String, String> asSortedMap(Properties properties){
        return properties.stringPropertyNames()
            .stream()
            .map(key -> Map.entry(key, properties.get(key)))
            .collect(TreeMap::new,
                     (map, entry) -> map.put(entry.getKey(), entry.getValue().toString()),
                     TreeMap::putAll);
    }
}
