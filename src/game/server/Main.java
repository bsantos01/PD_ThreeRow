package game.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalStateException, ClassNotFoundException {

        String ipAndPort = "localhost:3306";
        GameLauncher server;
        GameDBHandler database = new GameDBHandler(ipAndPort);
        List<Pair> pairs = new ArrayList<>();

        try {
            pairs = database.getPairs();
        } catch (SQLException ex) {
            System.err.println("Game Server SQLException - DB " + ex);

            //testing purposes
            try {
                //192.168.1.2
                server = new GameLauncher(InetAddress.getByName("localhost"), "7777", InetAddress.getByName("localhost"), "8888");
                server.start();

            } catch (UnknownHostException e) {
                System.err.println("Game Main Error - InetAdress " + e);
            }
        }

        for (Pair p : pairs) {
//            try {
//                InetAddress addr1 = InetAddress.getByName(database.getIPbyUsername(p.getUser1()));
//                String port1 = database.getPortbyUsername(p.getUser1());
//
//                InetAddress addr2 = InetAddress.getByName(database.getIPbyUsername(p.getUser2()));
//                String port2 = database.getPortbyUsername(p.getUser2());

            server = new GameLauncher(ipAndPort, p);
            server.start();

//            } catch (SQLException ex) {
//                System.err.println("Game Server SQLException Error - InetAdress");
//            } catch (UnknownHostException ex) {
//                System.err.println("Game Server Main Error - InetAdress");
//            }
        }

    }

}
