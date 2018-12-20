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
        //saveFile(sekwencjaA);
        odlegloscEdycyjna(sekwencjaA,sekwencjaB);
        funkcjaKaryWDopasowaniu(sekwencjaA,sekwencjaB);
        funkcjaKaryWDopasowaniu("AACCCAA","AAAA");
        funkcjaKaryWDopasowaniu("CAGCCCTAC","CCTGTACCC");

    }

    static int p(int x) {
        int a = 1;
        int b = 1;
        int function = -a-b*x;
        return function;
    }

    static int max(int[] a) {
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < a.length; i++) {
            if (a[i] > max)
                max = a[i];
        }

        return max;
    }

    static int s(char a, char b) {

        if (a == b) {
            return 6;
        }
        else if ( a == '-' || b == '-') {
            return 3;
        }
        else
            return -2;
    }

    static int funkcjaKaryWDopasowaniu(String sekwencjaA, String sekwencjaB) {

        int liczbaLiterA = sekwencjaA.length();
        int liczbaLiterB = sekwencjaB.length();

        int[][] A = new int[liczbaLiterA+1][liczbaLiterB+1];
        int[][] B = new int[liczbaLiterA+1][liczbaLiterB+1];
        int[][] C = new int[liczbaLiterA+1][liczbaLiterB+1];
        int[][] S = new int[liczbaLiterA+1][liczbaLiterB+1];
        int[][] SS = new int[liczbaLiterA+1][liczbaLiterB+1];

        // 0. inicjalizacja
        for (int i = 0; i < liczbaLiterA+1; i++) {
            for (int j = 0; j < liczbaLiterB+1; j++) {
                A[i][j] = Integer.MIN_VALUE;
                B[i][j] = Integer.MIN_VALUE;
                C[i][j] = Integer.MIN_VALUE;
                S[i][j] = Integer.MIN_VALUE;
                SS[i][j] = Integer.MIN_VALUE;
            }
        }

        // 1. Brzeg tabeli
        S[0][0] = 0;
        SS[0][0] = 0;

        for (int i = 1; i < liczbaLiterA+1; i++) {
            S[i][0] = p(i);
            B[i][0] = p(i);

        }
        for (int j = 1; j < liczbaLiterB+1; j++) {
            S[0][j] = p(j);
            A[0][j] = p(j);
        }


        // 2. Środek tabeli
        for (int i = 1; i < liczbaLiterA+1; i++) {
            for (int j = 1; j < liczbaLiterB+1; j++) {

                int[] AK = new int[j];
                for (int k = 0; k < j; k++) {
                    AK[k] = (Math.max(B[i][k],C[i][k]) + p(j-k));
                }
                A[i][j] = max(AK);

                int[] BK = new int[i];
                for (int k = 0; k < i; k++) {
                    BK[k] = (Math.max(A[k][j],C[k][j]) + p(i-k));
                }
                B[i][j] = max(BK);

                C[i][j] = S[i-1][j-1] + s(sekwencjaA.charAt(i-1),sekwencjaB.charAt(j-1));

                //S[i][j] = Math.max(Math.max(A[i][j],B[i][j]),C[i][j]);
                if (A[i][j] > B[i][j]) {
                    if (A[i][j] > C[i][j]) {
                        S[i][j] = A[i][j];
                        SS[i][j] = 2;
                    } else {
                        S[i][j] = C[i][j];
                        SS[i][j] = 1;
                    }
                } else {
                    if (B[i][j] > C[i][j]) {
                        S[i][j] = B[i][j];
                        SS[i][j] = 0;
                    } else {
                        S[i][j] = C[i][j];
                        SS[i][j] = 1;
                    }
                }

            }
        }


        System.out.println();
        for (int i = 0; i < liczbaLiterA+1; i++) {
            for (int j = 0; j < liczbaLiterB+1; j++) {
                System.out.print(S[i][j] + "   ");
            }
            System.out.println();
        }


        String newA = "";
        String newB = "";

        int i = liczbaLiterA;
        int j = liczbaLiterB;

        int[] best = new int[3];
        int bestOne;

        while(i > 0 && j > 0) {

            if (SS[i][j] == 0) {
                newA += sekwencjaA.charAt(i-1);
                newB += '-';
                i--;
            }
            else if (SS[i][j] == 1) {
                newA += sekwencjaA.charAt(i-1);
                newB += sekwencjaB.charAt(j-1);
                i--;
                j--;

            }
            else if (SS[i][j] == 2) {
                newA += '-';
                newB += sekwencjaB.charAt(j-1);
                j--;
            }

        }

        if(i>0) {
            for(int x = 0; x < i; x++) {
                sekwencjaA += '-';
            }

        }

        if(j>0) {
            for(int x = 0; x < j; x++) {
                sekwencjaB += '-';
            }
        }

        System.out.println(rev(newA));
        System.out.println(rev(newB));

        return S[liczbaLiterA][liczbaLiterB];
    }

    static int odlegloscEdycyjna(String sekwencjaA, String sekwencjaB) {

        //String sekwencjaA = "ACG"; // i u
        //String sekwencjaB = "ACA"; // j w
        int pgap = 1;
        int xy = 1;

        int liczbaLiterA = sekwencjaA.length();
        int liczbaLiterB = sekwencjaB.length();

        int[][] tablica = new int[liczbaLiterA+1][liczbaLiterB+1];

        // 1. Brzeg tabeli
        tablica[0][0] = 0;

        for(int i = 1; i < liczbaLiterA+1; i++) {
            for(int k = 0; k < i; k++) {
                //tablica[i][0] += metric.get(sekwencjaA.charAt(k), '-');
                tablica[i][0] = i * pgap;
            }
        }

        for(int j = 1; j < liczbaLiterB+1; j++) {
            for(int k = 0; k < j; k++) {
                //tablica[0][j] += metric.get(sekwencjaB.charAt(k), '-');
                tablica[0][j] = j * pgap;
            }
        }

        // 2. Środek tabeli
        for (int i = 1; i <= liczbaLiterA; i++)
            for (int j = 1; j <= liczbaLiterB; j++) {
                tablica[i][j] = Math.min(
                        tablica[i - 1][j - 1] + metric.get(sekwencjaA.charAt(i-1),sekwencjaB.charAt(j-1)),
                        Math.min(
                                tablica[i][j - 1] + metric.get('-',sekwencjaB.charAt(j-1)),
                                tablica[i - 1][j] + metric.get(sekwencjaA.charAt(i-1),'-')
                        )
                );
            }

        for (int i = 0; i < liczbaLiterA+1; i++) {
            for (int j = 0; j < liczbaLiterB+1; j++) {
                System.out.print(tablica[i][j] + "   ");
            }
            System.out.println();
        }


        int l = sekwencjaA.length() + sekwencjaB.length(); // maximum possible length

        int i = sekwencjaA.length(); int  j = sekwencjaB.length();

        int xpos = l;
        int ypos = l;

        // Final answers for
        // the respective strings
        int xans[] = new int[l + 1];
        int yans[] = new int[l + 1];

        while ( !(i == 0 || j == 0))
        {
            if (sekwencjaA.charAt(i - 1) == sekwencjaB.charAt(j - 1))
            {
                xans[xpos--] = (int)sekwencjaA.charAt(i - 1);
                yans[ypos--] = (int)sekwencjaB.charAt(j - 1);
                i--; j--;
            }
            else if (tablica[i - 1][j - 1] + 1 == tablica[i][j])
            {
                xans[xpos--] = (int)sekwencjaA.charAt(i - 1);
                yans[ypos--] = (int)sekwencjaB.charAt(j - 1);
                i--; j--;
            }
            else if (tablica[i - 1][j] + pgap == tablica[i][j])
            {
                xans[xpos--] = (int)sekwencjaA.charAt(i - 1);
                yans[ypos--] = (int)'-';
                i--;
            }
            else if (tablica[i][j - 1] + pgap == tablica[i][j])
            {
                xans[xpos--] = (int)'-';
                yans[ypos--] = (int)sekwencjaB.charAt(j - 1);
                j--;
            }
        }
        while (xpos > 0)
        {
            if (i > 0) xans[xpos--] = (int)sekwencjaA.charAt(--i);
            else xans[xpos--] = (int)'-';
        }
        while (ypos > 0)
        {
            if (j > 0) yans[ypos--] = (int)sekwencjaB.charAt(--j);
            else yans[ypos--] = (int)'-';
        }

        int id = 1;
        for (i = l; i >= 1; i--)
        {
            if ((char)yans[i] == '-' &&
                    (char)xans[i] == '-')
            {
                id = i + 1;
                break;
            }
        }

        System.out.println("The aligned genes are :");
        for (i = id; i <= l; i++)
        {
            System.out.print((char)xans[i]);
        }
        System.out.print("\n");
        for (i = id; i <= l; i++)
        {
            System.out.print((char)yans[i]);
        }

        return tablica[liczbaLiterA-1][liczbaLiterB-1];
    }

    static int[] NWScore(String X, String Y) {
        int scoreSub, scoreDel, scoreIns;

        //Metric metric = readMetrics();
        int[][] Score = new int[Y.length()][Y.length()]; // 2*length(Y) array

        for (int j = 1; j < Y.length(); j++) {
            Score[0][j] = Score[0][j-1] + metric.insert(Y.charAt(j));
        }

        for (int i = 1; i < X.length(); i++) {
            Score[1][0] = Score[0][0] + metric.delete(X.charAt(i));
            for (int j = 1; j < Y.length(); j++) {
                scoreSub = Score[0][j-1] + metric.get(X.charAt(i),Y.charAt(j));
                scoreDel = Score[0][j] + metric.delete(X.charAt(i));
                scoreIns = Score[1][j-1] + metric.insert(Y.charAt(j));
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
            //A = NeedlemanWunsch(X,Y);
            A = Hirschberg(X,Y);
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
