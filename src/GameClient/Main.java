package GameClient;

import java.io.IOException;

public class Main
{

    public static void main(String[] args) throws IllegalStateException, IOException, InterruptedException
    {

        GameClient c1 = new GameClient("7777");
        Thread cl1 = new Thread(c1);
        cl1.setDaemon(true);
        cl1.start();
        cl1.join();
        
        
        GameClient c2 = new GameClient("8888");
        Thread cl2 = new Thread(c2);
        cl2.setDaemon(true);
        cl2.start();
        cl2.join();

    }
}
