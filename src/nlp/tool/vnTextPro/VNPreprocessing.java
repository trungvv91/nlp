/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.tool.vnTextPro;

import java.io.*;

/**
 *
 * @author Manh Tien
 */
public class VNPreprocessing {

    /**
     * @param args the command line arguments
     */
    private static VNPreprocessing instance;

    public static VNPreprocessing getInstance() {
        if (instance == null) {
            instance = new VNPreprocessing();
        }
        return instance;
    }

    public static void preprocess(String inputFile, String outputFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
        String line;
        String str = "";
        while ((line = br.readLine()) != null) {
            str += line + "\n";
        }
        /// bỏ các phần trong ngoặc đơn
        str = str.replaceAll("\\(.+?\\)", "");

//        str2 = str2.replace("...", ".");
        /// viết thường các chữ cái đầu câu ???
        String[] sens = str.split("\\.");
        str = "";
        for (int i = 0; i < sens.length - 1; i++) {
            String s = sens[i].trim();
            str += Character.toLowerCase(s.charAt(0));
            str += s.substring(1);
            str += ". ";
        }
//
//        try (FileWriter fw = new FileWriter(new File(outputFile))) {
//            fw.write(str);
//        }
        
        VNTagger.WriteToFile(outputFile, str);
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
//        preprocess("output-data/hihi.txt", "output-data/hehe.txt");
    }
}
//toi (di (choi)) hay (hoac) di hoc.
