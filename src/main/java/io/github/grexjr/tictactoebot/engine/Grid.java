package io.github.grexjr.tictactoebot.engine;

public class Grid {

    private char[][] grid;

    // Initializes an empty tic-tac-toe grid
    public Grid(){
        // The basic grid of Xs and Os, initialized to blank
        grid = new char[][]{
                {' ',' ',' '},
                {' ',' ',' '},
                {' ',' ',' '}
        };
    }

    ///  Mainly for testing
    public Grid(char[][] board){
        grid = board;
    }

    public char[][] getRawGrid(){
        return grid;
    }

    /// Prints the 3x3 (or any arbitrary length) grid to the screen
    public void printGrid(){
        for(int k = 0; k < 3; k++){
            printHeader(k+1,true);
        }
        System.out.println();
        for(int i = 0; i < grid.length; i++){
            printHeader(i+1,false);
            for(int j = 0; j < grid[i].length; j++){
                printCell(i,j);
            }
            System.out.println();
        }
    }

    /// Changes cell to a given value; returns true if successful, false if not
    public boolean changeCell(int i, int j, char value){
        // Check if the values are in the grid
        if(!((i < grid.length) && (i >= 0))) return false;
        if(!((j < grid[0].length) && (j >= 0))) return false;

        if(grid[i][j] == ' '){
            grid[i][j] = value;
            return true;
        } else {
            return false;
        }
    }

    ///  Resets grid to blank
    public void resetGrid(){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                changeCell(i,j,' ');
            }
        }
    }

    ///  Prints an individual cell's value encased in brackets
    private void printCell(int i, int j){
        System.out.print("[ " + grid[i][j] + " ]");
    }

    ///  Prints row headers | TODO: Make better!
    private void printHeader(int label, boolean horizontal){
        if(horizontal){
            System.out.print("   " + label + "  ");
        } else {
            System.out.print(label + " ");
        }
    }


}
