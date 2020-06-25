package edu.hm.mrodic.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenAuction;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenResourceMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlayer;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenCity;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenBoard;
import edu.hm.mrodic.powergrid.MRodicBag;

import java.util.*;

/**
 * Produziert neue Bausteine des Spieles.
 * Kapselt die konkreten Implementierungen.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @author A. Hofmann, angelina.hofmann@edu.hm.edu
 * @version 2020-05-07
 */
@SuppressWarnings("checkstyle:classfanoutcomplexity")
public class MRodicFactory implements OpenFactory {
    /** Regex.*/
    private static final String REGEX ="[\\s]+";
    /** Menge aller Staedte. Jede Stadt kommt genau einmal vor (keine Duplikate). Nicht null.*/
    private final Map<String, OpenCity> actualCities=new TreeMap<>();
    /** Menge aller Spieler. Jeder Spieler kommt genau einmal vor (keine Duplikate). Nicht null.*/
    private final  Map<String, OpenPlayer> actualPlayers=new TreeMap<>();
    /** Menge aller Kraftwerke. Jedes Kraftwerk kommt genau einmal vor (keine Duplikate). Nicht null.*/
    private final Map<Integer, OpenPlant> actualPlants =new TreeMap<>();
    /** Spielplan dieses Spiels.*/
    private OpenBoard boardReturn;
    /** ResourceMarkt dieses Spiels.*/
    private OpenResourceMarket resourceMarketReturn;
    /** PlantMarkt dieses Spiels.*/
    private OpenPlantMarket plantMarketReturn;
    /** Menge aller Auctionen. Jede Auktion kommt genau einmal vor (keine Duplikate). Nicht null.*/
    private final Map<Number,OpenAuction> auctions=new HashMap<>();
    /** Spiel.*/
    private OpenGame gameReturn;


    /**
     * Eine Stadt.
     * Natuerliche Ordnung: Name, alphabetisch.
     * @param name Name. Nicht null und nicht leer.
     * @param region Gebiet, in dem diese Stadt liegt. Wenigstens 1.
     * @return eine Stadt.
     */
    @Override
    public OpenCity newCity(String name, int region){
        if(Objects.requireNonNull(name).length()<=0) {
            throw new IllegalArgumentException("Name of the City must be non null and not empty.");
        }
        final OpenCity city;
        if(actualCities.containsKey(name)) {
            city = actualCities.get(name);
        }
        else{
            city = new MRodicCity(name,region);
            actualCities.put(name,city);
        }
        return city;
    }

    /**
     * Ein Spieler.
     *  Natuerliche Ordnung: Anzahl angeschlossener Städte, fallend; bei gleicher Anzahl Städte: Nummer des größten Kraftwerkes, fallend; ohne Kraftwerke: Farbe, alphabetisch.
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     * @return ein Spieler.
     */
    @Override
    public OpenPlayer newPlayer(String secret, String color) {
        final OpenPlayer player;
        if(actualPlayers.containsKey(Objects.requireNonNull(color))) {
            player = actualPlayers.get(color);
        }
        else{
            player= new MRodicPlayer(secret,color);
            actualPlayers.put(color,player);
        }
        return player;
    }


    /**
     * Ein Kraftwerk.
     * Natuerliche Ordnung: Nummer, aufsteigend.
     * @param number    Nummer des Kraftwerks. Nicht negativ. Nicht null.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht. Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann. Echt positiv.
     * @return ein Kraftwerk.
     */
    @Override
    public OpenPlant newPlant(int number, Plant.Type type, int resources, int cities) {
        if(number<0) {
            throw new IllegalArgumentException("Number must be >= 0");
        }
        final OpenPlant plant;
        if(actualPlants.containsKey(Objects.requireNonNull(number))) {
            plant = actualPlants.get(number);
        }
        else{
            plant= new MRodicPlant(number,type,resources,cities);
            actualPlants.put(number,plant);
        }
        return plant;
    }

    /**
     * Ein Kraftwerksmarkt.
     * @param edition Ausgabe des Spieles.
     * @return Kraftwerksmarkt. Nicht null.
     */
    @Override
    public OpenPlantMarket newPlantMarket(Edition edition) {
        if(plantMarketReturn==null){
            plantMarketReturn = new MRodicPlantMarket(constructPlants(Objects.requireNonNull(edition).getPlantSpecifications()));
        }
        return plantMarketReturn;
    }

