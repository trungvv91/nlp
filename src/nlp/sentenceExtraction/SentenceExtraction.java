/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.sentenceExtraction;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nlp.dict.NounAnaphora;
import nlp.graph.QuickSort;
import nlp.tool.vnTextPro.VNTagger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Manh Tien
 */
public class SentenceExtraction {

    public static Map<Integer, Integer> mapSenOrderByScore;

    public SentenceExtraction() {
        mapSenOrderByScore = new HashMap<>();
    }

    /// Java manipulates objects by reference, but it passes object references to methods by value
    public static void NounAnaphoring(List<Datum> datums) {
        /// Noun Anaphoric 2
        for (int i = 1; i < datums.size();) {
            Datum di = datums.get(i);
            if (di.posTag.equals("Np")) {
                if (NounAnaphora.checkNounAnophoric2(datums.get(i - 1).word)) {
                    /// bệnh_nhân Vũ_Dư !!! bệnh_nhân_Vũ_Dư 
                    di.word = datums.get(i - 1).word + " " + di.word;
                    datums.remove(datums.get(i - 1));
                } else if (i >= 2 && NounAnaphora.checkNounAnophoric2(datums.get(i - 2).word)
                        && datums.get(i - 1).word.equals("của")) {
                    /// anh của Dư
                    di.word = datums.get(i - 2).word + " " + datums.get(i - 1).word + " " + di.word;
                    datums.remove(datums.get(i - 1));
                    datums.remove(datums.get(i - 2));
                    i--;
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }

        /// Noun Anaphoric 1
        for (int i = 1; i < datums.size() - 2; i++) {
            Datum di = datums.get(i);
            if (di.word.equals(".") || di.word.equals(",")) {
                /// đó là Dư, anh ấy đang bị ốm
                if (datums.get(i - 1).posTag.equals("Np")
                        && (NounAnaphora.checkNounAnophoric1(datums.get(i + 1).word)
                        || NounAnaphora.checkNounAnophoric1(datums.get(i + 1).word + " " + datums.get(i + 2).word))) {
                    datums.get(i + 1).word = datums.get(i - 1).word;
                }
            }
        }
    }

    /**
     * Eliminate similar sentences 
     * @param datums
     * @return
     * @throws IOException 
     */
    public static void SentenceRedundancing(DataList dataList) throws IOException {
        final double THRESHOLD = 0.7;
        int numOfSen = dataList.getNumOfSen();
        ArrayList<Datum> datums = dataList.datums;
        ArrayList<String> sentence = dataList.getSentenceList();
        ArrayList<ArrayList<Datum>> sentenceArray = dataList.getSentenceArray();
        /**
         * Similarity score between two sentence
         */
        double[][] senSim = new double[numOfSen][numOfSen];

        /**
         * array return whether sentence have a longer length
         */
        int[][] senLeg = new int[numOfSen][numOfSen];

        int iLeg, jLeg;       // length of sentence i,j
        double topI, bottomI, topJ, bottomJ;
        for (int i = 0; i < numOfSen - 1; i++) {
            for (int j = i + 1; j < numOfSen; j++) {
                iLeg = sentenceArray.get(i).size();
                jLeg = sentenceArray.get(j).size();
                senLeg[i][j] = iLeg >= jLeg ? i : j;
                
                topI = topJ = bottomI = bottomJ = 0;
                for (Datum di : sentenceArray.get(i)) {
                    bottomI += di.idf;
                    if (sentence.get(j).toLowerCase().contains(di.word.toLowerCase())) {        /// StringUtils.contains(sentence.get(i), dj.word)
                        topI += di.idf;
                    }
                }
                for (Datum dj : sentenceArray.get(j)) {
                    bottomJ += dj.idf;
                    if (sentence.get(i).toLowerCase().contains(dj.word.toLowerCase())) {
                        topJ += dj.idf;
                    }
                }
                senSim[i][j] = (topI / bottomI + topJ / bottomJ) / 2.0;
            }
        }
        
        String senRedun = "";
        for (int i = 0; i < numOfSen - 1; i++) {
            for (int j = i + 1; j < numOfSen; j++) {
                if (senSim[i][j] > THRESHOLD) {
                    senRedun += senLeg[i][j] + ":";
                }
            }
        }
//        System.out.println(senRedun);        
        System.out.println("Start of redundancy...");
//        System.out.println(datums.size());
        if (!senRedun.equals("")) {
            String[] arrRedun = senRedun.split(":");
            for (int i = 0; i < arrRedun.length; i++) {
                int tmp = Integer.parseInt(arrRedun[i]);
//                System.out.print(tmp + "\t");
                for (int j = 0; j < datums.size(); j++) {
                    Datum dt = datums.get(j);
                    if (dt.sentence == tmp) {
//                        System.out.print(dt.word + " ");
                        datums.remove(j);
                        j--;
                    }
                }
            }
        }
//        System.out.println(datums.size());
        System.out.println("End of redundancy...");
    }
    
    /**
     * Set properties for datum in datums list
     * @param inputNum - file name number
     * @param datums
     * @return tagged datums list
     * @throws IOException 
     */
    public static void ExtractSentences(String inputNum, List<Datum> datums) throws IOException {

//        datums.get(0).word = "Trung";
//        System.out.println(datums.get(0).word);
//        System.out.println(dataList.datums.get(0).word);

//        NounAnaphoring(datums);

        DataList dataList = new DataList(datums);
        List<String> wordList = dataList.getWordList();
        SentenceRedundancing(dataList);

        System.out.println("Start of sen-scoring");
        int phrase = 0;                             // phrase number of a word
        for (Datum dt : datums) {
            if (dt.chunk.contains("B")) {
                phrase++;
                dt.phrase = phrase;
            } else if (dt.chunk.contains("I")) {
                dt.phrase = phrase;
            } else {
                phrase++;
                dt.phrase = phrase;
            }
        }
        String strPhrase = "";
        for (Datum dt : datums) {
            if (dt.phrase != -1) {
                strPhrase += dt.word + " ";
            }
        }

        int numOfSen = datums.get(datums.size() - 1).sentence + 1;
        double[] senScore = new double[numOfSen];
        int[] senOrderByScore = new int[numOfSen];
        for (int i = 0; i < numOfSen; i++) {
            senScore[i] = 0.0;
            senOrderByScore[i] = i;
            int numOfChunks = 0;
            int tf = 0;
            for (Datum dt : datums) {
                if (dt.sentence == i) {
                    if (dt.chunk.equals("B-NP")) {
                        numOfChunks++;
                    }
                    if (dt.phrase != -1 || dt.stopWord == false) {
                        tf += StringUtils.countMatches(strPhrase, dt.word);
                    }
                }
            }
            senScore[i] = (double) tf / (double) numOfChunks;
        }
        QuickSort.QuickSortFunction(senScore, senOrderByScore, 0, numOfSen - 1);
        System.out.println("End of sen-scoring...");


        String taggerExt = "";
        int remainSen = (int) (2 * numOfSen / 3);   //Number of Sentences after ExtractSentences phrase
        String strExtract = "";                     //out-string extraction
        System.out.println(remainSen + " sentences remain");
        int[] topSen = new int[remainSen];
        System.arraycopy(senOrderByScore, 0, topSen, 0, remainSen);
        QuickSort.QuickSortFunction(topSen, 0, remainSen - 1);
        String[] strSen = new String[remainSen];
        for (int i = 0; i < remainSen; i++) {
            int tmp = topSen[i];
            strSen[i] = "";
            for (Datum dt : datums) {
                if (dt.sentence == tmp) {
                    strExtract += dt.word + " ";
                    strSen[i] += dt.word + " ";
                    taggerExt += dt.word + "/" + dt.posTag + "/" + dt.chunk + "/" + dt.phrase + "\n";
                }
            }
            strExtract += "\n";
        }

        System.out.println("Start of relevance...");

        //Remove sentences are not important.
        for (int i = remainSen; i < senOrderByScore.length; i++) {
            int tmp = senOrderByScore[i];
            for (int j = 0; j < datums.size(); j++) {
                Datum dt = datums.get(j);
                if (dt.sentence == tmp) {
                    datums.remove(j);
                    wordList.remove(j);
                    j--;
                }
            }
        }
        int tempSen = 0;
        for (Datum dt : datums) {
            int temp = dt.sentence;
            dt.sentence = tempSen;
            int index = datums.indexOf(dt);
            if (index > 0 && index < datums.size() - 1) {
                if (temp != datums.get(index + 1).sentence) {
                    tempSen++;
                }
            }
        }

        int[] topSenTmp = new int[topSen.length];
        System.arraycopy(topSen, 0, topSenTmp, 0, topSen.length);
        QuickSort.QuickSortFunction(topSenTmp, 0, topSenTmp.length - 1);
        for (int i = 0; i < topSenTmp.length; i++) {
            int tmp = topSenTmp[i];
            for (int j = 0; j < topSen.length; j++) {
                if (tmp == topSen[j]) {
                    topSen[j] = i;
                }
            }
        }

        for (int i = 0; i < topSen.length; i++) {
            mapSenOrderByScore.put(topSen[i], i);
        }
        datums.get(0).sentence = 0;
        for (int i = 0; i < datums.size(); i++) {
            datums.get(i).position = i;
        }
        System.out.println("Sentence size: " + (int) (datums.get(datums.size() - 1).sentence + 1));
        System.out.println("Datums size: " + datums.size());
        System.out.println("End of relevance...");
        System.out.println("--End of extraction...");
    }

    public static void main(String[] args) {
        VNTagger tagger = VNTagger.getInstance();
        List<Datum> datums = null;
        try {
            datums = tagger.tagger("0");
            ExtractSentences("0", datums);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
