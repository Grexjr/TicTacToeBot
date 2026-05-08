package io.github.grexjr.tictactoebot.engine;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.bot.HeuristicsBot;
import io.github.grexjr.tictactoebot.bot.RandomBot;

import java.util.Random;

public class Game {

    private Board gameBoard;
    private Player[] players;
    private StdIn input;

    boolean isGameOver;
    char whoWon;
    int turnNum;


    public Game(boolean debug){
        this.gameBoard = new Board();
        this.players = new Player[2];
        this.input = new StdIn();
        // Randomize who gets what symbol & what bot
        int rand = new Random().nextInt(0,4);

        // FOR NOW: just always heuristicsbot
        boolean player = new Random().nextBoolean();

        if(player){
            players[0] = new Player('X');
            players[1] = new HeuristicsBot('O');
        } else {
            players[1] = new Player('O');
            players[0] = new HeuristicsBot('X');
        }

        /*switch(rand){
            case 0 -> {
                players[0] = new Player('X');
                players[1] = new RandomBot('O');
                if(debug){
                    System.out.println("Random bot!");
                }
            }
            case 1 -> {
                players[1] = new Player('O');
                players[0] = new RandomBot('X');
                if(debug){
                    System.out.println("Random bot!");
                }
            }
            case 2 -> {
                players[0] = new Player('X');
                players[1] = new HeuristicsBot('O');
                if(debug){
                    System.out.println("Heuristics Bot!");
                }
            }
            case 3 -> {
                players[1] = new Player('O');
                players[0] = new HeuristicsBot('X');
                if(debug){
                    System.out.println("Heuristics Bot!");
                }
            }
        }*/

        isGameOver = false;
        whoWon = ' ';
        turnNum = 0;
    }

    // Mainly for testing to test bots against each other
    public Game(Player player1, Player player2){
        this(false);
        players[0] = player1;
        players[1] = player2;
    }

    /// Mainly for testing
    public Game(Board board){
        this(false);
        this.gameBoard = board;
    }

    public boolean isGameOver() { return isGameOver; }

    public int getTurnNum() { return turnNum; }

    public char getWhoWon() { return whoWon; }

    public void setGameBoard(Board board) { gameBoard = board; }

    /**
     * Checks winning conditions based on indices of chars
     * TODO: can definitely improve this in terms of efficiency and readability
     * TODO: Remove redundant loops, definitely find a more efficient way to check wins
     * TODO: Refactor, loops are hardcoded but just pretending to be loops
     * @return The winning character (blank space for default of no winner)
     */
    public char checkWin(){
        char[] grid = gameBoard.getRawGrid();
        // TODO: Can I combine these two for loops?
        // CHECK #1: ROWS
        // Checks every row (so every 3rd index, 0, 3, 6)
        for(int i = 0; i < grid.length; i += 3){
            // If blank space, exit loop early
            if(grid[i] == ' ') continue;
            // Checks its immediate neighbor; if not the same, continues on
            if(grid[i] != grid[i+1]) continue;
            // If that matches, checks if next neighbor is the same
            if(grid[i] != grid[i+2]) continue;
            // If both match, then return the character -- we have a winner!
            return grid[i];
        }
        // CHECK #2: COLUMNS
        // Checks every column (so every index up to 3)
        for(int i = 0; i < 3; i++){
            // If blank space, exit loop early
            if(grid[i] == ' ') continue;
            // Checks neighbor (i + 3)
            // If no match, continue to next check
            if(grid[i] != grid[i+3]) continue;
            // If no match on one next down, then continue to next check
            if(grid[i] != grid[i+6]) continue;
            // If both match, return character -- we have a winner!
            return grid[i];
        }
        // CHECK #3: DIAGONALS
        // Only need to check the two corners, and for both it's the same; index 4 is center of grid
        for(int i = 0; i < grid.length; i += 6){
            // If blank space, exit loop early
            if(grid[i] == ' ') continue;
            // First check, which both look at: i = 4; if no match, continue on
            if(grid[i] != grid[4]) continue;
            // First, the 0 check, which checks bottom right
            if(i == 0){
                // If not a match, continue on
                if(grid[i] != grid[8]) continue;
                // If a match, return character -- we have a winner!
                return grid[i];
            }
            // Next, the 6 check, which checks top right
            if(i == 6){
                // If not a match, continue on
                if(grid[i] != grid[2]) continue;
                // If a match, return character -- we have a winner!
                return grid[i];
            }
        }

        boolean hasEmptySpace = false;

        // Check if there are any unfilled spaces
        for (char c : grid) {
            // If a space is found, we break and return default value
            if (c == ' ') {
                hasEmptySpace = true;
                break;
            }
        }

        if(hasEmptySpace) return ' ';

        // The non-win, non-draw case: blank space return
        return 'u';

        //TODO: Maybe instead of caller deciding, we have a check here if the grid space is a blank space
    }

    /**
     * Runs the game.
     * @param isSilent True for print statements, false for none. Useful for testing.
     */
    public void runGame(boolean isSilent){

        while(!isGameOver){

            for(Player p : players){
                Player opponent = null;
                if(p == players[0]) opponent = players[1];
                if(p == players[1]) opponent = players[0];

                if(!isSilent){
                    gameBoard.printGrid();
                    System.out.println();
                    System.out.println(p.getSymbol() + "'s turn!");
                }

                do {
                    if(p instanceof Bot) continue;
                    System.out.print("Input a square from 1-9 (numbered from top left) -> ");
                } while (!p.playTurn(gameBoard, p.getInput(gameBoard, input, opponent,turnNum) - 1));

                turnNum++;

                whoWon = checkWin();
                if(whoWon != 'u' && whoWon != ' '){
                    if(!isSilent){
                        System.out.println(whoWon + " has won!");
                        gameBoard.printGrid();
                    }
                    isGameOver = true;
                    break;
                }
                if (whoWon == 'u'){
                    if(!isSilent){
                        System.out.println("Game is a draw!");
                        gameBoard.printGrid();
                    }
                    isGameOver = true;
                    break;
                } // else do nothing and continue
            }
        }
    }

}
