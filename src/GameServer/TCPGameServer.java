package GameServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import logic.GameModel;

public class TCPGameServer implements Runnable
{

    //TCP
    private Socket cOne = null;
    private Socket cTwo = null;
    private Game game;

    ObjectInputStream cOneIn;
    ObjectOutputStream cOneOut;

    ObjectInputStream cTwoIn;
    ObjectOutputStream cTwoOut;

    //Atempt
    boolean playerOne = true;
    boolean locking = true;
    int players = 0;

    public TCPGameServer(InetAddress cOneAdress, int cOneport, InetAddress cTwoAdress, int cTwoport)
    {
        try
        {
            cOne = new Socket(cOneAdress, cOneport);
            System.out.println("Adress: " + cOneAdress + " and Port: " + cOneport);
            cTwo = new Socket(cTwoAdress, cTwoport);
            System.out.println("Adress: " + cTwoAdress + " and Port: " + cTwoport);

        } catch (IOException e)
        {
            System.out.println("TCPGameServer: Error creating sockets.");
        }
    }

//    stream start for both clients
    public void startStreams() throws IOException
    {
        try
        {
            cOneOut = new ObjectOutputStream(cOne.getOutputStream());
            cOneIn = new ObjectInputStream(cOne.getInputStream());

            cTwoOut = new ObjectOutputStream(cTwo.getOutputStream());
            cTwoIn = new ObjectInputStream(cTwo.getInputStream());

            System.out.println("TCPGameServer: Streams are up! ");

        } catch (IOException e)
        {
            System.out.println("TCPGameServer: Error creating streams.");
        }
    }

    public void objectUpdate(Object obj)
    {
        if (obj instanceof String)
        {
            if (obj.equals("Ok") && players == 1)
            {
                game = new Game();
                System.err.println("TCPGameServer: PlayerTwo Ok recieved!! ");
                System.err.println("TCPGameServer: Both players ready! ");
                locking=false;
            }
            if (obj.equals("Ok") && players == 0) //not shure about this, it's here to validate 
            {
                players = 1;
                System.err.println("TCPGameServer: PlayerOne Ok recieved! ");

            } else
            {
                System.err.println("TCPGameServer: An unexpected string arrived...");
            }
            // end conditions?
            //socket disconnected conditions?
        }
        if (obj instanceof GameModel)
        {
            System.out.print("TCPGameServer: Recieved a GameModel.");
            game.setGame((GameModel) obj);
        } else
        {
            System.out.print("TCPGameServer: I don't really know what this is...");
        }
    }

    public void updateGame(ObjectOutputStream out, Object obj)
    {
        try
        {
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex)
        {
            System.err.println("TCPGameServer: updateGame IOException: " + ex);
        }
        System.out.println("TCPGameServer: updateGame(out, obj) sent! ");

    }

    @Override
    public void run()
    {
        try
        {
            while (!Thread.currentThread().isInterrupted())
            {
                startStreams();
                updateGame(cOneOut, "Play");
                updateGame(cTwoOut, "Play");

                while (true) // insert condition to end while
                {
                    if (playerOne && locking)
                    {
                        Object obj = cOneIn.readObject();
                        objectUpdate(obj);
                        playerOne = false;
                    }
                    if (playerOne && !locking)
                    {
                        Object obj = cOneIn.readObject();
                        objectUpdate(obj);
                        playerOne = false;
                        updateGame(cTwoOut, game.getGame()); //sends new gameModel to player two
                    }
                    if (!playerOne)
                    {
                        Object obj = cTwoIn.readObject();
                        objectUpdate(obj);
                        playerOne = true;
                        updateGame(cOneOut, game.getGame()); //sends new gameModel to player one
                    } else
                    {
                        System.out.println("TCPGameServer: Rejected content!");
                    }
                }
            }
        } catch (IOException e)
        {
            System.err.println("TCPGameServer: IOException: " + e);
        } catch (ClassNotFoundException ex)
        {
            System.err.println("TCPGameServer: ClassNotFoundException: " + ex);
        }
    }
}

// ObservableGame game = new ObservableGame();
//    //TCP
//    private final String serverName;
//    private final ServerSocket serverTCPSocket;
//    ObjectInputStream in;
//    ObjectOutputStream out;
//
//    public TCPGameServer(String serverName, int port) throws IOException
//    {
//        this.serverName = serverName;
//        serverTCPSocket = new ServerSocket(port);
//        System.out.println("Port " + serverTCPSocket.getLocalPort() + " ");
//    }
//
////    public void startSocket() throws IOException
////    {
////
////    }
//    @Override
//    public void run()
//    {
//        try
//        {
//            while (!Thread.currentThread().isInterrupted())
//            {
//                Socket client = null;
//                try
//                {
//                    client = serverTCPSocket.accept();
//                    System.out.println("TCPGameServer: new client accepted");
//
//                } catch (IOException ex)
//                {
//                    System.out.println("GameClient: Error starting socket.");
//                }
//                try
//                {
//                    out = new ObjectOutputStream(client.getOutputStream());
//                    in = new ObjectInputStream(client.getInputStream());
//
//                } catch (IOException e)
//                {
//                    System.out.println("GameClient: Error creating streams.");
//                }
//                
//                while (true)
//                {
//                    Object obj = in.readObject();
//                    if (obj instanceof GameModel)
//                    {
//                        System.out.println("TCPGameServer: GameModel game recieved!");
//                        game.setGameModel(((GameModel) obj));
//                        Thread.sleep(1000);
//                    }
//                    if (obj instanceof String && obj.equals("Play"))
//                    {
//                        System.out.println("TCPGameServer: String to play recieved!");
//                        game.addObserver(TCPGameServer.this);
//                        game.startGame();
//                    } else
//                    {
//                        System.out.println("TCPGameServer: Rejected content!");
//                        System.err.println("TCPGameServer: Recieved: " + obj.toString());
//                    }
//                }
//            }
//        } catch (IOException e)
//        {
//            System.err.println("TCPGameServer: IOException: " + e);
//        } catch (ClassNotFoundException ex)
//        {
//            System.err.println("TCPGameServer: ClassNotFoundException: " + ex);
//        } catch (InterruptedException ex)
//        {
//            System.err.println("TCPGameServer: InterruptedException" + ex);
//        }
//    }
//
//    @Override
//    public void update(Observable o, Object arg)
//    {
////        this will interfere with the use done inside runnable
//        try
//        {
//            System.out.println("TCPGameServer: GameModel game sent [update]!");
//            out.writeObject(game.getGameModel());
//            out.flush();
//        } catch (IOException ex)
//        {
//            System.err.println("TCPGameServer Update IOException" + ex);
//        }
//
//    }

