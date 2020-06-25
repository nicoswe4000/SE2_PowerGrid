import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.mrodic.powergrid.MRodicBag;

import java.util.Collection;

/**
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version last modified 2020-04-17
 */
public class PlainBagTest extends BagClassTest{

    /**
     * erzeugt einen Integer-Bag
     * Basistestklasse kann unabhaenig bleiben von konkreten Implementierungen
     * @return erzeugten Bag
     */
    @Override
    Bag<Integer> getSut() {
        return new MRodicBag<>();
    }

    /**
     * erzeugt einen Integer-Bag mit Integer Elementen
     * Basistestklasse kann unabhaenig bleiben von konkreten Implementierungen
     * @param elements Integer-Werte die im Bag abgelegt werden sollen
     * @return erzeugten Bag
     */
    @Override
    Bag<Integer> getSutIntegerElements(Integer... elements) {
        return new MRodicBag<>(elements);
    }

    /**
     * erzeugt einen Integer-Bag mit Integer Elementen
     * Basistestklasse kann unabhaenig bleiben von konkreten Implementierungen
     * @param collection Bag (Typ Integer) dessen Elemente im neuen Bag liegen
     * @return erzeugten Bag
     */
    @Override
    Bag<Integer> getSutIntegerCollection(Collection<? extends Integer> collection) {
        return new MRodicBag<>(collection);
    }

    /**
     * erzeugt einen String-Bag
     * Basistestklasse kann unabhaenig bleiben von konkreten Implementierungen
     * @return erzeugten Bag
     */
    @Override
    Bag<String> getSutString() {
        return new MRodicBag<>();
    }

    /**
     * erzeugt einen String-Bag mit Integer Elementen
     * Basistestklasse kann unabhaenig bleiben von konkreten Implementierungen
     * @param elements String-Werte die im Bag abgelegt werden sollen
     * @return erzeugten Bag
     */
    @Override
    Bag<String> getSutStringElements(String... elements) {
        return new MRodicBag<>(elements);
    }
    /**
     * erzeugt einen String-Bag mit Integer Elementen
     * Basistestklasse kann unabhaenig bleiben von konkreten Implementierungen
     * @param collection Bag (Typ String) dessen Elemente im neuen Bag liegen
     * @return erzeugten Bag
     */
    @Override
    Bag<String> getSutStringCollection(Collection<? extends String> collection) {
        return new MRodicBag<>(collection);
    }
}
