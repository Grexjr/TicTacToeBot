package io.github.grexjr.tictactoebot.engine;

import java.util.Arrays;

public class Board {

    private char[] grid;

    // Initializes an empty tic-tac-toe grid
    public Board(){
        // The basic grid of Xs and Os
        grid = new char[9];
        // Initialized to be blank (otherwise would print the missing character symbol
        blankGrid(grid);
    }

    ///  Mainly for testing
    public Board(char[] board){
        grid = board;
    }

    public char[] getRawGrid(){
        return grid;
    }

    /**
     * <p>Prints the 3x3 board to the screen.</p>
     * <p>~(3 + 9) = ~12 => O(1) runtime [known array length ahead of time].</p>
     */
    public void printGrid(){
        int row = 0;
        // Print horizontal labels
        for(int i = 1; i < 4; i++){
            System.out.print("    " + i + " "); // TODO: make this better
        }
        for(int i = 0; i < grid.length; i++){
            if(i % 3 == 0){
                System.out.println();
                row++;
                System.out.print(row + "  ");
            }
            printSquare(i);
        }
    }


    /**
     * Changes a square to a given character value.
     * @param i The index to change.
     * @param value The character to change that square to.
     */
    public void changeSquare(int i, char value) {
        grid[i]= value;
    }

    /**
     * Makes the grid parameter filled with blank spaces.
     * Uses Arrays.fill(), which is identical to:
     * <pre>{@code
     *      for(int i = 0; i < grid.length; i++){
     *          grid[i] = ' ';
     *      }
     * }</pre>
     * @param grid The grid to make blank
     */
    public void blankGrid(char[] grid){
        Arrays.fill(grid,' ');
    }

    ///  Prints an individual cell's value encased in brackets
    private void printSquare(int i){
        System.out.print("[ " + grid[i] + " ]");
    }

}
