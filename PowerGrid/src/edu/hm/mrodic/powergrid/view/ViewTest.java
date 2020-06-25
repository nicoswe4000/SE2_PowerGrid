package edu.hm.mrodic.powergrid.view;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Phase;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.MoveType;
import edu.hm.cs.rs.powergrid.logic.Rules;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class ViewTest {

    private final String fcqn = "edu.hm.mrodic.powergrid.datastore.MRodicFactory";
    private final String fcqnrules = "edu.hm.mrodic.powergrid.logic.GameRules";

    private final OpenFactory factory = OpenFactory.newFactory(fcqn);
    private final Edition testEdition = new EditionGermany();
    private final OpenGame game = factory.newGame(testEdition);
    private final Rules rules=Rules.newRules(fcqnrules,game);

    @Test
    public void joinPlayerViewTest(){
        List<String> expected = Arrays.asList(
          "+ player.0.city.num=0", "+ player.0.color=RED", "+ player.0.electro=0",
          "+ player.0.passed=false", "+ player.0.plant.num=0", "+ player.0.resource.num=0", "x player.num=1 //0"
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        game.setPhase(Phase.Opening);
        game.getOpenPlayers().clear();
        Optional<Move> move=rules.getMoves(Optional.empty()).stream().filter(prototype->prototype.getType().equals(MoveType.JoinPlayer)).findFirst();
        rules.fire(Optional.empty(),move.get());

        System.out.flush();
        System.setOut(old);
        String a = baos.toString();
        List<String> actual = Arrays.asList(a.split("\r\n"));
        Assert.assertEquals(expected, actual);

        // Hello Github
    }
}