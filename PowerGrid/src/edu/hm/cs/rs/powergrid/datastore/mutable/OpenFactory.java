package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import java.util.List;

/**
 * Produziert neue Bausteine des Spieles.
 * Kapselt die konkreten Implementierungen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-09
 */
public interface OpenFactory {
    /**
     * Liefert eine neue OpenFactory,
     * deren FQCN die Systemproperty powergrid.factory definiert.
     * Wenn diese Systemproperty undefiniert ist,
     * gilt die Umgebungsvariable POWERGRID_FACTORYTYPE.
     * @return neue OpenFactory.
     */
    static OpenFactory newFactory() {
        return newFactory(System.getProperty("powergrid.factory",
                                             System.getenv("POWERGRID_FACTORY")));
    }

    /**
     * Liefert eine neue OpenFactory des gegebenen Typs.
     * @param fqcn Fully qualified Classname der konkreten Factoryklasse.
     * @return neue OpenFactory.
     * @throws RuntimeException wenn die Methode kein Objekt des Typs erzeugen kann.
     */
    static OpenFactory newFactory(String fqcn) {
        try {
            return (OpenFactory)Class.forName(fqcn)
                    .getConstructor()
                    .newInstance();
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Eine Stadt, noch ohne Verbindungen.
     * Erst ist wenigstens ein connect-Aufruf noetig, dann einmal close.
     * @param name   Name. Nicht null und nicht leer.
     * @param region Gebiet, in dem diese Stadt liegt. Wenigstens 1.
     */
    OpenCity newCity(String name, int region);

    /**
     * Ein Spieler.
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     */
    OpenPlayer newPlayer(String secret, String color);

    /**
     * Ein Kraftwerk.
     * @param number    Nummer des Kraftwerks. Nicht negativ.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht. Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann. Echt positiv.
     */
    OpenPlant newPlant(int number, Plant.Type type, int resources, int cities);

    /**
     * Ein Kraftwerksmarkt.
     * Erzeugt alle Kraftwerke, die es in dieser Ausgabe gibt.
     * Die Kraftwerke liegen alle im Stapel mit den verborgenen Kraftwerken.
     * Die Reihenfolge spielt keine Rolle.
     * Der aktuelle und der zukuenftige Markt sind leer.
     * Die Karte "Stufe 3" ist noch nicht dabei.
     * @param edition Ausgabe des Spieles.
     * @return Kraftwerksmarkt. Nicht null.
     */
    OpenPlantMarket newPlantMarket(Edition edition);

    /**
     * Ein Rohstoffmarkt.
     * Erzeugt alle Rohstoffe gemaess Ausgabe.
     * Macht davon so viele verfuegbar, wie es die Ausgabe festlegt.
     * Der Rest bleibt im Vorrat.
     * @param edition Ausgabe des Spieles.
     * @return Rohstoffmarkt. Nicht null.
     */
    OpenResourceMarket newResourceMarket(Edition edition);

    /**
     * Ein Spielplan.
     * Fuegt die Staedte der Edition und ihre Verbindungen in den Spielplan ein.
     * Der Spielplan ist noch offen.
     * @param edition Ausgabe des Spieles.
     * @return Spielplan.
     */
    OpenBoard newBoard(Edition edition);

    /**
     * Eine Auktion.
     * Das Hoechstgebot ist gleich der Nummer des Kraftwerkes.
     * Der erste Spieler der Liste ist der Hoechstbietende.
     * @param plant   Kraft, um das es geht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     *                Die Spieler bieten in der Reihenfolge dieser Liste.
     * @return Auktion. Nicht null.
     */
    OpenAuction newAuction(OpenPlant plant, List<OpenPlayer> players);

    /**
     * Ein Spiel mit einem neuen Spielbrett, Kraftwerks- und Rohstoffmarkt.
     * Es gibt noch keine Spieler und keine Auktion.
     * Spielstufe (Index), Runden- und Zugnummer sind 0.
     * Die Phase ist Opening.
     * @param edition Ausgabe des Spieles.
     * @return Spiel. Nicht null.
     * @see Phase#Opening
     */
    OpenGame newGame(Edition edition);

}
