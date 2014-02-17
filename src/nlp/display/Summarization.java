/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.display;

import nlp.graph.*;
import java.io.*;
import java.util.List;
import nlp.sentenceExtraction.Datum;
import nlp.tool.vnTextPro.VNTagger;

/**
 *
 * @author Manh Tien
 */
public class Summarization {

    /**
     * @param source
     * @param wordMax
     * @return
     * @throws java.io.IOException
     */
    public static String summarize(String source, int wordMax) throws IOException {
//        System.out.println(source);
        String displayFile = "corpus/Plaintext/displayFile.txt";
        String inputNum = "displayFile";
        try (FileWriter fr = new FileWriter(new File(displayFile))) {
            fr.write(source);
        }
        WordsGraph graph = new WordsGraph();
        VNTagger tagger = VNTagger.getInstance();
        List<Datum> datums = tagger.tagger(inputNum);
        try {
            graph.mainWordGraph(inputNum, datums, wordMax);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        String out = graph.outString;
//        System.out.println(out);
        return out;
    }
}
