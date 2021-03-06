package game.server;

import files.FileUtility;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.GameModel;

public class GameCommTCP implements Runnable {

    //TCP
    private Socket cOne = null;
    private Socket cTwo = null;
    private Game game;

    String fileName = "";
    File file;
    String user1;
    String user2;

    ObjectInputStream cOneIn;
    ObjectOutputStream cOneOut;

    ObjectInputStream cTwoIn;
    ObjectOutputStream cTwoOut;

    boolean playerOne = true;
    private boolean stop = false;

    public GameCommTCP(String user1, InetAddress cOneAdress, int cOneport, String user2, InetAddress cTwoAdress, int cTwoport) {
        try {
            this.user1 = user1;
            cOne = new Socket(cOneAdress, cOneport);
            System.out.println("Adress: " + cOneAdress + " and Port: " + cOneport);
            this.user2 = user2;
            cTwo = new Socket(cTwoAdress, cTwoport);
            System.out.println("Adress: " + cTwoAdress + " and Port: " + cTwoport);

        } catch (IOException e) {
            System.err.println("TCPGameServer: Error creating sockets. INSURE CLIENTS ARE RUNNING!");
        }
        //long num = System.currentTimeMillis();
        fileName = user1 + user2 + ".bin";
    }

//    stream start for both clients
    public void startStreams() throws IOException {
        try {
            cOneOut = new ObjectOutputStream(cOne.getOutputStream());
            cOneIn = new ObjectInputStream(cOne.getInputStream());

            cTwoOut = new ObjectOutputStream(cTwo.getOutputStream());
            cTwoIn = new ObjectInputStream(cTwo.getInputStream());

            System.out.println("TCPGameServer: Streams are up! ");

        } catch (IOException e) {
            System.out.println("TCPGameServer: Error creating streams.");
        }
    }

    public void objectUpdate(Object obj) {
        if (obj instanceof String) {
            if (((String) obj).equalsIgnoreCase("Ok")) {
                GameModel model = getFile(fileName);
                if (model != null) {
                    game = new Game(user1, user2);
                    game.setGame(model);
                } else {
                    game = new Game(user1, user2);
                }
                System.out.println("TCPGameServer: Both players ready! ");
            } else if (((String) obj).equalsIgnoreCase("CLOSING")) {
                shutdownStreams();
                System.out.println("TCPGameServer: CLOSING clients recieved! ");

            } else {
                System.err.println("TCPGameServer: An unexpected string arrived..." + obj + " ");
            }

        } else if (obj instanceof GameModel) {
            System.out.print("TCPGameServer: Recieved a GameModel.");
            game.setGame((GameModel) obj);
            saveFile();
        } else {
            System.err.print("TCPGameServer: I don't really know what this is..." + obj.toString() + " ");
        }

    }

    public void updatePlayers(ObjectOutputStream out, Object obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException ex) {
            System.err.println("TCPGameServer: updateGame IOException: " + ex + " obj: " + obj);
            //Note to self:
            //So yeah, if the client kills the socket, there is no way of knowing it unless you try to write to it.
            //So our approach shoul be wrapp around it and make shure it does not blow anything...
            //isClosed() and the others work if GameCommTCP kill the server, but not from the other end.
        }
        System.out.println("TCPGameServer: updateGame sent! ");

    }

    public void shutdownStreams() {

        try {
            if (cOneOut != null) {
                System.out.print("cOneOut, GAMEOVER ");
                updatePlayers(cOneOut, "GAMEOVER");
            }
            if (cTwoOut != null) {
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

            deleteFile();
            Thread.sleep(1000);

            cOne.close();
            cTwo.close();
            Thread.currentThread().interrupt();
        } catch (IOException ex) {
            System.out.print("TCPGameServer: Shutdown error " + ex + " ");
        } catch (InterruptedException ex) {
            System.out.print("TCPGameServer: interrupted Shutdown error " + ex + " ");
        }

    }

    public GameModel getFile(String fileName) {
        GameModel model = null;
        try {
            file = new File(fileName);
            model = (GameModel) FileUtility.retrieveGameFromFile(file);
            System.out.println("TCPGameServer: Game saved as file!");
        } catch (IOException ex) {
            System.out.print("TCPGameServer: Error saving game" + ex + " ");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameCommTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

    public void saveFile() {
        try {
            file = new File(fileName);
            FileUtility.saveGameToFile(file, game.getGame());
            System.out.println("TCPGameServer: Game saved as file!");
        } catch (IOException ex) {
            System.out.print("TCPGameServer: Error saving game" + ex + " ");
        }
    }

    public void deleteFile() {
        if (file.delete()) {
            System.out.println("TCPGameServer: File deleted");
        } else {
            System.err.println("TCPGameServer: Error deleting file");
        }
    }

    @Override
    public void run() {
        try {
            //start streams here?
            while (!Thread.currentThread().isInterrupted()) {
                startStreams();
                updatePlayers(cOneOut, user1);
                updatePlayers(cTwoOut, user2);

                while (!stop) // insert condition to end while
                {
                    if (playerOne) {
                        Object obj = cOneIn.readObject();

                        if (game != null) {
                            if (game.getGame().isOver()) {
                                updatePlayers(cTwoOut, game.getGame());
                                shutdownStreams();
                            } else {
                                objectUpdate(obj);
                                updatePlayers(cTwoOut, game.getGame()); //sends new gameModel to player two
                            }
                        }
                        playerOne = false;

                    } else if (!playerOne) {
                        Object obj = cTwoIn.readObject();
                        if (game == null) {
                            objectUpdate(obj);
                            updatePlayers(cTwoOut, game.getGame()); //sends new gameModel to player two
                            updatePlayers(cOneOut, game.getGame()); //sends new gameModel to player one
                            playerOne = true;

                        } else if (game.getGame().isOver()) {
                            updatePlayers(cOneOut, game.getGame());
                            shutdownStreams();

                        } else {
                            objectUpdate(obj);
                            updatePlayers(cOneOut, game.getGame()); //sends new gameModel to player one
                            playerOne = true;

                        }

                    } else {
                        System.out.println("TCPGameServer: Rejected content!");
                    }

                }
            }
        } catch (IOException e) {
            //Sockets closed abruptly
            //deleteFile();

            //getFile and update database with it, MAYBE
            shutdownStreams();
            //write to mySQL server

            System.err.println("To the database and beyond!!!");
            System.err.println("TCPGameServer: IOException: " + e);
        } catch (ClassNotFoundException ex) {
            System.err.println("TCPGameServer: ClassNotFoundException: " + ex);
        }

    }
}
