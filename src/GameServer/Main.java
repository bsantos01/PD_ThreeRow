package GameServer;

public class Main
{
        public static void main(String[] args) throws IllegalStateException, ClassNotFoundException
    {
        UDPClient cliente = new UDPClient("UDPclient", 6999);
        cliente.start();
    }
    
    
}
