package RmiClient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface RemoteServiceInterface extends Remote {

    public String getTheTruth() throws RemoteException;

    public List<Player> getUsersLogged() throws RemoteException;   //Client->Player

    public Map<Player, Player> getPairs() throws RemoteException;  //or String, String

    public Map<Map<Player, Player>, String> getHistory() throws RemoteException;  //String, String, String | Player ->
    //Easyer to create a historicObject to pass...

    //Listeners :/
}
