package GameServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer
{

    private boolean hasStarted;
    private Thread tcpManagerThread;
    private TCPGameServer tcpGameServer;
    private InetAddress serviceAddress;
    private InetAddress serviceAddress2;
    private final int servicePort;
    private final int servicePort2;

    public GameServer(InetAddress serviceAddress, String servicePort, InetAddress serviceAddress2, String servicePort2)
    {
        this.serviceAddress = serviceAddress;
        this.serviceAddress2 = serviceAddress2;
        this.servicePort = Integer.parseInt(servicePort);
        this.servicePort2 = Integer.parseInt(servicePort2);
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
            println("GameServer Running");
            startTCPGameServer();
            heartbeatClient();
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

    private void startTCPGameServer() throws IOException
    {
        println("Starting TCPGameServer . . . ");

        tcpGameServer = new TCPGameServer(serviceAddress, servicePort, serviceAddress2, servicePort2);
        tcpManagerThread = new Thread(tcpGameServer);
        tcpManagerThread.setDaemon(true);
        tcpManagerThread.start();

        println("OK");
    }

    private void stopTCPManager()
    {
        println("GameServer: Stopping TCP Manager . . . ");
        tcpManagerThread.interrupt();
        println("GameServer: Stopped");
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
