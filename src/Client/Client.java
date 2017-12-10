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
    protected ObjectInputStream in;
    protected ObjectOutputStream out;

    
    public Client(String ServerAddr, String servicePort) {
        this.ServerAddr = ServerAddr;
        this.servicePort = Integer.parseInt(servicePort);

        
    }

    
    public void start(){
        
    commThread.start();
            try{
            ToServer= new Socket(this.ServerAddr, this.servicePort); 
        } catch (IOException ex) {
            System.out.println("Não foi possivel iniciar o socket");
        }
    }
    
    Thread commThread = new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            System.out.println("entrei");
                in = new ObjectInputStream(ToServer.getInputStream());
                out = new ObjectOutputStream(ToServer.getOutputStream());
                while (!Thread.currentThread().isInterrupted()) {
                    String temp= (String)in.readObject();
                    System.out.print("cenas"+temp);
                }
        } catch (Exception e) {

        }   
    }
    });
    
    
    
}
