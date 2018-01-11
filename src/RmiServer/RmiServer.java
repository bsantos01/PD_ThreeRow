package RmiServer;

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {

    //public Server() {
    public static void main(String[] args) {

        String registration;
        Remote server = null; //da fuck is suposed to be here?
        try {
            Registry r;
            try {
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            } catch (RemoteException e) {
                System.out.println("RmiServer: RemoteException " + e);

                r = LocateRegistry.getRegistry();
            }
            try {
                server = new RemoteServiceInterfaceImpl();

                registration = "rmi://localhost/ServidorGestao";
//                r.bind(registration, server);
                System.err.println("RmiServer: " + registration);
                Naming.bind(registration, server);
                //Stuff hapends
                //Naming.unbind(registration);

            } catch (AlreadyBoundException ex) {
                System.out.println("RmiServer: Url Exception " + ex);
//            } catch (NotBoundException ex) {
//                System.out.println("RmiServer: NotBoundException" + ex);
            } catch (Exception ex) {
                System.out.println("RmiServer: Exception - Probably bind it's already registered... " + ex);
            }

        } catch (RemoteException ex) {
            System.out.println("RmiServer: RemoteException 2 " + ex);
        }
    }

}
