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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderClient {
    private static final Logger LOG = Logger.getLogger(ReaderClient.class.getName());

    public static void main(String[] args) {
        try (ClientSocket client
                = new ClientSocket("localhost", 1234, "reader", "writer")) {
            Reader reader
                    = new InputStreamReader(client.getInputStream(), "UTF-8");
            BufferedReader in = new BufferedReader(reader);
            for (String s = in.readLine(); s != null; s = in.readLine()) {
                System.out.println(s);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
