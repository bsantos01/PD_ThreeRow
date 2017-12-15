/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public class Server {
    private String serverName;
    private boolean hasStarted;
    private Thread tcpManagerThread;
    private TCPManager tcpManager;
    private final String serviceAddress;
    private final int servicePort;
    private Thread inputListenerThread;
    private InputListener inputListener;
    
    public Server(String serverName, String serviceAddress, int servicePort)
    {
        this.serverName = serverName;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;   
        this.hasStarted = false;
    }
    
    
    public void start() throws IllegalStateException, ClassNotFoundException
    {
        if(hasStarted)
            throw new IllegalStateException("Server is running or has already ran.");
        else
            hasStarted = true;
    
    
     try{
            println("Running");
            
            startTCPManager();
            heartbeatServer();

            startInputListener();
            //join InputListener (typing "exit" would interrupt it
            //creating a domino effect that would close all other threads including
            //the main one)
            inputListenerThread.join();

        }catch(InterruptedException e){
            
        }
        catch(IOException e)
        {
            //printError(e);
        }
        finally
        {
            stop();
        }
    }
    
    public void heartbeatServer(){
                int listeningPort;
        HeartbeatServer heartbeat = null;
        try
        {

            listeningPort = Integer.parseInt("6999");
            heartbeat = new HeartbeatServer(listeningPort, true);
            heartbeat.processRequests();

        } catch (NumberFormatException e)
        {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e)
        {
            System.out.println("Ocorreu um erro ao nível do socket UDP:\n\t" + e);
        } finally
        {
            if (heartbeat != null)
            {
                heartbeat.closeSocket();
            }
        }
    }
    
    public void ListPairs(){
        
        System.out.println(this.tcpManager.getPairs());
    
    }
    
    private void startTCPManager() throws IOException
    {
        println("Starting TCP Manager . . . ");
        
        tcpManager = new TCPManager(serverName);
        tcpManagerThread = new Thread(tcpManager);
        tcpManagerThread.setDaemon(true);
        tcpManagerThread.start();
        
        println("OK");
    }
    
    private void stopTCPManager()
    {
        this.ListPairs();
        println("Stopping TCP Manager . . . ");
        try {
            tcpManager.killPlayers();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        tcpManagerThread.interrupt();
        
        println("Stopped");
    }
    
    private void startInputListener()
    {
        println("Launching Input Listener Thread . . . ");
        
        inputListener = new InputListener();
        inputListenerThread = new Thread(inputListener);
        inputListenerThread.setDaemon(true);
        inputListenerThread.start();
        
        println("OK");
    }
    
    private void stopInputListener()
    {
        println("Halting Input Listener Thread . . . ");
        
        inputListenerThread.interrupt();
        
        println("Stopped");
    }
    
    public void stop() throws IllegalStateException
    {
        if(!hasStarted)
            throw new IllegalStateException("Server is not yet running.");
        
        stopInputListener();
        //stopUDPManager();
        stopTCPManager();
        
       
        
        println("Exiting");
    }
    
        public synchronized static void println(Object message)
    {
        System.out.println(message);
    }
}
