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
import ui.gui.ThreeInRowView;

public final class GameClient implements Observer, Runnable
{

    ObservableGame game;

    ServerSocket clientServer;
    Socket server;
    int servicePort;

    ObjectInputStream in;
    ObjectOutputStream out;

    public GameClient(String servicePort)
    {
        this.servicePort = Integer.parseInt(servicePort);
    }

    public void startStreams()
    {
        try
        {
            out = new ObjectOutputStream(server.getOutputStream());
            in = new ObjectInputStream(server.getInputStream());

        } catch (IOException ex)
        {
            System.out.println("GameClient: Error creating streams.");

        }
    }

    public void objectUpdate(Object obj)
    {
        if (obj instanceof String)
        {
            if (obj.equals("Play"))
            {
                System.out.println("GameClient: String to play recieved!");
                updateGame("Ok");
            } else
            {
                System.out.println("GameClient: An unexpected string arrived...");
            }
        }
        if (obj instanceof GameModel)
        {
            if (game == null)
            {
                System.out.print("GameClient: Yesh, i was null... but no longer!");
                game = new ObservableGame();
                game.setGameModel(((GameModel) obj));
                game.addObserver(GameClient.this);

                System.out.print("GameClient: GameModel arrived!");
                ThreeInRowView GUI = new ThreeInRowView(game);
            } else
            {
                System.out.print("GameClient: Recieved a GameModel.");
                game.setGameModel(((GameModel) obj));
            }
        } else
        {
            System.out.print("GameClient: I don't really know what this is...");
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
            System.err.println("GameClient: updateGame IOException: " + ex);
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
                    server = clientServer.accept();
                    System.out.println("GameClient: GameServer accepted");
                    startStreams();

                } catch (IOException ex)
                {
                    System.out.println("GameClient: Error starting socket.");
                }

                while (true)
                {
                    Object obj = in.readObject();
                    objectUpdate(obj);
                    //if all goes according to plan the update from oberver takes care of every thing

                }
            }
        } catch (IOException e)
        {
            System.err.println("GameClient: IOException: " + e);
        } catch (ClassNotFoundException ex)
        {
            System.err.println("GameClient: ClassNotFoundException: " + ex);
        }

    }

    @Override
    public void update(Observable o, Object arg)
    {
        System.out.println("GameClient: GameModel game sent [update]!");
        updateGame(game.getGameModel());

    }

}

//public final class GameClient implements Observer
//{
//
//    ObservableGame game;
//
//    Socket ToServer;
//    String ServerAddr;
//    int servicePort;
//    ObjectInputStream in;
//    ObjectOutputStream out;
//
//    public GameClient(String ServerAddr, String servicePort)
//    {
//        this.ServerAddr = ServerAddr;
//        this.servicePort = Integer.parseInt(servicePort);
//
//    }
//
//    public void start() throws IOException
//    {
//
//        try
//        {
//            ToServer = new Socket(this.ServerAddr, this.servicePort);
//        } catch (IOException ex)
//        {
//            System.out.println("GameClient: Error starting socket.");
//        }
//        try
//        {
//            out = new ObjectOutputStream(ToServer.getOutputStream());//Cria um ObjectOutputStream associado ao socket s
//            in = new ObjectInputStream(ToServer.getInputStream()); //Cria um ObjectInputStream associado ao socket s
//        } catch (IOException e)
//        {
//            System.out.println("GameClient: Error creating streams.");
//        }
//        commThread.start();
//
//    }
//
//    Thread commThread = new Thread(new Runnable()
//    {
//        @Override
//        public void run()
//        {
//            try
//            {
//                out.writeObject("Play");
//                out.flush();
//                System.out.print("Login sent... waiting...");
//                //game = new ObservableGame();
//                //game.setGameModel((GameModel)in.readObject());
////                System.out.println("Login complete... ");
//                while (!Thread.currentThread().isInterrupted())
//                {
//                    while (true)
//                    {
//                        Object obj = in.readObject();
//                        if (obj instanceof GameModel)
//                        {
//                            if (game == null)
//                            {
//                                System.out.print("GameClient: Yesh, i was null... but no longer!");
//                                game = new ObservableGame();
//                                game.setGameModel(((GameModel) obj));
//                                game.addObserver(GameClient.this);
//
//                                System.out.print("GameClient: GameModel arrived!");
//                                ThreeInRowView GUI = new ThreeInRowView(game);
//                            } else
//                            {
//                                System.out.print("GameClient: Recieved a GameModel.");
//                                game.setGameModel(((GameModel) obj));
//                                Thread.sleep(1000);
//                            }
//                        } else
//                        {
//                            System.out.print("GameClient: I don't really know what this is...");
//                        }
//                    }
//
//                }
//            } catch (IOException | ClassNotFoundException e)
//            {
//                System.err.println("GameClient Exception" + e);
//            } catch (InterruptedException ex)
//            {
//                System.err.println("GameClient InterruptedException" + ex);
//            }
//        }
//    });
//
//    @Override
//    public void update(Observable o, Object arg)
//    {
//        try
//        {
//            System.out.println("GameClient: GameModel game sent [update]!");
//            out.writeObject(game.getGameModel());
//            out.flush();
//        } catch (IOException ex)
//        {
//            System.err.println("GameClient Update IOException" + ex);
//        }
//    }
//
//}

