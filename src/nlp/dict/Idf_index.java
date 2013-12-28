/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Manh Tien
 */
public class Idf_index {

    public static Map<String, Double> map = new HashMap<String, Double>();
    public static void main(String[] args) throws IOException{
        File folder = new File("C:\\Users\\Manh Tien\\Documents\\NetBeansProjects\\NLP-Tools\\corpus");
        File[] listOfFiles = folder.listFiles();
        int numOfDocs = 0;
        for(int i=0; i < listOfFiles.length; i++){
            File filename = listOfFiles[i];
            if(filename.isFile()){
                BufferedReader br=new BufferedReader(new FileReader(filename));
                String docs="";
                String line="";
                while((line=br.readLine())!=null){
                    docs+=line.replace("<<a", " ").replace("<<t", " ").replace("<<c", " ").replace("/t>>", " ").replace("/c>>", " ");;
                }
                String[] doc = docs.split("/a>>");
                numOfDocs += doc.length;
                for(int j = 0; j < doc.length; j++){
                    String str = doc[j];
                    Set<String> set = new HashSet<String>();
                    String[] words = str.split("\\s+");
                    for(int k = 0; k < words.length; k++){
                        String word = words[k].toLowerCase();
                        if(set.contains(word) == false){
                            if(map.containsKey(word)){
                                map.put(word, map.get(word) + 1.0);
                            }
                            else{
                                map.put(word,1.0);
                            }
                            set.add(word);
                        }
                    }
                }
           }  
        }
        String str = "";
        for(String word: map.keySet()){
            double idf = Math.log((double)numOfDocs/(double)map.get(word));
            str += word + " " + idf + "\n";
        }
        FileWriter fw=new FileWriter(new File("C:\\Users\\Manh Tien\\Documents\\NetBeansProjects\\NLP-Tools\\output-data\\idf_index_test.txt"));
        fw.write(str);
        fw.close();
        System.out.println(numOfDocs + " " + map.size());
    }
}
