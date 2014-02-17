package nlp.util;


public class StringUtil {
	
	static public String ArrayToString(String[] str, String separator) {
		String resultStr = "";
		for(int i = 0; i < str.length; i++) {
			if(i != str.length - 1) resultStr += str[i] + separator + " ";
			else resultStr += str[i];
		}
		return resultStr;
	}
	
	static public String[] StringToArray(String str, String ... separator) {
		String separatorRegex = "[" + ArrayToString(separator, "") + "]";
		return str.split(separatorRegex);
	}
        
}
