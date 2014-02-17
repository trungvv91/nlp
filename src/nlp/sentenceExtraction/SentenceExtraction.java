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

/**
 *
 * @author Manh Tien
 */
public class SentenceExtraction {

    public static Map<Integer, Integer> mapSenOrderByScore = new HashMap<>();
    public NounAnaphora na = new NounAnaphora();

    /**
     * Eliminate similar sentences !!!
     *
     * @param datums
     * @throws IOException
     */
    public void SentenceRedundancing(List<Datum> datums) throws IOException {
        System.out.println("Start of redundancy...");

        final double THRESHOLD = 0.7;
        ArrayList<ArrayList<Datum>> sentenceArray = DatumUtil.DatumToSentence(datums);
        int numOfSen = sentenceArray.size();
        /**
         * Similarity score between two iSentence
         */
        double[][] senSim = new double[numOfSen][numOfSen];

        /**
         * array return whether iSentence have a longer length
         */
        int[][] senLeg = new int[numOfSen][numOfSen];
        String senRedun = "";
        int iLeg, jLeg;       // length of iSentence i,j
        double topI, bottomI, topJ, bottomJ;
        for (int i = 0; i < numOfSen - 1; i++) {
            for (int j = i + 1; j < numOfSen; j++) {
                iLeg = sentenceArray.get(i).size();
                jLeg = sentenceArray.get(j).size();
                senLeg[i][j] = iLeg >= jLeg ? i : j;        /// delete longer iSentence

                topI = topJ = bottomI = bottomJ = 0;
                for (Datum di : sentenceArray.get(i)) {
                    bottomI += di.idf;
                    for (Datum dj : sentenceArray.get(j)) {
                        if (dj.equals(di)) {        /// StringUtils.contains(iSentence.get(i), dj.word)
                            topI += di.idf;
                            break;
                        }
                    }
                }
                for (Datum dj : sentenceArray.get(j)) {
                    bottomJ += dj.idf;
                    for (Datum di : sentenceArray.get(j)) {
                        if (di.equals(dj)) {
                            topJ += dj.idf;
                            break;
                        }
                    }
                }
                senSim[i][j] = (topI / bottomI + topJ / bottomJ) / 2.0;
                if (senSim[i][j] > THRESHOLD) {
                    senRedun += senLeg[i][j] + ":";
                }
            }
        }

//        System.out.println(senRedun);        
//        System.out.println(datums.size());
        if (!senRedun.equals("")) {
            String[] arrRedun = senRedun.split(":");
            for (int i = 0; i < arrRedun.length; i++) {
                sentenceArray.remove(Integer.parseInt(arrRedun[i]) - i);
            }
        }
//        System.out.println(datums.size());
        System.out.println("End of redundancy...");
    }

    /**
     * Setup mapSenOrderByScore, keep only 2/3 important sentences
     *
     * @param inputNum - file name number
     * @param datums
     * @return tagged datums list
     * @throws IOException
     */
    public ArrayList<Sentence> ExtractSentences(String inputNum, List<Datum> datums) throws IOException {
        ArrayList<Sentence> sentences = Sentence.DatumToSentence(datums);
        int numOfSen = sentences.size();

        na.nounAnaphoring(sentences);
//        SentenceRedundancing(datums);
        /// Set mapSenOrderByScore
        System.out.println("Start of sen-scoring");
        double[] senScore = new double[numOfSen];
        int[] senIndex = new int[numOfSen];
        for (int i = 0; i < numOfSen; i++) {
            ArrayList<Datum> seni = sentences.get(i).dataList;
            int numOfPhr_i = seni.get(seni.size() - 1).iPhrase + 1;
            senScore[i] = 0.0;
            senIndex[i] = i;
            int tf = 0;
            for (Datum dt : seni) {
                if (!dt.stopWord) {
                    tf += dt.tf;
                }
            }
            senScore[i] = tf / (double) numOfPhr_i;
        }
        QuickSort.QuickSort(senScore, senIndex, 0, numOfSen - 1);
        int remainSen = (int) (2 * numOfSen / 3) + 1;
        int[] topSenIndex = new int[remainSen];
        int[] topSenIndexTmp = new int[remainSen];
        System.arraycopy(senIndex, 0, topSenIndex, 0, remainSen);
        System.arraycopy(topSenIndex, 0, topSenIndexTmp, 0, remainSen);
        QuickSort.QuickSort(topSenIndexTmp, 0, remainSen - 1);
        for (int i = 0; i < topSenIndexTmp.length; i++) {
            for (int j = 0; j < topSenIndex.length; j++) {
                if (topSenIndexTmp[i] == topSenIndex[j]) {
                    topSenIndex[j] = i;     /// update sentence index after removing
                }
            }
        }
        for (int i = 0; i < topSenIndex.length; i++) {
            mapSenOrderByScore.put(topSenIndex[i], i);
        }
        System.out.println("End of sen-scoring...");

// <editor-fold defaultstate="collapsed" desc="Đoạn này không hiểu để làm gì">
//        String taggerExt = "";
//        String strExtract = "";                     //out-string extraction
//        String[] strSen = new String[remainSen];
//        for (int i = 0; i < remainSen; i++) {
//            int tmp = topSen[i];
//            strSen[i] = "";
//            for (Datum dt : datums) {
//                if (dt.iSentence == tmp) {
//                    strExtract += dt.word + " ";
//                    strSen[i] += dt.word + " ";
//                    taggerExt += dt.word + "/" + dt.posTag + "/" + dt.chunk + "/" + dt.iPhrase + "\n";
//                }
//            }
//            strExtract += "\n";
//        }
// </editor-fold>
        // Remove unimportant sentences 
        QuickSort.QuickSort(senIndex, remainSen, senIndex.length - 1);
        for (int i = senIndex.length - 1; i >= remainSen; i--) {
//            sentences.remove(senIndex[i] - (i - remainSen));
            sentences.remove(senIndex[i]);
        }
//        System.out.println(remainSen + " sentences remain");

        return sentences;
    }

    public static void main(String[] args) {
        VNTagger tagger = VNTagger.getInstance();
        List<Datum> datums;
        try {
            datums = tagger.tagger("6");
            SentenceExtraction se = new SentenceExtraction();
            se.ExtractSentences("6", datums);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
