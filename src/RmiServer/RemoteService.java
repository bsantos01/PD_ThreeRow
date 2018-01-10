package RmiServer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class RemoteService implements RemoteServiceInterface {

    public RemoteService() throws RemoteException {
    }

    @Override
    public List<Player> getUsersLogged() throws RemoteException {
        //DBHandler get Users, create Players, return
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

}
