/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TestFrame.java
 *
 * Created on Aug 31, 2013, 2:02:21 PM
 */
package nlp.display;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Manh Tien
 */
public class TestFrame extends javax.swing.JFrame {

    public String sourceText;
    public String sumText;
    
    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public void setSumText(String sumText) {
        this.sumText = sumText;
    }

    public String getSourceText() {
        return sourceText;
    }

    public String getSumText() {
        return sumText;
    }
    /** Creates new form TestFrame */
    public TestFrame() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        orginalDocumentLable = new javax.swing.JLabel();
        summaryDocumentLable = new javax.swing.JLabel();
        summaryButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        sourceTextArea = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        sumTextArea = new javax.swing.JTextPane();
        sourceNumOfWords = new javax.swing.JTextField();
        sumNumOfWords = new javax.swing.JTextField();
        orginalDocumentLable1 = new javax.swing.JLabel();
        orginalDocumentLable2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        orginalDocumentLable.setFont(new java.awt.Font("Tahoma", 1, 14));
        orginalDocumentLable.setText("Văn bản ban đầu");

        summaryDocumentLable.setFont(new java.awt.Font("Tahoma", 1, 14));
        summaryDocumentLable.setText("Văn bản tóm tắt");

        summaryButton.setText("Summary");
        summaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summaryButtonActionPerformed(evt);
            }
        });

        sourceTextArea.setMinimumSize(new java.awt.Dimension(450, 550));
        sourceTextArea.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                sourceTextAreaInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        sourceTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sourceTextAreaKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(sourceTextArea);
        sourceTextArea.getAccessibleContext().setAccessibleName("");
        sourceTextArea.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                String str = sourceTextArea.getText();
                String[] words = str.split("\\s+");
                sourceNumOfWords.setText(String.valueOf(words.length));
            }
        });

        sumTextArea.setMinimumSize(new java.awt.Dimension(450, 550));
        jScrollPane2.setViewportView(sumTextArea);

        sourceNumOfWords.setEnabled(false);
        sourceNumOfWords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceNumOfWordsActionPerformed(evt);
            }
        });

        sumNumOfWords.setText("120");
        sumNumOfWords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumNumOfWordsActionPerformed(evt);
            }
        });

        orginalDocumentLable1.setFont(new java.awt.Font("Tahoma", 1, 14));
        orginalDocumentLable1.setText("từ");

        orginalDocumentLable2.setFont(new java.awt.Font("Tahoma", 1, 14));
        orginalDocumentLable2.setText("từ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(orginalDocumentLable, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sourceNumOfWords, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orginalDocumentLable1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(summaryButton)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(summaryDocumentLable, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sumNumOfWords, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orginalDocumentLable2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(176, 176, 176))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourceNumOfWords, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(orginalDocumentLable)
                        .addComponent(summaryDocumentLable)
                        .addComponent(sumNumOfWords, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(orginalDocumentLable1)
                        .addComponent(orginalDocumentLable2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summaryButton)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void summaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_summaryButtonActionPerformed
        // TODO add your handling code here:
        
        if(sourceTextArea.getText()!=null){
            String out = "";
            sumTextArea.setText(null);
            int wordMax = Integer.parseInt(sumNumOfWords.getText());
            try {
                out = Compute.compute(sourceTextArea.getText(), wordMax);
            } catch (IOException ex) {
                Logger.getLogger(TestFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            sumTextArea.setText(out);
        }
    }//GEN-LAST:event_summaryButtonActionPerformed

    private void sourceNumOfWordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceNumOfWordsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sourceNumOfWordsActionPerformed

    private void sumNumOfWordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumNumOfWordsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sumNumOfWordsActionPerformed

    private void sourceTextAreaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_sourceTextAreaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_sourceTextAreaInputMethodTextChanged

    private void sourceTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sourceTextAreaKeyReleased
        // TODO add your handling code here:
//        String str = sourceTextArea.getText();
//        String[] words = str.split("\\s+");
//        sourceNumOfWords.setText(String.valueOf(words.length));
    }//GEN-LAST:event_sourceTextAreaKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TestFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel orginalDocumentLable;
    private javax.swing.JLabel orginalDocumentLable1;
    private javax.swing.JLabel orginalDocumentLable2;
    private javax.swing.JTextField sourceNumOfWords;
    private javax.swing.JTextPane sourceTextArea;
    private javax.swing.JTextField sumNumOfWords;
    private javax.swing.JTextPane sumTextArea;
    private javax.swing.JButton summaryButton;
    private javax.swing.JLabel summaryDocumentLable;
    // End of variables declaration//GEN-END:variables
}