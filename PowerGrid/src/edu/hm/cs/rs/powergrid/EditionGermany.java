package edu.hm.cs.rs.powergrid;

import java.util.List;

/**
 * Ausgabe Deutschland.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-07
 */
public class EditionGermany extends EditionStandard {

    @Override public List<String> getCitySpecifications() {
        return List.of("Flensburg 1 Kiel 4",
                       "Kiel 1 Hamburg 8 L\u00FCbeck 4",
                       "Cuxhaven 1 Bremen 8 Hamburg 11",
                       "Wilhelmshaven 1 Osnabr\u00FCck 14 Bremen 11",
                       "Hamburg 1  Bremen 11 Hannover 17 Schwerin 8",
                       "Bremen 1 Osnabr\u00FCck 11 Hannover 10",
                       "Hannover 1  Kassel 15 Erfurt 19 Magdeburg 15",
                       "L\u00FCbeck 2 Hamburg 6 Schwerin 6",
                       "Rostock 2 Schwerin 6 Torgelow 19",
                       "Schwerin 2 Hannover 19 Magdeburg 16 Berlin 18 Torgelow 19",
                       "Torgelow 2 Berlin 15",
                       "Berlin 2 Magdeburg 10 Halle 17 Frankfurt-Oder 6",
                       "Magdeburg 2 Halle 11",
                       "Frankfurt-Oder 2 Leipzig 21 Dresden 16",
                       "Osnabr\u00FCck 3 M\u00FCnster 7 Hannover 16 Kassel 20",
                       "M\u00FCnster 3 Essen 6 Dortmund 2",
                       "Duisburg 3 Essen 0",
                       "Essen 3 D\u00FCsseldorf 2 Dortmund 4",
                       "Dortmund 3 K\u00F6ln 10 Frankfurt-Main 20 Kassel 18",
                       "Kassel 3 Frankfurt-Main 13 Fulda 8 Erfurt 15",
                       "D\u00FCsseldorf 3 Aachen 9 K\u00F6ln 4",
                       "Halle 4 Erfurt 6 Leipzig 0",
                       "Leipzig 4 Dresden 13",
                       "Erfurt 4 Fulda 13 N\u00FCrnberg 21 Dresden 19",
                       "Dresden 4",
                       "Fulda 4 Frankfurt-Main 8 W\u00FCrzburg 11",
                       "W\u00FCrzburg 4 Mannheim 10 Stuttgart 12 Augsburg 19 N\u00FCrnberg 8",
                       "N\u00FCrnberg 4 Augsburg 18 Regensburg 12",
                       "K\u00F6ln 5 Aachen 7 Trier 20 Wiesbaden 21",
                       "Aachen 5 Trier 19",
                       "Frankfurt-Main 5 Wiesbaden 0 W\u00FCrzburg 13",
                       "Wiesbaden 5 Trier 18 Saarbr\u00FCcken 10 Mannheim 11",
                       "Trier 5 Saarbr\u00FCcken 11",
                       "Mannheim 5 Saarbr\u00FCcken 11 Stuttgart 6",
                       "Saarbr\u00FCcken 5 Stuttgart 17",
                       "Regensburg 6 Augsburg 13 M\u00FCnchen 10 Passau 12",
                       "Stuttgart 6 Freiburg 16 Konstanz 16 Augsburg 15",
                       "Augsburg 6 Konstanz 17 M\u00FCnchen 6",
                       "Passau 6 M\u00FCnchen 14",
                       "Freiburg 6 Konstanz 14",
                       "M\u00FCnchen 6",
                       "Konstanz 6");
    }

}
