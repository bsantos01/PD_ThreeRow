package GameServer;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("Sending packet");

            socket.send(sendPacket);
            System.out.print("Waiting for packet.... ");

            byte[] buffer = new byte[BUFSIZE];
            DatagramPacket receivePacket = new DatagramPacket(buffer, BUFSIZE);

            boolean timeout = false;

            try
            {
                socket.receive(receivePacket);
            } catch (InterruptedIOException e)
            {
                System.out.println("Timeout error " + e);
                timeout = true;
            }
            if (!timeout)
            {
                System.out.println("packet received!");
                System.out.println("Details : " + receivePacket.getAddress());

                in = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength()));
                String msgs = (String) in.readObject();

                System.out.println(msgs);
            } else
            {
                System.out.println("packet lost!");
            }

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                System.out.println("Interrupted " + e);
            }
        } catch (IOException e)
        {
            System.out.println("IoException error " + e);
        } catch (ClassNotFoundException ex)
        {
            System.out.println("Class not found error " + ex);
        }

    }

}
