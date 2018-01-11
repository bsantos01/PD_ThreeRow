package RmiClient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface RemoteServiceInterface extends Remote {

    String getTheTruth() throws RemoteException;

    List<Player> getUsersLogged() throws RemoteException;   //Client->Player

    Map<Player, Player> getPairs() throws RemoteException;  //or String, String

    Map<Map<Player, Player>, String> getHistory() throws RemoteException;  //String, String, String | Player ->
    //Easyer to create a historicObject to pass...

    //Listeners :/
}
