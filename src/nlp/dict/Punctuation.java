/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.dict;

/**
 *
 * @author TRUNG
 */
public class Punctuation {
    public final static String PUNCTUATIONS[] = { ".", ",", "!", "?", ";", "”", ":" };
    
    public static boolean checkPuctuation(String s)
    {        
        String s1 = s.toLowerCase();
        for (int i = 0; i < PUNCTUATIONS.length; i++) {
            if (s1.equals(PUNCTUATIONS[i])) {
                return true;
            }            
        }
        return false;
    }
    
    public static boolean isEndOfSentence(String s) {
        return (s.equals(".") || s.equals("?") || s.equals("!"));
    }
}
