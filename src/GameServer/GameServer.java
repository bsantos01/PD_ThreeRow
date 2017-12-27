package GameServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.ObservableGame;

public class GameServer
{

    private String serverName;
    private boolean hasStarted;
    private Thread tcpManagerThread;
    private TCPGameServer tcpGameServer;
    private final String serviceAddress;
    private final int servicePort;

    public GameServer(String serverName, String serviceAddress, int servicePort)
    {
        this.serverName = serverName;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
        this.hasStarted = false;
    }

    public void start() throws IllegalStateException, ClassNotFoundException
    {
        if (hasStarted)
        {
            throw new IllegalStateException("Server is running or has already ran.");
        } else
        {
            hasStarted = true;
        }

        try
        {
            println("Running");
            startTCPGameServer(servicePort);
//            heartbeatClient();
            tcpManagerThread.join();

        } catch (IOException e)
        {
        } catch (InterruptedException ex)
        {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            stop();
        }
    }

    public void heartbeatClient()
    {
        UDPClient cliente = new UDPClient("UDPclient", 6999);
        cliente.start();

    }

    private void startTCPGameServer(int servicePort) throws IOException
    {
        println("Starting TCPGameServer . . . ");

        tcpGameServer = new TCPGameServer(serverName, servicePort);
        tcpManagerThread = new Thread(tcpGameServer);
        tcpManagerThread.setDaemon(true);
        tcpManagerThread.start();

        println("OK");
    }

    private void stopTCPManager()
    {
        println("Stopping TCP Manager . . . ");
        tcpManagerThread.interrupt();
        println("Stopped");
    }
    

    public void stop() throws IllegalStateException
    {
        if (!hasStarted)
        {
            throw new IllegalStateException("Server is not yet running.");
        }
        stopTCPManager();
        println("Exiting");
    }

    public synchronized static void println(Object message)
    {
        System.out.println(message);
    }

}
