/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerTestes;

/**
 *
 * @author Bruno Santos
 */

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocket implements Closeable {
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private final String ownId;
    private final String peerId;

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        DataInputStream din = new DataInputStream(in);
        this.ownId = din.readUTF();
        this.peerId = din.readUTF();
    }

    public ClientSocket(String server, int port, String ownId, String peerId)
            throws IOException {
        this.socket = new Socket(server, port);
        this.socket.setTcpNoDelay(true);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.ownId = ownId;
        this.peerId = peerId;
        DataOutputStream dout = new DataOutputStream(out);
        dout.writeUTF(ownId);
        dout.writeUTF(peerId);
    }

    public String getOwnId() {
        return ownId;
    }

    public String getPeerId() {
        return peerId;
    }

    public InputStream getInputStream() {
        return in;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
