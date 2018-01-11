package RmiServer;

import RmiClient.Player;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RemoteServiceInterfaceImpl extends UnicastRemoteObject implements RemoteServiceInterface {

    public RemoteServiceInterfaceImpl() throws RemoteException {
    }

    @Override
    public String getTheTruth() throws RemoteException {

        return "Hell yeah!";
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> getUsersLogged() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Player, Player> getPairs() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Map<Player, Player>, String> getHistory() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {

        String registration;
        RemoteServiceInterfaceImpl server = null;
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
                r.bind("ServidorGestao", server);
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
