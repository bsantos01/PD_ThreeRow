/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
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
        println("Stopping TCP Manager . . . ");
        
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
