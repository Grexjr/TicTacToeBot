package io.github.grexjr.tictactoebot;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.bot.HeuristicsBot;
import io.github.grexjr.tictactoebot.bot.RandomBot;
import io.github.grexjr.tictactoebot.engine.Game;
import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.StdIn;
import io.github.grexjr.tictactoebot.testbots.*;
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
            int botResult = h_bot1.getInput(board1, dummy_input,new RandomBot('-'),10);
            assertEquals(3, botResult);
        }
        for(int i = 0; i < 100; i++){
            int botResult = h_bot2.getInput(board2, dummy_input,new RandomBot('-'),10);
            assertEquals(8, botResult);
        }
        for(int i = 0; i < 100; i++){
            int botResult = h_bot1.getInput(board3, dummy_input,new RandomBot('-'),10);
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
    public void testHeuristicsFinalVersusRandom(){
        String fileName = "hBot_final_v_rBot";
        String testHeader = "HEURISTICS_FINAL_VERSUS_RANDOM";
        HeuristicsBotFINAL hBotF = new HeuristicsBotFINAL('x');
        RandomBotTest rBot = new RandomBotTest('o');
        int iterations = 100000;
        double expectedWinRate = 85;

        double testResult = runVersusTest(hBotF,rBot,iterations,fileName,testHeader);

        assertTrue(testResult >= expectedWinRate);
    }

    @Test
    public void testHeuristicsFinalVersusHeuristicsWL(){
        String fileName = "hBot_final_v_hBot_lanes";
        String testHeader = "HEURISTICS_FINAL_VERSUS_HEURISTICS_WINNING_LANES";
        HeuristicsBotWinningLanes hBotWL = new HeuristicsBotWinningLanes('x');
        HeuristicsBotFINAL hBotF = new HeuristicsBotFINAL('o');
        int iterations = 100000;
        double expectedWinRate = 30;

        double testResult = runVersusTest(hBotF,hBotWL,iterations,fileName,testHeader);

        assertTrue(testResult >= expectedWinRate);
    }



}
