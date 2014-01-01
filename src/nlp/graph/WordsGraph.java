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
import java.util.Random;
import java.util.Set;
import nlp.dict.Conjunction;
import nlp.sentenceExtraction.DatumUtil;
import nlp.sentenceExtraction.SentenceExtraction;
import nlp.tool.vnTextPro.VNTagger;
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
     * Determine keywords in datums list by set d.importance=true
     * @param numOfWordImportance 
     */
    public void setWordImportance(int numOfWordImportance) {
        System.out.println("Start word-importance set...");
        /// construct word graph
        List<Datum> vertex = new ArrayList<>();
        for (Datum d : datums) {
            if (!vertex.contains(d)) {
                vertex.add(d);
            }
        }
        int n = vertex.size();
        double values[] = new double[n];
        int indices[] = new int[n];
//        System.out.println("Vertex size is: " + vertex.size());
        for (int i = 0; i < n; i++) {
            values[i] = vertex.get(i).score;
            indices[i] = i;
        }
        QuickSort.QuickSortFunction(values, indices, 0, n - 1);
        for (int i = 0; i < numOfWordImportance; i++) {
            int index = indices[i];
            Datum dimp = vertex.get(index);
            for (Datum d : datums) {
                if (d.equals(dimp)) {
                    d.importance = true;
                }
            }
//            dimp.importance = true;
        }
        System.out.println("End word-importance set...");
    }

    public void mainWordGraph(String inputNum, List<Datum> dts, int wordMax) throws IOException {
        Synonym.initSynonymMap();
        datums = SentenceExtraction.ExtractSentences(inputNum, dts);

        //Lay 15% so tu la importance words
        setWordImportance(datums.size() * 15 / 100);

        ArrayList<ArrayList<Datum>> sens = DatumUtil.DatumToSentence(datums);

        // <editor-fold defaultstate="collapsed" desc="xét 2 câu liên tiếp --> trùng subject thì ghép">
        List<Datum> tmpList = new ArrayList<>();
        for (int i = 0; i < sens.size() - 2; i++) {
            List<Datum> seni = sens.get(i);
            int j = i + 1;
            int indexToAdd = -1;
            List<Datum> senj = sens.get(j);
            tmpList.clear();
            for (int k = 0; k < seni.size(); k++) {
                Datum dik = seni.get(k);
                for (int h = 0; h < senj.size(); h++) {
                    Datum djh = senj.get(h);

                    /// co-ref là N or Np ??? 
                    if (dik.equals(djh) && (dik.posTag.equals("N") || dik.posTag.equals("Np"))) {
                        String phrasei = "";
                        int endChunki = k;
                        /// tim iPhrase cua di va dj
                        if (k > 0 && seni.get(k - 1).iPhrase == dik.iPhrase) {
                            phrasei += seni.get(k - 1).word.toLowerCase() + " ";
                        }
                        phrasei += dik.word.toLowerCase();
                        if (k < seni.size() - 1 && seni.get(k + 1).iPhrase == dik.iPhrase) {
                            phrasei += seni.get(k + 1).word.toLowerCase();
                            endChunki = k + 1;
                        }

                        String phrasej = "";
                        int endChunkj = h;
                        if (h > 0 && senj.get(h - 1).iPhrase == djh.iPhrase) {
                            phrasej += senj.get(h - 1).word + " ";
                        }
                        phrasej += djh.word.toLowerCase();
                        if (k < senj.size() - 1 && senj.get(h + 1).iPhrase == djh.iPhrase) {
                            phrasej += senj.get(h + 1).word.toLowerCase();
                            endChunkj = h + 1;
                        }
                        /// Bệnh_nhân Vũ_Dư vs. Vũ_Dư
                        if ((phrasej.contains(phrasei) || phrasei.contains(phrasej))
                                && endChunki < seni.size() - 1
                                && (seni.get(endChunki + 1).posTag.equals("R")
                                || seni.get(endChunki + 1).posTag.equals("V"))) {
                            boolean hasImportantWord = false;
                            for (int l = 0; l < k; l++) {
                                if (seni.get(l).importance == true) {
                                    hasImportantWord = true;    /// trước dik có 1 từ important
                                    break;
                                }
                            }
                            if (hasImportantWord == false) {
                                indexToAdd = endChunkj + 1;
                                for (int l = endChunki + 1; l < seni.size() - 1; l++) {
                                    tmpList.add(seni.get(l));   /// phần còn lại của câu seni
                                }
                            }
                        }
                    }
                }
            }       // end for k

            /// ghép seni và senj
            if (!tmpList.isEmpty() && indexToAdd > -1) {
                senj.addAll(indexToAdd, tmpList);
                senj.add(indexToAdd + tmpList.size(), new Datum("và", "C"));
                sens.remove(i);
                i--;
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="xét 2 câu liên tiếp --> trùng verb thì ghép">
        for (int i = 0; i < sens.size() - 2; i++) {
            //ghep 2 cau neu trung Verb
            List<Datum> seni = sens.get(i);
            List<Datum> senj = sens.get(i + 1);

            /// tìm subject
            String verbi = "";
            for (Datum di : seni) {
                if (di.posTag.equals("V")) {
                    for (int k = seni.indexOf(di); seni.get(k).iPhrase == di.iPhrase; k++) {
                        verbi += seni.get(k).word + " ";
                    }
                    break;
                }
            }

            String verbj = "";
            for (Datum dj : senj) {
                if (dj.posTag.equals("V")) {
                    for (int k = senj.indexOf(dj); senj.get(k).iPhrase == dj.iPhrase; k++) {
                        verbj += senj.get(k).word + " ";
                    }
                    break;
                }
            }

            if (verbi.equals(verbj)) {
                Random r = new Random();
                String[] list = {"và", ","};
                Datum dt = new Datum(list[r.nextInt(2)], "C");
                dt.chunk = "C";
                dt.stopWord = true;
                int k;
                for (k = 0; seni.get(k).iPhrase == 0; k++) {
                }
                seni.add(k, dt);
                for (int l = 0; senj.get(l).iPhrase == 0; l++) {
                    seni.add(k + 1, senj.get(l));
                }
                sens.remove(i + 1);     // remove senj
            }
        }   /// end for
        // </editor-fold>

        System.out.println("----------");
        ArrayList<Integer[]> termIndex = new ArrayList<>();
        for (List<Datum> sen : sens) {
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

    public static void main(String[] args) {
        try {
            WordsGraph graph = new WordsGraph();
            VNTagger tagger = VNTagger.getInstance();
            List<Datum> datums = tagger.tagger("0");
            graph.mainWordGraph("0", datums, 20);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
