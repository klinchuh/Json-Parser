package Main;

import Parse.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleApplication {

    public static void main(String[] args) {
        ConsoleApplication app = new ConsoleApplication();

        Parser parser = new Parser();

        if(args.length == 0) {
            System.out.println("No file path");
            return;
        }

        try {
            parser.get(ConsoleApplication.class.getClassLoader().getResourceAsStream(args[0])
                    , new FileOutputStream(new File("out.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
