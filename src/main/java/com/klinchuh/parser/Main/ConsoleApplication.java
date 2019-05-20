package com.klinchuh.parser.Main;

import com.klinchuh.parser.Parse.*;

import java.io.*;

public class ConsoleApplication {

    public static void main(String[] args) {
        ConsoleApplication app = new ConsoleApplication();

        Parser parser = new Parser();

        if(args.length == 0) {
            System.out.println("No file path");
            return;
        }

        File outFile, inFile;

        inFile = new File(args[0]);

        if(args.length < 2) {
            outFile = new File("out.json");
        }  else {
            outFile = new File(args[1]);
        }

        System.out.println("Read from \"" + inFile.getAbsolutePath() + "\" and parse to \"" + outFile.getAbsolutePath() + "\"");

        try {
            parser.get(new FileInputStream(inFile), new FileOutputStream(outFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
