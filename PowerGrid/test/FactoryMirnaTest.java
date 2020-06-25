import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.*;
import edu.hm.cs.rs.powergrid.datastore.mutable.*;
import org.junit.Assert;
import org.junit.Test;
/**
 * @author Mirna Rodic, IF2B, mrodic@edu.edu.hm.edu
 * @version 2020-04-30
 */

public class FactoryMirnaTest {
    private final String fqcn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final OpenFactory factory = OpenFactory.newFactory(fqcn);

    @Test
    public void newCity1() {
        City city1 = factory.newCity("Erste", 1);
        City city2 = factory.newCity("Erste", 9);
        Assert.assertEquals(true, city1==city2);
    }
    @Test
     (expected=IllegalStateException.class) public void newCity2() {
        City city1 = factory.newCity("Erste", 1);
        city1.close();
        City city2 = factory.newCity("Erste", 1);
        city2.close();
    }
    @Test
    public void newPlant1() {
        Plant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        Plant plant2 = factory.newPlant(1, Plant.Type.Hybrid,8,5);
        Assert.assertEquals(true, plant1==plant2);
    }
    @Test
    public void newPlant2() {
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        plant1.setOperated(true);
        Plant plant2 = factory.newPlant(1, Plant.Type.Hybrid,8,5);
        Assert.assertEquals(true, plant2.hasOperated());
    }
    @Test
    public void newPlayer1() {
        Player player1 = factory.newPlayer("12345","red");
        Player player2 = factory.newPlayer("abcdefgh","red");
        Assert.assertEquals(true, player1==player2);
    }
    @Test
    public void newPlayer2() {
        Player player1 = factory.newPlayer("12345","red");
        player1.getSecret();
        Player player2 = factory.newPlayer("abcdefgh","red");
        Assert.assertEquals(null, player2.getSecret());
    }
    @Test
    public void newBoard1() {
        Board board1= factory.newBoard(new EditionGermany());
        Board board2= factory.newBoard(new EditionGermany());
        Assert.assertEquals(true, board1==board2);
    }
    @Test
    public void newBoard2() {
        OpenBoard board1= factory.newBoard(new EditionGermany());
        OpenCity sut=factory.newCity("MirnaRodic", 1);
        board1.getOpenCities().add(sut);
        Board board2= factory.newBoard(new EditionGermany());
        Assert.assertEquals(true, board2.findCity("MirnaRodic")==sut);
    }
    @Test
    public void newBoard3() {
        Board board1= factory.newBoard(new EditionGermany());
        OpenFactory factory2 = OpenFactory.newFactory(fqcn);
        Board board2= factory2.newBoard(new EditionGermany());
        Assert.assertEquals(true, board1!=board2);
    }
    @Test
    public void compareCity1() {
        City city1 = factory.newCity("Abc", 1);
        City city2 = factory.newCity("Abz", 9);
        Assert.assertEquals(true, city1.compareTo(city2)<0);
    }
    @Test
    public void compareCity2() {
        City city1 = factory.newCity("Abc", 1);
        City city2 = factory.newCity("abc", 9);
        Assert.assertEquals(true, city1.compareTo(city2)==0);
    }
    @Test
    public void compareCity3() {
        City city1 = factory.newCity("Zahn", 1);
        City city2 = factory.newCity("Ahn", 9);
        Assert.assertEquals(true, city1.compareTo(city2)>0);
    }
    @Test
    public void comparePlant1() {
        Plant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        Plant plant2 = factory.newPlant(1, Plant.Type.Hybrid,8,5);
        Assert.assertEquals(true, plant1.compareTo(plant2)==0);
    }
    @Test
    public void comparePlant2() {
        Plant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        OpenFactory factory2 = OpenFactory.newFactory(fqcn);
        Plant plant2 = factory2.newPlant(1, Plant.Type.Hybrid,8,5);
        Assert.assertEquals(true, plant1.compareTo(plant2)==0);
    }
    @Test
    public void comparePlant3() {
        Plant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        Plant plant2 = factory.newPlant(2, Plant.Type.Hybrid,8,5);
        Assert.assertEquals(true, plant1.compareTo(plant2)<0);
    }
    @Test
    public void comparePlant4() {
        Plant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        Plant plant2 = factory.newPlant(10, Plant.Type.Hybrid,8,5);
        Assert.assertEquals(true, plant2.compareTo(plant1)>0);
    }
    @Test
    public void comparePlayer() {
        Player player1 = factory.newPlayer("12345","red");
        Player player2 = factory.newPlayer("abcdefgh","blue");
        Assert.assertEquals(true, player2.compareTo(player1)<0);
    }
    @Test
    public void comparePlayer1() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        Player player2 = factory.newPlayer("abcdefgh","red");
        OpenCity city = factory.newCity("Stadt", 1);
        player1.getOpenCities().add(city);
        Assert.assertEquals(true, player1.compareTo(player2)==0);
    }
    @Test
    public void comparePlayer2() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        Player player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        player1.getOpenCities().add(city);
        Assert.assertEquals(true, player2.compareTo(player1)>0);
    }
    @Test
    public void comparePlayer3() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        OpenPlayer player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        player1.getOpenCities().add(city);
        player2.getOpenCities().add(city);
        Assert.assertEquals(true, player2.compareTo(player1)<0);
    }
    @Test
    public void comparePlayer4() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        OpenPlayer player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        player1.getOpenCities().add(city);
        player2.getOpenCities().add(city);
        Assert.assertEquals(true, player1.compareTo(player2)>0);
    }
    @Test
    public void comparePlayer5() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        Player player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        player1.getOpenCities().add(city);
        Assert.assertEquals(true, player1.compareTo(player2)<0);
    }
    @Test
    public void comparePlayer6() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        OpenPlayer player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        OpenPlant plant = factory.newPlant(1, Plant.Type.Eco,2,3);
        player1.getOpenCities().add(city);
        player2.getOpenCities().add(city);
        player1.getOpenPlants().add(plant);
        Assert.assertEquals(true, player1.compareTo(player2)<0);
    }
    @Test
    public void comparePlayer7() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        OpenPlayer player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        OpenPlant plant = factory.newPlant(1, Plant.Type.Eco,2,3);
        player1.getOpenCities().add(city);
        player2.getOpenCities().add(city);
        player1.getOpenPlants().add(plant);
        Assert.assertEquals(true, player2.compareTo(player1)>0);
    }
    @Test
    public void comparePlayer8() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        OpenPlayer player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        OpenPlant plant2 = factory.newPlant(10, Plant.Type.Eco,2,3);
        player1.getOpenCities().add(city);
        player2.getOpenCities().add(city);
        player1.getOpenPlants().add(plant1);
        player2.getOpenPlants().add(plant2);
        Assert.assertEquals(true, player2.compareTo(player1)<0);
    }
    @Test
    public void comparePlayer9() {
        OpenPlayer player1 = factory.newPlayer("12345","red");
        OpenPlayer player2 = factory.newPlayer("abcdefgh","blue");
        OpenCity city = factory.newCity("Stadt", 1);
        OpenPlant plant1 = factory.newPlant(1, Plant.Type.Eco,2,3);
        OpenPlant plant2 = factory.newPlant(10, Plant.Type.Eco,2,3);
        player1.getOpenCities().add(city);
        player2.getOpenCities().add(city);
        player1.getOpenPlants().add(plant1);
        player2.getOpenPlants().add(plant2);
        Assert.assertEquals(true, player1.compareTo(player2)>0);
    }
}
