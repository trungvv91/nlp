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
        int i = -1;
        String s1 = s.toLowerCase();
        for (i = 0; i < PUNCTUATIONS.length; i++) {
            if (s1.equals(PUNCTUATIONS[i])) {
                break;
            }            
        }
        return (i >= 0);
    }
    
    public static boolean isEndOfSentence(String s) {
        return (s.equals(".") || s.equals("?") || s.equals("!"));
    }
}
