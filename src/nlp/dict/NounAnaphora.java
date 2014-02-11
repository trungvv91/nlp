/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private final ArrayList<String> listNounAnaphora1;
    private final ArrayList<String> listNounAnaphora2;
    private final String filename1 = "train-data/nounAnaphoric1.txt";
    private final String filename2 = "train-data/nounAnaphoric2.txt";

    public ArrayList<String> getNounAnaphora1() {
        return listNounAnaphora1;
    }

    public NounAnaphora() {
        listNounAnaphora1 = new ArrayList<>();
        listNounAnaphora2 = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(
                new File(filename1)))) {
            while ((line = br.readLine()) != null) {
                listNounAnaphora1.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Stopword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Stopword.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(
                new File(filename2)))) {
            while ((line = br.readLine()) != null) {
                listNounAnaphora2.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Stopword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Stopword.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isNounAnaphora1(String s) {
        String s1 = s.toLowerCase();
        for (String na : listNounAnaphora1) {
            if (s1.equals(na)) {
                return true;
            }
        }        
        return false;
    }
    
    public boolean isNounAnaphora2(String s) {
        String s1 = s.toLowerCase();
        for (String na : listNounAnaphora2) {
            if (s1.equals(na)) {
                return true;
            }
        }        
        return false;
    }
    
    public static void main(String[] args) {
//        System.out.println(NounAnaphora.checkNounAnophoric2("bệnh_nhân"));
        NounAnaphora na = new NounAnaphora();
        System.out.println(na.isNounAnaphora1("ông_ấy"));
    }
}
