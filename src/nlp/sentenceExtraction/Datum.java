/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.sentenceExtraction;

import java.util.Objects;

/**
 *
 * @author Manh Tien
 */
public class Datum {

    /**
     * the position of sentence containing Datum
     */
    public int iSentence;
    /**
     * the position of phrase in sentence containing Datum
     */
    public int iPhrase;
    public String word;
    public String posTag;
    public String chunk;
    /**
     * tf-idf score
     */
    public int tf;
    public double idf;
    public double score;
    public boolean stopWord = false;
    public boolean semiStopWord = false;
    public boolean importance = false;

    public Datum(String word, String posTag) {
        this.word = word;
        this.posTag = posTag;
        tf = 0;
        idf = 0;
        iPhrase = -1;
    }

    public String toString() {
        return word + " " + posTag + " " + chunk + " " + iSentence;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Datum other = (Datum) obj;
        if (!Objects.equals(this.word.toLowerCase(), other.word.toLowerCase())) {
            return false;
        }
        if (!Objects.equals(this.posTag, other.posTag)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.word.toLowerCase());
        hash = 83 * hash + Objects.hashCode(this.posTag);
        return hash;
    }

    public static void main(String[] args) {
        Datum d1 = new Datum("hello", "V");
        Datum d2 = new Datum("Hello", "V");
        System.out.println(d1.equals(d2));
    }
}
