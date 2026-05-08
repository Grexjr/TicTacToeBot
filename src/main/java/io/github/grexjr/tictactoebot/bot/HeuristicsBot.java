package io.github.grexjr.tictactoebot.bot;

import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.Player;
import io.github.grexjr.tictactoebot.engine.StdIn;

import java.util.Random;

public class HeuristicsBot extends Bot {

    // All rows, diagonals, columns add to 15
    // Use this for getting the values from board indices
    private final static int[] magic_square = new int[]{2,7,6,9,5,1,4,3,8};
    // Size 10 because indexed 1-9, no zero-value squares so indices are 1-9
    private final static int[] ms = new int[10];

    private int moveIndex;

    // ### A bot that uses heuristics to try and win (or draw) the game of tic-tac-toe
    public HeuristicsBot(char symbol){
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
        return doHeuristicsBrain(board,opponent,turnNum);
    }

    // ### THE BRAIN: run certain rules
    private int doHeuristicsBrain(Board board, Player opponent, int turnNum){
        // Set this back at the start
        moveIndex = -1;

        if(turnNum < 2){
            makeStartingMove(board, opponent.getSymbol());
            return moveIndex + 1;
        }

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
        if(moveIndex == -1){
            maximizeWinningLines(grid, opponent.getSymbol());
        }

        // PRIORITY 4: BLOCK ENEMY WINNING LANES
        if(moveIndex == -1){
            blockEnemyWinningLines(grid, opponent.getSymbol());
        }

        // PRIORITY 5: START MOVES
        if(moveIndex == -1){
            makeStartingMove(board, opponent.getSymbol());
        }

        // ELSE: Do a random move
        if(moveIndex == -1){// Otherwise (for now) return a random value
            Random rand = new Random();
            // Random 1-9, if not blank then re rolls
            int random = rand.nextInt(0, 9);
            while (board.getRawGrid()[random] != ' ') {
                random = rand.nextInt(0, 9);
            }

            moveIndex = random;
        }

        return moveIndex + 1;
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

                // Check if un-occupied, if not, then move to next iteration
                if (grid[ms[comparison]] != ' ') continue;

                // Comparison is our magic square value we are looking for. Get the corresponding index from the ms
                // array
                moveIndex = ms[comparison];

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

                // Check if un-occupied, if not, then move to next iteration
                if (grid[ms[comparison]] != ' ') continue;

                // Comparison is our magic square value we are looking for. Get the corresponding index from the ms
                // array
                moveIndex = ms[comparison];

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

            // If occupied, continue on
            if(grid[i] != ' ') continue;

            // Otherwise imagine what happens if you put symbol there

            // Check which row & column you are in and if on diagonal
            /* ROW CHECK:
            Can just use modulus!
            >if i < 3 (row 0), 0 - 0, 1 - 1, 2 - 2
            >if i < 6 (row 3), 3 - 0, 4 - 1, 5 - 2
            >if i < 8 (row 6), 6 - 0, 7 - 1, 8 - 2
            */
            int rowStart = i - (i % 3);
            // COLUMN CHECK:
            // Can just use modulus! since rows are +3; 0, 3, 6; 1, 4, 7; 2, 5, 8
            int colStart = i % 3;
            // DIAGONAL CHECK:
            // on the evens!!!! mod 2!!!!
            boolean isOnDiagonal = i % 2 == 0;

            // Check row
            if(checkNeighbors(grid,opponentSymbol,rowStart,rowStart+2,1)) tempWinningLines++;

            // Check column
            if(checkNeighbors(grid,opponentSymbol,colStart,colStart+6,3)) tempWinningLines++;

            // Check diagonals if it is on the diagonal
            if(isOnDiagonal){
                // Do first diagonal (Ascending)
                if(getDiagonalStartIndex(i) == -1 || getDiagonalStartIndex(i) == 0){
                    if(checkNeighbors(grid,opponentSymbol,0,8,4)) tempWinningLines++;
                }

                // Do descending diagonal
                if(getDiagonalStartIndex(i) == -1 || getDiagonalStartIndex(i) == 2){
                    if(checkNeighbors(grid,opponentSymbol,2,6,2)) tempWinningLines++;
                }
            }

            // Otherwise just continue on

            // Then check if temp winning lines from this square are greater than current max, if so replace winning
            // max and change move index to this square
            if(tempWinningLines > mostWinningLines){
                mostWinningLines = tempWinningLines;
                moveIndex = i;
            }
        }
        // If you go through entire grid and find nothing, move index will be -1 still
    }

    private boolean checkNeighbors(char[] grid, char opponent, int start, int end, int additive){
        int neighbors = 0;
        int enemies = 0;

        // Search through neighbors
        // <= so we check the last index too
        for(int i = start; i <= end; i += additive){
            if(grid[i] == getSymbol()) neighbors++;
            else if(grid[i] == opponent) enemies++;
        }

        // If there is one piece there, means this hypothetical would cause winning lanes
        return neighbors == 1 && enemies == 0;
    }

    private int getDiagonalStartIndex(int i){
        int diagonalStart = -1;

        // Check which diagonal it is on
        if (i % 4 == 0) {
            // If not center and mod 4 == 0, then on descending diagonal, so start at 0
            if(i != 4) {
                diagonalStart = 0;
            }
        }
        // Otherwise, start at 2, on ascending diagonal
        else { // i % 4 = 2
            diagonalStart = 2;
        }

        // If both, (i == 4) return -1
        return diagonalStart;
    }

    // Method to block enemy winning lanes if they exist
    private void blockEnemyWinningLines(char[] grid, char opponentSymbol){
        // Set move index to blank (shouldn't matter because functions before it will change it)
        moveIndex = -1;

        // Variable for how many winning lines pass through square
        int mostWinningLines = 0;

        // Loop through all the array
        for(int i = 0; i < grid.length; i++){
            // Temporary variable for winning lines of specific square
            int tempWinningLines = 0;

            // If occupied, continue on
            if(grid[i] != ' ') continue;

            // Otherwise imagine what happens if you put symbol there

            // Check which row & column you are in and if on diagonal
            /* ROW CHECK:
            Can just use modulus!
            >if i < 3 (row 0), 0 - 0, 1 - 1, 2 - 2
            >if i < 6 (row 3), 3 - 0, 4 - 1, 5 - 2
            >if i < 8 (row 6), 6 - 0, 7 - 1, 8 - 2
            */
            int rowStart = i - (i % 3);
            // COLUMN CHECK:
            // Can just use modulus! since rows are +3; 0, 3, 6; 1, 4, 7; 2, 5, 8
            int colStart = i % 3;
            // DIAGONAL CHECK:
            // on the evens!!!! mod 2!!!!
            boolean isOnDiagonal = i % 2 == 0;

            // Check row
            if(checkNeighborsForEnemy(grid,opponentSymbol,rowStart,rowStart+2,1)) tempWinningLines++;

            // Check column
            if(checkNeighborsForEnemy(grid,opponentSymbol,colStart,colStart+6,3)) tempWinningLines++;

            // Check diagonals if it is on the diagonal
            if(isOnDiagonal){
                // Do first diagonal (Ascending)
                if(getDiagonalStartIndex(i) == -1 || getDiagonalStartIndex(i) == 0){
                    if(checkNeighborsForEnemy(grid,opponentSymbol,0,8,4)) tempWinningLines++;
                }

                // Do descending diagonal
                if(getDiagonalStartIndex(i) == -1 || getDiagonalStartIndex(i) == 2){
                    if(checkNeighborsForEnemy(grid,opponentSymbol,2,6,2)) tempWinningLines++;
                }
            }

            // Otherwise just continue on

            // Then check if temp winning lines from this square are greater than current max, if so replace winning
            // max and change move index to this square
            if(tempWinningLines > mostWinningLines){
                mostWinningLines = tempWinningLines;
                moveIndex = i;
            }
        }
        // If you go through entire grid and find nothing, move index will be -1 still
    }

    private boolean checkNeighborsForEnemy(char[] grid, char opponent, int start, int end, int additive){
        int neighbors = 0;
        int enemies = 0;

        // Search through neighbors
        // <= so we check the last index too
        for(int i = start; i <= end; i += additive){
            if(grid[i] == getSymbol()) neighbors++;
            else if(grid[i] == opponent) enemies++;
        }

        // If there is one piece there, means this hypothetical would cause winning lanes
        return neighbors == 0 && enemies == 1;
    }

    // Final heuristic: make the smartest starting move
    // If they move first...
        // If they take center, you take corner
        // If they take corner, you take center
        // If they take side, you take center
    // If you move first...
        // Take a corner
    private void makeStartingMove(Board board, char opponentSymbol){
        // array of corner indices for easy access
        int[] corners = new int[]{0,2,6,8};
        char[] grid = board.getRawGrid();

        // If starting first (board is empty), take a corner
        // Random for taking a random coner when needed
        int rand = new Random().nextInt(0,4);

        // If move first, take a random corner
        if(board.isEmpty()) {
            // Check if corner is empty
            if(grid[corners[rand]] != ' ') return;
            moveIndex = corners[rand];
            // Early return
            return;
        }

        // If board is not empty, check center square
        // If it equals the opponent's piece, take a corner
        if(grid[4] == opponentSymbol){
            // Check if corner is empty
            if(grid[corners[rand]] != ' ') return;
            moveIndex = corners[rand];
            // Early return
            return;
        }

        // ELSE: If they take corner or side, take center
        // Check if center is empty
        if(grid[4] != ' ') return;
        moveIndex = 4;
    }

}
