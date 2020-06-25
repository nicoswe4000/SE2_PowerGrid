
package edu.hm.cs.rs.powergrid;

import edu.hm.cs.rs.powergrid.datastore.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ABC fuer das Standardspiel.
 * Der Spielplan fehlt.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-07
 */
public abstract class EditionStandard implements Edition {
    @Override public int getPlayersMinimum() {
        return 2;
    }

    @Override public int getPlayersMaximum() {
        return 6;
    }

    @Override public int getInitialElectro() {
        return 50;
    }

    @Override public int getActualPlants(int levelIndex) {
        return new int[] {4, 4, 6}[levelIndex];
    }

    @Override public int getFuturePlants(int levelIndex) {
        return new int[] {4, 4, 0}[levelIndex];
    }

    @Override public Map<Resource, Integer> getResourceToNumber() {
        return Map.of(Resource.Coal, 24,
                      Resource.Oil, 24,
                      Resource.Garbage, 24,
                      Resource.Uranium, 12);
    }

    @Override public Map<Resource, List<Integer>> getResourceAvailableToCost() {
        final List<Integer> groupsOf3 = IntStream.range(0, 24)
                .map(n -> 8 - n/3)
                .boxed()
                .collect(Collectors.toList());
        return Map.of(Resource.Coal, groupsOf3,
                      Resource.Oil, groupsOf3,
                      Resource.Garbage, groupsOf3,
                      Resource.Uranium, IntStream.range(0, 12)
                              .map(n -> n > 3? 12 - n: 16 - 2*n)
                              .boxed()
                              .collect(Collectors.toList()));
    }

    @Override public Map<Resource, Integer> getResourcesInitiallyAvailable() {
        return Map.of(Resource.Coal, 24,
                      Resource.Oil, 18,
                      Resource.Garbage, 6,
                      Resource.Uranium, 2);
    }

    @Override public List<Integer> levelToCityCost() {
        return List.of(10, 15, 20);
    }

    @Override public Map<Resource, List<List<Integer>>> getResourcePlayersToSupply() {
        Function<String, List<List<Integer>>> digitSplitter = string ->
                Stream.of(string.split("/"))
                        .map(digits -> digits.chars()
                                .map(n -> n - '0')
                                .boxed()
                                .collect((Collectors.toList())))
                        .collect(Collectors.toList());
        return Map.of(Resource.Coal, digitSplitter.apply("0/0/343/453/564/575/796"),
                      Resource.Oil, digitSplitter.apply("0/0/224/234/345/456/567"),
                      Resource.Garbage, digitSplitter.apply("0/0/123/123/234/335/356"),
                      Resource.Uranium, digitSplitter.apply("0/0/111/111/122/232/233"));
    }

    @Override public List<Integer> getPoweredCitiesIncome() {
        return List.of(10, 22, 33, 44, 54, 64, 73, 82, 90, 98, 105, 112, 118, 124, 129, 134, 138, 142, 145, 148, 150);
    }

    @Override public List<String> getPlayerColors() {
        return List.of("FF0001", "0000F2", "00FF03", "FFFF04", "000005", "8000F6");
    }

    @Override public List<Integer> getPlayersPlantsInitiallyRemoved() {
        return List.of(-1, -1, 8, 8, 4, 0, 0);
    }

    @Override public List<Integer> getPlayersPlantsLimit() {
        return List.of(-1, -1, 4, 3, 3, 3, 3);
    }

    @Override public List<Integer> getPlayersLevel2Cities() {
        return List.of(-1, -1, 10, 7, 7, 7, 6);
    }

    @Override public List<Integer> getPlayersEndgameCities() {
        return List.of(-1, -1, 21, 17, 17, 15, 14);
    }

    @Override public List<Integer> getRegionsUsed() {
        // das letzte Element weicht von den Originalregeln ab.
        // (dort nur 5 Regionen).
        return List.of(-1, -1, 3, 3, 4, 5, 6);
    }

    @Override public List<String> getPlantSpecifications() {
        return List.of("3 OO 1",
                       "4 CC 1",
                       "5 HH 1",
                       "6 G 1",
                       "7 OOO 2",
                       "8 CCC 2",
                       "9 O 1",
                       "10 CC 2",
                       "11 U 2",
                       "12 HH 2",
                       "13 E 1",
                       "14 GG 2",
                       "15 CC 3",
                       "16 OO 3",
                       "17 U 2",
                       "18 E 2",
                       "19 GG 3",
                       "20 CCC 5",
                       "21 HH 4",
                       "22 E 2",
                       "23 U 3",
                       "24 GG 4",
                       "25 CC 5",
                       "26 OO 5",
                       "27 E 3",
                       "28 U 4",
                       "29 H 4",
                       "30 GGG 6",
                       "31 OOO 6",
                       "32 CCC 6",
                       "33 E 4",
                       "34 U 5",
                       "35 O 5",
                       "36 CCC 7",
                       "37 E 4",
                       "38 GGG 7",
                       "39 U 6",
                       "40 OO 6",
                       "42 CC 6",
                       "44 E 5",
                       "46 HHH 7",
                       "50 F 6");
    }

    @Override public String toString() {
        return getClass().getSimpleName() + "{}";
    }
}
