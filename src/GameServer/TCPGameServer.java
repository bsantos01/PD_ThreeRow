package GameServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.GameModel;
import logic.ObservableGame;

public class TCPGameServer extends Observable implements Runnable//, ClientHandlerCallback
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

                client = serverTCPSocket.accept();
                System.out.println("TCPGameServer: new client accepted");
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                Object obj = in.readObject();
                if (obj instanceof String && obj.equals("Play"))
                {
                    System.out.println("TCPGameServer: String to play recieved!");
                    game.startGame();
                    out.writeObject(game.getGameModel());
                    out.flush();
                }
                else if (obj instanceof GameModel)
                {
                    System.out.println("TCPGameServer: GameModel game recieved!");
                    game.setGameModel(((ObservableGame) obj).getGameModel());
                    out.writeObject(game.getGameModel());
                    out.flush();
                } else
                {
                    System.out.println("TCPGameManager: Rejected content!");
                }
            }
        } catch (IOException e)
        {
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(TCPGameServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {

        }
    }

}
