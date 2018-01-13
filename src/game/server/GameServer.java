package game.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable {

    private int port;
    private String serverIp;
    private boolean exit = false;

    private GameCommUDP heartbeat;
    private String ipAndPort;
    private GameLauncher server;
    private List<Pair> pairs;

    public GameServer(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;

    }

    @Override
    public void run() {

        System.out.println("Game server initianting!");
        System.err.println("Heartbeat starting");

        heartbeat = new GameCommUDP(serverIp, 6999);
        heartbeat.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.err.println("Heartbeat error" + ex);
        }
        System.out.println("hearbeat: " + heartbeat.port());

        ipAndPort = heartbeat.port();

        while (!exit) {
            GameDBHandler database = new GameDBHandler(ipAndPort);
            try {
                pairs = new ArrayList<>();
                if (database.getPairs() != null) {
                    pairs = database.getPairs();
                }
            } catch (SQLException ex) {
                System.err.println("Game Server SQLException - DB " + ex);

            }

            if (pairs != null) {
                for (Pair p : pairs) {

                    try {
                        server = new GameLauncher(ipAndPort, p);
                        server.start();
                    } catch (IllegalStateException ex) {
                        System.out.println("GameServer main run() IllegalStateException" + ex);
                    } catch (ClassNotFoundException ex) {
                        System.out.println("GameServer main run() ClassNotFoundException" + ex);
                    }
                }
            } else {
                System.out.println("GameServer: No pair in creation");
            }
            try {
                Thread.sleep(5 * 1000);

            } catch (InterruptedException ex) {
                System.out.println("GameServer main run() IllegalStateException" + ex);
            }
        }
    }

}
