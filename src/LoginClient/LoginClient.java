/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginClient;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import login.ui.gui.LoginView;

/**
 *
 * @author migue
 */
public class LoginClient implements Observer, Runnable {
    
    LoginView gui;

    public LoginClient() {
        gui = new LoginView("Login");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
        {

        }
    }


    @Override
    public void update(Observable o, Object arg) {
        //Enviar Cenas
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
}
