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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriterClient {
    private static final Logger LOG = Logger.getLogger(ReaderClient.class.getName());

    public static void main(String[] args) {
        try (ClientSocket client
                = new ClientSocket("localhost", 1234, "writer", "reader")) {
            Writer writer
                    = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
            PrintWriter out = new PrintWriter(writer);
            while(true){
                out.println("Message line ");
            
            out.flush();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }    
}
