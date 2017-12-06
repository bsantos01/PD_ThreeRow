/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerTestes;

/**
 *
 * @author Bruno Santos
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamCopier extends Thread {
    private static final Logger LOG
            = Logger.getLogger(StreamCopier.class.getName());

    private final InputStream in;
    private final OutputStream out;

    public StreamCopier(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        setDaemon(true);
    }

    @Override
    public void run() {
        LOG.info("Start stream copier");
        try {
            for (int b = in.read(); b != -1; b = in.read()) {
                out.write(b);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            LOG.info("End stream copier");
            try {
                out.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}