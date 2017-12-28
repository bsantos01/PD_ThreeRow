package GameServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.GameModel;
import logic.ObservableGame;

public class TCPGameServer implements Runnable, Observer//, ClientHandlerCallback
{

    ObservableGame game = new ObservableGame();

    //TCP
    private final String serverName;
    private final ServerSocket serverTCPSocket;
    ObjectInputStream in;
    ObjectOutputStream out;

    public TCPGameServer(String serverName, int port) throws IOException
    {
        this.serverName = serverName;
        serverTCPSocket = new ServerSocket(port);
        System.out.println("Port " + serverTCPSocket.getLocalPort() + " ");
    }

    @Override
    public void run()
    {
        try
        {
            Socket client;

            while (!Thread.currentThread().isInterrupted())
            {
//                System.out.println("im here -- pass here!");

                client = serverTCPSocket.accept();
                System.out.println("TCPGameServer: new client accepted");
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                Object obj = in.readObject();
                if (obj instanceof GameModel)
                {
                    System.out.println("TCPGameServer: GameModel game recieved!");
                    game.setGameModel(((GameModel) obj));
//                        out.writeObject(game.getGameModel());
//                        out.flush();
                }
                if (obj instanceof String && obj.equals("Play"))
                {
                    System.out.println("TCPGameServer: String to play recieved!");
                    game.addObserver(TCPGameServer.this);
                    game.startGame();
//                    out.writeObject(game.getGameModel());
//                    out.flush();
                } else
                {
                    System.out.println("TCPGameServer: Rejected content!");
                    System.err.println("Was: " + obj);
                }
            }
        } catch (IOException e)
        {
            System.out.println("TCPGameServer: IOException: " + e);
        } catch (ClassNotFoundException ex)
        {
            System.out.println("TCPGameServer: ClassNotFoundException: " + ex);
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
//        this will interfere with the use done inside runnable
        try
        {
            System.out.println("TCPGameServer: GameModel game sent [update]!");
            out.writeObject(game.getGameModel());
            out.flush();
        } catch (IOException ex)
        {
            System.err.println("TCPGameServer Update IOException" + ex);
        }

    }

}
