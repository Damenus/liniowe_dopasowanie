package pl.edu.pg;

//System.out.println((int) 'A' + (int) 'C');
//System.out.println((int) 'A' + (int) 'G');
//System.out.println((int) 'A' + (int) 'T');
//System.out.println((int) 'C' + (int) 'G');
//System.out.println((int) 'C' + (int) 'T');
//System.out.println((int) 'G' + (int) 'T');
//System.out.println((int) 'A' + (int) '-');
//System.out.println((int) 'C' + (int) '-');
//System.out.println((int) 'G' + (int) '-');
//System.out.println((int) 'T' + (int) '-');
//System.out.println((int) '-' + (int) 'T');
//System.out.println("-T".codePointAt(1) + "-T".codePointAt(0));
//
//HashMap<Integer, Integer> metrics = new HashMap<>();
//metrics.put(Integer.valueOf((int) 'A' + (int) 'C'), 1); //132
//metrics.put(Integer.valueOf((int) 'A' + (int) 'G'), 1); //136
//metrics.put(Integer.valueOf((int) 'A' + (int) 'T'), 1); //149
//metrics.put(Integer.valueOf((int) 'C' + (int) 'G'), 1); //138
//metrics.put(Integer.valueOf((int) 'C' + (int) 'T'), 1); //151
//metrics.put(Integer.valueOf((int) 'G' + (int) 'T'), 1); //155
//metrics.put(Integer.valueOf((int) 'A' + (int) '-'), 1); //110
//metrics.put(Integer.valueOf((int) 'C' + (int) '-'), 1); //112
//metrics.put(Integer.valueOf((int) 'G' + (int) '-'), 1); //116
//metrics.put(Integer.valueOf((int) 'T' + (int) '-'), 1); //129

import java.util.HashMap;

public class Metric {

    HashMap<Integer, Integer> metrics;
    HashMap<Character, Integer> insertCost;
    HashMap<Character, Integer> deleteCost;

    public Metric(HashMap<Integer, Integer> metrics) {
        this.metrics = metrics;
    }

    public Metric(String metricsString) {
        this.metrics = new HashMap<>();
        this.insertCost = new HashMap<>();
        this.deleteCost = new HashMap<>();

        Integer sumLeters;
        Integer valueBetweenLeters;
        String[] lines = metricsString.split(System.getProperty("line.separator"));
        for (String line: lines) {
            sumLeters = line.codePointAt(0) + line.codePointAt(1);
            valueBetweenLeters = Integer.valueOf(line.split("=")[1]);
            metrics.put(sumLeters,valueBetweenLeters);
        }

        insertCost.put('A', -1);
        insertCost.put('C', -1);
        insertCost.put('G', -1);
        insertCost.put('T', -1);

        deleteCost.put('A', -1);
        deleteCost.put('C', -1);
        deleteCost.put('G', -1);
        deleteCost.put('T', -1);

    }

    public int get(char a, char b) {
//        if (a == b)
//            return 0;
//        else
            return metrics.get((int) a + (int) b).intValue();
    }

    public int get(String a, String b) {
        return metrics.get(a.codePointAt(0) + b.codePointAt(0)).intValue();
    }

    /*public int get(String a) {
        return metrics.get(a.codePointAt(0) + a.codePointAt(1)).intValue();
    }*/

    public int insert(char a) {
        if (insertCost.get(a) == null){
            throw new UnsupportedOperationException("DeleteCost " + a + " does not exist");
        }
        return insertCost.get(a).intValue();
    }

    public int delete(char a) {
        if (deleteCost.get(a) == null){
            throw new UnsupportedOperationException("DeleteCost " + a + " does not exist");
        }
        return deleteCost.get(a).intValue();
    }
}
