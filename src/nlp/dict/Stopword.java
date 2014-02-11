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
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manh Tien
 */
public class Stopword {

//    // <editor-fold defaultstate="collapsed" desc="mảng static chứa các stopwords">
//    public final static String STOPWORDS[] = {
//        ",",
//        ":",
//        ";",
//        "\"",
//        "-",
//        "'",
//        "“",
//        "”",
//        "﻿a_ha",
//        "a-lô",
//        "à_ơi",
//        "á",
//        "à",
//        "á_à",
//        "ạ",
//        "ạ_ơi",
//        "ai",
//        "ai_ai",
//        "ai_nấy",
//        "ái",
//        "ái_chà",
//        "ái_dà",
//        "alô",
//        "amen",
//        "áng",
//        "ào",
//        "ắt",
//        "ắt_hẳn",
//        "ắt_là",
//        "âu_là",
//        "ầu_ơ",
//        "ấy",
//        "bài",
//        "bản",
//        "bao_giờ",
//        "bao_lâu",
//        "bao_nả",
//        "bao_nhiêu",
//        "bay_biến",
//        "bằng",
//        "bằng_ấy",
//        "bằng_không",
//        "bằng_nấy",
//        "bắt_đầu_từ",
//        "bập_bà_bập_bõm",
//        "bập_bõm",
//        "bất_chợt",
//        "bất_cứ",
//        "bất_đồ",
//        "bất_giác",
//        "bất_kể",
//        "bất_kì",
//        "bất_kỳ",
//        "bất_luận",
//        "bất_nhược",
//        "bất_quá",
//        "bất_thình_lình",
//        "bất_tử",
//        "bây_bẩy",
//        "bây_chừ",
//        "bây_giờ",
//        "bây_giờ",
//        "bây_nhiêu",
//        "bấy",
//        "bấy_giờ",
//        "bấy_chầy",
//        "bấy_chừ",
//        "bấy_giờ",
//        "bấy_lâu",
//        "bấy_lâu_nay",
//        "bấy_nay",
//        "bấy_nhiêu",
//        "bèn",
//        "béng",
//        "bển",
//        "bệt",
//        "biết_bao",
//        "biết_bao_nhiêu",
//        "biết_chừng_nào",
//        "biết_đâu",
//        "biết_đâu_chừng",
//        "biết_đâu_đấy",
//        "biết_mấy",
//        "bộ",
//        "bội_phần",
//        "bông",
//        "bỗng",
//        "bỗng_chốc",
//        "bỗng_dưng",
//        "bỗng_đâu",
//        "bỗng_không",
//        "bỗng_nhiên",
//        "bỏ_mẹ",
//        "bớ",
//        "bởi",
//        "bởi_chưng",
//        "bởi_nhưng",
//        "bởi_thế",
//        "bởi_vậy",
//        "bởi_vì",
//        "bức",
//        "cả",
//        "cả_thảy     ",
//        "cái",
//        "các",
//        "cả_thảy",
//        "cả_thể",
//        "càng",
//        "căn",
//        "căn_cắt",
//        "cật_lực",
//        "cật_sức",
//        "cây",
//        "cha_",
//        "cha_chả",
//        "chành_chạnh",
//        "chao_ôi",
//        "chắc",
//        "chắc_hẳn",
//        "chăn_chắn",
//        "chăng",
//        "chẳng_lẽ",
//        "chẳng_những",
//        "chẳng_nữa",
//        "chẳng_phải",
//        "chậc",
//        "chầm_chập",
//        "chết_nỗi",
//        "chết_tiệt",
//        "chết_thật",
//        "chí_chết",
//        "chỉn",
//        "chính",
//        "chính_là",
//        "chính_thị",
//        "chỉ",
//        "chỉ_do",
//        "chỉ_là",
//        "chỉ_tại",
//        "chỉ_vì",
//        "chiếc",
//        "cho_đến",
//        "cho_đến_khi",
//        "cho_nên",
//        "cho_tới",
//        "cho_tới_khi",
//        "choa",
//        "chốc_chốc",
//        "chớ",
//        "chớ_chi",
//        "chợt",
//        "chú",
//        "chu_cha",
//        "chú_mày",
//        "chú_mình",
//        "chui_cha",
//        "chùn_chùn",
//        "chùn_chũn",
//        "chủn",
//        "chung_cục",
//        "chung_qui",
//        "chung_quy",
//        "chung_quy_lại",
//        "chúng_mình",
//        "chúng_ta",
//        "chúng_tôi",
//        "chứ",
//        "chứ_lị",
//        "có_chăng_là",
//        "có_dễ",
//        "có_vẻ",
//        "cóc_khô",
//        "coi_bộ",
//        "coi_mòi",
//        "con",
//        "còn",
//        "cô",
//        "cô_mình",
//        "cổ_lai",
//        "công_nhiên",
//        "cơ",
//        "cơ_chừng",
//        "cơ_hồ",
//        "cơ_mà",
//        "cơn",
//        "cu_cậu",
//        "của",
//        "cùng",
//        "cùng_cực",
//        "cùng_nhau",
//        "cùng_với",
//        "cũng",
//        "cũng_như",
//        "cũng_vậy",
//        "cũng_vậy_thôi",
//        "cứ",
//        "cứ_việc",
//        "cực_kì     ",
//        "cực_kỳ",
//        "cực_lực",
//        "cuộc",
//        "cuốn",
//        "dào",
//        "dạ",
//        "dần_dà",
//        "dần_dần",
//        "dầu_sao",
//        "dẫu",
//        "dẫu_sao",
//        "dễ_sợ",
//        "dễ_thường",
//        "do",
//        "do_vì",
//        "do_đó",
//        "do_vậy",
//        "dở_chừng",
//        "dù_cho",
//        "dù_rằng",
//        "duy",
//        "dữ",
//        "dưới",
//        "đã",
//        "đại_để",
//        "đại_loại",
//        "đại_nhân",
//        "đại_phàm",
//        "đang",
//        "đáng_lẽ",
//        "đáng_lí",
//        "đáng_lý",
//        "đành_đạch",
//        "đánh_đùng",
//        "đáo_để",
//        "nấy",
//        "nên_chi",
//        "nền",
//        "nếu",
//        "nếu_như",
//        "ngay",
//        "ngay_cả",
//        "ngay_lập_tức",
//        "ngay_lúc",
//        "ngay_khi",
//        "ngay_từ",
//        "ngay_tức_khắc",
//        "ngày_càng",
//        "ngày_ngày",
//        "ngày_xưa",
//        "ngày_xửa",
//        "ngăn_ngắt",
//        "nghe_chừng",
//        "nghe_đâu",
//        "nghen",
//        "nghiễm_nhiên",
//        "nghỉm",
//        "ngõ_hầu",
//        "ngoải",
//        "ngoài",
//        "ngôi",
//        "ngọn",
//        "ngọt",
//        "ngộ_nhỡ",
//        "ngươi",
//        "nhau",
//        "nhân_dịp",
//        "nhân_tiện",
//        "nhất",
//        "nhất_đán",
//        "nhất_định",
//        "nhất_loạt",
//        "nhất_luật",
//        "nhất_mực",
//        "nhất_nhất",
//        "nhất_quyết",
//        "nhất_sinh",
//        "nhất_tâm",
//        "nhất_tề",
//        "nhất_thiết",
//        "nhé",
//        "nhỉ",
//        "nhiên_hậu",
//        "nhiệt_liệt",
//        "nhón_nhén",
//        "nhỡ_ra",
//        "nhung_nhăng",
//        "như",
//        "như_chơi",
//        "như_không",
//        "như_quả",
//        "như_thể",
//        "như_tuồng",
//        "như_vậy",
//        "nhưng     ",
//        "nhưng_mà",
//        "những",
//        "những_ai",
//        "những_như",
//        "nhược_bằng",
//        "nó",
//        "nóc",
//        "nọ",
//        "nổi",
//        "nớ",
//        "nữa",
//        "nức_nở",
//        "oai_oái",
//        "oái",
//        "ô_hay",
//        "ô_hô",
//        "ô_kê",
//        "ô_kìa",
//        "ồ",
//        "ôi_chao",
//        "ôi_thôi",
//        "ối_dào",
//        "ối_giời",
//        "ối_giời_ơi",
//        "ôkê",
//        "ổng",
//        "ơ",
//        "ơ_hay",
//        "ơ_kìa",
//        "ờ",
//        "ớ",
//        "ơi",
//        "phải",
//        "phải_chi",
//        "phải_chăng",
//        "phăn_phắt",
//        "phắt",
//        "phè",
//        "phỉ_phui",
//        "pho",
//        "phóc",
//        "phỏng",
//        "phỏng_như",
//        "phót",
//        "phốc",
//        "phụt",
//        "phương_chi",
//        "phứt",
//        "qua_quít",
//        "qua_quýt",
//        "quả",
//        "quả_đúng",
//        "quả_là",
//        "quả_tang",
//        "quả_thật",
//        "quả_tình",
//        "quả_vậy",
//        "quá",
//        "quá_chừng",
//        "quá_độ",
//        "quá_đỗi",
//        "quá_lắm",
//        "quá_sá",
//        "quá_thể",
//        "quá_trời",
//        "quá_ư",
//        "quá_xá",
//        "quý_hồ",
//        "quyển",
//        "quyết",
//        "quyết_nhiên",
//        "ra",
//        "ra_phết",
//        "ra_trò",
//        "ráo",
//        "ráo_trọi",
//        "rày",
//        "răng",
//        "rằng",
//        "rằng_là",
//        "rất",
//        "rất_chi_là",
//        "rất_đỗi",
//        "rất_mực",
//        "ren_rén",
//        "rén",
//        "rích",
//        "riệt",
//        "riu_ríu",
//        "rón_rén",
//        "rồi",
//        "rốt_cục",
//        "rốt_cuộc",
//        "rút_cục",
//        "rứa",
//        "sa_sả     ",
//        "sạch",
//        "sao",
//        "sau_chót",
//        "sau_cùng",
//        "sau_cuối",
//        "sau_đó",
//        "sắp",
//        "sất",
//        "sẽ",
//        "sì",
//        "song_le",
//        "số_là",
//        "sốt_sột",
//        "sở_dĩ",
//        "suýt",
//        "sự",
//        "tà_tà",
//        "tại",
//        "tại_vì",
//        "tấm",
//        "tấn",
//        "tự_vì",
//        "tanh",
//        "tăm_tắp",
//        "tắp",
//        "tắp_lự",
//        "tất_cả",
//        "tất_tần_tật",
//        "tất_tật",
//        "tất_thảy",
//        "tênh",
//        "tha_hồ",
//        "thà",
//        "thà_là",
//        "thà_rằng",
//        "thái_quá",
//        "than_ôi",
//        "thanh",
//        "thành_ra",
//        "thành_thử",
//        "thảo_hèn",
//        "thảo_nào",
//        "thậm",
//        "thậm_chí",
//        "thật_lực",
//        "thật_vậy",
//        "thật_ra",
//        "thẩy",
//        "thế",
//        "thế_à",
//        "thế_là",
//        "thế_mà",
//        "thế_nào",
//        "thế_nên",
//        "thế_ra",
//        "thế_thì",
//        "thếch",
//        "thi_thoảng",
//        "thì",
//        "thình_lình",
//        "thỉnh_thoảng",
//        "thoạt",
//        "thoạt_nhiên",
//        "thoắt",
//        "thỏm",
//        "thọt",
//        "thốc",
//        "thốc_tháo",
//        "thộc",
//        "thôi",
//        "thốt",
//        "thốt_nhiên",
//        "thuần",
//        "thục_mạng",
//        "thúng_thắng",
//        "thửa",
//        "thực_ra",
//        "thực_vậy",
//        "thương_ôi",
//        "tiện_thể",
//        "tiếp_đó",
//        "tiếp_theo",
//        "tít_mù",
//        "tỏ_ra",
//        "tỏ_vẻ",
//        "tò_te",
//        "toà",
//        "toé_khói",
//        "toẹt",
//        "tọt",
//        "tốc_tả",
//        "tôi",
//        "tối_ư",
//        "tông_tốc",
//        "tột     ",
//        "tràn_cung_mây",
//        "trên",
//        "trển",
//        "trệt",
//        "trếu_tráo",
//        "trệu_trạo",
//        "trong",
//        "trỏng",
//        "trời_đất_ơi",
//        "trước",
//        "trước_đây",
//        "trước_đó",
//        "trước_kia",
//        "trước_nay",
//        "trước_tiên",
//        "trừ_phi",
//        "tù_tì",
//        "tuần_tự",
//        "tuốt_luốt",
//        "tuốt_tuồn_tuột",
//        "tuốt_tuột",
//        "tuy",
//        "tuy_nhiên",
//        "tuy_rằng",
//        "tuy_thế",
//        "tuy_vậy",
//        "tuyệt_nhiên",
//        "từng",
//        "tức_thì",
//        "tức_tốc",
//        "tựu_trung",
//        "ủa",
//        "úi",
//        "úi_chà",
//        "úi_dào",
//        "ư",
//        "ứ_hự",
//        "ứ_ừ",
//        "ử",
//        "ừ",
//        "và",
//        "vả_chăng",
//        "vả_lại",
//        "vạn_nhất",
//        "văng_tê",
//        "vẫn",
//        "vâng",
//        "vậy",
//        "vậy_là",
//        "vậy_thì",
//        "veo",
//        "veo_veo",
//        "vèo",
//        "về",
//        "vì",
//        "vì_chưng",
//        "vì_thế",
//        "vì_vậy",
//        "ví_bằng",
//        "ví_dù",
//        "ví_phỏng",
//        "ví_thử",
//        "vị_tất",
//        "vô_hình_trung",
//        "vô_kể",
//        "vô_luận",
//        "vô_vàn",
//        "vốn_dĩ",
//        "với",
//        "với_lại",
//        "vở",
//        "vung_tàn_tán",
//        "vung_tán_tàn",
//        "vung_thiên_địa",
//        "vụt",
//        "vừa_mới",
//        "xa_xả",
//        "xăm_xăm",
//        "xăm_xắm",
//        "xăm_xúi",
//        "xềnh_xệch",
//        "xệp",
//        "xiết_bao",
//        "xoành_xoạch",
//        "xoẳn",
//        "xoét",
//        "xoẹt",
//        "xon_xón",
//        "xuất_kì_bất_ý",
//        "xuất_kỳ_bất_ý",
//        "xuể",
//        "xuống",
//        "ý",
//        "ý_chừng",
//        "ý_da"
//    };
//    // </editor-fold>  
//    public static boolean checkStopWord(String s) {
////        int index = Arrays.binarySearch(STOPWORDS, s);
////        return (index >= 0);
//        String s1 = s.toLowerCase();
//        for (int i = 0; i < STOPWORDS.length; i++) {
//            if (s1.equals(STOPWORDS[i])) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public ArrayList<String> getStopWordList() {
//        ArrayList<String> words = new ArrayList<>();
//        words.addAll(Arrays.asList(STOPWORDS));
//        return words;
//    }
    private final ArrayList<String> stopwordList;
    private final String filename = "train-data/VNstopwords.txt";

