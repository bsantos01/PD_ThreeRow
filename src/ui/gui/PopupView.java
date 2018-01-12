package ui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopupView extends JFrame
{

    public PopupView()
    {
        super("Game Over");

        addComponents();

        setVisible(true);
        this.setSize(400, 150);
        this.setMinimumSize(new Dimension(300, 100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        validate();
    }

    private void addComponents()
    {
        Container cp = getContentPane();

        cp.setLayout(new BorderLayout());
        ///
        JPanel j = new JPanel();
        JLabel label = new JLabel("The game is shutting down... ");
        j.add(label);
        ///
        cp.add(j, BorderLayout.NORTH);
    }

    public void close()
    {
        setVisible(false); //you can't see me!
        dispose(); //Destroy the JFrame object
    }
    
}
