/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

import java.io.*;

/**
 *
 * @author Manh Tien
 */
public class Idf_normalize {

    /**
     * @param args the command line arguments
     */
    public static String[] kitu = {
      ".",
      ",",
      ">",
      "<",
      "=",
      ")",
      "(",
      "}",
      "{",
      "/",
      "…",
      "%",
      "!",
      "@",
      "&",
      "*",
      "+",
      "~",
      "`",
      "?",
      "#",
      "[",
      "]",
      "”",
      "“",
      "\"",
      "'",
      ":",
      ";",
      "^"
    };
    
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        String file = "train-data/idf_index.txt";
        BufferedReader br = new BufferedReader(new FileReader(new File(file)));
        String str = "";
        String line = "";
        while((line=br.readLine())!=null)
        {
                String[] s = line.split(" ");
                boolean hasKitu = false;
                for(int i = 0; i < kitu.length; i++){
                    if(s[0].contains(kitu[i])){
                        hasKitu = true;
                    }
                }
                if(hasKitu == false){
                    str += line + "\n";
                }
        }
        br.close();
        FileWriter fw = new FileWriter("train-data/idf_final.txt");
        fw.write(str);
        fw.close();
    }
}
