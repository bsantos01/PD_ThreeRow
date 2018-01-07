/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.ui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 * 
 */

public class LoginView extends JFrame implements Observer{

    LoginPanel panel;
    
    public LoginView(String title) throws HeadlessException {
        super(title);
        panel = new LoginPanel();
        addComponents();

        setVisible(true);
        this.setSize(300, 150);
        this.setMinimumSize(new Dimension(300, 150));
        this.setMaximumSize(new Dimension(300, 150));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        validate();
    }

    private void addComponents()
    {
        Container cp = getContentPane();

        cp.setLayout(new BorderLayout());
        cp.add(panel, BorderLayout.CENTER);
        ///
        JPanel j = new JPanel();
        JButton b = new JButton("register");
        b.setBounds(180, 80, 80, 25);
        j.add(b,BorderLayout.WEST);
        
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        JButton b2 = new JButton("login");
        b2.setBounds(180, 80, 80, 25);
        j.add(b2,BorderLayout.EAST);
        
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        ///
        cp.add(j, BorderLayout.SOUTH);
        
    }
    
    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
