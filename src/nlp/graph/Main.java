/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.graph;

import java.io.*;
import java.util.List;
import nlp.sentenceExtraction.Datum;
import nlp.tool.vnTextPro.VNTagger;

/**
 *
 * @author Manh Tien
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String path = "corpus/Plaintext";
        String file;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                file = listOfFiles[i].getName();
                if (file.endsWith(".txt")) {
                    String inputNum = file.split("\\.")[0];
                    try {
                        WordsGraph graph = new WordsGraph();
                        VNTagger tagger = VNTagger.getInstance();
                        List<Datum> datums = tagger.tagger(inputNum);
                        graph.mainWordGraph(inputNum, datums, 120);
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }
                }
            }
        }
    }
}
