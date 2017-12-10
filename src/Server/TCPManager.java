/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import static java.sql.DriverManager.println;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Bruno Santos
 */
public class TCPManager implements Runnable//, ClientHandlerCallback
{
    //TCP
    private final String serverName;
    private final ServerSocket serverTCPSocket;
    
    //private final Map<Long, ClientInfo> clients;
    //private final Map<Long, ClientHandler> clientHandlers;
    private final Map<Long, Thread> clientHandlerThreads;
    
    public TCPManager(String serverName) throws IOException
    {
        this.serverName = serverName;
        
        serverTCPSocket = new ServerSocket(6001);
        
        System.out.println("Port " + serverTCPSocket.getLocalPort() + " ");
        
      //  clients = new ConcurrentHashMap();
        //clientHandlers = new ConcurrentHashMap();
        clientHandlerThreads = new ConcurrentHashMap();
    }
    
    @Override
    public void run()
    {
        //registar instância do TCPManager para threads mais "exteriores" poderem aceder a alguns dados
       // GlobalReferences.registerReference(GlobalReferences.ReferenceType.TCP_MANAGER, this);
                        

        try
        {
            Socket nextClient;
            long clientID;
           // ClientHandler clientHandler;
            Thread clientHandlerThread;
            
            while(!Thread.currentThread().isInterrupted())
            {
                //wait for new Clients to connect
                nextClient = serverTCPSocket.accept();
              

                //clientID = System.currentTimeMillis();
                
                //create ClientListener Runnable and respective Thread
               // clientHandler = new ClientHandler(this, clientID, nextClient);
                //clientHandlerThread = new Thread(clientHandler);
               // clientHandlerThread.setDaemon(true);
                
                //store thread in respective Maps
                //clientHandlers.put(clientID, clientHandler);
                //clientHandlerThreads.put(clientID, clientHandlerThread);
                
                //start thread
                //clientHandlerThread.start();
                System.out.println("TCPManager: new client accepted");
                
                ObjectOutputStream out = new ObjectOutputStream(nextClient.getOutputStream());
                String temp= "cenas";
                out.writeObject(temp);
                out.flush();
                
            }
        }
        catch(IOException e)
        {
            //printError(e);
        }
        finally
        {
//            try
//            {
//                shutdown();
//            }
//            catch(IOException e)
//            {
//                
//            }
        }
    }
    
//    @Override
//    public synchronized void clientHasConnected(ClientInfo clientInfo, long clientID)
//    {
//        //register clientInfo to the clients map
//        clients.put(clientID, clientInfo);
//        
//        println("TCPManager: client " + clientInfo.getClientName() + " connected");
//    }
//    
//    @Override
//    public synchronized void clientHasDisconnected(ClientInfo clientInfo, long clientID)
//    {
//        try
//        {
//            //interrupt ClientListener Thread
//            clientHandlerThreads.get(clientID).interrupt();
//            
//            clientHandlers.get(clientID).disconnect(false);
//            
//            //map it's value to null for future references (null means that no ClientListener Thread
//            //is running for clientID client, meanning clientID is NOT active)
//            //CORRECTION: HashMaps do NOT accept null keys or values (just check if the thread is interrupted or not)
//            //clientHandlers.put(clientID, null);
//            //clientHandlerThreads.put(clientID, null);
//        }
//        catch(IOException e)
//        {
//            printError(e);
//        }
//        
//        println("TCPManager: client " + (clientInfo != null? clientInfo.getClientName() : "") + " disconnected");
//    }
//    
//    public synchronized InetAddress getTCPAddress() {return serverTCPSocket.getInetAddress();}
//    public synchronized int getBoundTCPPort() {return serverTCPSocket.getLocalPort();}
//    
//    public synchronized ClientInfoList getResgiteredClientList()
//    {
//        ClientInfo infoArray[] = new ClientInfo[clients.size()];
//        
//        int i = 0;
//        for(Long key : clients.keySet())
//            infoArray[i++] = clients.get(key);
//        
//        return new ClientInfoList(infoArray);
//    }
//    
//    public synchronized ClientInfoList getActiveClientList()
//    {
//        List<ClientInfo> infoList = new ArrayList(clients.size());
//        
//        for(Long key : clients.keySet())
//        {
//            if(clientHandlerThreads.get(key).isAlive()) //active client  = alive thread
//                if(clientHandlers.get(key).isLoggedIn() && clientHandlers.get(key).isRegistered())
//                    infoList.add(clients.get(key));
//        }
//        
//        return new ClientInfoList(infoList.toArray(new ClientInfo[infoList.size()]));
//    }
//    
//    
//    private void shutdown() throws IOException
//    {
//        for(Long l : clientHandlers.keySet())
//        {
//            clientHandlerThreads.get(l).interrupt();
//            clientHandlers.get(l).disconnect(true);
//        }
//        
//        serverTCPSocket.close();
//    }
}