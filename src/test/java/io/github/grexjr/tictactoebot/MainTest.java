package io.github.grexjr.tictactoebot;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.bot.HeuristicsBot;
import io.github.grexjr.tictactoebot.bot.RandomBot;
import io.github.grexjr.tictactoebot.engine.Game;
import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.StdIn;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testCreateGrid(){
        Board testBoard = new Board();

        testBoard.printGrid();

        for(int i = 0; i < testBoard.getRawGrid().length; i++){
            assertEquals(' ', testBoard.getRawGrid()[i]);
        }
    }

    @Test
    public void testChangingGrid(){

    }

    @Test
    public void testCheckWin(){
        char[] grid1 = new char[]{
                'x','x','x',
                'o',' ','o',
                ' ','o',' '
        };
        char[] grid2 = new char[]{
                'x',' ', ' ',
                ' ',' ',' ',
                ' ',' ',' ',
        };

        char[] grid3 = new char[]{
                'x','o','x',
                'x','o','o',
                'o','x','x',
        };

        Board board1 = new Board(grid1);
        Game game = new Game(board1);
        assertEquals('x',game.checkWin());
        game.setGameBoard(new Board(grid2));
        assertEquals(' ', game.checkWin());
        game.setGameBoard(new Board(grid3));
        assertEquals('u',game.checkWin());

    }

    @Test
    public void testHeuristicBotWin(){
        char[] grid1 = new char[]{
                'x','x',' ',
                'o',' ','o',
                ' ','o',' '
        };
        char[] grid2 = new char[]{
                'x','o', ' ',
                ' ','o',' ',
                ' ',' ',' ',
        };

        char[] grid3 = new char[]{
                'x','o','x',
                'x',' ','o',
                'o',' ','x',
        };

        StdIn dummy_input = new StdIn();
        Board board1 = new Board(grid1);
        Board board2 = new Board(grid2);
        Board board3 = new Board(grid3);
        HeuristicsBot h_bot1 = new HeuristicsBot('x');
        HeuristicsBot h_bot2 = new HeuristicsBot('o');
        // Run it 100 times to make sure its not just random
        for(int i = 0; i < 100; i++){
            int botResult = h_bot1.getInput(board1, dummy_input);
            assertEquals(3, botResult);
        }
        for(int i = 0; i < 100; i++){
            int botResult = h_bot2.getInput(board2, dummy_input);
            assertEquals(8, botResult);
        }
        for(int i = 0; i < 100; i++){
            int botResult = h_bot1.getInput(board3, dummy_input);
            assertEquals(5, botResult);
        }
    }

    @Test
    public void testHeuristicsCheckWinVersusRandom(){
        String fileName = "hBot_checkWin_v_rBot";

        Random rand = new Random();
        Game game;
        int hBotXWins = 0;
        int hBotOWins = 0;
        int rBotXWins = 0;
        int rBotOWins = 0;
        int totalGames = 100000;
        double expectedWinRate = 55;

        for(int iteration = 0; iteration < totalGames; iteration++){
            HeuristicsBot hBotX = new HeuristicsBot('X');
            HeuristicsBot hBotO = new HeuristicsBot('O');

            RandomBot rBotX = new RandomBot('X');
            RandomBot rBotO = new RandomBot('O');

            boolean gameType = rand.nextBoolean();

            if (gameType) {
                game = new Game(hBotX, rBotO);
                game.runTextlessGame();
                if(game.getWhoWon() == hBotX.getSymbol()) hBotXWins++;
                if(game.getWhoWon() == rBotO.getSymbol()) rBotOWins++;
            } else {
                game = new Game(rBotX, hBotO);
                game.runTextlessGame();
                if(game.getWhoWon() == rBotX.getSymbol()) rBotXWins++;
                if(game.getWhoWon() == hBotO.getSymbol()) hBotOWins++;
            }
        }

        int totalHBotWins = hBotXWins + hBotOWins;
        int hBotLosses = rBotXWins + rBotOWins;

        double winPercent = ((double) totalHBotWins / totalGames) * 100;

        // Display test information
        System.out.println("RANDOM BOT VERSUS HEURISTICS BOT: ");
        System.out.println("Total games played: " + totalGames);
        System.out.println("Heuristic bot wins: " + totalHBotWins);
        System.out.println("As x: " + hBotXWins + " | " + "As o: " + hBotOWins);
        System.out.println("Heuristic bot losses: " + hBotLosses);
        System.out.println("Heuristics bot win rate: " + winPercent);

        // Create a visual bar
        int barSections = 100; // 100 bars
        int winBars = (int) (barSections * (winPercent/100));

        System.out.print("[");
        for(int i = 0; i < barSections; i++){
            if(i < winBars) System.out.print("\u001B[32m" + "-" + "\u001B[0m");
            else System.out.print("\u001B[31m" + "-" + "\u001B[0m");
        }
        System.out.println("]");

        // Write results to file
        TestLogger.logResult(fileName,"\n");
        TestLogger.logResult(fileName,"RANDOM VS HEURISTICS_CHECK_WIN");
        TestLogger.logResult(fileName,"Total games played: " + totalGames);
        TestLogger.logResult(fileName, "Heuristic bot wins: " + totalHBotWins);
        TestLogger.logResult(fileName,"As x: " + hBotXWins + " | " + "As o: " + hBotOWins);
        TestLogger.logResult(fileName, "Heuristic bot losses: " + hBotLosses);
        TestLogger.logResult(fileName, "Heuristics bot win rate: " + winPercent);

        // Should probably win more than 55% of games -- pass condition
        assertTrue(winPercent >= expectedWinRate);
    }

}
