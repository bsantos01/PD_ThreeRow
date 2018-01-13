package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeartbeatServer
{

    public static final int MAX_SIZE = 10000;
    public static final String TIME_REQUEST = "HEARTBEAT";

    private DatagramSocket socket;
    private DatagramPacket packet; 
    private boolean debug;
    private String dbAdress;

    public HeartbeatServer(int listeningPort, boolean debug, String dbAdress) throws SocketException
    {
        socket = null;
        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
        socket = new DatagramSocket(listeningPort);
        this.debug = debug;
        this.dbAdress=dbAdress;
    }

    public String waitDatagram() throws IOException
    {
        String request;
        ObjectInputStream in;

        if (socket == null)
        {
            return null;
        }

        socket.receive(packet);
        in = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));

        try
        {
            request = (String) (in.readObject());
        } catch (ClassCastException | ClassNotFoundException e)
        {
            System.out.println("Object was not a String " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
            return null;
        }

        if (debug)
        {
            System.out.println("Recebido \"" + request + "\" de "
                    + packet.getAddress().getHostAddress() + ":" + packet.getPort());
        }

        return request;

    }
    
    public void start() {
        Thread HeartbeatThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    processRequests();
                }

            });
        HeartbeatThread.start();
        

    }
    
    public void processRequests()
    {
        String receivedMsg;
        ByteArrayOutputStream bOut;
        ObjectOutputStream out;

        if (socket == null)
        {
            return;
        }

        if (debug)
        {
            System.out.println("UDP Serialized Time Server iniciado...");
        }

        int goOut=0;
        while (goOut<3)
        {

            try
            {

                receivedMsg = waitDatagram();

                /*if (receivedMsg == null)
                {
                    goOut++;
                    continue;
                }*/

                if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST))
                {
                    goOut++;
                    continue;
                }
                else{
                    goOut=0;
                }
                bOut = new ByteArrayOutputStream(MAX_SIZE);
                out = new ObjectOutputStream(bOut);

                out.writeObject(dbAdress);
                packet.setData(bOut.toByteArray());
                packet.setLength(bOut.size());

                System.out.println("Answer size: " + bOut.size());
                socket.send(packet);
                Thread.sleep(10*1000);
            } catch (IOException e)
            {
                System.out.println(e);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }

    }

//    public void closeSocket()
//    {
//        if (socket != null)
//        {
//            socket.close();
//        }
//    }
//    public static void main(String[] args)
//    {
//        int listeningPort;
//        HeartbeatServer heartbeat = null;
//        try
//        {
//
//            listeningPort = Integer.parseInt("6999");
//            heartbeat = new HeartbeatServer(listeningPort, true);
//            heartbeat.processRequests();
//
//        } catch (NumberFormatException e)
//        {
//            System.out.println("O porto de escuta deve ser um inteiro positivo.");
//        } catch (SocketException e)
//        {
//            System.out.println("Ocorreu um erro ao nível do socket UDP:\n\t" + e);
//        } finally
//        {
//            if (heartbeat != null)
//            {
//                heartbeat.closeSocket();
//            }
//        }
//    }

}
