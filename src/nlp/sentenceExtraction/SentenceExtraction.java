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

    /// Java manipulates objects by reference, but it passes object references to methods by value
    /**
     * !!!
     * @param datums 
     */
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
     * Eliminate similar sentences !!!
     * @param datums
     * @return
     * @throws IOException 
     */
    public static void SentenceRedundancing(List<Datum> datums) throws IOException {
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
                sentenceArray.remove(Integer.parseInt(arrRedun[i]));
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
    public static List<Datum> ExtractSentences(String inputNum, List<Datum> datums) throws IOException {
        ArrayList<ArrayList<Datum>> sentences = DatumUtil.DatumToSentence(datums);
        int numOfSen = sentences.size();

//        NounAnaphoring(datums);

//        SentenceRedundancing(datums);

        /// Set mapSenOrderByScore
        System.out.println("Start of sen-scoring");
        double[] senScore = new double[numOfSen];
        int[] senIndex = new int[numOfSen];
        for (int i = 0; i < numOfSen; i++) {
            ArrayList<Datum> seni = sentences.get(i);
            int numOfPhr_i = seni.get(seni.size() - 1).iPhrase + 1;
            senScore[i] = 0.0;
            senIndex[i] = i;
            int tf = 0;
            for (Datum dt : seni) {
                if (!dt.stopWord) {
                    tf += dt.tf;
                }
            }
            senScore[i] = tf / (double) numOfPhr_i;       /// ???
        }
        QuickSort.QuickSortFunction(senScore, senIndex, 0, numOfSen - 1);        
        int remainSen = (int) (2 * numOfSen / 3);
        int[] topSenIndex = new int[remainSen];
        int[] topSenIndexTmp = new int[remainSen];
        System.arraycopy(senIndex, 0, topSenIndex, 0, remainSen);
        System.arraycopy(topSenIndex, 0, topSenIndexTmp, 0, remainSen);
        QuickSort.QuickSortFunction(topSenIndexTmp, 0, remainSen - 1);
        for (int i = 0; i < topSenIndexTmp.length; i++) {
            for (int j = 0; j < topSenIndex.length; j++) {
                if (topSenIndexTmp[i] == topSenIndex[j]) {
                    topSenIndex[j] = i;     /// update sentence index
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
        for (int i = remainSen; i < senIndex.length; i++) {
            sentences.remove(senIndex[i]);
        }
//        System.out.println(remainSen + " sentences remain");
        
        return DatumUtil.SentenceToDatum(sentences);
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
