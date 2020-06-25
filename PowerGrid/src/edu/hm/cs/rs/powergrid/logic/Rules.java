package edu.hm.cs.rs.powergrid.logic;

import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import java.util.Optional;
import java.util.Set;

/**
 * Spielregeln.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-23
 */
public interface Rules {
    /**
     * Liefert konkrete Spielregeln.
     * Wertet die System-Property powergid.rules aus, die den FQCN der Spielregeln enthaelt.
     * @param game Spiel, auf das sich die Regeln beziehen.
     * @return Spielregeln.
     */
    static Rules newRules(OpenGame game) {
        return newRules(System.getProperty("powergrid.rules",
                                           System.getenv("POWERGRID_RULES")),
                        game);
    }

    /**
     * Liefert konkrete Spielregeln.
     * @param fqcn FQCN der Spielregel-Klasse.
     * @param game Spiel, auf das sich die Regeln beziehen.
     * @return Spielregeln.
     */
    static Rules newRules(String fqcn, OpenGame game) {
        try {
            return (Rules)Class.forName(fqcn)
                    .getConstructor(OpenGame.class)
                    .newInstance(game);
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Spiel, auf das sich diese Regeln beziehen.
     * @return Ein Spiel.
     */
    Game getGame();

    /**
     * Die momentan zulaessigen Zuege eines Spielers.
     * @param secret Geheimnis eines Spielers oder leer bei einem neuen Spieler,
     *               der noch kein Geheimnis hat.
     * @return Die erlaubten Zuege.
     * Leer, wenn es keine Zuege fuer den Spieler gibt.
     */
    Set<Move> getMoves(Optional<String> secret);

    /**
     * Fuehrt einen der Zuege aus, die getMoves geliefert hat.
     * @param secret Geheimnis eines Spielers oder leer bei einem neuen Spieler,
     *               der noch kein Geheimnis hat.
     * @param move   Ein Zug.
     * @return Leer, wenn der Zug erfolgreich war; ansonsten Problem, das ihn verhindert hat.
     * @throws IllegalArgumentException wenn der Zug nicht von getMoves stammt.
     */
    Optional<Problem> fire(Optional<String> secret, Move move);

    /**
     * Prototypen.
     * @return Menge der Prototypen. Nicht null.
     */
    default Set<Move> getPrototypes() {
        throw new UnsupportedOperationException("please override this default method");
    }
}
