/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import static Server.Server.println;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import static java.sql.DriverManager.println;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public class TCPManager implements Runnable//, ClientHandlerCallback
{
    //TCP
    private final String serverName;
    private final ServerSocket serverTCPSocket;

    DBhandler uh;
    private Thread ClientHandlerThread;
    private ClientHandler CHandler;
    //private final Map<Long, ClientInfo> clients;
    //private final Map<Long, ClientHandler> clientHandlers;
    
    
    public TCPManager(String serverName) throws IOException
    {
        this.serverName = serverName;

        serverTCPSocket = new ServerSocket(6001);
        uh= new DBhandler();
        StartClientHandler();
        System.out.println("Port " + serverTCPSocket.getLocalPort() + " ");
        
    }
    
    public void StartClientHandler() throws IOException{
    
        println("Starting ClientHandler . . . ");
        
        CHandler = new ClientHandler();
        ClientHandlerThread = new Thread(CHandler);
        ClientHandlerThread.setDaemon(true);
        ClientHandlerThread.start();
    }
    
    
    public String getPairs(){
        String listPairs="";
        


        return listPairs;
    }
    
    public void killPlayers() throws SQLException{
        uh.killPlayers();
    }
    
    @Override
    public void run()
    {
        //registar instância do TCPManager para threads mais "exteriores" poderem aceder a alguns dados
       // GlobalReferences.registerReference(GlobalReferences.ReferenceType.TCP_MANAGER, this);
        try
        {
            String temp=null;
            
   
            while(!Thread.currentThread().isInterrupted())
            {
                final ObjectInputStream in;
                final ObjectOutputStream out;
                final Socket nextClient;
                nextClient = serverTCPSocket.accept();
                System.out.println("TCPManager: new client accepted");
                out = new ObjectOutputStream(nextClient.getOutputStream());
                in = new ObjectInputStream(nextClient.getInputStream());
             
                temp= (String) in.readObject();

                String[] arr = temp.split("[\\W]");

                int control=0;

                if(arr[0].equalsIgnoreCase("Register"))
                {
                    if(uh.register(arr[1], arr[2])){
                        out.writeObject("Sucefully registered, player "+ arr[1]+".");
                        control=1;
                    }else 
                        out.writeObject("Impossible to register.");
                    out.flush();
                } else
                if(arr[0].equalsIgnoreCase("Login")){
                    if(uh.login(arr[1], arr[2], nextClient.getInetAddress(), nextClient.getPort())){
                        out.writeObject("Sucefully logged in, player "+ arr[1]+".");
                        control=1;
                    }else 
                        out.writeObject("Faulty login");
                    out.flush();
                } 
                else
                {
                    System.out.println("TCPManager: Rejected client, faulty login data");
                }
                
                if (control==1){
                    this.CHandler.addNewClient(arr[1], nextClient, in, out);
                }     
            }
        }
           
        
        catch(IOException e)
        {
            //printError(e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TCPManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TCPManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomException ex) {
            Logger.getLogger(TCPManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    	Thread commThread = new Thread(new Runnable() {
//		@Override
//		public void run() {
//                    
//		}
//	});
  
}
