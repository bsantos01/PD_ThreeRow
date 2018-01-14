package GameClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IllegalStateException, IOException, InterruptedException {
//
//        GameClientDELETE c1 = new GameClientDELETE("7777");
//        Thread cl1 = new Thread(c1);
//        cl1.setDaemon(true);
//        cl1.start();
//        cl1.join();

        Client cl = new Client("localhost", "6001");
        cl.start();


    }
}
