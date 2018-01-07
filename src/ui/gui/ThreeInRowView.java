package ui.gui;

import logic.ObservableGame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThreeInRowView extends JFrame implements Observer
{

    ObservableGame game;
    ThreeInRowGamePanel panel;
    String player;

    public ThreeInRowView(ObservableGame j, String player)
    {
        super("Three in a row");

        game = j;
        game.addObserver(this);
        panel = new ThreeInRowGamePanel(game);
        this.player = player;

        addComponents();

        setVisible(true);
        this.setSize(700, 500);
        this.setMinimumSize(new Dimension(650, 450));
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
        JLabel b = new JLabel(player);
        j.add(b);
        ///
        cp.add(j, BorderLayout.NORTH);
    }

    public void enableGrid(boolean b)
    {
        panel.enableGrid(b);
    }

    public void close()
    {
        setVisible(false); //you can't see me!
        dispose(); //Destroy the JFrame object
    }

    @Override
    public void update(Observable o, Object arg)
    {
        repaint();

    }

}
