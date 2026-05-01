package io.github.grexjr.tictactoebot.engine;

public class Player {

    private final char symbol;
    private boolean isTurn;

    public Player(char symbol){
        this.symbol = symbol;
        this.isTurn = false;
    }

    public char getSymbol() { return symbol; }

    public boolean isTurn() { return isTurn; }

    public void setTurn(boolean value) { this.isTurn = value; }

    /// returns true on successful turn, false on unsuccessful turn
    public boolean playTurn(Board board, int index) {
        // References
        char[] grid = board.getRawGrid();

        // CHECK #1: out of bounds check, if out of bounds return false and prompt for new input
        if(index > grid.length || index < 0) {
            System.out.println("Input a number between 1-9!");
            return false;
        }

        // CHECK #2: occupied check, if not empty square return false and prompt for new input
        if(grid[index] != ' '){
            System.out.println("Space is occupied!");
            return false;
        }

        // If validation successful, then change square and return true for successful player turn
        board.changeSquare(index,symbol);
        return true;
    }

}
