package GameServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLauncher {

    private boolean hasStarted;
    private Thread tcpManagerThread;
    private GameCommTCP tcpGameServer;
    private InetAddress serviceAddress;
    private InetAddress serviceAddress2;
    private int servicePort = 0;
    private int servicePort2 = 0;

    //private String ipAndPort = "";
    private GameDBHandler database;

    //getFromDb
    String user1 = "Bruno";
    String user2 = "Miguel";

    public GameLauncher(InetAddress serviceAddress, String servicePort, InetAddress serviceAddress2, String servicePort2) {
        this.serviceAddress = serviceAddress;
        this.serviceAddress2 = serviceAddress2;
        this.servicePort = Integer.parseInt(servicePort);
        this.servicePort2 = Integer.parseInt(servicePort2);
        this.hasStarted = false;
    }

    public GameLauncher(String ipAndPort, Pair p) {
        try {
            database = new GameDBHandler(ipAndPort);

            user1 = p.getUser1();
            user2 = p.getUser2();

            if (database.isActive(user1) && database.isActive(user2)) {

                InetAddress addr1 = InetAddress.getByName(database.getIPbyUsername(user1));
                String port1 = database.getPortbyUsername(user1);
                InetAddress addr2 = InetAddress.getByName(database.getIPbyUsername(user2));
                String port2 = database.getPortbyUsername(user2);

                this.serviceAddress = addr1;
                this.serviceAddress2 = addr2;
                this.servicePort = Integer.parseInt(port1);
                this.servicePort2 = Integer.parseInt(port2);

                this.hasStarted = false;
            }
        } catch (SQLException ex) {
            System.err.println("GameLauncher: SQLException Error - InetAdress");
        } catch (UnknownHostException ex) {
            System.err.println("GameLauncher: Main Error - InetAdress");
        }
    }

    public void start() throws IllegalStateException, ClassNotFoundException {
        if (hasStarted) {
            throw new IllegalStateException("GameServer is running or has already ran.");
        } else {
            hasStarted = true;
        }

        try {
            println("GameServer Running");
            startTCPGameServer();
            heartbeatClient();
            tcpManagerThread.join();

        } catch (IOException e) {
        } catch (InterruptedException ex) {
            Logger.getLogger(GameLauncher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            stop();
        }
    }

    public void heartbeatClient() {
        GameCommUDP cliente = new GameCommUDP("UDPclient", 6999);
        cliente.start();
    }

    private void startTCPGameServer() throws IOException {
        println("Starting TCPGameServer . . . ");

        tcpGameServer = new GameCommTCP(user1, serviceAddress, servicePort, user2, serviceAddress2, servicePort2);
        tcpManagerThread = new Thread(tcpGameServer);
        tcpManagerThread.setDaemon(true);
        tcpManagerThread.start();

        println("OK");
    }

    private void stopTCPManager() {
        println("GameServer: Stopping TCP Manager . . . ");
        tcpManagerThread.interrupt();
        println("GameServer: Stopped");
    }

    public void stop() throws IllegalStateException {
        if (!hasStarted) {
            throw new IllegalStateException("Server is not yet running.");
        }
        stopTCPManager();
        println("Exiting");
    }

    public synchronized static void println(Object message) {
        System.out.println(message);
    }

}
