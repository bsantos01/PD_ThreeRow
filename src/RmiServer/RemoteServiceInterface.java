package RmiServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface RemoteServiceInterface extends Remote {

    List<Player> getUsersLogged() throws RemoteException;   //Client->Player

    Map<Player, Player> getPairs() throws RemoteException;         //String, String

    Map<Map<Player, Player>, String> getHistory() throws RemoteException;  //String, String, String | Player
    //Listeners :/
}
