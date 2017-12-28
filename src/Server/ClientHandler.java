/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public class ClientHandler implements Runnable{

    Map<String,ClientT> PlayerThread;
    PipedInputStream Tin;
    PipedOutputStream Cout;
    DBhandler uh;
    
    ClientHandler() throws IOException{
        PlayerThread= new HashMap<String, ClientT>();
        Cout = new PipedOutputStream();
	Tin = new PipedInputStream(Cout);
        uh= new DBhandler();
    }
    
    @Override
    public void run() {

        while(true){
            try {
                StringBuilder sb = new StringBuilder();
                int input = Tin.read();
                
                while (input != -1){ //EOF               
                    sb.append((char)input);
                    input = Tin.read();
                    if((char)input=='?')
                        break;
                }
                
                String str = sb.toString();
              
                if(str.length()>8){
                    
                    
                    String[] arr = str.split("[\\W]");

                    if(arr[0].equals("game")){ //arranca thread que envia pedido ao jogador 2 para formar jogo
                        uh.setOcuppied(arr[1]); //mete os players como ocupados
                        uh.setOcuppied(arr[2]);
                        
                        //envia directamente para o socket do cliente o pedido com o username do user que pediu
                        PlayerThread.get(arr[1]).ToClientRequest(arr[2]); 
                    }
                    else if(arr[0].equals("gamereq"))
                    {
                      //arranca jogo
                        if (arr[1].equals("yes")){
                            
                        } else
                        {
                            uh.freePlayer(arr[2]);
                            uh.freePlayer(arr[3]);
                        }
                    }    
                }

                 
            } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    


    
    public void addNewClient(String username, Socket ToClient, ObjectInputStream in, ObjectOutputStream out) throws IOException{

        System.out.println("entrei " + username);
        
        
        ClientT Client = new ClientT(Cout, ToClient, username, in, out);
        Client.setDaemon(true);
        Client.start();
        PlayerThread.put(username,Client);
        
        
    }
    
}
