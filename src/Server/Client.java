/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Bruno Santos
 */
public class Client {

    private Socket skt;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public Client(Socket skt,ObjectOutputStream out,ObjectInputStream in) throws IOException {
        this.skt = skt;
        this.out= out;
        this.in= in;
    }

    public Socket getSkt() {
        return skt;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }
    
    
    

    
    
}
