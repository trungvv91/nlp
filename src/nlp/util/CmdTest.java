/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.util;

/**
 *
 * @author Manh Tien
 */
import java.io.*;

public class CmdTest {

    public static void runCommand(String filename) throws Exception {
        String[] command = {"cmd"};
        Process p = Runtime.getRuntime().exec(command);
        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
        new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
        try (PrintWriter stdin = new PrintWriter(p.getOutputStream())) {
            stdin.println("cd data");
            String cmd = "crf_test.exe -m model_crf2 0< " + filename + "-postag.txt 1> " + filename + "-chunk.txt";
            stdin.println(cmd);
        }
        int returnCode = p.waitFor();
        System.out.println("Return code = " + returnCode);

    }

    public static class SyncPipe implements Runnable {

        public SyncPipe(InputStream istrm, OutputStream ostrm) {
            istrm_ = istrm;
            ostrm_ = ostrm;
        }

        @Override
        public void run() {
            try {
                final byte[] buffer = new byte[1024];
                for (int length = 0; -1 != (length = istrm_.read(buffer));) {
                    ostrm_.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private final OutputStream ostrm_;
        private final InputStream istrm_;
    }
}
