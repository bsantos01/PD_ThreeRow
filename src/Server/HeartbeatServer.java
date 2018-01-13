package Server;

import java.io.*;
import java.net.*;

public class HeartbeatServer {

    public static final int MAX_SIZE = 10000;
    public static final String TIME_REQUEST = "HEARTBEAT";

    private DatagramSocket socket;
    private DatagramPacket packet;
    private String dbAdress;

    private String receivedMsg;
    private ByteArrayOutputStream bOut;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String request;

    public HeartbeatServer(int listeningPort, String dbAdress) throws SocketException {
        socket = null;
        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
        socket = new DatagramSocket(listeningPort);
        this.dbAdress = dbAdress;
    }

    public String waitDatagram() throws IOException {

        if (socket == null) {
            return null;
        }

        socket.receive(packet);
        in = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));

        try {
            request = (String) (in.readObject());
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("Object was not a String " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
            return null;
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

    public void processRequests() {

        if (socket == null) {
            return;
        }

        int goOut = 0;
        while (goOut < 3) {

            try {

                receivedMsg = waitDatagram();
                if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                    goOut++;
                    continue;
                } else {
                    goOut = 0;
                }
                bOut = new ByteArrayOutputStream(MAX_SIZE);
                out = new ObjectOutputStream(bOut);

                out.writeObject(dbAdress);
                packet.setData(bOut.toByteArray());
                packet.setLength(bOut.size());

                System.out.println("Answer size: " + bOut.size());
                socket.send(packet);
                Thread.sleep(10 * 1000);
            } catch (IOException e) {
                System.out.println(e);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }

    }

    public void interrupt() {
        try {

            if (in != null) {
                in.close();
            }
            if (bOut != null) {
                bOut.close();
            }
            if (out != null) {
                out.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            System.err.println("Heartbeat server: IOException closing..." + ex);
        }
    }

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
