package Server;

import java.io.IOException;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import rmi.server.RMIService;

public class Server {

    private String serverName;
    private boolean hasStarted;
    private Thread tcpManagerThread;
    private TCPManager tcpManager;
    private final String serviceAddress;
    private final int servicePort;
    private Thread inputListenerThread;
    private InputListener inputListener;
    private HeartbeatServer hb;

    public Server(String serverName, String serviceAddress, int servicePort) {
        this.serverName = serverName;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
        this.hasStarted = false;
    }

    public void start() throws IllegalStateException, ClassNotFoundException {
        if (hasStarted) {
            throw new IllegalStateException("Server is running or has already ran.");
        } else {
            hasStarted = true;
        }

        try {
            println("Running");

            startTCPManager();
            startHeartbeat();
            startInputListener();

            try {
                RMIService rmiService = new RMIService("localhost:3306");
                rmiService.run();

            } catch (RemoteException ex) {
                System.out.println("Erro ao iniciar o servico RMI! " + ex);
            }
            //join InputListener (typing "exit" would interrupt it
            //creating a domino effect that would close all other threads including
            //the main one)
            inputListenerThread.join();

        } catch (InterruptedException e) {
            System.out.println("Server InterruptedException start(): " + e);

        } catch (IOException e) {
            System.out.println("Server IOException start(): " + e);
        } finally {
            stop();
        }
    }

    public void ListPairs() {

        System.out.println(this.tcpManager.getPairs());

    }

    private void startTCPManager() throws IOException {
        println("Starting TCP Manager . . . ");

        tcpManager = new TCPManager(serverName);
        tcpManagerThread = new Thread(tcpManager);
        tcpManagerThread.setDaemon(true);
        tcpManagerThread.start();

        println("OK");
    }

    private void stopTCPManager() {
        this.ListPairs();
        println("Stopping TCP Manager . . . ");
        try {
            tcpManager.killPlayers();
        } catch (SQLException ex) {
            System.err.println("Server: SQLException stopTCPManager(): " + ex);
        }
        tcpManagerThread.interrupt();

        println("Stopped");
    }

    private void startInputListener() {
        println("Launching Input Listener Thread . . . ");

        inputListener = new InputListener();
        inputListenerThread = new Thread(inputListener);
        inputListenerThread.setDaemon(true);
        inputListenerThread.start();

        println("OK");
    }

    private void stopInputListener() {
        println("Halting Input Listener Thread . . . ");

        inputListenerThread.interrupt();

        println("Stopped");
    }

    public void stop() throws IllegalStateException {
        if (!hasStarted) {
            throw new IllegalStateException("Server is not yet running.");
        }
        hb.interrupt();

        stopInputListener();
        stopTCPManager();

        println("Exiting");
    }

    public synchronized static void println(Object message) {
        System.out.println(message);
    }

    private void startHeartbeat() {
        try {
            println("Starting heartbeat . . . ");

            hb = new HeartbeatServer(6999, "localhost:3306");
            hb.start();

            println("OK");
        } catch (SocketException ex) {
            System.out.println("Server.starHeartbeat()");
        }
    }

}
