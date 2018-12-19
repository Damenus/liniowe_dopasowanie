package pl.edu.pg;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static java.lang.Class.forName;

public class Main {

    static Metric metric;

    static Metric readMetrics() {

        String metricsString = readFile("metryka.ini");
        System.out.println(metricsString);
        metric = new Metric(metricsString);

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
        String sekwencjaB = readFile("sekwencjaB.txt");
        saveFile(sekwencjaA);
        odlegloscEdycyjna(metric);


        NeedlemanWunsch(sekwencjaA,sekwencjaB);
        String A,B;
        String[] result;
        result = Hirschberg(sekwencjaA, sekwencjaB);
        A = result[0];
        B = result[1];
        System.out.println(A);
        System.out.println(B);
        saveFile(A + ":" + B);

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

    static String[] NeedlemanWunsch(String X, String Y) {
        int scoreSub, scoreDel, scoreIns;

        //Metric metric = readMetrics();
        int[][] Score = new int[2][Y.length()]; // 2*length(Y) array

        for (int j = 1; j < Y.length(); j++) {
            Score[0][j] = Score[0][j-1] + metric.insert(Y.charAt(j-1));
        }

        for (int i = 1; i < X.length(); i++) {
            Score[1][0] = Score[0][0] + metric.delete(X.charAt(i-1));
            for (int j = 1; j < Y.length(); j++) {
                scoreSub = Score[0][j-1] + metric.get(X.charAt(i-1),Y.charAt(j-1));
                scoreDel = Score[0][j] + metric.delete(X.charAt(i-1));
                scoreIns = Score[1][j-1] + metric.insert(Y.charAt(j-1));
                Score[1][j] = Math.max(scoreSub, Math.max(scoreDel, scoreIns));
                //if (Score[1][j] == scoreSub){
                    //NIC NIE MUSIMY WSTAWIAC
                //}
                if (Score[1][j] == scoreDel){
                    Y = Y.substring(0,j) + "-" + Y.substring(j);
                }
                if (Score[1][j] == scoreIns){
                    X = X.substring(0,i) + "-" + X.substring(i);
                }
            }
            //copy Score[1] to Score[0]
            Score[0] = Score[1];
        }
        int[] LastLine = new int[Y.length()];
        for (int j = 0; j < Y.length(); j++) {
            LastLine[j] = Score[1][j];
        }
        String[] ret = {X,Y};
        return ret;

    }

    //A-T
    //AGT



    static int[] NWScore(String X, String Y) {
        int scoreSub, scoreDel, scoreIns;

        //Metric metric = readMetrics();
        int[][] Score = new int[2][Y.length()]; // 2*length(Y) array

        for (int j = 1; j < Y.length(); j++) {
            Score[0][j] = Score[0][j-1] + metric.insert(Y.charAt(j-1));
        }

        for (int i = 1; i < X.length(); i++) {
            Score[1][0] = Score[0][0] + metric.delete(X.charAt(i-1));
            for (int j = 1; j < Y.length(); j++) {
                scoreSub = Score[0][j-1] + metric.get(X.charAt(i-1),Y.charAt(j-1));
                scoreDel = Score[0][j] + metric.delete(X.charAt(i-1));
                scoreIns = Score[1][j-1] + metric.insert(Y.charAt(j-1));
                Score[1][j] = Math.max(scoreSub, Math.max(scoreDel, scoreIns));

            }
            //copy Score[1] to Score[0]
            Score[0] = Score[1];
        }
        int[] LastLine = new int[Y.length()];
        for (int j = 0; j < Y.length(); j++) {
            LastLine[j] = Score[1][j];
        }
        return LastLine;

    }

    static String[] Hirschberg(String X, String Y) {

        String Z = "";
        String W = "";

        int xlen,xmid,ylen,ymid;

        int[] ScoreL,ScoreR;

        if (X.length() == 0) {
            for (int i = 1; i < Y.length(); i++) {
                Z = Z + '-';
                W = W + Y.charAt(i);
            }
        }
        else if (Y.length() == 0) {
            for (int i = 1; i < X.length(); i++) {
                Z = Z + X.charAt(i);
                W = W + '-';
            }
        }
        else if (X.length() == 1 || Y.length() == 1) {
            String[] A;
            A = NeedlemanWunsch(X,Y);
            //A = Hirschberg(X,Y);
            Z = Z + A[0];
            W = W + A[1];
        }
        else {
            xlen = X.length();
            xmid =  (int) X.length()/2; //X.length()%2==0 ? X.length()/2-1 : X.length()/2+1;
            ylen = Y.length();

            ScoreL = NWScore(X.substring(0,xmid), Y);
            ScoreR = NWScore(rev(X.substring(xmid+1,xlen)), rev(Y));
            //ymid = max(concatenate(ScoreL, rev(ScoreR)));
            ymid = partition(ScoreL, rev(ScoreR));

            String[] A, B;
            A = Hirschberg(X.substring(0,xmid), Y.substring(0,ymid));
            B = Hirschberg(X.substring(xmid+1,xlen), Y.substring(ymid+1,ylen));
            Z = A[0] + B[0];
            W = A[1] + B[1];
        }

        String[] A = {Z, W};
        return A;
    }

    static String rev(String a) {
        return new StringBuffer(a).reverse().toString();
    }

    static int[] rev(int[] a) {
        for(int i = 0; i < a.length / 2; i++)
        {
            int temp = a[i];
            a[i] = a[a.length - i - 1];
            a[a.length - i - 1] = temp;
        }
        return a;
    }

    static int[] concatenate(int[] a, int[] b) {
        int aLen = a.length;
        int bLen = b.length;

        int[] c = (int[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    static int max(int[] a) {
        int max = -10000;

        for (int i = 0; i < a.length; i++) {
            if (a[i] > max)
                max = a[i];
        }

        return max;
    }

    private static int partition(int[] scoreL, int[] scoreR) {
        int maxSum = Integer.MIN_VALUE;
        int index = 0;
        for(int iii = 0; iii < scoreL.length; iii++) {
            int sum = scoreL[iii] + scoreR[scoreL.length - iii - 1];
            if(sum >= maxSum) {
                maxSum = sum;
                index = iii;
            }
        }
        return index;
    }


}
