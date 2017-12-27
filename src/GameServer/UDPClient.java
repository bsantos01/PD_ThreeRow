package GameServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient 
{

    public static final int BUFSIZE = 3500;

    private final String clientName;
    private final int port;
    InetAddress addr = null;
    DatagramSocket socket;
    DatagramPacket sendPacket;
    ObjectInputStream in;
    ObjectOutputStream out;
    ByteArrayOutputStream bOut;

    public UDPClient(String clientName, int port)
    {
        this.clientName = clientName;
        this.port=port;
        try
        {
            addr = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException ex)
        {
            System.err.println("UDP UnknownHostException");
        }
    }

    public void start()
    {

        try
        {

            socket = new DatagramSocket();
            socket.setSoTimeout(2 * 1000);

            String message = "HEARTBEAT";

            bOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bOut);

            out.writeObject(message);
            out.flush();

            sendPacket = new DatagramPacket(bOut.toByteArray(), bOut.size(), addr, port);
            System.out.println("UDP Sending packet");

            socket.send(sendPacket);
            System.out.print("UDP Waiting for packet.... ");

            byte[] buffer = new byte[BUFSIZE];
            DatagramPacket receivePacket = new DatagramPacket(buffer, BUFSIZE);
            boolean timeout = false;

            try
            {
                socket.receive(receivePacket);
            } catch (InterruptedIOException e)
            {
                System.out.println("UDP Timeout error " + e);
                timeout = true;
            }
            if (!timeout)
            {
                System.out.println("UDP Packet received!");
                System.out.println("Details : " + receivePacket.getAddress());

                in = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength()));
                String msgs = (String) in.readObject();

                System.out.println(msgs);
            } else
            {
                System.out.println("Nothing recieved... UDP Packet lost!");
            }

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                System.out.println("UDP Interrupted thread " + e);
            }
        } catch (IOException e)
        {
            System.out.println("UDP IoException error " + e);
        } catch (ClassNotFoundException ex)
        {
            System.out.println("UDP Class not found error " + ex);
        }

    }

}
