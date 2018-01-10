package RmiServer;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    //public Server() {
    public static void main(String[] args) {

        String registration;
        Remote server = null; //da fuck is suposed to be here?

        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            System.out.println("RmiServer: RemoteException " + e);
            try {

                registration = "rmi://localhost/ServerService";
                Naming.bind(registration, server);
                //Stuff hapends
                Naming.unbind(registration);

            } catch (MalformedURLException | AlreadyBoundException ex) {
                System.out.println("RmiServer: Url Exception " + ex);
            } catch (RemoteException ex) {
                System.out.println("RmiServer: RemoteException 2 " + ex);
            } catch (NotBoundException ex) {
                System.out.println("RmiServer: NotBoundException" + ex);
            }
        }

    }
}
