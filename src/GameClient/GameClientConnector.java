package GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.GameModel;

public final class GameClientConnector implements Runnable {

    ServerSocket clientServer;
    Socket socket;

    int servicePort;

    String player = null;

    public GameClientConnector(int servicePort, String username) throws IOException {
        this.player = username;
        this.servicePort = servicePort;
        clientServer = new ServerSocket(this.servicePort);
        System.out.println("ServerSocket started @Port " + clientServer.getLocalPort() + " ");
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket = clientServer.accept();
                System.out.println("GameClientConnector: GameServer accepted");

            } catch (IOException ex) {
                System.out.println("GameClientConnector: Error starting socket." + ex + " ");
            }
            Thread GameThread = new Thread(new Runnable() {
                boolean stop = false;
                private boolean lock = true;
                Game game;

                @Override
                public void run() {
                    try {
                        Socket Client = socket;
                        final ObjectInputStream in;
                        final ObjectOutputStream out;
                        out = new ObjectOutputStream(Client.getOutputStream());
                        in = new ObjectInputStream(Client.getInputStream());
                        while (!stop) {
                            Object obj = in.readObject();

                            if (obj instanceof String) {
                                if (obj.equals(player)) {

                                    System.out.println("GameClientConnector: String to play recieved! I'm player " + player);
                                    out.writeObject("ok");
                                    out.flush();
                                    System.out.println("GameClientConnector: String OK sent!");

                                } else if (obj.equals("GAMEOVER")) {
                                    System.out.println("GameClientConnector: GAMEOVER arrived...");
                                  
                                    out.close();
                                    in.close();
                                    if (socket != null) {
                                        socket.close();
                                    }

                                    stop = true;
                                    Thread.sleep(2000);
                                    game.closeGui();
                                    Thread.sleep(2000);
                                    game.closePop();
                                    
                                    Thread.currentThread().interrupt();
                                    return;
                                } else {
                                    System.out.println("GameClientConnector: An unexpected string arrived..." + obj + "");
                                }
                            } else if (obj instanceof GameModel) {
                                if (game == null) {
                                    game = new Game(player);
                                    game.updateGame((GameModel) obj);
                                    System.out.println("GameClientConnector: New Game created...");
                                } else {
                                    game.updateGame((GameModel) obj);
                                    System.out.println("GameClientConnector: Game updated with object " + obj);
                                }
                            } else {
                                System.out.print("GameClientConnector: I don't really know what this is... " + obj.toString() + " ");
                            }

                            if (game != null) {
                                
                                while (game.getGame().getCurrentPlayerName().equals(player) && !game.getGame().isOver()){
                                    Thread.sleep(2);
                                    //do nothing
                                    //sleep
                                }
                                
                                if (!lock || player.equals(game.getGame().getPlayer1().getName())) {
                                    out.writeObject(game.getGame());
                                    out.flush();
                                    System.out.println("After do nothing... updta central");
                                } else if (player.equals(game.getGame().getPlayer2().getName())) {
                                    lock = false;
                                    System.err.println("GameClientConnector: Player 2 unlocked");
                                }
                               

                            }
                            //

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(GameClientConnector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(GameClientConnector.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameClientConnector.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            GameThread.setDaemon(true);
            GameThread.start();

        }

    }
}
