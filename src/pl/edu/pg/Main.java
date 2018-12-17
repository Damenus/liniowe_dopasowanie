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
        odlegloscEdycyjna(metric);


    }

    static int odlegloscEdycyjna(Metric metric) {

        String sekwencjaA = "ACG"; // i u
        String sekwencjaB = "ACA"; // j w

        int liczbaLiterA = sekwencjaA.length();
        int liczbaLiterB = sekwencjaB.length();

        int[][] tablica = new int[liczbaLiterA][liczbaLiterB];

        // 1. Brzeg tabeli
        tablica[0][0] = 0;

        for(int i = 1; i < liczbaLiterA; i++) {
            for(int k = 0; k < i; k++) {
                tablica[i][0] += metric.get(sekwencjaA.charAt(k), '-');
            }
        }

        for(int j = 1; j < liczbaLiterB; j++) {
            for(int k = 0; k < j; k++) {
                tablica[0][j] += metric.get(sekwencjaA.charAt(k), '-');
            }
        }

        // 2. Środek tabeli
        for (int i = 1; i < liczbaLiterA; i++)
            for (int j = 1; j < liczbaLiterB; j++)
                tablica[i][j] = Math.min(
                        tablica[i-1][j-1]+metric.get(sekwencjaA.charAt(i),sekwencjaB.charAt(j)),
                        Math.min(
                                tablica[i][j-1]+metric.get('-',sekwencjaB.charAt(j)),
                                tablica[i-1][j]+metric.get(sekwencjaA.charAt(i),'-')
                        )
                );

        for (int i = 0; i < liczbaLiterA; i++) {
            for (int j = 0; j < liczbaLiterB; j++) {
                System.out.print(tablica[i][j] + "   ");
            }
            System.out.println();
        }

        return tablica[liczbaLiterA-1][liczbaLiterB-1];
    }
}
