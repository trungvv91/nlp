package nlp.tool.vnTextPro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jvnpostag.CRFTagger;
import jvnpostag.MaxentTagger;
import jvnpostag.POSTagger;
import nlp.dict.Punctuation;
import nlp.dict.Stopword;
import nlp.sentenceExtraction.Datum;
import nlp.sentenceExtraction.IdfScore;
import nlp.util.CmdTest;

public class VNTagger {

    private static VNTagger instance;
    private static POSTagger tagger;

    public static VNTagger getInstance() {
        if (instance == null) {
            instance = new VNTagger();
        }
        return instance;
    }

    private VNTagger() {
        this("maxent", "models/vntagger/maxent");
    }

    public VNTagger(String algorithm, String modelRes) {
        init(algorithm, modelRes);
    }

    private void init(String algorithm, String modelRes) {
        if ("maxent".equalsIgnoreCase(algorithm)) {
            tagger = new MaxentTagger(modelRes);
        } else {
            tagger = new CRFTagger(modelRes);
        }
    }

//    public void tag(String inputFile, PrintStream out) {
//        String returnStr = tagger.tagging(new File(inputFile));
//        String[] str = StringUtil.StringToArray(returnStr, new String[]{"\n"});
//        for (String s : str) {
//            out.println(s);
//        }
//    }
    public String tag(String str) {
        return tagger.tagging(str);
    }

    /**
     * POS tagging
     * @param inputNum
     * @return
     * @throws IOException 
     */
    public List<Datum> tagger(String inputNum) throws IOException {
        String fileNameSource = "corpus/Plaintext/" + inputNum + ".txt";
        String outputFileToken = "data/" + inputNum + "-token.txt";
        String outputFilePre = "data/" + inputNum + "-token-temp.txt";

        VNTokenizer token = VNTokenizer.getInstance();
        VNPreprocessing.preprocess(fileNameSource, outputFilePre);
        token.tokenize(outputFilePre, outputFileToken);
        File deleteTmpFile1 = new File(outputFilePre);
        deleteTmpFile1.delete();

        String str = "";
        try (BufferedReader br = new BufferedReader(new FileReader(
                        new File(outputFileToken)))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                str += line.replace(",", " , ").replace(":", " : ").replace("-", " - ").replace(".", " . ").replace(";", " ; ").replace("“", "").replace("”", "").replace("\"", "") + "\n";
            }
        }
        str = str.replace(".  .  .", "...");
//        System.out.println(str);
        String strPos = tag(str).trim().replace(" ", "\n").replace("/", " ");
        String outputFileTagger = "data/" + inputNum + "-postag.txt";
        try (FileWriter fw2 = new FileWriter(new File(outputFileTagger))) {
            fw2.write(strPos);
//            System.out.println("Print strPos:\n" + strPos);
        }

        /// //Chay command line cua chuong trinh VietChunker
        try {
            CmdTest.runCommand(inputNum);
        } catch (Exception e) {
            System.out.println("Can't run VietChunker. Error : " + e);
        }

        String fileName = "data/" + inputNum + "-chunk.txt";        //Input Tagger file
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if (!"".equals(line)) {
                    lines.add(line);    // each line of the form: Vũ_Dư	Np	I-NP
                }
            }
        }

        List<Datum> datums = new ArrayList<>();
        int nLines = lines.size();
        int sentenceCounter = 0;
        int phrase = -1;         // set iphrase of a word

        for (int i = 0; i < nLines; i++) {
//            System.out.println("old: " + datums.get(i).tf);
            String word = lines.get(i);
            String[] w = word.split("\\s");
            Datum d = new Datum(w[0], w[1]);
            d.iSentence = sentenceCounter;
            d.chunk = w[2];
            if (d.chunk.contains("B-")) {
                phrase++;
            }
            d.iPhrase = phrase;

            if (Stopword.checkStopWord(w[0]) && !"Np".equals(d.posTag)) {
                d.stopWord = true;
            }
            if (Punctuation.isEndOfSentence(d.word)) {
                sentenceCounter++;
                phrase = -1;
            } else if (d.posTag.equals("E") || d.posTag.equals("M") || d.word.toLowerCase().equals("không")) {
                d.semiStopWord = true;
            }

            if (Punctuation.checkPuctuation(d.word)) {
                d.chunk = "O";      /// dấu câu
                if (i < nLines - 1) {
                    lines.set(i + 1, lines.get(i + 1).replace("I-", "B-"));      /// continue --> begin nếu trước đó là dấu câu
                }
            }
//            System.out.println("old: " + datums.get(i).posTag + " vs. new: " + d.posTag);
//            datums.set(i, d);

            datums.add(d);
        }

        /// Calculate tf-idf score
        System.out.println("Start of idf-scoring...");
        IdfScore idfScore = new IdfScore();
        Map<String, Double> maps = idfScore.getIdfScoreMap(datums);
        for (Datum di : datums) {
            if (di.tf == 0) {
                int count = 1;
                int i = datums.indexOf(di);
                for (int j = i + 1; j < datums.size(); j++) {
                    Datum dj = datums.get(j);
                    if (di.equals(dj)) {
                        count++;
                        dj.tf = -i;     /// !!! cần tốt hơn
                    }
                }
                di.tf = count;
            } else if (di.tf < 0) {
                di.tf = datums.get(-di.tf).tf;
            }
            di.idf = maps.get(di.word.toLowerCase());
            di.score = di.idf * di.tf;
        }
        System.out.println("End of idf-scoring...");

        return datums;
    }

    public static void main(String[] args) {
        VNTagger ins = VNTagger.getInstance();
        //String s = ins.tag("Bệnh nhân Vũ Dư dùng thuốc Biseptol.");
        //System.out.println(s);
        try {
            List<Datum> datums = ins.tagger("0");
            System.out.println(datums.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
