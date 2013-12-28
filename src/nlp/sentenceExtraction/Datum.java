/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.sentenceExtraction;

/**
 *
 * @author Manh Tien
 */
public class Datum {

    /**
     * position of datum in sentence
     */
    public int position;
    public String word;
    public String posTag;
    public String chunk;
    /**
     * tf-idf score
     */
    public double score;
    public int tf;
    public double idf;
    /**
     * the position of sentence containing Datum
     */
    public int sentence;
    /**
     * the position of phrase in sentence containing Datum
     */
    public int phrase = -1;
    public boolean topScore = false;
    public boolean stopWord = false;
    public boolean semiStopWord = false;
    public boolean importance = false;
    public String predictedLabel;

    public Datum(String word, String posTag) {
        this.word = word;
        this.posTag = posTag;
        tf = 0;
        idf = 0;
    }

    public String toString() {
        String str = word + " " + posTag + " " + chunk + " " + position + " " + sentence;
        return str;
    }

    public int getPosition() {
        return position;
    }

    public String getLabel() {
        return posTag;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public void setLabel(String label) {
        this.posTag = label;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
