/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.graph;

import nlp.sentenceExtraction.Datum;
import nlp.dict.Synonym;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import nlp.dict.Conjunction;
import nlp.sentenceExtraction.DatumUtil;
import nlp.sentenceExtraction.SentenceExtraction;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Manh Tien
 */
public class WordsGraph {

    public List<Datum> datums;
    public Conjunction conj = new Conjunction();
    public String outString = "";

    /**
     * Determine keywords in dt list by set d.importance=true
     * @param dt
     * @param numOfWordImportance 
     */
    public void setWordImportance(List<Datum> dt, int numOfWordImportance) {
        System.out.println("Start word-importance set...");
        List<Datum> vertex = new ArrayList<>();
        for (Datum d : dt) {
            boolean contain = false;
            for (Datum vt : vertex) {
                if (vt.word.equals(d.word) && vt.posTag.equals(d.posTag)) {
                    contain = true;
                }
            }
            if (contain == false) {
                vertex.add(d);      /// delete unsplitable iPhrase, check stop word ???
            }
        }
        double Value[] = new double[vertex.size()];

        int Index[] = new int[vertex.size()];
        System.out.println("Vertex size is: " + vertex.size());
        for (int i = 0; i < vertex.size(); i++) {
            Value[i] = vertex.get(i).score;
            Index[i] = i;
        }
        QuickSort.QuickSortFunction(Value, Index, 0, Value.length - 1);
        for (int i = 0; i < vertex.size(); i++) {
            //int index = Index[i];
        }

        for (int i = 0; i < numOfWordImportance; i++) {
            int index = Index[i];
            Datum dimp = vertex.get(index);
            for (Datum d : dt) {
                if (d.word.equals(dimp.word) && d.posTag.equals(dimp.word)) {
                    d.importance = true;
                }
            }
            vertex.get(index).importance = true;
        }
        System.out.println("End word-importance set...");
    }
    
