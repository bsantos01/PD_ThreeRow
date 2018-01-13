package game.server;

public class Main {

    public static void main(String[] args) {

        GameServer server = new GameServer("localhost", 6999);
        server.run();

    }

}
