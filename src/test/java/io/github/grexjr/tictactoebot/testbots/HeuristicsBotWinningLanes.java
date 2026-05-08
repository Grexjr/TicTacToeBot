package io.github.grexjr.tictactoebot.testbots;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.Player;
import io.github.grexjr.tictactoebot.engine.StdIn;

import java.util.Random;

public class HeuristicsBotWinningLanes extends Bot {

    // All rows, diagonals, columns add to 15
    // Use this for getting the values from board indices
    private final static int[] magic_square = new int[]{2,7,6,9,5,1,4,3,8};
    // Size 10 because indexed 1-9, no zero-value squares so indices are 1-9
    private final static int[] ms = new int[10];

    private int moveIndex;

    // ### A bot that uses heuristics to try and win (or draw) the game of tic-tac-toe
    public HeuristicsBotWinningLanes(char symbol){
        super(symbol);
        this.name = "HeuristicsBot";
        moveIndex = -1;
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
    public int getInput(Board board, StdIn input, Player opponent, int turnNum){
        return doHeuristicsBrain(board,opponent);
    }

    // ### THE BRAIN: run certain rules
    private int doHeuristicsBrain(Board board, Player opponent){
        char[] grid = board.getRawGrid();

        // PRIORITY 1: Check if we can win
        if(checkWin(grid)){
            return moveIndex + 1;
        }
        // PRIORITY 2: check if we can block
        if(checkBlock(grid,opponent)){
            return moveIndex + 1;
        }
        // PRIORITY 3: MAXIMIZE WINNING LANES
        maximizeWinningLines(grid,opponent.getSymbol());

        if(moveIndex == -1){// Otherwise (for now) return a random value
            Random rand = new Random();
            // Random 1-9, if not blank then re rolls
            int random = rand.nextInt(0, 9);
            while (board.getRawGrid()[random] != ' ') {
                random = rand.nextInt(0, 9);
            }

            moveIndex = random + 1;
        }

        return moveIndex;
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
                moveIndex = ms[comparison];

                // Check if un-occupied, if not, then move to next iteration
                if (grid[moveIndex] != ' ') continue;

                // If un occupied and found our winning index, return true
                return true;
            }
        }
        // Fail: return false; gone through whole index found no winning index
        return false;
    }

    // Basically exactly same as above
    public boolean checkBlock(char[] grid, Player opponent){
        int result;

        for(int i = 0; i < grid.length; i++){
            for(int j = 1; j < grid.length; j++) {
                // If they are the same index, move on
                if(i == j) continue;
                // If they are not the same symbol (opponent's symbol), move on
                if(!(grid[i] == opponent.getSymbol() && grid[j] == opponent.getSymbol())) continue;

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
                moveIndex = ms[comparison];

                // Check if un-occupied, if not, then move to next iteration
                if (grid[moveIndex] != ' ') continue;

                // If un occupied and found our winning index, return true
                return true;
            }
        }
        // If went through whole thing and didn't get to it, return false
        return false;
    }

    // Move where you can maximize winning lines
    private void maximizeWinningLines(char[] grid, char opponentSymbol){
        // Set move index to blank (shouldn't matter because functions before it will change it)
        moveIndex = -1;

        // Variable for how many winning lines pass through square
        int mostWinningLines = 0;

        // Loop through all the array
        for(int i = 0; i < grid.length; i++){
            // Temporary variable for winning lines of specific square
            int tempWinningLines = 0;
            boolean isOnDiagonal = false;

            // If occupied, continue on
            if(grid[i] != ' ') continue;

            // Otherwise simulate putting your symbol there

            // Check how many winning lines in row (indices are +1 and -1)
            // Save a variable for the starting index of the row and column you are in
            int rowStart = -1;
            int colStart = -1;
            int diagonalStart = -1;
            // Save a variable for how many pieces in the queried geometry are yours and opponents
            int neighbors = 0;
            int enemies = 0;


            // Check which row & column you are in and if on diagonal
            if(i < 3) {
                rowStart = 0;
            }
            else if(i < 6) {
                rowStart = 3;
            }
            else if(i < 9) {
                rowStart = 6;
            }
            // Column check
            // Can just use modulus! since rows are +3; 0, 3, 6; 1, 4, 7; 2, 5, 8
            colStart = i % 3;
            // Diagonal check; on the evens!!!! mod 2!!!!
            // But need to check WHICH diagonal... 0,4,8 or 6,4,2
            if(i % 2 == 0) isOnDiagonal = true;
            if(isOnDiagonal) {// This means it is on the descending diagonal
                if (i % 4 == 0) {
                    // If not center, on descending diagonal; if on center, diagonal start = -1
                    if(i != 4) {
                        diagonalStart = 0;
                    }
                }
                else { // i % 4 = 2
                    diagonalStart = 2;
                }
            }

            // Check row
            // <= because we want to check the last one too; i.e. j = 0, j = 1, j = 2 all need to be checked
            for(int j = rowStart; j <= rowStart + 2; j++){
                if(grid[j] == getSymbol()) neighbors++;
                else if(grid[j] == opponentSymbol) enemies++;
            }

            // Now check if neighbors and enemies are 2 and 0 respectively (1 because piece is not there yet)
            if(neighbors == 1 && enemies == 0){
                tempWinningLines += 1;
            }

            // Reset neighbors and enemies for column check
            neighbors = 0; enemies = 0;

            // Check column
            // <= because we want to check last one too; k += 3 because rows are +3
            for(int k = colStart; k <= colStart + 6; k += 3) {
                if(grid[k] == getSymbol()) neighbors++;
                if(grid[k] == opponentSymbol) enemies++;
            }

            // If the columns also have neighbors 2 and enemies 0, add it as a winning line
            if(neighbors == 1 && enemies == 0){
                tempWinningLines++;
            }

            // Reset neighbors and enemies for diagonal check
            neighbors = 0; enemies = 0;

            // Check diagonals if it is on the diagonal
            if(isOnDiagonal){
                // Do first diagonal (Ascending)
                if(diagonalStart == -1 || diagonalStart == 0){
                    for(int w = 0; w <= 8; w += 4){
                        if(grid[w] == getSymbol()) neighbors++;
                        if(grid[w] == opponentSymbol) enemies++;
                    }
                }

                // Add to temp winning lanes and reset values
                if(neighbors == 1 && enemies == 0){
                    tempWinningLines++;
                }
                neighbors = 0; enemies = 0;

                // Do descending diagonal
                if(diagonalStart == -1 || diagonalStart == 2){
                    for(int z = 2; z <= 6; z += 2){
                        if(grid[z] == getSymbol()) neighbors++;
                        if(grid[z] == opponentSymbol) enemies++;
                    }
                }

                // Add to temp winning lanes
                if(neighbors == 1 && enemies == 0){
                    tempWinningLines++;
                }
            }

            // Otherwise just continue on

            // Then check if temp winning lines from this square are greater than current max, if so replace winning
            // max and change move index to this square
            if(tempWinningLines > mostWinningLines){
                mostWinningLines = tempWinningLines;
                moveIndex = i + 1;
            }
        }
        // If you go through entire grid and find nothing, move index will be -1 still
    }

}
