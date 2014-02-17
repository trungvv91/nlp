/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.display;

import nlp.sentenceExtraction.IdfScore;

/**
 *
 * @author Manh Tien
 */
public class TestGraphic {

    /**
     */
    public IdfScore idf = new IdfScore();
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable(){
            @Override
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
