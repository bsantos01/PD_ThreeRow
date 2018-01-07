package GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import logic.GameModel;
import logic.ObservableGame;
import ui.gui.PopupView;
import ui.gui.ThreeInRowView;

public final class GameClient implements Observer, Runnable
{

    ObservableGame game;
    ThreeInRowView gui;

    ServerSocket clientServer;
    Socket socket;
    int servicePort;

    ObjectInputStream in;
    ObjectOutputStream out;

    String player = null;
    private boolean stop = false;

    public GameClient(String servicePort) throws IOException
    {
        this.servicePort = Integer.parseInt(servicePort);
        clientServer = new ServerSocket(this.servicePort);
        System.out.println("ServerSocket started @Port " + clientServer.getLocalPort() + " ");
    }

    public void startStreams()
    {
        try
        {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException ex)
        {
            System.out.println("GameClient: Error creating streams.");

        }
    }

    public void objectUpdate(Object obj) throws InterruptedException, IOException
    {
        if (obj instanceof String)
        {
            if (obj.equals("Player1") || obj.equals("Player2"))
            {
                if (obj.equals("Player1"))
                {
                    player = "A";
                } else
                {
                    player = "B";
                }
                System.out.println("GameClient: String to play recieved! I'm player " + player);
                updateGame("Ok");
                System.out.println("GameClient: String OK sent!");
            } else
            {
                if (obj.equals("GAMEOVER"))
                {
                    System.out.println("GameClient: GAMEOVER arrived...");
                    //warn the client?!?
                    shutdown();
                } else
                {
                    System.out.println("GameClient: An unexpected string arrived..." + obj + "");
                }
            }
        } else
        {
            if (obj instanceof GameModel)
            {
                if (game == null)
                {
                    System.out.print("GameClient: Yesh, i was null... but no longer!");
                    game = new ObservableGame();
                    game.setGameModel(((GameModel) obj));
                    game.addObserver(GameClient.this);

                    System.out.print("GameClient: GameModel arrived! ");
                    gui = new ThreeInRowView(game, player);
                    if (!game.getCurrentPlayerName().equals(player))
                    {
                        gui.enableGrid(false);
                    }
                } else
                {
                    System.out.print("GameClient: Recieved a GameModel. " + game.getCurrentPlayerName() + " ");
                    game.setGameModel(((GameModel) obj));
                }
            } else
            {
                System.out.print("GameClient: I don't really know what this is... " + obj.toString() + " ");
            }
        }

    }

    public void shutdown()
    {
        try
        {
            //how to show client?
            updateGame("CLOSING");
            out.close();
            in.close();
            if (socket != null)
            {
                socket.close();
            }
            if (clientServer != null)
            {
                clientServer.close();
            }
            stop = true;
            PopupView pop = new PopupView();
            Thread.sleep(2000);
            gui.close();
            pop.close();
            Thread.currentThread().interrupt();

        } catch (IOException ex)
        {
            System.out.print("GameClient: Shutdown error " + ex + "");
        } catch (InterruptedException ex)
        {
            System.out.print("GameClient: interrupted Shutdown error " + ex + "");
        }
    }

    public void updateGame(Object obj)
    {
        try
        {
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex)
        {
            System.err.println("GameClient: updateGame IOException: " + ex + "");
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (!Thread.currentThread().isInterrupted())
            {
                try
                {
                    socket = clientServer.accept();
                    System.out.println("GameClient: GameServer accepted");
                    startStreams();

                } catch (IOException ex)
                {
                    System.out.println("GameClient: Error starting socket." + ex + " ");
                }
                while (!stop)
                {
                    Object obj = in.readObject();
                    objectUpdate(obj);
                    System.out.println("GameClient: objectUpdate(obj)");

                }
            }
        } catch (IOException e)
        {
            System.err.println("GameClient: run() IOException: " + e + "");
        } catch (ClassNotFoundException ex)
        {
            System.err.println("GameClient: ClassNotFoundException: " + ex + "");
        } catch (InterruptedException ex)
        {
            System.err.println("GameClient: InterruptedException: " + ex + "");
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {

        if (!game.getCurrentPlayerName().equals(player))
        {
            updateGame(game.getGameModel());
            gui.enableGrid(false);
        } else
        {
            gui.enableGrid(true);
        }
        if (game.hasWon(game.getCurrentPlayer()))
        {
            updateGame(game.getGameModel());

        }

    }
}
