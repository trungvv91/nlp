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
        String str2 = str.replaceAll("\\(.+?\\)", "");
//        str2 = str2.replace("...", ".");
        try (FileWriter fw = new FileWriter(new File(outputFile))) {
            fw.write(str2);
        }
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        preprocess("output-data/hihi.txt", "output-data/hehe.txt");
    }
}
//toi (di (choi)) hay (hoac) di hoc.
