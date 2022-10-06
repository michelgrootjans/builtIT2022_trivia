package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.Printer;
import org.approvaltests.combinations.CombinationApprovals;
import org.approvaltests.reporters.ClipboardReporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.approvaltests.Approvals.verify;
import static org.assertj.core.api.Assertions.assertThat;

@UseReporter(ClipboardReporter.class)
public class SomeTest {

    private TestPrinter printer;
    private Game game;

    @BeforeEach
    void setUp() {
        printer = new TestPrinter();
        game = new Game(printer);
    }

    @Test
    public void singlePlayer() {
        game.add("Christian");
        assertThat(game.isPlayable()).isFalse();
        verify(printer.messages);
    }

    @Test
    public void twoPlayers() {
        game.add("Christian");
        game.add("Bart");
        assertThat(game.isPlayable()).isTrue();
        verify(printer.messages);
    }

    @Test
    public void advance30times_1_WithRightAnswer() {
        game.add("Christian");
        game.add("Bart");
        for (int i = 0; i < 30; i++) {
            game.roll(1);
            game.wasCorrectlyAnswered();
        }
        verify(printer.messages);
    }

    @Test
    public void advance30_3_timesWithRightAnswer() {
        game.add("Christian");
        game.add("Bart");
        for (int i = 0; i < 30; i++) {
            game.roll(3);
            game.wasCorrectlyAnswered();
        }
        verify(printer.messages);
    }

    @Test
    public void twoPlayersGame_1() {
        CombinationApprovals.verifyAllCombinations(
                this::firstPlayerGame,
                new Integer[]{1, 2, 3, 4, 5, 6},
                new Boolean[]{true, false},
                new Integer[]{1, 2, 3, 4, 5, 6},
                new Boolean[]{true, false}
        );
    }

    @Test
    public void twoPlayersGame_2() {
        CombinationApprovals.verifyAllCombinations(
                this::secondPlayerGame,
                new Integer[]{1, 2, 3, 4, 5, 6},
                new Boolean[]{true, false},
                new Integer[]{1, 2, 3, 4, 5, 6},
                new Boolean[]{true, false}
        );
    }

    private String firstPlayerGame(int roll1, boolean right1, int roll2, boolean right2) {
        game = twoPlayerGame();
        //Player 1 -- not important
        game.roll(1);
        game.wasCorrectlyAnswered();
        //Player 2
        game.roll(roll1);
        if (right1) {
            game.wasCorrectlyAnswered();
        } else {
            game.wrongAnswer();
        }
        //Player 1 -- not important
        game.roll(1);
        game.wasCorrectlyAnswered();
        //Player 2 again
        game.roll(roll2);
        if (right2) {
            game.wasCorrectlyAnswered();
        } else {
            game.wrongAnswer();
        }
        return String.join("\n", printer.messages);
    }

    private String secondPlayerGame(int roll1, boolean right1, int roll2, boolean right2) {
        game = twoPlayerGame();
        //Player 1
        game.roll(roll1);
        if (right1) {
            game.wasCorrectlyAnswered();
        } else {
            game.wrongAnswer();
        }
        //Player 2 -- not important
        game.roll(1);
        game.wasCorrectlyAnswered();
        //Player 1 again
        game.roll(roll2);
        if (right2) {
            game.wasCorrectlyAnswered();
        } else {
            game.wrongAnswer();
        }
        //Player 2 -- not important
        game.roll(1);
        game.wasCorrectlyAnswered();
        return String.join("\n", printer.messages);
    }

    @Test
    void name() {
        Game game = twoPlayerGame();
        game.roll(1);
        game.wrongAnswer();
    }

    private Game twoPlayerGame() {
        game.add("Christian");
        game.add("Bart");
        return game;
    }

    private static class TestPrinter implements Printer {


        private final List<String> messages = new ArrayList<>();

        @Override
        public void print(String message) {
            messages.add(message);
        }
    }
}
