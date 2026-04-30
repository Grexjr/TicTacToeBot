package io.github.grexjr.tictactoebot;

import io.github.grexjr.tictactoebot.engine.Game;
import io.github.grexjr.tictactoebot.engine.Grid;
import io.github.grexjr.tictactoebot.engine.StdIn;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testCreateGrid(){
        Grid testGrid = new Grid();

        for(int i = 0; i < testGrid.getRawGrid().length; i++){
            for(int j = 0; j < testGrid.getRawGrid()[i].length; j++){
                assertEquals(' ',testGrid.getRawGrid()[i][j]);
            }
        }
    }

    @Test
    public void testChangingGrid(){
        Grid testGrid = new Grid();
        testGrid.changeCell(1,1,'x');

        assertEquals('x',testGrid.getRawGrid()[1][1]);
        testGrid.printGrid();
    }

    @Test
    public void testCheckWin(){
        Grid scenario1 = new Grid(
                new char[][]{
                        {'x','x','x'},
                        {'o',' ','o'},
                        {'o',' ',' '}
                }
        );
        Grid scenario2 = new Grid(
                new char[][]{
                        {' ','o','x'},
                        {' ',' ','x'},
                        {' ','o','x'}
                }
        );
        Grid scenario3 = new Grid(
                new char[][]{
                        {'x','o','x'},
                        {'o','x','o'},
                        {' ','o','x'}
                }
        );
        Grid scenario4 = new Grid(
                new char[][]{
                        {'x','o','o'},
                        {'x','o','x'},
                        {'o','o','x'}
                }
        );
        Grid scenario5 = new Grid(
                new char[][]{
                        {'x','o','x'},
                        {'o','x','o'},
                        {'o','x','o'}
                }
        );

        Game g1 = new Game(scenario1);
        Game g2 = new Game(scenario2);
        Game g3 = new Game(scenario3);
        Game g4 = new Game(scenario4);
        Game g5 = new Game(scenario5);

        assertEquals('x',g1.checkWin());
        assertEquals('x',g2.checkWin());
        assertEquals('x',g3.checkWin());
        assertEquals('o',g4.checkWin());
        assertEquals(' ',g5.checkWin());
    }

}
