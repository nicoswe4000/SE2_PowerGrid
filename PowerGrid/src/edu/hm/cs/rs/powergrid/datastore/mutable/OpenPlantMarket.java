package edu.hm.cs.rs.powergrid.datastore.mutable;

import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.PlantMarket;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Ein veraenderlicher Kraftwerksmarkt mit aktuellem und zukuenftigen Angebot und einem
 * Stapel nicht oeffentlich sichtbarer Kraftwerke.
 * Die Karte "Stufe 3" verhaelt sich wie ein Kraftwerk mit unbezahlbaren Kosten.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-12
 */
public interface OpenPlantMarket extends PlantMarket, Checksumed {
    /**
     * Entfernt das Kraftwerk mit der gegebenen Nummer aus dem Spiel.
     * Sucht im aktuellen und im zukuenftigen Markt und im Stapel.
     * Die Methode kann ein Kraftwerk nur einmal entfernen.
     * Dann ist es nicht mehr im Spiel.
     * @param number Nummer eines Kraftwerks.
     * @return Kraftwerk oder null, wenn es nicht im Spiel ist.
     */
    OpenPlant removePlant(int number);

    @Override OpenPlant findPlant(int number);

    /**
     * Sortierte, veraenderliche Menge der momentan kaeuflichen Kw.
     * @return Verfuegbare Kw. Nicht null, aber eventuell leer.
     */
    Set<OpenPlant> getOpenActual();

    /**
     * Sortierte, veraenderliche Menge der naechstens kaeuflichen Kw.
     * @return Nachrueckende Kw. Nicht null, aber eventuell leer.
     */
    Set<OpenPlant> getOpenFuture();

    /**
     * Liste der Kw, die spaeter ins Spiel kommen
     * und derzeit nicht oeffentlich sichtbar sind.
     * Die Reihenfolge ist signifikant, folgt aber keiner Ordnung.
     * @return Liste der Kw. Nicht null, aber eventuell leer.
     */
    List<OpenPlant> getOpenHidden();

    @Override default Set<Plant> getActual() {
        return Collections.unmodifiableSet(getOpenActual());
    }

    @Override default Set<Plant> getFuture() {
        return Collections.unmodifiableSet(getOpenFuture());
    }

    @Override default int checksum() {
        return Objects.hash(Checksumed.checksumOf(getOpenActual()),
                            Checksumed.checksumOf(getOpenFuture()),
                            Checksumed.checksumOf(getOpenHidden()));
    }
}
