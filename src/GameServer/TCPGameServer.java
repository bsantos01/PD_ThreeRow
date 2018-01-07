package GameServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private boolean stop = false;

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
            System.err.println("TCPGameServer: Error creating sockets. INSURE CLIENTS ARE RUNNING!");
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
            if (((String) obj).equalsIgnoreCase("Ok"))
            {
                game = new Game();
                System.out.println("TCPGameServer: Both players ready! ");
            } else
            {
                if (((String) obj).equalsIgnoreCase("CLOSING"))
                {
                    shutdownStreams();
                } else
                {
                    System.err.println("TCPGameServer: An unexpected string arrived..." + obj + " ");
                }
            }
            // end conditions?
            //socket disconnected conditions?
        } else
        {
            if (obj instanceof GameModel)
            {
                System.out.print("TCPGameServer: Recieved a GameModel.");
                game.setGame((GameModel) obj);
            } else
            {
                System.err.print("TCPGameServer: I don't really know what this is..." + obj.toString() + " ");
            }
        }
    }

    public void updatePlayers(ObjectOutputStream out, Object obj)
    {
        try
        {
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex)
        {
            System.err.println("TCPGameServer: updateGame IOException: " + ex + " obj: " + obj);
            //Note to self:
            //So yeah, if the client kills the socket, there is no way of knowing it unless you try to write to it.
            //So our approach shoul be wrapp around it and make shure it does not blow anything...
            //isClosed() and the others work if TCPGameServer kill the server, but not from the other end.
        }
        System.out.println("TCPGameServer: updateGame sent! ");

    }

//    public void shutdownGame(ObjectOutputStream one, ObjectOutputStream two)
//    {
//        updatePlayers(one, "GAMEOVER");
//        updatePlayers(two, "GAMEOVER");
//    }
    public void shutdownStreams()
    {

        try
        {
            if (cOneOut != null)
            {
                System.out.print("cOneOut, GAMEOVER ");
                updatePlayers(cOneOut, "GAMEOVER");
            }
            if (cTwoOut != null)
            {
                System.out.print("cTwoOut, GAMEOVER ");
                updatePlayers(cTwoOut, "GAMEOVER");
            }
            //shutdownGame(cOneOut, cTwoOut);
            Thread.sleep(1000);

            cOneIn.close();
            cOneOut.close();
            cTwoIn.close();
            cTwoOut.close();
            stop = true;
            Thread.sleep(1000);
            cOne.close();
            cTwo.close();
            Thread.currentThread().interrupt();
        } catch (IOException ex)
        {
            System.out.print("TCPGameServer: Shutdown error " + ex + " ");
        } catch (InterruptedException ex)
        {
            System.out.print("TCPGameServer: interrupted Shutdown error " + ex + " ");
        }

    }

    @Override
    public void run()
    {
        try
        {
            //start streams here?
            while (!Thread.currentThread().isInterrupted())
            {
                startStreams();
                updatePlayers(cOneOut, "Player1");
                updatePlayers(cTwoOut, "Player2");

                while (!stop) // insert condition to end while
                {
                    if (playerOne)
                    {
                        Object obj = cOneIn.readObject();

                        if (game != null)
                        {
                            if (game.getGame().isOver())
                            {
                                updatePlayers(cTwoOut, game.getGame());
                                shutdownStreams();
                            }
                            objectUpdate(obj);
                            updatePlayers(cTwoOut, game.getGame()); //sends new gameModel to player two

                        }
                        playerOne = false;

                    } else
                    {
                        if (!playerOne)
                        {
                            Object obj = cTwoIn.readObject();
                            if (game == null)
                            {
                                objectUpdate(obj);
                                updatePlayers(cTwoOut, game.getGame()); //sends new gameModel to player two
                                updatePlayers(cOneOut, game.getGame()); //sends new gameModel to player one
                                playerOne = true;

                            } else
                            {
                                if (game.getGame().isOver())
                                {
                                    updatePlayers(cOneOut, game.getGame());
                                    shutdownStreams();

                                } else
                                {
                                    objectUpdate(obj);
                                    updatePlayers(cOneOut, game.getGame()); //sends new gameModel to player one
                                    playerOne = true;

                                }
                            }

                        } else
                        {
                            System.out.println("TCPGameServer: Rejected content!");
                        }
                    }
                }
            }
        } catch (IOException e)
        {
            //Sockets closed abruptly
            shutdownStreams();
            //write to mySQL server

            System.err.println("To the database and beyond!!!");
            System.err.println("TCPGameServer: IOException: " + e);
        } catch (ClassNotFoundException ex)
        {
            System.err.println("TCPGameServer: ClassNotFoundException: " + ex);
        }

    }
}
