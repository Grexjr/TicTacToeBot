package io.github.grexjr.tictactoebot;

import io.github.grexjr.tictactoebot.bot.Bot;
import io.github.grexjr.tictactoebot.engine.Game;

public class TestHelper {

    protected static double runVersusTest(Bot challenger, Bot defender, int totalGames, String logFileName,
                                        String testHeader){
        // Variables for wins and game amounts and expected values
        int challengerXWins = 0;
        int challengerOWins = 0;
        int defenderXWins = 0;
        int defenderOWins = 0;
        int draws = 0;

        for(int iteration = 0; iteration < totalGames; iteration++){
            if(iteration <= totalGames/2){
                challenger.setSymbol('x');
                defender.setSymbol('o');
                char winner = runTestGame(challenger, defender);
                if(winner == challenger.getSymbol()) challengerXWins++;
                if(winner == defender.getSymbol()) defenderOWins++;
                if(winner == 'u') draws++;
            } else {
                defender.setSymbol('x');
                challenger.setSymbol('o');
                char winner = runTestGame(defender, challenger);
                if(winner == defender.getSymbol()) defenderXWins++;
                if(winner == challenger.getSymbol()) challengerOWins++;
                if(winner == 'u') draws++;
            }
        }

        int totalChallengerWins = challengerXWins + challengerOWins;
        int challengerLosses = defenderXWins + defenderOWins;

        double winPercent = ((double) totalChallengerWins / totalGames) * 100;
        double drawPercent = ((double) draws / totalGames) * 100;

        // Print results
        printTestResults(testHeader,totalGames,challengerXWins,challengerOWins,challengerLosses,winPercent,
                drawPercent,challenger);

        // Write results to file
        logTestResults(logFileName, testHeader, totalChallengerWins,totalGames,winPercent,challenger);

        // Returns win percent to test conditions
        return winPercent;
    }

    protected static void printTestResults(String header, int totalGames, int challengerXWins,
                                  int challengerOWins, int challengerLosses, double winPercent, double drawPercent,
                                  Bot challenger){
        // Display test information
        System.out.println(header);
        System.out.println("Total games played: " + totalGames);
        System.out.println(challenger.getName() + " wins: " + (challengerXWins + challengerOWins));
        System.out.println("As x: " + challengerXWins + " | " + "As o: " + challengerOWins);
        System.out.println("Draws: " + (totalGames * (drawPercent / 100)));
        System.out.println(challenger.getName() + " losses: " + challengerLosses);
        System.out.println(challenger.getName() + " win rate: " + winPercent);

        // Create a visual bar
        int barSections = 100; // 100 bars
        int winBars = (int) (barSections * (winPercent/100));
        int drawBars = (int) (barSections * (drawPercent/100));

        System.out.print("[");
        for(int i = 0; i < barSections; i++){
            if(i < winBars) System.out.print("\u001B[32m" + "-" + "\u001B[0m");
            else if(i < (drawBars + winBars)) System.out.print("\u001B[37m" + "-" + "\u001B[0m");
            else System.out.print("\u001B[31m" + "-" + "\u001B[0m");
        }
        System.out.println("]");
    }

    protected static void logTestResults(String fileName, String header, int challengerWins, int totalGames,
                                       double winPercent, Bot challenger){
        TestLogger.logResult(fileName, "\n");
        TestLogger.logResult(fileName,header);
        TestLogger.logResult(fileName,"Total games played: " + totalGames);
        TestLogger.logResult(fileName, challenger.getName() + " wins: " + challengerWins);
        TestLogger.logResult(fileName, challenger.getName() + " win rate: " + winPercent);
    }

    protected static char runTestGame(Bot challenger, Bot defender){
        Game game = new Game(challenger,defender);
        // Runs a silent, printless game
        game.runGame(true);

        return game.getWhoWon();
    }
}
