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
public class DataList {

    public ArrayList<Datum> datums;
    private ArrayList<String> sentenceList;
    private ArrayList<ArrayList<Datum>> sentenceArray;
    private ArrayList<String> wordList;
    private int numOfSen;
    private int numOfWord;

    public DataList(List<Datum> datums) {
        this.datums = (ArrayList<Datum>) datums;
        numOfWord = datums.size();
        numOfSen = datums.get(numOfWord - 1).sentence + 1;
        wordList = new ArrayList<>(numOfWord);
        sentenceList = new ArrayList<>(numOfSen);
        sentenceArray = new ArrayList<>();

        String sentence = "";
        ArrayList<Datum> senList = new ArrayList<>();
        for (Datum dt : datums) {
            wordList.add(dt.word);
            sentence += dt.word;
            senList.add(dt);
            if (Punctuation.isEndOfSentence(dt.word)) {
                sentenceList.add(sentence);
                sentence = "";
                sentenceArray.add(senList);
                senList = new ArrayList<>();
            } else {
                sentence += " ";
            }
        }
    }

    public void update() {
        numOfWord = datums.size();
        numOfSen = datums.get(numOfWord - 1).sentence + 1;
        wordList.clear();
        sentenceList.clear();
        sentenceArray.clear();

        String sentence = "";
        ArrayList<Datum> senList = new ArrayList<>();
        for (Datum dt : datums) {
            wordList.add(dt.word);
            sentence += dt.word;
            senList.add(dt);
            if (Punctuation.isEndOfSentence(dt.word)) {
                sentenceList.add(sentence);
                sentence = "";
                sentenceArray.add(senList);
                senList = new ArrayList<>();
            } else {
                sentence += " ";
            }
        }
        
        /// update di's position
        for (Datum di : datums) {
            di.position = datums.indexOf(di);
        }
    }

    public int getNumOfSen() {
        return numOfSen;
    }

    public int getNumOfWord() {
        return numOfWord;
    }

    public ArrayList<String> getSentenceList() {
        return sentenceList;
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }

    public ArrayList<ArrayList<Datum>> getSentenceArray() {
        return sentenceArray;
    }
}
