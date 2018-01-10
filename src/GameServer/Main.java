package GameServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws IllegalStateException, ClassNotFoundException {

        GameServer server;
        try {
            server = new GameServer(InetAddress.getLocalHost(), "7777", InetAddress.getLocalHost(), "8888");
            server.start();

        } catch (UnknownHostException ex) {
            System.err.println("Game Server Main Error - InetAdress");
        }

    }

}
