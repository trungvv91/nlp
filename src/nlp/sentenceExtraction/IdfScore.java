/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.sentenceExtraction;

import java.util.*;
import java.io.*;

/**
 *
 * @author Manh Tien
 */
public class IdfScore {

    Map<String, Double> map;
    Map<String, Double> idf_index;

    public IdfScore() {
        map = new HashMap<>();
        idf_index = new HashMap<>();
    }

    public Map<String, Double> getIdfScoreMap(List<Datum> datums) throws IOException {
        String fileName = "train-data/idf_final.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] word = line.split(" ");
                double idf = Double.parseDouble(word[1]);
                idf_index.put(word[0], idf);
            }
        }
        for (Datum datum : datums) {
            String word = datum.word.toLowerCase();
            double idf = 0.0;
            if (idf_index.containsKey(word)) {
                idf = (double) idf_index.get(word);
            } else {
                idf = Math.log(21628.0);        /// !!!
            }
            map.put(word, idf);
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
//        IdfScore is = new IdfScore();
//        ArrayList<String> list = new ArrayList<>();
//        list.add("hello");
//        Map m;
//        m = is.getIdfScoreMap(list);
//        System.out.println(m.toString());
    }
}
