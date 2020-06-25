package edu.hm.cs.rs.powergrid;

import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenFactory;
import edu.hm.cs.rs.powergrid.datastore.mutable.OpenGame;
import edu.hm.cs.rs.powergrid.logic.Move;
import edu.hm.cs.rs.powergrid.logic.Rules;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Minimales Hauptprogramm Funkenschlag.
 * Alle Spieler an der selben Konsole (Hot seat).
 * Die Eingaben beziehen sich auf jeweils einen Spieler.
 * Eingaben sind: Nummer eines angebotenen Zuges oder
 * Geheimnis zum Wechsel zu einem anderen Spieler (0 fuer unbekannten Spieler).
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-05-06
 */
public class PowergridHotseat1 {
    /**
     * Kapselt einen Aufruf, der scheitern kann.
     * @param <R>      Ergebnistyp des Aufrufs.
     * @param callable Aufzurufender Code.
     * @throws RuntimeException Kapsel mit der Exception, wenn der Aufruf schief geht.
     */
    private static <R> R wrap(Callable<R> callable) {
        try {
            return callable.call();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Entry point.
     * @param args Kommandozeilenargumente: Properties key=value oder @-Zeichen mit Responsefile,
     *             das Kommandozeileargumente enthaelt (eines pro Zeile).
     */
    public static void main(String... args) {
        // Kommandozeilenargumente auswerten
        Stream.of(args)
                .flatMap(arg -> arg.startsWith("@")?
                        wrap(() -> Files.lines(Path.of(arg.substring(1)))):
                        Stream.of(arg))
                .map(propertyDefinition -> propertyDefinition.split("="))
                .forEach(keyValue -> System.setProperty(keyValue[0], keyValue[1]));

        // Datastore, Logic erzeugen
        final OpenFactory factory = OpenFactory.newFactory();
        final OpenGame game = factory.newGame(new EditionGermany() {
            @Override public List<String> getPlayerColors() {
                return List.of("RED", "GREEN", "BLUE", "YELLOW", "BLACK", "PURPLE");
            }
        });
        final Rules logic = Rules.newRules(game);

        // Dialogschleife
        final Map<String, String> knownSecretsToColor = new HashMap<>();
        Optional<String> currentSecret = Optional.empty();
        boolean run = true;
        while(run) {
            final List<Move> moves = presentMoves(logic, knownSecretsToColor, currentSecret);
            currentSecret = getAndApplyChoice(logic, knownSecretsToColor, currentSecret, moves);
            tellSecretIfNew(game, knownSecretsToColor);
        }

    }

    /**
     * Praesentiert die Zugmoeglichkeiten des aktuellen Spielers.
     * @param logic               Spielregeln.
     * @param knownSecretsToColor Geheimnisse und Farben
     * @param currentSecret       Geheimnis des Spielers, der gerade spielt.
     *                            Leer bei einem unbekannten Spieler.
     * @return Zugmoeglichkeiten. Eventuell leer.
     */
    private static List<Move> presentMoves(Rules logic, Map<String, String> knownSecretsToColor, Optional<String> currentSecret) {
        final List<Move> moves = new ArrayList<>(logic.getMoves(currentSecret));
        System.out.printf("%nPlayer %s, your moves are:%n",
                          knownSecretsToColor.getOrDefault(currentSecret.orElse(null), "unknown"));
        int moveNumber = 1;
        for(Move move : moves)
            System.out.printf("%d.) %s%n", moveNumber++, move);
        return moves;
    }

    /**
     * Liest die Eingabe und fuehrt sie aus.
     * @param logic               Spielregeln.
     * @param knownSecretsToColor Geheimnisse und Farben
     * @param currentSecret       Geheimnis des Spielers, der gerade spielt.
     *                            Leer bei einem unbekannten Spieler.
     * @param moves               Zugmoeglichkeiten. Eventuell leer.
     * @return Geheimnis des Spielers, der ab jetzt spielt.
     * Leer bei einem unbekannten Spieler.
     */
    private static Optional<String> getAndApplyChoice(Rules logic, Map<String, String> knownSecretsToColor, Optional<String> currentSecret, List<Move> moves) {
        System.out.print("Please enter a move number or another player's secret or 0 for player unknown: ");
        int moveNumber;
        boolean validEntry = false;
        final Scanner scanner = new Scanner(System.in);
        do
            try {
                final String entry = scanner.nextLine();
                if(entry.equals("0"))
                    currentSecret = Optional.empty();
                else if(knownSecretsToColor.containsKey(entry))
                    currentSecret = Optional.of(entry);
                else {
                    moveNumber = Integer.parseInt(entry);
                    logic.fire(currentSecret, moves.get(moveNumber - 1)).ifPresent(System.out::println);
                }
                validEntry = true;
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Sorry, not a valid entry - please try again.");
            }
        while(!validEntry);
        return currentSecret;
    }

    /**
     * Zeigt einem neuen Spieler sein Geheimnis und merkt es sich zusammen mit seiner Farbe.
     * @param game                Spiel.
     * @param knownSecretsToColor Geheimnisse und Farben
     */
    private static void tellSecretIfNew(Game game, Map<String, String> knownSecretsToColor) {
        game.getPlayers()
                .stream() // alle Spieler
                .map(Player::getSecret) // Geheimnisse oder null
                .filter(secret -> secret != null) // Geheimnis
                .findAny()
                .ifPresent(secret -> {
                    String color = game.findPlayer(secret).getColor(); // Farbe
                    System.out.printf("New player %s, your secret is: %s%n", color, secret); // mitteilen
                    knownSecretsToColor.put(secret, color); // abspeichern
                });
    }

}

