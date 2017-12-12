/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Bruno Santos
 */
public class ClientPair {

    Client Client1;
    Client Client2;
    final int gameID;

    public ClientPair(Client Client1, Client Client2, int gameID) {
        this.Client1 = Client1;
        this.Client2 = Client2;
        this.gameID = gameID;
    }

    @Override
    public String toString() {
        return "ClientPair{" + "gameID=" + gameID + '}';
    }

    /**
     *
     * @return
     */
    
    public Client getClient1() {
        return Client1;
    }

    public Client getClient2() {
        return Client2;
    }

    public int getGameID() {
        return gameID;
    }        
    
    public Socket getClientSkt(int index){
        if(index==1)
            return Client1.getSkt();
        else
            return Client2.getSkt();
    }
    
    public ObjectInputStream getClientInput(int index){
        if(index==1)
            return Client1.getIn();
        else
            return Client2.getIn();
    }
    public ObjectOutputStream getClientOutput(int index){
        if(index==1)
            return Client1.getOut();
        else
            return Client2.getOut();
    }
    
}
