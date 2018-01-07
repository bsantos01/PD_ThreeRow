package GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import logic.GameModel;

public final class GameClientConnector implements Runnable
{

    Game game;

    ServerSocket clientServer;
    Socket socket;
    int servicePort;

    ObjectInputStream in;
    ObjectOutputStream out;

    String player = null;
    private boolean stop = false;
    private boolean lock = true;

    public GameClientConnector(String servicePort) throws IOException
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
            System.out.println("GameClientConnector: Error creating streams.");

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
                System.out.println("GameClientConnector: String to play recieved! I'm player " + player);
                updateCentralServer("Ok");
                System.out.println("GameClientConnector: String OK sent!");

            } else
            {
                if (obj.equals("GAMEOVER"))
                {
                    System.out.println("GameClientConnector: GAMEOVER arrived...");
                    shutdown();
                } else
                {
                    System.out.println("GameClientConnector: An unexpected string arrived..." + obj + "");
                }
            }
        } else
        {
            if (obj instanceof GameModel)
            {
                if (game == null)
                {
                    game = new Game(player);
                    game.updateGame((GameModel) obj);
                    System.out.println("GameClientConnector: New Game created...");
                } else
                {
                    game.updateGame((GameModel) obj);
                    System.out.println("GameClientConnector: Game updated with object " + obj);
                }
            } else
            {
                System.out.print("GameClientConnector: I don't really know what this is... " + obj.toString() + " ");
            }
        }

    }

    public void shutdown()
    {
        try
        {
            updateCentralServer("CLOSING");
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
            Thread.sleep(2000);
            game.closeGui();
            Thread.sleep(2000);
            game.closePop();

            Thread.currentThread().interrupt();

        } catch (IOException ex)
        {
            System.out.print("GameClientConnector: Shutdown error " + ex + "");
        } catch (InterruptedException ex)
        {
            System.out.print("GameClientConnector: interrupted Shutdown error " + ex + "");
        }
    }

    public void updateCentralServer(Object obj)
    {
        try
        {
            out.writeObject(obj);
            out.flush();
            // System.err.println("GameClientConnector: updateCentralServer sent " + obj);

        } catch (IOException ex)
        {
            System.err.println("GameClientConnector: updateGame IOException: " + ex + "");
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
                    System.out.println("GameClientConnector: GameServer accepted");
                    startStreams();

                } catch (IOException ex)
                {
                    System.out.println("GameClientConnector: Error starting socket." + ex + " ");
                }
                while (!stop)
                {
                    Object obj = in.readObject();
                    objectUpdate(obj);
                    System.out.println("GameClientConnector: objectUpdate(obj)");

                    //and here!
                    if (game != null)
                    {

                        while (game.getGame().getCurrentPlayerName().equals(player) && !game.getGame().isOver())
                        {
                            Thread.sleep(10);
                            //do nothing
                            //sleep
                        }

                        if (!lock || player.equals("A"))
                        {
                            updateCentralServer(game.getGame());
                            System.out.println("After do nothing... updta central");
                        } else
                        {
                            if (player.equals("B")) //carefull here - FUTURE!
                            {
                                lock = false;
                                System.err.println("GameClientConnector: Player 2 unlocked");
                            }
                        }
                        System.out.println("After do nothing...");

                    }
                    //
                }
            }
        } catch (IOException e)
        {
            System.err.println("GameClientConnector: run() IOException: " + e + "");
        } catch (ClassNotFoundException ex)
        {
            System.err.println("GameClientConnector: ClassNotFoundException: " + ex + "");
        } catch (InterruptedException ex)
        {
            System.err.println("GameClientConnector: InterruptedException: " + ex + "");
        }
    }
}
