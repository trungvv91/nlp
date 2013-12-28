/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.sentenceExtraction;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Manh Tien
 */
public class SentenceRedundancy {

//    /**
//     * 
//     * @param datums
//     * @return
//     * @throws IOException 
//     */
//    public static String sentenceRedundancy(DataList dataList) throws IOException {
//        final double THRESHOLD = 0.7;
//        int numOfSen = dataList.getNumOfSen();
//        ArrayList<String> sentence = dataList.getSentenceList();
//        ArrayList<ArrayList<Datum>> sentenceArray = dataList.getSentenceArray();
//        /**
//         * Similarity score between two sentence
//         */
//        double[][] senSim = new double[numOfSen][numOfSen];
//
//        /**
//         * array return whether sentence have a longer length
//         */
//        int[][] senLeg = new int[numOfSen][numOfSen];
//
//        int iLeg, jLeg;       // length of sentence i,j
//        double topI, bottomI, topJ, bottomJ;
//        for (int i = 0; i < numOfSen - 1; i++) {
//            for (int j = i + 1; j < numOfSen; j++) {
//                iLeg = jLeg = 0;
//                topI = topJ = bottomI = bottomJ = 0;
//                for (Datum di : sentenceArray.get(i)) {
//                    iLeg++;
//                    bottomI += di.idf;
//                    if (sentence.get(j).toLowerCase().contains(di.word.toLowerCase())) {        /// StringUtils.contains(sentence.get(i), dj.word)
//                        topI += di.idf;
//                    }
//                }
//                for (Datum dj : sentenceArray.get(j)) {
//                    jLeg++;
//                    bottomJ += dj.idf;
//                    if (sentence.get(i).toLowerCase().contains(dj.word.toLowerCase())) {
//                        topJ += dj.idf;
//                    }
//                }
//                senSim[i][j] = (topI / bottomI + topJ / bottomJ) / 2.0;
//                senLeg[i][j] = iLeg >= jLeg ? i : j;
//            }
//        }
//        String senRedun = "";
//        for (int i = 0; i < numOfSen - 1; i++) {
//            for (int j = i + 1; j < numOfSen; j++) {
//                if (senSim[i][j] > THRESHOLD) {
//                    senRedun += senLeg[i][j] + ":";
//                }
//            }
//        }
//        System.out.println(senRedun);
//        return senRedun;
//    }
//    public static double simiSen(List<Vertex> vertex,List<Datum> datums)
//    {
//        double simVTop = 0.0;
//        double simVBottom = 0.0;
//        double simDTop = 0.0;
//        double simDBottom = 0.0;
//        for(Vertex v: vertex){
//            int count = 0;
//            for(Datum dt: datums){
//                if(v.name.equals(dt.word) && v.posTag.equals(dt.posTag)){
//                    count=1;
//                    break;
//                }
//            }
//            simVTop += (double)count*v.idf;
//            simVBottom += (double)v.idf;
//        }
//        for(Datum dt: datums){
//            int count = 0;
//            for(Vertex v: vertex){
//                if(v.name.equals(dt.word) && v.posTag.equals(dt.posTag)){
//                    count=1;
//                    break;
//                }
//            }
//            simDTop += (double)count*dt.idf;
//            simDBottom += (double)dt.idf;
//        }
//        double sim = (double)(simVTop/simVBottom + simDTop/simDBottom)/2;
//        
//        return sim;
//    }
}
