package io.github.grexjr.tictactoebot;

import io.github.grexjr.tictactoebot.engine.Game;
import io.github.grexjr.tictactoebot.engine.Board;
import org.junit.jupiter.api.*;

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

}
