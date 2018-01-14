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

    boolean exit;

    public HeartbeatServer(int listeningPort, String dbAdress) throws SocketException {
        exit = false;
        socket = null;
        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
        socket = new DatagramSocket(listeningPort);
        this.dbAdress = dbAdress;
    }

    public String waitDatagram() throws IOException {

        if (socket == null) {
            return null;
        }
        socket.setSoTimeout(5000);
        
        if(exit) return "exit"; 
        System.out.println(exit);
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
    Thread HeartbeatThread = new Thread(new Runnable() {

        @Override
        public void run() {
            processRequests();
            socket.close();
        }

    });

    public void start() {

        HeartbeatThread.start();

    }

    public void processRequests() {

        if (socket == null) {
            return;
        }

        int goOut = 0;
        while (goOut < 3 && !exit) {

            try {

                receivedMsg = waitDatagram();
                if(exit)return;
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
                for(int i = 0; i < 10; i++){
                    Thread.sleep(1000);
                    if(exit)return;
                }
            } catch (IOException e) {
                System.out.println(e);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }

    }

    public void interrupt() {
        try {
            exit = true;
            //System.out.println("Thread is alive?" + HeartbeatThread.isAlive());
            //System.out.println(exit);

            HeartbeatThread.interrupt();

            if (in != null) {
                in.close();
            }
            if (bOut != null) {
                bOut.close();
            }
            if (out != null) {
                out.close();
            }

            socket.close();
            
            
            System.err.println("HeartbeatServer.interrupt()");
            HeartbeatThread.join();

        } catch (IOException ex) {
            System.err.println("Heartbeat server: IOException closing..." + ex);
        } catch (Exception ex) {
            System.err.println("Heartbeat server: Exception closing..." + ex);
        }
    }
}
