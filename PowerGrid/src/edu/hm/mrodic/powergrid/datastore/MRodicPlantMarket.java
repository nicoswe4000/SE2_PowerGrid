package edu.hm.mrodic.powergrid.datastore;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlant;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenPlantMarket;

import java.util.*;

/**
 * Ein Kraftwerkmarkt.
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-05-07
 */
class MRodicPlantMarket implements OpenPlantMarket {
    /** Zukuenftiger Markt.*/
    private final Set<OpenPlant> futurePlants=new TreeSet<>();
    /** Verborgener Stapel.*/
    private final List<OpenPlant> hiddenPlants;
    /** Jetziger Markt.*/
    private final Set<OpenPlant> actualPlants=new TreeSet<>();
    /** Alle Kraftwerke.*/
    private Set<OpenPlant> allPlants;

    MRodicPlantMarket(Set<OpenPlant> plants){
        this.hiddenPlants= new ArrayList<>(plants);
        this.allPlants=plants;
    }
    @Override
    public Set<Plant> getActual() {
        return Collections.unmodifiableSet(getOpenActual());
    }

    @Override
    public OpenPlant removePlant(int number) {
        OpenPlant plantRemoved=null;
        fuseAllPlants();
        for(OpenPlant plant:allPlants){
            if(plant.getNumber()==number){
                futurePlants.remove(plant);
                actualPlants.remove(plant);
                hiddenPlants.remove(plant);
                plantRemoved=plant;
            }
        }
        fuseAllPlants();
        return plantRemoved;
    }

    @Override
    public Set<Plant> getFuture() {
        return Collections.unmodifiableSet(getOpenFuture());
    }

    @Override
    public int getNumberHidden() {
        return hiddenPlants.size();
    }

    @Override
    public OpenPlant findPlant(int number) {
        OpenPlant plantReturn = null;
        fuseAllPlants();
        for(OpenPlant plant:allPlants){
            if(plant.getNumber()==number){
                plantReturn = plant;
            }
        }
        return plantReturn;
    }

    @Override
    public Set<OpenPlant> getOpenActual() {
        return actualPlants;
    }

    @Override
    public Set<OpenPlant> getOpenFuture() {
        return futurePlants;
    }

    @Override
    public List<OpenPlant> getOpenHidden() {
        return hiddenPlants;
    }

    /** Hilfsmethode.
     * Fügt alle Kraftwerke aus allen 3 Stapeln zusammen in eins.
     */
    private void fuseAllPlants(){
        allPlants=new HashSet<>();
        allPlants.addAll(hiddenPlants);
        allPlants.addAll(actualPlants);
        allPlants.addAll(futurePlants);
    }
}
