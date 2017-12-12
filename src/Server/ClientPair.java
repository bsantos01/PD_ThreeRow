/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.Socket;

/**
 *
 * @author Bruno Santos
 */
public class ClientPair {

    public ClientPair(Socket Client1, Socket Client2, int gameID) {
        this.Client1 = Client1;
        this.Client2 = Client2;
        this.gameID = gameID;
    }
    
    Socket Client1;
    Socket Client2;
    final int gameID;
    
}