    public void mainWordGraph(String inputNum, List<Datum> dts, int wordMax) throws IOException {
        Synonym.initSynonymMap();
        datums = SentenceExtraction.ExtractSentences(inputNum, dts);

        //Lay 15% so tu la importance words
        setWordImportance(datums, datums.size() * 15 / 100);

        ArrayList<ArrayList<Datum>> sens = DatumUtil.DatumToSentence(datums);

        /// xét 2 câu liên tiếp --> tìm co-ref, xử lý
        List<Integer> senBeDelete = new ArrayList<>();
        List<Datum> tmpList = new ArrayList<>();
        for (int i = 0; i < sens.size() - 2; i++) {
            List<Datum> seni = sens.get(i);
            int j = i + 1;
            int indexToAdd = -1;
            List<Datum> senj = sens.get(j);
            for (int k = 0; k < seni.size(); k++) {
                Datum di = seni.get(k);
                String wi = di.word;
                String posi = di.posTag;
                int chunki = di.iPhrase;
                for (int h = 0; h < senj.size(); h++) {
                    Datum dj = senj.get(h);
                    String wj = dj.word;
                    String posj = dj.posTag;
                    int chunkj = dj.iPhrase;

                    /// co-ref là N or Np ??? 
                    if (wi.equals(wj) && posi.equals(posj) && (posi.equals("N") || posi.equals("Np"))) {
                        String phrasei = "";
                        String phrasej = "";
                        int endChunki = k;
                        int endChunkj = h;

                        /// tim iPhrase cua di va dj
                        for (Datum d : seni) {
                            if (d.iPhrase == chunki && !d.equals(di)) {
                                phrasei += d.word + " ";
                                endChunki++;
                            }
                        }
                        for (Datum d : senj) {
                            if (d.iPhrase == chunkj && !d.equals(dj)) {
                                phrasej += d.word + " ";
                                endChunkj++;
                            }
                        }
                        /// !!!
                        if (phrasej.contains(phrasei) && endChunki <= seni.size() - 2) {
                            if (seni.get(endChunki + 1).posTag.equals("R")
                                    || seni.get(endChunki + 1).posTag.equals("V")) {
                                boolean checkIptWord = false;
                                for (Datum d : seni) {
                                    ///System.out.println("Trung: " + seni.indexOf(d));
                                    if (seni.indexOf(d) < seni.indexOf(di) && d.importance == true) {
                                        checkIptWord = true;
                                    }
                                }
                                if (checkIptWord == false) {
                                    indexToAdd = endChunkj;
                                    for (Datum d : seni) {
                                        if (seni.indexOf(d) >= endChunki) {
                                            tmpList.add(d);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!tmpList.isEmpty() && indexToAdd > -1) {
                /// không cần đến ???
                Datum dtemp = sens.get(j).get(sens.get(j).size() - 1);
                for (Datum d : tmpList) {
                    int index = indexToAdd + tmpList.indexOf(d);
                    sens.get(j).add(index, d);
                }
                for (int m = 0; m < sens.get(j).size(); m++) {
                    Datum d = sens.get(j).get(m);
                    if (d.word.equals(".")) {
                        sens.get(j).remove(m);
                    }
                }
                sens.get(j).add(dtemp);
                senBeDelete.add(i);
            }
        }

        /// tạo nút chung trên graph | iSentence combination ???
        for (List<Datum> sen : sens) {
            int i = sens.indexOf(sen);
            //ghep 2 cau neu trung Subject hoac Verb
            if (i <= sens.size() - 2) {
                String subjecti = "";
                String subjectj = "";
                String verbi = "";
                int indexVerbi = -1;
                String verbj = "";
                int indexVerbj = -1;
                for (Datum di : sen) {
                    if (di.posTag.equals("V")) {
                        indexVerbi = sen.indexOf(di);
                        break;
                    }
                }
                for (Datum dj : sens.get(i + 1)) {
                    if (dj.posTag.equals("V")) {
                        indexVerbj = sens.get(i + 1).indexOf(dj);
                        break;
                    }
                }
                for (Datum di : sen) {
                    if (di.iPhrase == sen.get(0).iPhrase) {
                        subjecti += di.word + " ";
                    }
                    if (indexVerbi > -1 && di.iPhrase == sen.get(indexVerbi).iPhrase) {
                        verbi += di.word + " ";
                    }
                }
                for (Datum dj : sens.get(i + 1)) {
                    if (dj.iPhrase == sens.get(i + 1).get(0).iPhrase) {
                        subjectj += dj.word + " ";
                    }
                    if (indexVerbj > -1 && dj.iPhrase == sens.get(i + 1).get(indexVerbj).iPhrase) {
                        verbj += dj.word + " ";
                    }
                }
                if (subjecti.toLowerCase().equals(subjectj.toLowerCase())) {
                    Random r = new Random();
                    String[] list = {"và", ","};
                    Datum dt = new Datum(list[r.nextInt(2)], "O");
                    dt.chunk = "O";
                    dt.stopWord = true;
                    sen.add(dt);
                    for (Datum dj : sens.get(i + 1)) {
                        if (sens.get(i + 1).indexOf(dj) >= indexVerbj) {
                            sen.add(dj);
                        }
                    }
                    Datum dtemp = sens.get(i + 1).get(sens.get(i + 1).size() - 1);
                    for (int k = 0; k < sen.size(); k++) {
                        Datum d = sen.get(k);
                        if (d.word.equals(".")) {
                            sen.remove(k);
                        }
                    }
                    sen.add(dtemp);
                    senBeDelete.add(i + 1);
                }
                if (verbi.toLowerCase().equals(verbj.toLowerCase())) {
                    Random r = new Random();
                    String[] list = {"và", ","};
                    Datum dt = new Datum(list[r.nextInt(2)], "O");
                    dt.chunk = "O";
                    dt.stopWord = true;
                    int endSubjecti = 0;
                    for (int k = sen.size() - 1; k >= 0; k--) {
                        Datum di = sen.get(k);
                        if (di.iPhrase == sen.get(0).iPhrase) {
                            endSubjecti = k;
                            break;
                        }
                    }
                    sen.add(endSubjecti, dt);
                    for (Datum dj : sens.get(i + 1)) {
                        if (dj.iPhrase == sens.get(i + 1).get(0).iPhrase) {
                            sen.add(endSubjecti + 1, dj);
                        }
                    }
                    senBeDelete.add(i + 1);
                }
            }
        }

        int senDeleted = 0;
        for (int sen : senBeDelete) {
            try {
                sens.remove(sens.get(sen - senDeleted));
                senDeleted++;
            } catch (Exception e) {
                System.out.println("Can't delete sth!");
            }
        }

        System.out.println("");
        ArrayList<Integer[]> termIndex = new ArrayList<>();
        for (List<Datum> sen : sens) {
            String term = "";
            Integer[] index = new Integer[2];
            for (int i = 0; i < sen.size() - 1; i++) {
                Datum di = sen.get(i);
                if (di.importance == true) {
                    int iphrase = di.iPhrase;
                    for (Datum d : sen) {
                        if (d.iPhrase == iphrase) {
                            index[0] = sen.indexOf(d);
                            break;
                        }
                    }
                    term += di.word;
                    if (i != sen.size() - 1) {
                        for (int j = sen.size() - 1; j > i; j--) {
                            Datum dj = sen.get(j);
                            if (dj.importance == true) {
                                index[1] = j;
                                int phrase = dj.iPhrase;
                                for (int k = sen.size() - 1; k >= j; k--) {
                                    if (sen.get(k).iPhrase == phrase) {
                                        index[1] = k;
                                        break;
                                    }
                                }
                                break;
                            } else {
                                index[1] = null;
                            }
                        }
                        break;
                    } else if (i == sen.size() - 1 && sen.get(i).importance == true) {
                        index[1] = i;
                    } else {
                        index[1] = index[0];
                    }
                } else {
                    index[0] = null;
                    index[1] = null;
                }
            }
            termIndex.add(index);
        }
        System.out.println("----------");


        for (List<Datum> sen : sens) {
            int senIndex = sens.indexOf(sen);
            if (termIndex.get(senIndex)[0] != null && termIndex.get(senIndex)[1] != null) {
                int iIndex = termIndex.get(senIndex)[0];
                int jIndex = termIndex.get(senIndex)[1];
                //Kiem tra bat dau la VP
                if (sen.get(iIndex).chunk.equals("B-VP")) {
                    int iIndexSource = iIndex;
                    if (iIndexSource > 0) {
                        for (int i = iIndexSource - 1; i >= 0; i--) {
                            Datum d = sen.get(i);
                            if (d.chunk.equals("B-NP")) {
                                iIndex = i;
                                termIndex.get(senIndex)[0] = i;
                                break;
                            }
                        }
                    } else {
                        termIndex.get(senIndex)[0] = iIndex;
                    }
                }
                //Kiem tra ket thuc la NP
                if ((sen.get(jIndex).chunk.equals("B-NP") || sen.get(jIndex).chunk.equals("I-NP")) && jIndex < sen.size() - 1) {
                    if (sen.get(jIndex + 1).posTag.equals("V")) {
                        int phrase = sen.get(jIndex + 1).iPhrase;
                        int count = 0;
                        for (Datum d : sen) {
                            if (d.iPhrase == phrase) {
                                count++;
                            }
                        }
                        jIndex = jIndex + count;
                        if (jIndex > sen.size()) {
                            jIndex = sen.size();
                        }
                        termIndex.get(senIndex)[1] = jIndex;
                    }
                }
                //Kiem tra ket thuc la V
                if (sen.get(jIndex).posTag.equals("V") && jIndex < sen.size() - 1) {
                    if (sen.get(jIndex + 1).chunk.equals("B-NP")) {
                        int phrase = sen.get(jIndex + 1).iPhrase;
                        int count = 0;
                        for (Datum d : sen) {
                            if (d.iPhrase == phrase) {
                                count++;
                            }
                        }
                        jIndex = jIndex + count;
                        termIndex.get(senIndex)[1] = jIndex;
                        if (sen.get(jIndex + 1).chunk.equals("B-VP") && jIndex < sen.size() - 1) {
                            int phrase1 = sen.get(jIndex + 1).iPhrase;
                            int count1 = 0;
                            for (Datum d : sen) {
                                if (d.iPhrase == phrase1) {
                                    count1++;
                                }
                            }
                            jIndex = jIndex + count1;
                            termIndex.get(senIndex)[1] = jIndex;
                        }
                    }
                }
                boolean hasV = false;
                boolean hasN = false;
                for (int i = termIndex.get(senIndex)[0]; i <= termIndex.get(senIndex)[1]; i++) {
                    Datum d = sen.get(i);
                    if (d != null) {
                        if (d.posTag.equals("V")) {
                            hasV = true;
                        }
                        if (d.chunk.equals("B-NP")) {
                            hasN = true;
                        }
                    }
                }
                //kiem tra co N va khong co V
                if (hasN == true && hasV == false) {
                    int indexSource = termIndex.get(senIndex)[0];
                    if (indexSource > 0) {
                        for (int i = indexSource - 1; i >= 0; i--) {
                            Datum d = sen.get(i);
                            if (d.posTag.equals("V")) {
                                termIndex.get(senIndex)[0] = i;
                                if (i > 0) {
                                    for (int j = i - 1; j >= 0; j--) {
                                        Datum dj = sen.get(j);
                                        if (dj.chunk.equals("B-NP")) {
                                            termIndex.get(senIndex)[0] = j;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                } //khong co ca N va V
                else if (hasN == false && hasV == false) {
                    int indexSource = termIndex.get(senIndex)[0];
                    if (indexSource > 0) {
                        boolean foundV = false;
                        for (int i = indexSource - 1; i >= 0; i--) {
                            Datum d = sen.get(i);
                            if (d.chunk.equals("B-VP")) {
                                termIndex.get(senIndex)[0] = i;
                                foundV = true;
                                if (i > 0) {
                                    for (int j = i - 1; j >= 0; j--) {
                                        if (d.chunk.equals("B-NP")) {
                                            termIndex.get(senIndex)[0] = j;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if (foundV == false) {
                            for (int i = indexSource - 1; i >= 0; i--) {
                                Datum d = sen.get(i);
                                if (d.chunk.equals("B-NP")) {
                                    termIndex.get(senIndex)[0] = i;
                                    foundV = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                //Kiem tra Declare word
                boolean hasDeclare = false;
                int iDec = -1;
                for (Datum di : sen) {
                    for (int i = 0; i < conj.Declare.length; i++) {
                        if (di.word.toLowerCase().equals(conj.Declare[i])) {
                            hasDeclare = true;
                            iDec = sen.indexOf(di);
                        }
                    }
                }
                if (hasDeclare == true && iDec < termIndex.get(senIndex)[0]) {
                    termIndex.get(senIndex)[0] = iDec + 1;
                } else if (hasDeclare == true && iDec > termIndex.get(senIndex)[0]) {
                    termIndex.get(senIndex)[0] = 0;
                }
                //Kiem tra conjunction
                boolean hasFConj = false;
                int iConj = -1;
                int jConj = -1;
                int indexOfConj = -1; //Thu tu conjunction trong mang
                for (Datum di : sen) {
                    for (int i = 0; i < conj.Conj.length; i++) {
                        if (di.word.toLowerCase().equals(conj.Conj[i][0])) {
                            hasFConj = true;
                            iConj = sen.indexOf(di);
                            indexOfConj = i;
                            break;
                        }
                    }
                }
                if (hasFConj && conj.Conj[indexOfConj][1] != null) {
                    for (Datum di : sen) {
                        if (sen.indexOf(di) > iConj && di.word.toLowerCase().equals(conj.Conj[indexOfConj][1])) {
                            jConj = sen.indexOf(di);
                            boolean hasIptWord = false; // kiem tra important word giua 2 conj
                            for (int i = iConj; i <= jConj; i++) {
                                Datum d = sen.get(i);
                                if (d.importance == true) {
                                    hasIptWord = true;
                                    break;
                                }
                            }
                            if (hasIptWord) {
                                termIndex.get(senIndex)[0] = 0;
                            } else {
                                if (conj.Conj[indexOfConj][2].equals("A")) {
                                    termIndex.get(senIndex)[0] = jConj + 1;
                                } else {
                                    termIndex.get(senIndex)[1] = jConj - 1;
                                }
                            }
                        }
                    }
                } else if (hasFConj == true && conj.Conj[indexOfConj][1] == null) {
                    boolean hasIptWord = false;
                    for (int i = 0; i <= iConj; i++) {
                        Datum di = sen.get(i);
                        if (di.importance == true) {
                            hasIptWord = true;
                        }
                    }
                    if (hasIptWord) {
                        termIndex.get(senIndex)[0] = 0;
                    } else {
                        if ("A".equals(conj.Conj[indexOfConj][2])) {
                            termIndex.get(senIndex)[0] = iConj + 1;
                        } else {
                            termIndex.get(senIndex)[1] = iConj - 1;
                        }
                    }
                }
                if ("và".equals(sen.get(termIndex.get(senIndex)[1]).word) || "hoặc".equals(sen.get(termIndex.get(senIndex)[1]).word)
                        || "và ".equals(sen.get(termIndex.get(senIndex)[1]).word) || "hoặc ".equals(sen.get(termIndex.get(senIndex)[1]).word)) {
                    termIndex.get(senIndex)[1]--;
                }
                if (sen.get(termIndex.get(senIndex)[0]).word.equals(",") || sen.get(termIndex.get(senIndex)[0]).word.equals(",")) {
                    termIndex.get(senIndex)[0]++;
                }

            }
        }


        int numOfWords = 0;
        Set<Integer> senExclude = new HashSet<>();
        boolean is120Words = false;
        int count = 1;
        while (is120Words == false) {
            int words = 0;
            int sizeOfSens = sens.size();
            for (List<Datum> sen : sens) {
                if (!senExclude.contains(sens.indexOf(sen))) {
                    for (Datum dt : sen) {
                        if (!dt.chunk.equals("O")) {
                            words++;
                        }
                    }
                }
            }
            if (words > wordMax) {
                senExclude.add(SentenceExtraction.mapSenOrderByScore.get(sizeOfSens - count));
                count++;
            } else {
                is120Words = true;
            }
        }

        /// Done, decoration
        for (List<Datum> sen : sens) {
            //Capitalize the first word in a iSentence
            if (!senExclude.contains(sens.indexOf(sen))) {
                int senIndex = sens.indexOf(sen);
                if (termIndex.get(senIndex)[0] != null && termIndex.get(senIndex)[1] != null) {
                    sen.get(termIndex.get(senIndex)[0]).word = WordUtils.capitalize(sen.get(termIndex.get(senIndex)[0]).word);
                    for (Datum d : sen) {
                        if (!d.chunk.endsWith("O")) {
                            numOfWords++;
                        }
                        if (sen.indexOf(d) >= termIndex.get(senIndex)[0] && sen.indexOf(d) <= termIndex.get(senIndex)[1]) {
                            if (!d.word.contains("nlp.sentenceExtraction.Datum")) {
                                System.out.print(d.word);
                                outString += d.word.replace("_", " ");
                                outString += " ";
                                System.out.print(" ");
                            }
                        }
                    }
                    System.out.println(" ");
                    outString += "\n";
                }
            }
        }
        System.out.println("Number of words: " + numOfWords);
        String outputFilename = "corpus/AutoSummary/" + inputNum + ".txt";
        try (FileWriter fwriter = new FileWriter(new File(outputFilename))) {
            fwriter.write(outString);
        }

    }
}
