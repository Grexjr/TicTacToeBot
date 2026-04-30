package io.github.grexjr.tictactoebot.engine;

import java.util.Scanner;

public class StdIn {

    private Scanner in;

    public StdIn(){
        in = new Scanner(System.in);
    }

    public String readLine(){
        return in.nextLine();
    }

    public int readInt(){
        while(true){
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e){
                System.out.println("Input must be an integer!");
            }
        }
    }


}
