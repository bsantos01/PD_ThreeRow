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
public class ClientPair {
    private final String client1;
    private final String client2;

    public ClientPair(String client1, String client2) {
        this.client1 = client1;
        this.client2 = client2;
    }

    public String getClient1() {
        return client1;
    }

    public String getClient2() {
        return client2;
    }

    public ClientPair opposite() {
        return new ClientPair(client2, client1);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + client1.hashCode();
        hash = 73 * hash + client2.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientPair other = (ClientPair) obj;
        return client1.equals(other.client1) && client2.equals(other.client2);
    }

    @Override
    public String toString() {
        return "[" + client1 + "," + client2 + "]";
    }
}