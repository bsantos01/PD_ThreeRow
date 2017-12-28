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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public class ClientT extends Thread
{
    PipedOutputStream outputP;
    Socket ToClient;
    String Username;
    ObjectInputStream in;
    ObjectOutputStream out;
    int run;
    
    
    public ClientT(PipedOutputStream outP, Socket skt, String user, ObjectInputStream in, ObjectOutputStream out) {
        ToClient=skt;  
        outputP = outP; 
        Username=user;
        this.in=in;
        this.out=out;
        
        
      }
    
    public void ToClientRequest(String player) throws IOException{
        
        out.writeObject(("gamereq "+ player));
       
    }
    
    @Override
    public void run()//listener do socket
    {
        while (!Thread.currentThread().isInterrupted()) {    

            try {           
                
                Object temp= in.readObject();
                
            
                String msg = (String) temp;
                
                String[] arr = msg.split("[\\W]");
                
                
                if (arr[0].equals("game")) //pretende iniciar um jogo, nos seguintes argumentos estarão mais dados
                {
                    outputP.write(("game "+arr[1]+" "+ Username+"?").getBytes());
                       
                }
                else
                    out.writeObject("Comando Invalido");
                
      
            } catch (IOException ex) {
                Logger.getLogger(ClientT.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }
}