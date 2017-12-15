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
     ObjectInputStream in;
     ObjectOutputStream out;
    protected List<ClientPair> Pairs;
    static int ngames;
    DBhandler uh;
    //private final Map<Long, ClientInfo> clients;
    //private final Map<Long, ClientHandler> clientHandlers;
    
    
    public TCPManager(String serverName) throws IOException
    {
        this.serverName = serverName;
        Pairs= new ArrayList();
        serverTCPSocket = new ServerSocket(6001);
        ngames=0;
        uh= new DBhandler();
        System.out.println("Port " + serverTCPSocket.getLocalPort() + " ");
        
    }
    
    public String getPairs(){
        String listPairs="";
        
        for(int i=0; i<Pairs.size(); i++){
            listPairs.concat(Pairs.get(i).toString());
            System.out.println(Pairs.get(i).toString());
        }

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
            Client waiting=null, next;
            Socket nextClient;


            
            while(!Thread.currentThread().isInterrupted())
            {
             
                nextClient = serverTCPSocket.accept();
                System.out.println("TCPManager: new client accepted");
                out = new ObjectOutputStream(nextClient.getOutputStream());
                in = new ObjectInputStream(nextClient.getInputStream());
                
                
             
                temp= (String) in.readObject();
                
                String[] arr = temp.split("[\\W]");
               
                
                if(arr[0].equalsIgnoreCase("Register"))
                {
                    if(uh.register(arr[1], arr[2])){
                        out.writeObject("Sucefully registered, player "+ arr[1]+".");
                    }else 
                        out.writeObject("Impossible to register.");
                } else
                if(arr[0].equalsIgnoreCase("Login")){
                    if(uh.login(arr[1], arr[2])){
                        out.writeObject("Sucefully logged in, player "+ arr[1]+".");
                    }else 
                        out.writeObject("Faulty login");
                } 
                else
                {
                    System.out.println("TCPManager: Rejected client, faulty login data");
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
  
}