    public ArrayList<String> getStopwordList() {
        return stopwordList;
    }

    public Stopword() {
        this.stopwordList = new ArrayList<>();
        // read txt file
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(
                new File(filename)))) {
            while ((line = br.readLine()) != null) {
                stopwordList.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Stopword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Stopword.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(stopwordList.size());
//        Collections.sort(stopwordList);
    }

    public boolean isStopWord(String s) {
        String s1 = s.toLowerCase();
        for (String sw : stopwordList) {
            if (s1.equals(sw)) {
                return true;
            }
        }
        return false;
//        int index = Collections.binarySearch(stopwordList, s1);
//        return index;
    }

    public static void main(String[] args) {
        //System.out.println(checkStopWord("xoẹt"));
        Stopword stopword = new Stopword();
        //stopword.getStopWords();
        System.out.println(stopword.isStopWord("xoet"));
        
//        Locale vnLocale = new Locale("vi", "VN");
//        Locale.setDefault(vnLocale);
//        Collator vnCollator = Collator.getInstance();
//        System.out.println(vnCollator.compare("a", "bá"));
//        System.out.println("xoẹt".compareTo("xa_xả"));
//        System.out.println("xoẹt".compareTo("xăm_xăm"));
//        System.out.println("xoẹt".compareTo("xềnh_xệch"));
    }

}
