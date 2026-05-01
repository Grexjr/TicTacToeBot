package io.github.grexjr.tictactoebot.bot;

import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.Player;
import io.github.grexjr.tictactoebot.engine.StdIn;

import java.util.Random;

public class Bot extends Player {

    // TODO: Variable for brain type (random, heuristic, mini-max)

    public Bot(char symbol){
        super(symbol);
    }

    // #### THE BRAINS
    // Random selection
    @Override
    public int getInput(Board board, StdIn input){
        return doRandomBrain(board);
    }

    private int doRandomBrain(Board board){
        Random rand = new Random();
        // Random 1-9, if not blank then re rolls
        int random = rand.nextInt(0,9);
        while(board.getRawGrid()[random] != ' '){
            random = rand.nextInt(0,9);
        }
        return random + 1;
    }


}
