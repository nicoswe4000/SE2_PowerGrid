package edu.hm.cs.rs.powergrid.logic;

/**
 * Gruende, die einen Zug verhindern koennen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-30
 */
public enum Problem {
    /** Spiel hat sich veraendert. Der Zug gilt nicht mehr. */
    Expired,
    /** Maximale Spieleranzahl ist erreicht. Weitere koennen nicht teilnehmen. */
    MaxPlayers,
    /** Zug ist jetzt nicht moeglich. */
    NotNow,
    /** Spieler nicht an der Reihe. */
    NotYourTurn,
    /** Kraftwerk ist nicht im aktuellen Markt verfuegbar. */
    PlantNotAvailable,
    /** Zu wenig Electro. */
    NoCash,
    /** Spieler war schon an der Reihe, kommt nicht nochmal dran. */
    AlreadyPassed,
    /** Spieler hat keine Kraftwerke. Er muss wenigstens eines besitzen. */
    NoPlants,
    /** Spieler ist selbst Hoechstbietender und kann weder ueberbieten noch aussteigen. */
    TopBidder,
    /** Auktion ist noch nicht beendet. Es gibt noch Konkurrenten, die mitbieten koennen. */
    AuctionRunning,
    /** Es waren noch nicht alle Spieler an der Reihe. */
    PlayersRemaining,
    /** Rohstoff ist nicht auf dem Markt verfuegbar. */
    NoResource,
    /** Spieler kann den Rohstoff nicht lagern. */
    NoCapacity,
    /** Spieler hat schon eine Stadt. */
    HasCities,
    /** Stadt ist schon besetzt. */
    CityTaken,
    /** Spieler hat keine Stadt. */
    NoCities,
    /** Spieler hat die Stadt schon angeschlossen. */
    CityAlreadyConnected,
    /** Kw ist schon gelaufen, kann nicht nochmal laufen. */
    PlantHasOperated,
    /** Spiel laeuft nicht. */
    NotRunning,
    /** Kraftwerksmarkt ist kompett. Nichts zu ergaenzen. */
    PlantMarketFine,
    /** Falsche Spielstufe. */
    WrongLevel,
    /** Kraftwerk ist gross genug und wird nicht verschrottet. */
    PlantSave,
    /** Spiel laeuft noch. */
    GameRunning,
    /** Spiel ist vorbei. */
    GameOver,
    /** Es gibt noch wenigstens 1 Kraftwerk im Markt. */
    PlantsUnsold,
    /** Kraftwerk gehoert nicht diesem Spieler. */
    OtherPlant,
    /** Zu wenig Spieler zum Start. */
    TooFewPlayers
}
