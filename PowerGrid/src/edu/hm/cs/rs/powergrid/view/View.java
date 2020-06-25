package edu.hm.cs.rs.powergrid.view;

import edu.hm.cs.rs.powergrid.Observer;
import edu.hm.cs.rs.powergrid.datastore.Game;
import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Ein Beobachter.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-11
 */
public interface View extends Observer {
    /** Erzeugt neue Views gemaess der Systemproperty powergrid.views, die die FQCN auflistet.
     * Trennzeichen ist File.pathSeparator.
     * Liest ansonsten die Umgebungsvariable POWERGRID_VIEWS mit der gleichen Syntax.
     * @param game Spiel.
     * @return Sammlung von Views.
     * @see java.io.File#pathSeparator
     */
    static Collection<View> newViews(Game game) {
        final String fqcns = System.getProperty("powergrid.views",
                                                System.getenv("POWERGRID_VIEWS"));
        return Stream.of(fqcns.split(File.pathSeparator))
            .map(fqcn -> make(fqcn, game))
            .collect(Collectors.toList());
    }

    /** Erzeugt eine neue View.
     * @param fqcn FQCN der konkreten View-Implementierung.
     * @param game Spiel.
     * @return Neue View.
     */
    static View make(String fqcn, Game game) {
        try {
            final View view = (View)Class.forName(fqcn)
                .getDeclaredConstructor(Game.class)
                .newInstance(game);
 //           view.update(null);  // send initial update
            return view;
        }
        catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /** Meldet diese View beim Observable ab. */
    default void stop() {}

}
