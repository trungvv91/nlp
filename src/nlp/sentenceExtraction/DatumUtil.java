/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.sentenceExtraction;

import java.util.ArrayList;
import java.util.List;
import nlp.dict.Punctuation;

/**
 *
 * @author TRUNG
 */
public class DatumUtil {

    public static int getNumberOfSentences(List<Datum> datums) {
        return datums.get(datums.size() - 1).iSentence + 1;
    }

    public static List<Datum> SentenceToDatum(List<ArrayList<Datum>> sentences) {
        ArrayList<Datum> datums = new ArrayList<>();
        for (List<Datum> sentence : sentences) {
            for (Datum datum : sentence) {
                datum.iSentence = sentences.indexOf(sentence);
                datums.add(datum);
            }
        }

        return datums;
    }

    public static ArrayList<String> DatumToWord(List<Datum> datums) {
        ArrayList<String> wordList = new ArrayList<>();
        for (Datum dt : datums) {
            wordList.add(dt.word);
        }
        return wordList;
    }

    public static ArrayList<ArrayList<Datum>> DatumToSentence(List<Datum> datums) {
        ArrayList<ArrayList<Datum>> sentenceArray = new ArrayList<>();

        ArrayList<Datum> senList = new ArrayList<>();
        for (Datum dt : datums) {
            senList.add(dt);
            if (Punctuation.isEndOfSentence(dt.word)) {
                sentenceArray.add(senList);
                senList = new ArrayList<>();
            } else {
            }
        }
        return sentenceArray;
    }
}
