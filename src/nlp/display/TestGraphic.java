/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.display;

import nlp.sentenceExtraction.IdfScore;
import nlp.tool.vnTextPro.VNTagger;

/**
 *
 * @author Manh Tien
 */
public class TestGraphic {

    /**
     * @param args the command line arguments
     */
    public IdfScore idf = new IdfScore();
    public static void main(String[] args) {
        // TODO code application logic here
        VNTagger tagger = VNTagger.getInstance();
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run() {
                TestFrame fr = new TestFrame();
//                if(fr.)
                fr.setSize(935, 550);
                fr.setResizable(false);
                fr.setVisible(true);
            }
        });
    }
}
