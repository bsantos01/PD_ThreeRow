package Server;

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;

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

            starHeartbeat();
            startInputListener();
            //join InputListener (typing "exit" would interrupt it
            //creating a domino effect that would close all other threads including
            //the main one)
            inputListenerThread.join();

        } catch (InterruptedException e) {

        } catch (IOException e) {
            //printError(e);
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

        stopInputListener();
        stopHeartbeat();
        stopTCPManager();

        println("Exiting");
    }

    public synchronized static void println(Object message) {
        System.out.println(message);
    }

    private void starHeartbeat() {
        try {
            println("Starting heartbeat . . . ");

            hb = new HeartbeatServer(6999, "localhost:3306");
            hb.start();

            println("OK");
        } catch (SocketException ex) {
            System.out.println("Server.starHeartbeat()");
        }
    }

    private void stopHeartbeat() {

        println("Closing heartbeat . . . ");
        //hb.interrupt();

    }
}
