package io.github.grexjr.tictactoebot;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.bot.HeuristicsBot;
import io.github.grexjr.tictactoebot.bot.RandomBot;
import io.github.grexjr.tictactoebot.engine.Game;
import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.StdIn;
import io.github.grexjr.tictactoebot.testbots.HeuristicsBotCheckBlock;
import io.github.grexjr.tictactoebot.testbots.HeuristicsBotCheckWin;
import io.github.grexjr.tictactoebot.testbots.HeuristicsBotWinningLanes;
import io.github.grexjr.tictactoebot.testbots.RandomBotTest;
import org.junit.jupiter.api.*;

import java.util.Random;

import static io.github.grexjr.tictactoebot.TestHelper.*;
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

        double testResult = runVersusTest(hBotCW,rBotTest,iterations,fileName,testHeader);
    }

    @Test
    public void testHeuristicsWinBlockVersusRandom(){
        String fileName = "hBot_checkBlock_v_rBot";
        String testHeader = "HEURISTICS_CHECK_BLOCK_VERSUS_RANDOM";
        HeuristicsBotCheckBlock hBotCB = new HeuristicsBotCheckBlock('x');
        RandomBotTest rBot = new RandomBotTest('o');
        int iterations = 100000;
        double expectedWinRate = 75;

        double testResult = runVersusTest(hBotCB,rBot,iterations,fileName,testHeader);

        assertTrue(testResult >= expectedWinRate);
    }

    @Test
    public void testHeuristicsWinVersusHeuristicsBlock(){
        String fileName = "hBot_win_v_hBot_block";
        String testHeader = "HEURISTICS_WIN_VERSUS_BLOCK";
        HeuristicsBotCheckWin hBotCW = new HeuristicsBotCheckWin('x');
        HeuristicsBotCheckBlock hBotCB = new HeuristicsBotCheckBlock('o');
        int iterations = 100000;
        double expectedWinRate = 55;

        double testResult = runVersusTest(hBotCB,hBotCW,iterations,fileName,testHeader);

        assertTrue(testResult >= expectedWinRate);
    }

    @Test
    public void testHeuristicsLanesVersusRandom(){
        String fileName = "hBot_lanes_v_rBot";
        String testHeader = "HEURISTICS_WINNING_LANES_VERSUS_RANDOM";
        HeuristicsBotWinningLanes hBotWL = new HeuristicsBotWinningLanes('x');
        RandomBotTest rBot = new RandomBotTest('o');
        int iterations = 100000;
        double expectedWinRate = 80;

        double testResult = runVersusTest(hBotWL,rBot,iterations,fileName,testHeader);

        assertTrue(testResult >= expectedWinRate);
    }

    @Test
    public void testWinLanesVersusCheckBlock(){
        String fileName = "hBot_lanes_v_hBot_block";
        String testHeader = "HEURISTICS_LANES_VERSUS_BLOCK";
        HeuristicsBotWinningLanes hBotWL = new HeuristicsBotWinningLanes('x');
        HeuristicsBotCheckBlock hBotCB = new HeuristicsBotCheckBlock('o');
        int iterations = 100000;
        double expectedWinRate = 55;

        // Fail because not blocking other player's accidental forking
        double testResult = runVersusTest(hBotWL,hBotCB,iterations,fileName,testHeader);

        assertTrue(testResult >= expectedWinRate);
    }

}
