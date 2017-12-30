//package GameServer;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class ClientT extends Thread
//{
//    //TCP
//
//    private final ServerSocket serverTCPSocket;
//    ObjectInputStream in;
//    ObjectOutputStream out;
//
//    public ClientT(int port) throws IOException
//    {
//        serverTCPSocket = new ServerSocket(port);
//        System.out.println("Port " + serverTCPSocket.getLocalPort() + " ");
//    }
//
//    public void startSocket() throws IOException
//    {
//        Socket client = null;
//        try
//        {
//            client = serverTCPSocket.accept();
//            System.out.println("TCPGameServer: new client accepted");
//
//        } catch (IOException ex)
//        {
//            System.out.println("GameClient: Error starting socket.");
//        }
//        try
//        {
//            out = new ObjectOutputStream(client.getOutputStream());
//            in = new ObjectInputStream(client.getInputStream());
//
//        } catch (IOException e)
//        {
//            System.out.println("GameClient: Error creating streams.");
//        }
//    }
//
//    @Override
//    public void run()
//    {
//        try
//            {
//                while (!Thread.currentThread().isInterrupted())
//                {
//                    startSocket();
//                    while (true)
//                    {
//                        Object obj = in.readObject();
//                        if (obj instanceof GameModel)
//                        {
//                            System.out.println("TCPGameServer: GameModel game recieved!");
//                            game.setGameModel(((GameModel) obj));
//                            Thread.sleep(1000);
//                        }
//                        if (obj instanceof String && obj.equals("Play"))
//                        {
//                            System.out.println("TCPGameServer: String to play recieved!");
//                            game.addObserver(TCPGameServer.this);
//                            game.startGame();
//                        } else
//                        {
//                            System.out.println("TCPGameServer: Rejected content!");
//                            System.err.println("TCPGameServer: Recieved: " + obj.toString());
//                        }
//                    }
//                }
//            } catch (IOException e)
//            {
//                System.err.println("TCPGameServer: IOException: " + e);
//            } catch (ClassNotFoundException ex)
//            {
//                System.err.println("TCPGameServer: ClassNotFoundException: " + ex);
//            } catch (InterruptedException ex)
//            {
//                System.err.println("TCPGameServer: InterruptedException" + ex);
//            }
//        }
//
//        @Override
//        public void update
//        (Observable o, Object arg
//        
//            )
//    {
////        this will interfere with the use done inside runnable
//        try
//            {
//                System.out.println("TCPGameServer: GameModel game sent [update]!");
//                out.writeObject(game.getGameModel());
//                out.flush();
//            } catch (IOException ex)
//            {
//                System.err.println("TCPGameServer Update IOException" + ex);
//            }
//
//        }
//
//    }
//
//}
//
//}
