package game.server;

import java.io.*;
import java.net.*;

public class GameCommUDP {

    public static final int BUFSIZE = 3500;

    private String clientName;
    private int port;
    private InetAddress addr = null;

    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ByteArrayOutputStream bOut;

    private String msgs = "";
    private boolean stop = false;

    public GameCommUDP(String clientName, int port) {
        this.clientName = clientName;
        this.port = port;
        try {
            addr = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException ex) {
            System.err.println("UDP UnknownHostException");
        }
    }

    public String port() {
        return msgs;
    }

    void heartbeat() {
        try {

            socket = new DatagramSocket();
            socket.setSoTimeout(3 * 1000);

            String message = "HEARTBEAT";

            bOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bOut);

            try {
                out.writeObject(message);
                out.flush();

                sendPacket = new DatagramPacket(bOut.toByteArray(), bOut.size(), addr, port);
                System.out.println("UDP Sending packet");

                socket.send(sendPacket);
                System.out.print("UDP Waiting for packet.... ");

                byte[] buffer = new byte[BUFSIZE];
                DatagramPacket receivePacket = new DatagramPacket(buffer, BUFSIZE);
                boolean timeout = false;

                try {
                    socket.receive(receivePacket);
                } catch (InterruptedIOException e) {
                    System.out.println("UDP Timeout error " + e);
                    timeout = true;
                }
                if (!timeout) {
                    System.out.println("UDP Packet received!");
                    System.out.println("Details : " + receivePacket.getAddress());

                    in = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength()));
                    msgs = (String) in.readObject();

                    System.out.println(msgs);
                } else {
                    System.out.println("Nothing recieved... UDP Packet lost!");
                }

                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("UDP Interrupted thread " + e);
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("UDP Class not found error " + ex);
            } catch (IOException e) {
                System.out.println("UDP IoException error " + e);
            }

        } catch (IOException e) {
            System.out.println("UDP IoException error " + e);
        }
    }

    public void start() {
        stop =false;

        Thread HeartbeatThread = new Thread(new Runnable() {
                
            @Override
            public void run() {
                while (!stop) {
                    heartbeat();
                }
            }

        });
        HeartbeatThread.start();

    }

    public void stop() {
        stop = true;
    }

}
