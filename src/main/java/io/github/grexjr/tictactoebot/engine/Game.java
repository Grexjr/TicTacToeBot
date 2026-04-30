package io.github.grexjr.tictactoebot.engine;

public class Game {

    private Grid gameGrid;
    private Player[] players;
    private StdIn input;

    boolean isGameOver;
    char whoWon;


    public Game(){
        this.gameGrid = new Grid();
        this.players = new Player[2];
        this.input = new StdIn();
        players[0] = new Player('X');
        players[1] = new Player('O');

        isGameOver = false;
        whoWon = ' ';
    }

    /// Mainly for testing
    public Game(Grid grid){
        this();
        this.gameGrid = grid;
    }

    //TODO: refactor this to make it cleaner
    public void runGame(){
        gameGrid.printGrid();
        while(!isGameOver){
            for(Player player : players){
                System.out.println(player.getSymbol() +"'s turn!");

                System.out.print("Input a row -> ");
                // Minus one so grid is 1-indexed
                int row = input.readInt() - 1;
                System.out.print("Input a column -> ");
                // Minus one so grid is 1-indexed
                int col = input.readInt() - 1;

                while(!player.playTurn(gameGrid,row,col)){
                    System.out.println("Invalid move!");
                    System.out.print("Input a row -> ");
                    // Minus one so grid is 1-indexed
                    row = input.readInt() - 1;
                    System.out.print("Input a column -> ");
                    // Minus one so grid is 1-indexed
                    col = input.readInt() - 1;
                }

                System.out.println();
                System.out.println("Player put " + player.getSymbol() + " at " + (row + 1) + "," + (col + 1));

                gameGrid.printGrid();

                char winner = checkWin();

                if(checkWin() != ' '){
                    System.out.println(winner + " has won!");
                    isGameOver = true;
                    break;
                }
            }
        }
    }

    /// Returns space if no winner, or if there are 3 spaces in a row somewhere; thus, need to use this always in a
    /// conditional that checks if the result returns 'x', 'o', or ' '. If the last, then don't do anything with it.
    public char checkWin(){
        // Check rows
        for(int i = 0; i < gameGrid.getRawGrid().length; i++){
            if(gameGrid.getRawGrid()[i][0] == gameGrid.getRawGrid()[i][1]
                    && gameGrid.getRawGrid()[i][1] == gameGrid.getRawGrid()[i][2]){
                return gameGrid.getRawGrid()[i][0];
            }
        }
        // Check columns
        for(int j = 0; j < gameGrid.getRawGrid()[0].length; j++){
            if(gameGrid.getRawGrid()[0][j] == gameGrid.getRawGrid()[1][j]
                    && gameGrid.getRawGrid()[1][j] == gameGrid.getRawGrid()[2][j]){
                return gameGrid.getRawGrid()[0][j];
            }
        }
        // Check descending diagonal
        if(gameGrid.getRawGrid()[0][0] == gameGrid.getRawGrid()[1][1]
                && gameGrid.getRawGrid()[1][1] == gameGrid.getRawGrid()[2][2]){
            return gameGrid.getRawGrid()[0][0];
        }
        // Check ascending diagonal
        if(gameGrid.getRawGrid()[2][0] == gameGrid.getRawGrid()[1][1]
                && gameGrid.getRawGrid()[1][1] == gameGrid.getRawGrid()[0][2]){
            return gameGrid.getRawGrid()[2][0];
        }
        // Return space to show no character has yet won
        return ' ';
    }





}
