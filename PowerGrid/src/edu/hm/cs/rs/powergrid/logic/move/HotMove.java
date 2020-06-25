package edu.hm.cs.rs.powergrid.logic.move;

import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.Problem;
import java.util.Optional;
import java.util.Set;

/**
 * Ein Spielzug.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-20
 */
public interface HotMove extends Move {
    /**
     * Aktiviert den Zug.
     * @param real false, um den Zug nur zu testen (keine Aenderung am Datastore) oder
     *             true, um ihn wirklich auszufueheren (aender das Datastore).
     * @return Leer, wenn der Zug erlaubt ist.
     * Ansonsten Problem, das den Spielzug verhindert.
     */
    Optional<Problem> run(boolean real);

    /**
     * Test, ob der Spielzug erlaubt ist.
     * @return Leer, wenn der Zug erlaubt ist.
     * Ansonsten Problem, das den Spielzug verhindert.
     */
    default Optional<Problem> test() {
        return run(false);
    }

    /**
     * Fuehrt den Spielzug aus.
     * @return Leer, wenn der Zug erfolgreich war.
     * Ansonsten Problem, das den Spielzug verhindert hat.
     * In diesem Fall ist das Spiel unveraendert.
     */
    default Optional<Problem> fire() {
        return run(true);
    }

    /**
     * Spiel, auf das sich dieser Zug bezieht.
     * @return Spiel.
     */
    OpenGame getGame();

    /**
     * Sammelt alle Zuege dieser Art, die im Moment erlaubt sind.
     * @param game   Aktuelles Spiel.
     * @param player Ein Spieler oder leer, wenn er noch nicht existiert.
     * @return Alle Zuege dieser Art, die gerade moeglich sind.
     * Eventuell leer.
     */
    Set<HotMove> collect(OpenGame game, Optional<OpenPlayer> player);

    /**
     * Legt eine Eigenschaft fest.
     * @param name  Name der Eigenschaft. Nicht null.
     * @param value Wert der Eigenschaft. Nicht null.
     */
    default void setProperty(String name, String value) {throw new UnsupportedOperationException();}

}
