package GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.GameModel;
import logic.ObservableGame;
import ui.gui.ThreeInRowView;

public final class GameClient implements Observer
{

    ObservableGame game;

    Socket ToServer;
    String ServerAddr;
    int servicePort;
    ObjectInputStream in;
    ObjectOutputStream out;

    public GameClient(String ServerAddr, String servicePort)
    {
        this.ServerAddr = ServerAddr;
        this.servicePort = Integer.parseInt(servicePort);

    }

    public void start() throws IOException
    {

        try
        {
            ToServer = new Socket(this.ServerAddr, this.servicePort);
        } catch (IOException ex)
        {
            System.out.println("Não foi possivel iniciar o socket");
        }
        try
        {
            out = new ObjectOutputStream(ToServer.getOutputStream());//Cria um ObjectOutputStream associado ao socket s
            in = new ObjectInputStream(ToServer.getInputStream()); //Cria um ObjectInputStream associado ao socket s
        } catch (IOException e)
        {
            System.out.println("erro ao criar streams");
        }
        commThread.start();

    }

    Thread commThread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                out.writeObject("Play");
                out.flush();
                System.out.print("Login sent... waiting...");
                //game = new ObservableGame();
                //game.setGameModel((GameModel)in.readObject());
//                System.out.println("Login complete... ");
                while (!Thread.currentThread().isInterrupted())
                {
                    Object obj = in.readObject();
                    if (obj instanceof GameModel)
                    {
                        if (game == null)
                        {
                            System.out.print("Yesh, i was null...");
                            game = new ObservableGame();
                            game.setGameModel(((GameModel) obj));
                            System.out.print("GameClient: GameModel arrived!");
                            ThreeInRowView GUI = new ThreeInRowView(game);
                            game.addObserver(GameClient.this);

//                            if (GUI.getGame().hasChanged())
//                            {
//                                System.out.println("GameClient: GameModel game sent [hasChanged]!");
//                                //game.setGameModel(((GameModel) obj));
//                                out.writeObject(game.getGameModel());
//                                out.flush();
//                            }
                        }
                    } else
                    {
                        System.out.print("GameClient: I don't really know what this is...");
                    }

                }
            } catch (IOException | ClassNotFoundException e)
            {
                System.err.println("GameClient Exception" + e);
            }
        }
    });

     @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            System.out.println("GameClient: GameModel game sent [update]!");
            GameModel gg = game.getGameModel();
            out.writeObject(gg);
            out.flush();
        } catch (IOException ex)
        {
            System.err.println("GameClient Update IOException" + ex);
        }
    }

}
