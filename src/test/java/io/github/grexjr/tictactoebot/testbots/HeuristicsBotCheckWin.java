package io.github.grexjr.tictactoebot.testbots;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.StdIn;

import java.util.Random;

public class HeuristicsBotCheckWin extends Bot {

    // All rows, diagonals, columns add to 15
    // Use this for getting the values from board indices
    private final static int[] magic_square = new int[]{2,7,6,9,5,1,4,3,8};
    // Size 10 because indexed 1-9, no zero-value squares so indices are 1-9
    private final static int[] ms = new int[10];

    private int winningIndex;

    // ### A bot that uses heuristics to try and win (or draw) the game of tic-tac-toe
    public HeuristicsBotCheckWin(char symbol){
        super(symbol);
        winningIndex = -1;
        initMagicSquare();
    }

    /**
     * <p>
     *  Magic square representation of the board. Indices are the values of the magic square at that cell, because it
     *  allows for constant time lookup by searching ms[value] = index we want to place a character at.
     * </p>
     * <p>
     *     Based on this magic square from:
     *     <a href="https://medium.com/@helenjoy88/tic-tac-toe-game-playing-using-magic-square-program-2-in-ai-c9b0ad66ea3b">
     *     This Medium.com article
     *     </a>
     * </p>
     *
     *  <p>[2][7][6]</p>
     *  <p>[9][5][1]</p>
     *  <p>[4][3][8]</p>
     */
    private void initMagicSquare(){
        ms[2] = 0;
        ms[7] = 1;
        ms[6] = 2;
        ms[9] = 3;
        ms[5] = 4;
        ms[1] = 5;
        ms[4] = 6;
        ms[3] = 7;
        ms[8] = 8;
    }

    @Override
    public int getInput(Board board, StdIn input){
        return doHeuristicsBrain(board);
    }

    // ### THE BRAIN: run certain rules
    private int doHeuristicsBrain(Board board){
        char[] grid = board.getRawGrid();
        // PRIORITY 1: Check if we can win
        if(checkWin(grid)){
            // If so, get the index that lets us win based on what our winning value is
            // TODO: might be able to reverse the magic_square array to avoid this loop; i.e. indices are the values,
            //  and values are the board indices. Would mean constant time lookup.
            return winningIndex + 1;
        }

        // PRIORITY 2: Check if

        // Otherwise (for now) return a random value
        Random rand = new Random();
        // Random 1-9, if not blank then re rolls
        int random = rand.nextInt(0,9);
        while(board.getRawGrid()[random] != ' '){
            random = rand.nextInt(0,9);
        }
        return random + 1;
    }

    private boolean checkWin(char[] grid){
        int result;
        // Uses a magic square array to test if it can win
        for(int i = 0; i < grid.length; i++){
            for(int j = 1; j < grid.length; j++) {
                // If they are the same index, move on
                if(i == j) continue;
                // If they are not the same symbol (player's symbol), move on
                if(!(grid[i] == getSymbol() && grid[j] == getSymbol())) continue;

                // ELSE...

                // Get the corresponding magic square numbers from the magic square array and add them
                // Use the first magic_square by board index because coming from the board
                result = magic_square[i] + magic_square[j];

                // Test result by comparing it to 15
                int comparison = 15 - result;

                // If negative number or greater than 9, not in straight line, so continue search for next one
                if (comparison < 0 || comparison > 9) continue;

                // Comparison is our magic square value we are looking for. Get the corresponding index from the ms
                    // array
                winningIndex = ms[comparison];

                // Check if un-occupied, if not, then move to next iteration
                if (grid[winningIndex] != ' ') continue;

                // If un occupied and found our winning index, return true
                return true;
            }
        }
        // Fail: return false; gone through whole index found no winning index
        return false;
    }

}
