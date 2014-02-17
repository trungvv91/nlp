package nlp.tool.vnTextPro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jvnpostag.CRFTagger;
import jvnpostag.MaxentTagger;
import jvnpostag.POSTagger;
import nlp.dict.NounAnaphora;
import nlp.dict.Punctuation;
import nlp.dict.Stopword;
import nlp.sentenceExtraction.Datum;
import nlp.sentenceExtraction.IdfScore;
import nlp.util.CmdTest;
import org.apache.commons.lang3.StringUtils;

public class VNTagger {

    private static VNTagger instance;
    private static POSTagger tagger;
    private final Stopword stopword = new Stopword();
    private final NounAnaphora nounAnaphora = new NounAnaphora();

    public static void WriteToFile(String filename, String text) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            bw.write(text);
        } catch (IOException ex) {
            Logger.getLogger(VNTagger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<String> ReadFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!"".equals(line)) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VNTagger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VNTagger.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lines;
    }

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
     *
     * @param inputNum
     * @return
     * @throws IOException
     */
    public ArrayList<Datum> tagger(String inputNum) throws IOException {
        String fileNameSource = "corpus/Plaintext/" + inputNum + ".txt";
        String outputFileToken = "data/" + inputNum + "-token.txt";
        String outputFilePre = "data/" + inputNum + "-token-temp.txt";

        VNTokenizer token = VNTokenizer.getInstance();
        VNPreprocessing.preprocess(fileNameSource, outputFilePre);
        token.tokenize(outputFilePre, outputFileToken);
        File deleteTmpFile1 = new File(outputFilePre);
        deleteTmpFile1.delete();

        String str = "";
        List<String> tokenList = VNTagger.ReadFile(outputFileToken);
        for (String line : tokenList) {
            str += line.replace(",", " , ").replace(":", " : ").replace("-", " - ").replace(".", " . ").replace(";", " ; ")
                    .replace("“", "").replace("”", "").replace("\"", "") + "\n";
        }
        str = str.replace(".  .  .", "...");
        if ((int) str.charAt(0) == 65279) {
            str = str.substring(1).trim();
        }
        String strPos = tag(str).trim().replace(" ", "\n").replace("/", " ");
        String outputFileTagger = "data/" + inputNum + "-postag.txt";
        VNTagger.WriteToFile(outputFileTagger, strPos);

        /// Chay command line cua chuong trinh VietChunker
        /// đã có file -postag.txt
        try {
            CmdTest.runCommand(inputNum);
        } catch (Exception e) {
            System.out.println("Can't run VietChunker. Error : " + e);
        }

        String fileName = "data/" + inputNum + "-chunk.txt";        //Input Tagger file
        List<String> lines = VNTagger.ReadFile(fileName);           // each line of the form: Vũ_Dư	Np	I-NP

        ArrayList<Datum> datums = new ArrayList<>();
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

            if (nounAnaphora.isNounAnaphora1(d.word)) {
                d.posTag = "P";
            } else if (d.word.equals("ấy") || d.word.equals("này") || d.word.equals("đó") || d.word.equals("ta")) {
                d.posTag = "Nb";
            } else if (d.word.equals("họ")) {
                d.posTag = "P";
            } else if (d.word.equals("của")) {      // mẹ của Bách
                if (nounAnaphora.isNounAnaphora1(datums.get(i - 1).word)) {
                    datums.get(i - 1).posTag = "N";
                }
            } else if (d.word.equals("nó")) {
                d.posTag = "P";
                if (datums.get(i - 1).word.equals("chúng")) {
                    datums.get(i - 1).posTag = "Nc";
                }
            } else if (d.word.equals("anh_ấy") || d.word.equals("anh_ta") || d.word.equals("chị_ta")) {      /// StringUtils.uncapitalize
                String[] split = d.word.split("_");
                d.word = split[1];
                d.posTag = "Nb";
                Datum d1 = new Datum(split[0], "P");
                d1.iSentence = d.iSentence;
                d1.chunk = d.chunk;
                d1.iPhrase = d.iPhrase;
                datums.add(d1);
                d.chunk = "I-NP";
            }

            if (stopword.isStopWord(d.word) && !"Np".equals(d.posTag)) {
                d.stopWord = true;
            }

            if (Punctuation.isEndOfSentence(d.word)) {
                sentenceCounter++;
                phrase = -1;
            } else if (d.posTag.equals("E") || d.posTag.equals("M") || d.word.equals("không")) {
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
        String fileNameSource = "corpus/Plaintext/test.txt";
        String strTest = "Việc xử lý ngôn ngữ tự nhiên là dùng thuốc Biseptol. Sau khi uống, anh ấy đã khỏi bệnh.";
//        String strTest = "Bố Bách mua thuốc về cho Bách uống. Sau khi uống, anh ấy bị đỏ môi.";
        VNTagger.WriteToFile(fileNameSource, strTest);

        List<Datum> datum;
        try {
            datum = ins.tagger("test");
            System.out.println("\n");
            System.out.println(strTest);
            System.out.println("");
            System.out.println(datum.toString());
        } catch (IOException ex) {
            Logger.getLogger(VNTagger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
