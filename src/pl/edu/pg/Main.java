package pl.edu.pg;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static java.lang.Class.forName;

public class Main {

    static Metric readMetrics() {

        String metricsString = readFile("metryka.ini");
        System.out.println(metricsString);
        Metric metric = new Metric(metricsString);

        return metric;
    }

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

        String folderName = "results";
        String fileName = String.valueOf(folderName + "/" +System.currentTimeMillis()) + "_" + "wynik.txt";

        new File(folderName).mkdirs();

        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Metric metric = readMetrics();
        System.out.println(metric.get("A", "T"));
        String sekwencjaA = readFile("sekwencjaA.txt");
        saveFile(sekwencjaA);


    }
}
