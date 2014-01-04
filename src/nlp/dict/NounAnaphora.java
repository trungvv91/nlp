/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author tien
 */
public class NounAnaphora {
    // <editor-fold defaultstate="collapsed" desc="mảng static chứa các đồng tham chiếu thường gặp">
    public final static String NA1[] = {
        "anh_ấy",
        "anh này",
        "anh_ta",
        "ả",
        "ả ta",
        "bà ấy",
        "bà này",
        "bà ta",
        "bác ấy",
        "bác này",
        "chị ấy",
        "chị ta",
        "chị này",
        "cô này",
        "cô ấy",
        "cô ta",
        "chú ấy",
        "chú này",
        "hắn",
        "hắn ta",
        "ông ấy",
        "ông này",
        "mợ ấy",
        "mụ ấy",
        "mụ ta",
        "người này",
        "người đó",
        "thằng ấy",
        "thím ấy",
        "ông ta",
        "thằng",
        "thằng này",
        "y", 
        "anh_chàng đó",
        "anh_chàng ấy",
        "anh_chàng này",
        "cô_nàng đó",
        "cô_nàng này",
        "cô_nàng ấy",
        "cô_bé đó",
        "cô_bé này",
        "cô_bé ấy",
        "cậu_bé đó",
        "cậu_bé này",
        "cậu_bé ấy"
    };
    
    public final static String NA2[] = {
        "anh",
        "chị",
        "em",
        "bố",
        "mẹ",
        "cô",
        "dì",
        "chú",
        "bác",
        "ông",
        "bà",
        "thím",
        "giám_đốc",
        "chủ_tịch",
        "trưởng_phòng",
        "bệnh_nhân"
    };    
    // </editor-fold>
    
    public static boolean checkNounAnophoric1(String s)
    {               
//        int index = Arrays.binarySearch(NA1, s);
//        return (index >= 0);
        String s1 = s.toLowerCase();
        for (int i = 0; i < NA1.length; i++) {
            if (s1.equals(NA1[i])) {
                return true;
            }            
        }
        return false;
    }
    
    public static boolean checkNounAnophoric2(String s)
    {               
        String s1 = s.toLowerCase();
        for (int i = 0; i < NA2.length; i++) {
            if (s1.equals(NA2[i])) {
                return true;
            }            
        }
        return false;
    }
    
    public ArrayList<String> getAnophoricList1() {
        ArrayList<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(NA1));
        return words;
    }
    
    public ArrayList<String> getAnophoricList2() {
        ArrayList<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(NA2));
        return words;
    }
    
    public static void main(String[] args) {
        System.out.println(NounAnaphora.checkNounAnophoric2("bệnh_nhân"));
    }
}
