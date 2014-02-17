/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nlp.sentenceExtraction.Datum;
import nlp.sentenceExtraction.Sentence;
import nlp.tool.vnTextPro.VNTagger;

/**
 *
 * @author tien
 */
public class NounAnaphora {
//    // <editor-fold defaultstate="collapsed" desc="mảng static chứa các đồng tham chiếu thường gặp">
//    public final static String NA1[] = {
//        "anh_ấy",
//        "anh này",
//        "anh_ta",
//        "ả",
//        "ả ta",
//        "bà ấy",
//        "bà này",
//        "bà ta",
//        "bác ấy",
//        "bác này",
//        "chị ấy",
//        "chị này",
//        "chị ta",
//        "cô ấy",
//        "cô này",
//        "cô ta",
//        "chú ấy",
//        "chú này",
//        "hắn",
//        "hắn ta",
//        "ông ấy",
//        "ông này",
//        "mợ ấy",
//        "mụ ấy",
//        "mụ ta",
//        "người này",
//        "người đó",
//        "thằng ấy",
//        "thím ấy",
//        "ông ta",
//        "thằng",
//        "thằng này",
//        "y", 
//        "anh_chàng đó",
//        "anh_chàng ấy",
//        "anh_chàng này",
//        "cô_nàng đó",
//        "cô_nàng này",
//        "cô_nàng ấy",
//        "cô_bé đó",
//        "cô_bé này",
//        "cô_bé ấy",
//        "cậu_bé đó",
//        "cậu_bé này",
//        "cậu_bé ấy"
//    };
//    
//    public final static String NA2[] = {
//        "anh",
//        "chị",
//        "em",
//        "bố",
//        "mẹ",
//        "cô",
//        "dì",
//        "chú",
//        "bác",
//        "ông",
//        "bà",
//        "thím",
//        "giám_đốc",
//        "chủ_tịch",
//        "trưởng_phòng",
//        "bệnh_nhân"
//    };    
//    // </editor-fold>

//    public static boolean checkNounAnophoric1(String s)
//    {               
////        int index = Arrays.binarySearch(NA1, s);
////        return (index >= 0);
//        String s1 = s.toLowerCase();
//        for (int i = 0; i < NA1.length; i++) {
//            if (s1.equals(NA1[i])) {
//                return true;
//            }            
//        }
//        return false;
//    }
//    
//    public static boolean checkNounAnophoric2(String s)
//    {               
//        String s1 = s.toLowerCase();
//        for (int i = 0; i < NA2.length; i++) {
//            if (s1.equals(NA2[i])) {
//                return true;
//            }            
//        }
//        return false;
//    }
//    
//    public ArrayList<String> getAnophoricList1() {
//        ArrayList<String> words = new ArrayList<>();
//        words.addAll(Arrays.asList(NA1));
//        return words;
//    }
//    
//    public ArrayList<String> getAnophoricList2() {
//        ArrayList<String> words = new ArrayList<>();
//        words.addAll(Arrays.asList(NA2));
//        return words;
//    }
    /**
     * "anh + ấy"
     */
    private final ArrayList<String> listNounAnaphora1;

    /**
     * "mẹ của Bách"
     */
    private final ArrayList<String> listNounAnaphora2;

    private final String filename1 = "train-data/nounAnaphoric1.txt";
    private final String filename2 = "train-data/nounAnaphoric2.txt";

    public ArrayList<String> getNounAnaphora1() {
        return listNounAnaphora1;
    }

    public ArrayList<String> getListNounAnaphora2() {
        return listNounAnaphora2;
    }

    public NounAnaphora() {
        listNounAnaphora1 = VNTagger.ReadFile(filename1);
        listNounAnaphora2 = VNTagger.ReadFile(filename2);
    }

    /**
     * check "anh + ấy"
     *
     * @param s
     * @return
     */
    public boolean isNounAnaphora1(String s) {
//        String s1 = s.toLowerCase();
        for (String na : listNounAnaphora1) {
            if (s.equals(na)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check "mẹ của Bách"
     *
     * @param s
     * @return
     */
    public boolean isNounAnaphora2(String s) {
//        String s1 = s.toLowerCase();
        for (String na : listNounAnaphora2) {
            if (s.equals(na)) {
                return true;
            }
        }
        return false;
    }

    /// Java manipulates objects by reference, but it passes object references to methods by value
    /**
     * Phân giải đồng tham chiếu cho danh từ là chủ ngữ
     *
     * @param sentences
     */
    public void nounAnaphoring(ArrayList<Sentence> sentences) {
        /// Noun Anaphoric 1
        for (int i = 1; i < sentences.size(); i++) {
            Sentence seni = sentences.get(i);
            int[] indices = seni.getSubject();
            for (int j = indices[0]; j <= indices[1]; j++) {
                if (seni.dataList.get(j).posTag.equals("P")) {
                    Sentence seni_1 = sentences.get(i - 1);
                    int[] indices_1 = seni_1.getSubject();
                    /// anaphoring pronoun
                    seni.deletePhrase(indices[0], indices[1]);
                    for (int k = indices_1[1]; k >= indices_1[0]; k--) {
                        seni.dataList.add(indices[0], seni_1.dataList.get(k));
                    }
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
//        System.out.println(NounAnaphora.checkNounAnophoric2("bệnh_nhân"));
        NounAnaphora na = new NounAnaphora();
        System.out.println(na.isNounAnaphora1("ông_ấy"));
        
        VNTagger ins = VNTagger.getInstance();
        String fileNameSource = "corpus/Plaintext/test.txt";
        String strTest = "Bệnh nhân Vũ Dư dùng thuốc Biseptol. "
                + "Anh ấy bị biến chứng nặng.";
//        String strTest = "Bố Bách mua thuốc về cho Bách uống. Sau khi uống, anh ấy bị đỏ môi.";
        VNTagger.WriteToFile(fileNameSource, strTest);

        List<Datum> datum = null;
        try {
            datum = ins.tagger("test");
        } catch (IOException ex) {
            Logger.getLogger(VNTagger.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Sentence> sens = Sentence.DatumToSentence(datum);
        na.nounAnaphoring(sens);
        System.out.println("\n");
        System.out.println(strTest);
        System.out.println("");
         System.out.println(sens.toString());
    }
}
