/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manh Tien
 * Conjunction for 
 */
public class Conjunction {
    public List<List<String>> conj= new ArrayList<>();
    
    public final static String DECLARE_WORDS[] = {
        "nêu_rõ",
        "cho_biết",
        "tuyên_bố",
        "phát_biểu",
        "nhấn_mạnh",
        ":"
    };
    
    public final static String CONJUNCTIONS[][] =
    {
        {"nếu_như", "thì", "A"},
        {"nếu", "thì", "A"},
        {"mặc_dù", "nhưng", "A"},
        {"dù", "nhưng", "A"},
        {"tuy", "nhưng", "A"},
        {"dầu_cho", "nhưng", "A"},
        {"khi", ",", "A"},
        {"nhờ_có", ",", "A"},
        {"nhờ", ",", "A"},
        {"nhờ_có", null, "B"},
        {"nhờ", null, "B"},
        {"miễn_là", "thì", "A"},
        {"chỉ_khi", "thì", "A"},
        {"trừ_khi", "thì", "A"},
        {"miễn_là", null, "B"},
        {"chỉ_khi", null, "B"},
        {"trừ_khi", null, "B"},
        {"thay_vì", ",", "A"},
        {"thay_vì", null, "B"},
        {"do", null, "B"},
        {"nhưng", null, "A"},
        {", trong_đó", null, "B"},
        {"thành_ra", null, "A"},
        {"có_nghĩa_là", null, "B"},
        {"nghĩa_là", null, "B"},
        {"tức_là", null, "B"},
        {"chẳng_hạn", null, "B"},
        {"chẳng_hạn_như", null, "B"},
        {"ví_dụ", null, "B"},
        {"ví_dụ_như", null, "B"}
//        {"bởi_vì", "nên", "A"},
//        {"bởi_vì", "cho_nên", "A"},
//        {"chính_vì", "nên", "A"},
//        {"chính_vì", "cho_nên", "A"},
//        {"bởi", "nên", "A"},
//        {"bởi", "cho_nên", "A"},
//        {"vì", "nên", "A"},
//        {"vì", "cho_nên", "A"},
//        {"do", "nên", "A"},
//        {"do", "cho_nên", "A"},
//        {"bởi_vì", null, "B"},
//        {"chính_vì", null, "B"},
//        {"bởi", null, "B"},
//        {"vì", null, "B"},
//        {"để_cho", ",", "A"},
//        {"cốt_cho", ",", "A"},
//        {"nhằm", ",", "A"},
//        {"nhằm", "thì", "A"},
//        {"sau_khi", ",", "A"},
//        {"ngay_khi", ",", "A"},
//        {"trước_khi", ",", "A"},
//        {"vào_lúc", ",", "A"},
//        {"cùng_lúc", ",", "A"},
//        {"cho_đến_khi", ",", "A"},
        
//        {"do_vậy_nên", null, "A"},
//        {"do_vậy", null, "A"},
//        {"cho_nên", null, "A"},
//        {"thành_ra", null, "A"},
//        {"nên", null, "A"},      
    };
    
    public static boolean checkDeclareWord(String s) {
//        int index = Arrays.binarySearch(STOPWORDS, s);
//        return (index >= 0);
        String s1 = s.toLowerCase();
        for (int i = 0; i < DECLARE_WORDS.length; i++) {
            if (s1.equals(DECLARE_WORDS[i])) {
                return true;
            }
        }
        return false;
    }
}
