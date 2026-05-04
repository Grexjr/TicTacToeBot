package io.github.grexjr.tictactoebot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class TestLogger {

    ///  Logs test results as a txt file to the results/ directory
    public static void logResult(String fileName, String message){
        try {
            Path outDir = Paths.get("results");

            if(Files.notExists(outDir)){
                Files.createDirectories(outDir);
            }

            Path reportFile = outDir.resolve(fileName + ".txt");
            String timeStamp = LocalDateTime.now().toString();
            Files.writeString(reportFile,"[" + timeStamp + "]: " + message + "\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e){
            System.err.println("UNABLE TO LOG TEST RESULTS");
            e.printStackTrace();
        }

    }


}
