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
    public boolean playTurn(Grid grid, int row, int col){
        return grid.changeCell(row,col,symbol);
    }

}
