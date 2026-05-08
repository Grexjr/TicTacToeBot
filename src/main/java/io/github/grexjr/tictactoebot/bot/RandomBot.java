package io.github.grexjr.tictactoebot.bot;

import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.Player;
import io.github.grexjr.tictactoebot.engine.StdIn;

import java.util.Random;

public class RandomBot extends Bot {

    public RandomBot(char symbol){
        super(symbol);
        this.name = "RandomBot";
    }

    @Override
    public int getInput(Board board, StdIn input, Player opponent, int turnNum){
        return doRandomBrain(board);
    }

    // ### THE BRAIN; random selection of un-occupied spaces
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
