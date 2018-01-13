package GameClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Santos
 */
public final class Client {

    Socket ToServer;
    String ServerAddr;
    int servicePort;
    ObjectInputStream in;
    ObjectOutputStream out;
    Scanner sc;
    String username;

    public Client(String ServerAddr, String servicePort) {
        this.ServerAddr = ServerAddr;
        this.servicePort = Integer.parseInt(servicePort);
        sc = new Scanner(System.in);

    }

    private Integer findRandomOpenPort() {
        try (
                ServerSocket socket = new ServerSocket(0);) {
            return socket.getLocalPort();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void login() {
        String msg;
        String[] arr = null;
        System.out.println("LOGIN PAGE");
        System.out.println("please use comand ->login username password");
        System.out.println("please use comand ->register username password");
        try {
            do {
                Object temp = (String) sc.nextLine();
                out.writeObject(temp);

                temp = (String) in.readObject();
                System.out.println("Login: " + temp);

                msg = (String) temp;
                arr = msg.split("[\\W]");
            } while (!arr[1].equals("sucefully"));

        } catch (Exception e) {
            System.out.println("Client: Error during login.");
        }
        username = arr[0];
        int freePort = findRandomOpenPort();
        GameClientConnector c1;
        try {
            out.writeObject(("port " + username + " "+freePort));//escrever para o servidor de gestão actualizar a base de dados com a porta

            c1 = new GameClientConnector(freePort, username); //username
            Thread cl1 = new Thread(c1);
            cl1.setDaemon(true);
            cl1.start();
            //cl1.join();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        commThread.start();
        ScListener();

    }

    public void ScListener() {

        String[] arr = null;
        while (true) {
            try {
                System.out.println("Execute Comando");
                Object temp = (String) sc.nextLine();
                if (temp != null) {
                    String msg = (String) temp;

                    arr = msg.split("[\\W]");
                }
                if (arr[0].equals("help")) {
                    ShowHelp();
                } else {
                    out.writeObject(temp);
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void start() throws IOException {

        try {
            ToServer = new Socket(this.ServerAddr, this.servicePort);
        } catch (IOException ex) {
            System.out.println("Não foi possivel iniciar o socket");
        }
        try {
            out = new ObjectOutputStream(ToServer.getOutputStream());//Cria um ObjectOutputStream associado ao socket s
            in = new ObjectInputStream(ToServer.getInputStream()); //Cria um ObjectInputStream associado ao socket s
        } catch (IOException e) {
            System.out.println("erro ao criar streams");
        }
        login();

    }

    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Object temp = in.readObject();
                    System.out.println("recebi" + temp.toString());
                    if (temp instanceof List) {
                        List<String> list = (List) temp;
                        for (int i = 0; i < list.size(); i++) {
                            System.out.print("MSG: " + list.get(i));
                        }
                    } else if (temp instanceof String) {
                        System.out.println("recebi uma String");
                        String msg = (String) temp;
                        String[] arr = msg.split("[\\W]");
                        if (arr[0].equals("gamereq")) {

                            System.out.println("O jogador " + arr[1] + " pretende iniciar um jogo consigo. Responda! (S/N)");

                        }
                        if (temp instanceof List) {
                            List<String> list = (List) temp;
                            for (int i = 0; i < list.size(); i++) {
                                System.out.println(list.get(i));
                            }
                        } else {
                            System.out.println(Arrays.toString(arr));
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    });

    private void ShowHelp() {
        System.out.println("-----------------Comandos-----------------");
        System.out.println("|gamereq user                            |");
        System.out.println("|accept yes/no                           |");
        System.out.println("|list                                    |");
        System.out.println("|                                        |");
        System.out.println("|                                        |");
        System.out.println("------------------------------------------");
    }

}
