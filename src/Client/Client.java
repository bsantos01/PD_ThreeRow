/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import static Server.Server.println;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public final class Client{
    
        
    
    Socket ToServer;
    String ServerAddr;
    int servicePort;
     ObjectInputStream in;
     ObjectOutputStream out;
     Scanner sc;
    
    public Client(String ServerAddr, String servicePort) {
        this.ServerAddr = ServerAddr;
        this.servicePort = Integer.parseInt(servicePort);
        sc=new Scanner(System.in);
        
    }

    
    public void start() throws IOException{
        
    
            try{
            ToServer= new Socket(this.ServerAddr, this.servicePort); 
        } catch (IOException ex) {
            System.out.println("Não foi possivel iniciar o socket");
        }
            try{
                out = new ObjectOutputStream(ToServer.getOutputStream());//Cria um ObjectOutputStream associado ao socket s
                in = new ObjectInputStream(ToServer.getInputStream()); //Cria um ObjectInputStream associado ao socket s
            }catch (IOException e){
                System.out.println("erro ao criar streams");
            }
        commThread.start();
            
    }
    
    Thread commThread = new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            
                String temp=sc.nextLine();
                out.writeObject(temp);
                
                temp= (String) in.readObject();
                System.out.println("Login: "+ temp);
                while (!Thread.currentThread().isInterrupted()) {
                    
                    temp= (String)in.readObject();
                    System.out.print("MSG: "+temp);
                }
        } catch (Exception e) {

        }   
    }
    });
    
    
    
}
