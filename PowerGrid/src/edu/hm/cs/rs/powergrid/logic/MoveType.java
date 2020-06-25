package edu.hm.cs.rs.powergrid.logic;

/**
 * Arten von Zuegen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-06
 */
public enum MoveType {
    /** Neuen Spieler aufnehmen. */
    JoinPlayer(false),
    /** Spiel beginnen. */
    CommenceGame(false),
    /** Reihenfolge der Spieler festlegen. */
    OrderPlayers,
    /** Spieler erwirbt kein Kraftwerk. */
    PassAuction,
    /** Spieler startet Auktion eines Kraftwerks. */
    StartAuction,
    /** Spieler erhoeht das Gebot in der Auktion. */
    RaiseBid,
    /** Spieler steigt aus einer Auktion aus. */
    LeaveAuction,
    /** Auktion beenden und Kraftwerk dem Hoechstbietenden verkaufen. */
    CloseAuction,
    /** Auktionen beenden. */
    EndAuctions,
    /** Spieler kauft Rohstoffe. */
    BuyResource,
    /** Spieler kauft keine Rohstoffe. */
    BuyNoResource,
    /** Rohstoff kaufen abschliessen. */
    EndResourceBuying,
    /** Spieler platziert die erste Stadt. */
    Build1stCity,
    /** Spieler schliesst neue Stadt an sein Netz an. */
    ConnectCity,
    /** Spieler schliesst keine Stadt an. */
    ConnectNoCity,
    /** Staedte anschliessen beenden. */
    EndBuilding,
    /** Spieler betreibt Kraftwerk. */
    OperatePlant,
    /** Spieler betreibt kein Kraftwerk. */
    OperateNoPlant,
    /** Einkommen fuer betriebene Kraftwerke ausschuetten. */
    SupplyElectricity,
    /** Rohstoffmarkt nachfuellen und neue Runde starten. */
    TurnOver,
    /** Kraftwerksmarkt ergaenzen. */
    UpdatePlantMarket(true),
    /** Spieler gibt ueberzaehliges Kraftwerk ab. */
    DropPlant(true),
    /** Spieler gibt Rohstoff ab, den er nicht lagern kann. */
    DropResource(true),
    /** In Stufe 2 wechseln. */
    EnterLevel2(true),
    /** In Stufe 3 wechseln. */
    EnterLevel3(true),
    /** Ueberholtes Kw aus dem Markt nehmen. */
    ScrapPlant(true),
    /** Spiel beenden. */
    EndGame(true),
    /** Spiel abbrechen. */
    KillGame(true);

    /** Zug laeuft ohne Rueckfrage vor allen anderen ab? */
    private final boolean priority;

    /** Zug startet automatisch, wenn es keinen anderen gibt? */
    private final boolean auto;

    MoveType(boolean auto, boolean priority) {
        this.priority = priority;
        this.auto = auto;
    }

    MoveType(boolean autoAndPriority) {
        this(autoAndPriority, autoAndPriority);
    }

    MoveType() {
        this(true, false);
    }

    /**
     * Test, ob der Zug ohne Rueckfrage und vor allen anderen ohne Vorrang ablaufen muss.
     * Bei mehreren vorrangigen Zuegen ist die Reihenfolge unbestimmt.
     * @return true genau dann, wenn der Zug Vorrang hat.
     */
    public boolean hasPriority() {
        return priority;
    }

    /**
     * Test, ob der Zug automatisch ablaufen kann, wenn es keinen anderen gibt.
     * @return true genau dann, wenn der Zug automatische feuern darf.
     */
    public boolean isAutoFire() {
        return auto;
    }
}
