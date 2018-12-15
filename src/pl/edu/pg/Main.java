package pl.edu.pg;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Class.forName;

public class Main {

    static String readFile(String path) {
        byte[] encoded = new byte[0];
        Charset encoding = Charset.defaultCharset(); //StandardCharsets.UTF_8

        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, encoding);
    }

    static void saveFile(String data) {

        String fileName = String.valueOf("results/" +System.currentTimeMillis()) + "_" + "wynik.txt";

        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String sekwencjaA = readFile("sekwencjaA.txt");
        saveFile(sekwencjaA);
    }
}
