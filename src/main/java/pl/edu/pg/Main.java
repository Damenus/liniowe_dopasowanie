package pl.edu.pg;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.NeedlemanWunsch;

import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.core.alignment.matrices.SimpleSubstitutionMatrix;
import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SequencePair;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.BasicSequence;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
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


        DNASequence dnaSequence1 = new DNASequence(sekwencjaA);
        DNASequence dnaSequence2 = new DNASequence(sekwencjaB);

        // najlepsze globalne
        SubstitutionMatrix<NucleotideCompound> matrix = SubstitutionMatrixHelper.getNuc4_4();
        SequencePair<DNASequence, NucleotideCompound> pair = Alignments.getPairwiseAlignment(dnaSequence1, dnaSequence2, Alignments.PairwiseSequenceAlignerType.GLOBAL, new SimpleGapPenalty(), matrix);
        System.out.printf("Best global alignments \n%s", pair);

        // najlepsze lokalne
        SubstitutionMatrix<NucleotideCompound> matrix2 = SubstitutionMatrixHelper.getNuc4_4();
        SequencePair<DNASequence, NucleotideCompound> pair2 = Alignments.getPairwiseAlignment(dnaSequence1, dnaSequence2, Alignments.PairwiseSequenceAlignerType.LOCAL, new SimpleGapPenalty(), matrix2);
        System.out.printf("Best local alignments \n%s", pair2);
        
    }
}
