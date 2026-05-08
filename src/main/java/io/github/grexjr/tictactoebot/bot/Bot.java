package io.github.grexjr.tictactoebot.bot;

import io.github.grexjr.tictactoebot.engine.Board;
import io.github.grexjr.tictactoebot.engine.Player;
import io.github.grexjr.tictactoebot.engine.StdIn;

import java.util.Random;

public abstract class Bot extends Player {

    public Bot(char symbol){
        super(symbol);
    }

}
