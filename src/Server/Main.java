package Server;

/**
 *
 * @author Bruno Santos
 */
public class Main {

    public static void main(String[] args) throws IllegalStateException, ClassNotFoundException {
//        if(args.length != 3)
//        {
//            System.out.println("Wrong syntax! serverName serviceIP servicePort");
//            return;
//        }

        Server server = new Server("server", "localhost", 6001);
        server.start();
    }

}
