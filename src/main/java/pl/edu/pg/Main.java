package pl.edu.pg;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.biojava.nbio.alignment.NeedlemanWunsch;

import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.core.alignment.matrices.SimpleSubstitutionMatrix;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.BasicSequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.sequence.template.Sequence;

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

    public static void main(String[] args) throws Exception{
        String sekwencjaA = readFile("sekwencjaA.txt");
        String sekwencjaB = readFile("sekwencjaB.txt");
        
        CompoundSet compoundSet = new DNACompoundSet();
        
        Sequence seq1 = new BasicSequence(sekwencjaA, compoundSet);
        Sequence seq2 = new BasicSequence(sekwencjaB, compoundSet);
        GapPenalty gapPenalty = new SimpleGapPenalty(0, 1); // gap open, gap extend
        SubstitutionMatrix subMatrix = new SimpleSubstitutionMatrix(compoundSet, (short)2, (short)-1); // match, replace
        
        NeedlemanWunsch nw = new NeedlemanWunsch(seq1, seq2, gapPenalty, subMatrix);
        
        //System.out.println(nw.getProfile().toString());
        //System.out.println(nw.getQuery().toString());
        //System.out.println(nw.getTarget().toString());
        System.out.println(nw.getScore());
        
        System.out.println(nw.getScoreMatrixAsString());
        
    }
}
