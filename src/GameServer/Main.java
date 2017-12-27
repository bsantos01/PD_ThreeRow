package GameServer;

public class Main
{

    public static void main(String[] args) throws IllegalStateException, ClassNotFoundException
    {

//        ThreeInRowView GUI = new ThreeInRowView(new ObservableGame());
//        ObservableGame obs = new ObservableGame();
        GameServer server = new GameServer("server", "localhost", 7777);
        server.start();

    }

}
