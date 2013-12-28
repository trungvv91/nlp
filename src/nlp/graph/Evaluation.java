/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.graph;

import java.io.*;
import nlp.tool.vnTextPro.VNTokenizer;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Manh Tien
 */
public class Evaluation {

    /**
     * @param args the command line arguments
     */
    public static double rouge1 (String sum, String source){
        double r = 0.0;
        String[] w = sum.split("\\s+");
        double match = 0.0;
        for(int i = 0; i < w.length; i++){
            if(StringUtils.contains(source, w[i]))
                match += 1.0;
        }
        r = match/(double)(source.length());
        return r;
    }
    public static double rouge2(String sum, String source){
        double r = 0.0;
        String[] w= sum.split("\\s+");
        double match = 0.0;
        for(int i = 0; i < w.length - 1; i++){
            String bigram = w[i] + " " + w[i+1];
            if(StringUtils.contains(source, bigram)){
                match += 1.0;
            }
        }
        r = match/(double)(source.length());
        return r;
    }
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        String fileAuto;
        String fileSum;
        String pathAuto = "corpus/AutoSummary/";
        String pathSum = "corpus/Summary/";
        File folder = new File(pathAuto);
        File[] listOfFiles = folder.listFiles();
        int countSen = 0;
        VNTokenizer tokenizer = VNTokenizer.getInstance();
        double r1 = 0.0;
        double r2 = 0.0;
        int wordsSum = 0;
        int wordsAuto = 0;
        for(int i = 0; i < listOfFiles.length; i++)
        {
            if(listOfFiles[i].isFile())
            {
                fileAuto = pathAuto+listOfFiles[i].getName();
                fileSum = pathSum+listOfFiles[i].getName();
                String strAuto = "";
                String strSum = "";
                BufferedReader brAuto = new BufferedReader(new FileReader(new File(fileAuto)));
                String lineAuto = "";
                while((lineAuto = brAuto.readLine())!=null){
                    strAuto += lineAuto + "\n";
                }
                brAuto.close();
                BufferedReader brSum = new BufferedReader(new FileReader(new File(fileSum)));
                String lineSum = "";
                while((lineSum = brSum.readLine())!=null){
                    strSum += lineSum;
                }
                brSum.close();
                strAuto = tokenizer.tokenize(strAuto);
                String[] auto = strAuto.split("\\s+");
                wordsAuto += auto.length;
//                System.out.println(strAuto);
                strSum = tokenizer.tokenize(strSum);
                String[] sum = strSum.split("\\s+");
                wordsSum += sum.length;
//                System.out.println(strSum);
                r1 += rouge1(strAuto, strSum);
                r2 += rouge2(strAuto, strSum);
            }
        }
        System.out.println(r1/listOfFiles.length);
        System.out.println(r2/listOfFiles.length);
        System.out.println("Auto: " + wordsAuto);
        System.out.println("Sum: " + wordsSum);
    }
}
