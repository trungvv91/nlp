/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

import java.io.*;
import java.util.*;

/**
 *
 * @author Manh Tien
 */
public class Synonym {

    public static Map<String, String> synonymMap = new HashMap<>();

    public static boolean checkSynonum(String str) {
        if (synonymMap.containsKey(str.toLowerCase()) == true) {
            return true;
        }
        return false;
    }

    public static String[] synonym(String str) {
        String tmp = synonymMap.get(str.toLowerCase());
        String[] array = tmp.split("\\,");
        return array;
    }

    /**
     * Setup synonymMap
     * @throws IOException 
     */
    public static void initSynonymMap() throws IOException {
        String input = "train-data/VNsynonym.txt";
//        String output = "train-data/out.txt";
        String line = "";
        BufferedReader br = new BufferedReader(new FileReader(new File(input)));
        while ((line = br.readLine()) != null) {
            if (line.contains("::") == false) {
                String[] tmp = line.split("\\:");
                synonymMap.put(tmp[0], tmp[1]);
            }
        }
    }
}
