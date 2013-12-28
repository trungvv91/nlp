package nlp.tool.vnTextPro;

import nlp.tool.vnTextPro.VNPreprocessing;
import java.io.IOException;
import nlp.util.StringUtil;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class VNTokenizer {
	private VietTokenizer tokenizer;
        private static VNTokenizer instance;
	private VNTokenizer(){
		this("config/tokenizer.properties");
	}
	
        public static VNTokenizer getInstance() {
            if (instance == null) {
                instance = new VNTokenizer();
            }
            return instance;
        }
        
	public VNTokenizer(String properties){
		tokenizer = new VietTokenizer(properties);
	}
	
	// Tokenize a string
	public String tokenize(String str){
		String[] token = tokenizer.tokenize(str);
		return StringUtil.ArrayToString(token, " ");
	}
	
	// Tokenize a file and write to another file which same format 
	public void tokenize(String inputFile, String outputFile) {
		tokenizer.turnOnSentenceDetection();
		tokenizer.tokenize(inputFile, outputFile);
	}
	// Tokenize a file and write to another file which multi line format
	public void tokenize2(String inputFile, String outputFile) {
		tokenizer.turnOnSentenceDetection();
		tokenizer.tokenize2(inputFile, outputFile);
	}
	
	// Tokenize a directory and write to another directory
	public void tokenizeDir(String inputDir, String outputDir) {
		tokenizer.turnOnSentenceDetection();
		tokenizer.tokenizeDirectory(inputDir, outputDir);
	}
	
	public static void main(String[] args) throws IOException{
		//String str = "Người mẫu cao tuổi trong bức hình đó là Thúy Hạnh";
		VNTokenizer tokenizer = new VNTokenizer();
		
		//String returnStr = tokenizer.tokenize(str);
		//System.out.println(returnStr);
		
//		String inputFile = "data/1-source.txt";
//		String outputFile = "data/1-token.txt";
//                String outputFilePre = "temp/1-token-temp.txt";
//                VNPreprocessing.preprocess(inputFile, outputFilePre);
//		String outputFile2 = "output-data/vnTokenizer-test-output2.txt";
//		
//		tokenizer.tokenize(outputFilePre, outputFile);
//		tokenizer.tokenize2(inputFile, outputFile2);
                String test = "Ông ấy rât là tham vọng.người đầy tham vọng và thông minh";
                String result = tokenizer.tokenize(test);
                System.out.println(result);
                String test2 = "Hôm nay trời nắng chang chang. Mèo con đi học chẳng mang cái gì";
                System.out.println(tokenizer.tokenize(test2));
	}
}
