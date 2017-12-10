/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerTestes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.ObservableGame;

/**
 *
 * @author Bruno Santos
 */
public class GameServer extends Thread{

    ClientSocket c1, c2;
    ObservableGame game;
    
    public GameServer(ClientSocket client1, ClientSocket client2) {
    
        c1=client1;
        c2= client2;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while(true){
            int temp=0;
            try {
                temp=c1.getInputStream().read();
            } catch (IOException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                c2.getOutputStream().write(temp);
                
                c2.getOutputStream().flush();
            } catch (IOException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
}
