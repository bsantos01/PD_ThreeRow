/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.ui.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author migue
 */
public class LoginPanel extends JPanel {

    JLabel Lusername;
    JLabel Lpassword;
    JTextField TFusername;
    JPasswordField TFpassword;
    
    
    
    public LoginPanel() {
        setupComponents();
        setupLayout();
    }
    
    private void setupComponents()
    {
        //optionPanel = new StartOptionPanel(game);
        Lusername = new JLabel("Username");
        Lpassword = new JLabel("Password");
        TFusername = new JTextField(10);
        TFpassword = new JPasswordField(10);

    }

    private void setupLayout()
    {
        JPanel pPrincipal, pTop, pDown;
       
        setLayout(new BorderLayout());

        pPrincipal = new JPanel();
        //pLabel = new JPanel();
        pTop = new JPanel();
        pDown = new JPanel();
                
        
        pTop.add(Lusername, BorderLayout.WEST);
        pTop.add(TFusername, BorderLayout.EAST);
        pPrincipal.add(pTop, BorderLayout.NORTH);

        pDown.add(Lpassword, BorderLayout.WEST);
        pDown.add(TFpassword, BorderLayout.EAST);
        pPrincipal.add(pDown, BorderLayout.SOUTH);

        
        
        add(pPrincipal, BorderLayout.CENTER);

        //add(optionPanel, BorderLayout.EAST);

        validate();
    }
    
}
