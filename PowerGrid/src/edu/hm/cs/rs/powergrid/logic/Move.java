package edu.hm.cs.rs.powergrid.logic;

import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Ein Spielzug.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */
public interface Move {
    /**
     * Test, ob der Zug ohne Rueckfrage erlaubt ist, wenn es keinen anderen gibt.
     * @return true genau dann, wenn der Zug automatisch ablaufen darf.
     */
    default boolean isAutoFire() {
        return getType().isAutoFire();
    }

    /**
     * Test, ob der Zug ohne Rueckfrage und vor allen anderen ohne Vorrang ablaufen muss.
     * Bei mehreren vorrangigen Zuegen ist die Reihenfolge unbestimmt.
     * @return true genau dann, wenn der Zug Vorrang hat.
     */
    default boolean hasPriority() {
        return getType().hasPriority();
    }

    /**
     * Die Art des Zuges.
     * @return Art.
     */
    MoveType getType();

    /**
     * Alle Eigenschaften des Zuges.
     * Die verfuegbaren Eigenschaften haengen vom Typ ab.
     * @return Alle Eigenschaften.
     */
    default Properties getProperties() {throw new UnsupportedOperationException();}

    /**
     * Eigenschaft des Zuges.
     * Die verfuegbaren Eigenschaften haengen vom Typ ab.
     * player = Spielerfarbe;
     * city = Name einer Stadt;
     * plant = String mit der Nummer eines Kraftwerks;
     * resource = ein Rohstoffname;
     * resources = Liste von Rohstoffennamen;
     * @return Wert der Eigenschaft. Nicht null.
     */
    default String getProperty(String name) {
        return Objects.requireNonNull(getProperties().getProperty(name));
    }

    /** Textdarstellung dieses Zuges.
     * @return Zug als String.
     */
    default String asText() {
        Properties properties = getProperties();
        return getType().name()
                + properties.stringPropertyNames().stream()
                .filter(name -> !name.equals("type"))
                .sorted()
                .map(name -> name + '=' + properties.getProperty(name))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
