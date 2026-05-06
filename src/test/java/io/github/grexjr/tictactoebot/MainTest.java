package io.github.grexjr.tictactoebot;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.bot.HeuristicsBot;
import io.github.grexjr.tictactoebot.bot.RandomBot;
import io.github.grexjr.tictactoebot.engine.Game;
import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.StdIn;
import io.github.grexjr.tictactoebot.testbots.HeuristicsBotCheckBlock;
import io.github.grexjr.tictactoebot.testbots.HeuristicsBotCheckWin;
import io.github.grexjr.tictactoebot.testbots.RandomBotTest;
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
            int botResult = h_bot1.getInput(board1, dummy_input,new RandomBot('-'));
            assertEquals(3, botResult);
        }
        for(int i = 0; i < 100; i++){
            int botResult = h_bot2.getInput(board2, dummy_input,new RandomBot('-'));
            assertEquals(8, botResult);
        }
        for(int i = 0; i < 100; i++){
            int botResult = h_bot1.getInput(board3, dummy_input,new RandomBot('-'));
            assertEquals(5, botResult);
        }
    }

    @Test
    public void testHeuristicsCheckWinVersusRandom(){
        String fileName = "hBot_checkWin_v_rBot";
        String testHeader = "HEURISTICS_CHECK_WIN_VERSUS_RANDOM_BOT";
        HeuristicsBotCheckWin hBotCW = new HeuristicsBotCheckWin('x');
        RandomBotTest rBotTest = new RandomBotTest('o');
        int iterations = 100000;
        double expectedWinRate = 55;

        runVersusTest(hBotCW,rBotTest,iterations,expectedWinRate,fileName,testHeader);
    }

    @Test
    public void testHeuristicsWinBlockVersusRandom(){
        String fileName = "hBot_checkBlock_v_rBot";
        String testHeader = "HEURISTICS_CHECK_BLOCK_VERSUS_RANDOM";
        HeuristicsBotCheckBlock hBotCB = new HeuristicsBotCheckBlock('x');
        RandomBotTest rBot = new RandomBotTest('o');
        int iterations = 100000;
        double expectedWinRate = 75;

        runVersusTest(hBotCB,rBot,iterations,expectedWinRate,fileName,testHeader);
    }

    @Test
    public void testHeuristicsWinVersusHeuristicsBlock(){
        String fileName = "hBot_win_v_hBot_block";
        String testHeader = "HEURISTICS_WIN_VERSUS_BLOCK";
        HeuristicsBotCheckWin hBotCW = new HeuristicsBotCheckWin('x');
        HeuristicsBotCheckBlock hBotCB = new HeuristicsBotCheckBlock('o');
        int iterations = 100000;
        double expectedWinRate = 55;

        runVersusTest(hBotCB,hBotCW,iterations,expectedWinRate,fileName,testHeader);
    }





    private void runVersusTest(Bot challenger, Bot defender, int totalGames, double expectedChallengerWinRate,
                               String logFileName, String testHeader){
        // Variables for wins and game amounts and expected values
        int challengerXWins = 0;
        int challengerOWins = 0;
        int defenderXWins = 0;
        int defenderOWins = 0;
        int draws = 0;

        for(int iteration = 0; iteration < totalGames; iteration++){
            if(iteration <= totalGames/2){
                challenger.setSymbol('x');
                defender.setSymbol('o');
                char winner = runTestGame(challenger, defender);
                if(winner == challenger.getSymbol()) challengerXWins++;
                if(winner == defender.getSymbol()) defenderOWins++;
                if(winner == 'u') draws++;
            } else {
                defender.setSymbol('x');
                challenger.setSymbol('o');
                char winner = runTestGame(defender, challenger);
                if(winner == defender.getSymbol()) defenderXWins++;
                if(winner == challenger.getSymbol()) challengerOWins++;
                if(winner == 'u') draws++;
            }
        }

        int totalChallengerWins = challengerXWins + challengerOWins;
        int challengerLosses = defenderXWins + defenderOWins;

        double winPercent = ((double) totalChallengerWins / totalGames) * 100;
        double drawPercent = ((double) draws / totalGames) * 100;

        // Print results
        printTestResults(testHeader,totalGames,challengerXWins,challengerOWins,challengerLosses,winPercent,
                drawPercent,challenger);

        // Write results to file
        logTestResults(logFileName, testHeader, totalChallengerWins,totalGames,winPercent,challenger);

        // Should probably win more than 55% of games -- pass condition
        assertTrue(winPercent >= expectedChallengerWinRate);
    }

    private char runTestGame(Bot challenger, Bot defender){
        Game game = new Game(challenger,defender);
        // Runs a silent, printless game
        game.runGame(true);

        return game.getWhoWon();
    }

    private void printTestResults(String header, int totalGames, int challengerXWins,
                                  int challengerOWins, int challengerLosses, double winPercent, double drawPercent,
                                  Bot challenger){
        // Display test information
        System.out.println(header);
        System.out.println("Total games played: " + totalGames);
        System.out.println(challenger.getName() + " wins: " + (challengerXWins + challengerOWins));
        System.out.println("As x: " + challengerXWins + " | " + "As o: " + challengerOWins);
        System.out.println("Draws: " + (totalGames * (drawPercent / 100)));
        System.out.println(challenger.getName() + " losses: " + challengerLosses);
        System.out.println(challenger.getName() + " win rate: " + winPercent);

        // Create a visual bar
        int barSections = 100; // 100 bars
        int winBars = (int) (barSections * (winPercent/100));
        int drawBars = (int) (barSections * (drawPercent/100));

        System.out.print("[");
        for(int i = 0; i < barSections; i++){
            if(i < winBars) System.out.print("\u001B[32m" + "-" + "\u001B[0m");
            else if(i < (drawBars + winBars)) System.out.print("\u001B[37m" + "-" + "\u001B[0m");
            else System.out.print("\u001B[31m" + "-" + "\u001B[0m");
        }
        System.out.println("]");
    }

    private void logTestResults(String fileName, String header, int challengerWins, int totalGames, double winPercent,
                                Bot challenger){
        TestLogger.logResult(fileName, "\n");
        TestLogger.logResult(fileName,header);
        TestLogger.logResult(fileName,"Total games played: " + totalGames);
        TestLogger.logResult(fileName, challenger.getName() + " wins: " + challengerWins);
        TestLogger.logResult(fileName, challenger.getName() + " win rate: " + winPercent);
    }

}