    /**
     * Hilfsmethode für die Konstruktion aller Kraftwerke in dem Spiel.
     * @param plantSpecifications Liste aller Krafterke aus dieser Edition.
     * @return Menge aller Kraftwerke.
     */
    private Set<OpenPlant> constructPlants(List<String> plantSpecifications){
        final Set<OpenPlant> plants=new HashSet<>();
        final Plant.Type[] types= Plant.Type.values();
        final String typeLetters="COGUHEF";
        for(String row:plantSpecifications) {
            final String[] informations = row.split(REGEX);
            final Plant.Type type = types[typeLetters.indexOf(informations[1].charAt(0))];
            int amount = informations[1].length();
            if (informations[1].charAt(0) == 'E' || informations[1].charAt(0) == 'F') {
                amount = 0;
            }
            plants.add(newPlant(Integer.parseInt(informations[0]), type, amount, Integer.parseInt(informations[2])));
        }
        return plants;
    }


    /**
     * Ein Rohstoffmarkt.
     * @param edition Ausgabe des Spieles.
     * @return Rohstoffmarkt. Nicht null.
     */
    @Override
    public OpenResourceMarket newResourceMarket(Edition edition) {
        if(resourceMarketReturn==null){
            final Map<Resource, List<Integer>> cost = edition.getResourceAvailableToCost();
            final Map<Resource, Integer> totalResourceNumber = edition.getResourceToNumber();
            final Bag<Resource> totalNumberConstruct = new MRodicBag<>();
            final Map<Resource, Integer> initializeAvalable = edition.getResourcesInitiallyAvailable();
            final Bag<Resource> initializeConstruct = new MRodicBag<>();
            initializeAvalable.forEach(initializeConstruct::add);
            totalResourceNumber.forEach(totalNumberConstruct::add);
            totalNumberConstruct.remove(initializeConstruct);
            resourceMarketReturn = new MRodicResourceMarket(initializeConstruct, totalNumberConstruct, cost);
        }
        return resourceMarketReturn;
    }

    /**
     * Ein Spielplan.
     * @param edition Ausgabe des Spieles.
     * @return Spielplan.
     */
    @Override
    public OpenBoard newBoard(Edition edition) {
        if(boardReturn==null){
            boardReturn=new MRodicBoard(Objects.requireNonNull(edition),createBoard(edition));
        }
        return boardReturn;
    }

    /**Eine Auktion.
     * @param plant   Kraftwerk, das zum Verkauf steht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     *                Die Spieler bieten in der Reihenfolge dieser Liste.
     * @return bisher null
     */

    @Override
    public OpenAuction newAuction(OpenPlant plant, List<OpenPlayer> players) {
        final OpenAuction auction;
        if(auctions.containsKey(plant.getNumber())) {
            auction = auctions.get(plant.getNumber());
        }
        else{
            if(Objects.requireNonNull(players).size()==0)
                throw new IllegalArgumentException("List of Players must not be empty.");
            auction=new MRodicAuction(players,plant);
            auctions.put(plant.getNumber(),auction);
        }
        return auction;
    }

    /**
     * Hilsmethode fuer newBoard Methode.
     * Erzeugt alle Staedte.
     * @param edition Edition dieses Spieles.
     * @return Menge aller Staedte.
     */
    private Set<OpenCity> createBoard(Edition edition){
        final List<String> cityList =edition.getCitySpecifications();
        final Set<OpenCity> citiesReturn=new HashSet<>();
        cityList.forEach(row->citiesReturn
                .add(newCity(row.split(REGEX)[0], Integer.parseInt(row.split(REGEX)[1]))));
        return citiesReturn;
    }

    /**
     * Ein Spiel.
     * @param edition Ausgabe des Spieles.
     * @return Spiel. Nicht null.
     */
    @Override
    public OpenGame newGame(Edition edition) {
        if(gameReturn==null){
            gameReturn=new MRodicGame(Objects.requireNonNull(edition),this);
        }
        return gameReturn;
    }

}