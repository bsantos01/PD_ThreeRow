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
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Matchmaker extends Thread {
    private static final Logger LOG
            = Logger.getLogger(Matchmaker.class.getName());

    private final int port;
    private final Map<ClientPair,ClientSocket> waiting = new HashMap<>();

    public static void main(String[] args) {
        try {
            int port = 1234;
            int st = 0;
            for (String arg: args) {
                switch (st) {
                    case 0:
                        switch (arg) {
                            case "-p":
                                st = 1;
                                break;
                            default:
                                System.out.println("Unknown option: " + arg);
                                return;
                        }
                        break;
                    case 1:
                        port = Integer.parseInt(arg);
                        st = 0;
                        break;
                }
            }
            Matchmaker server = new Matchmaker(port);
            server.start();
            server.join();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private Matchmaker(int port) {
        this.port = port;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                ClientSocket socket = new ClientSocket(server.accept());
                ClientPair pair = new ClientPair(
                        socket.getOwnId(), socket.getPeerId());
                ClientSocket other;
                synchronized(this) {
                    other = waiting.remove(pair.opposite());
                    if (other == null) {
                        waiting.put(pair, socket);
                    }
                }
                if (other != null) {
                    LOG.log(Level.INFO, "Establishing connection for {0}",
                            pair);
                    establishConnection(socket, other);
                } else {
                    LOG.log(Level.INFO, "Waiting for counterpart {0}", pair);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private void establishConnection(ClientSocket socket, ClientSocket other)
            throws IOException {
        Thread thread = new StreamCopier(
                socket.getInputStream(), other.getOutputStream());
        thread.start();
        thread = new StreamCopier(
                other.getInputStream(), socket.getOutputStream());
        thread.start();
    }
}