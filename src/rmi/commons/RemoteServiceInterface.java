package rmi.commons;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteServiceInterface extends Remote {

    public List<String> getUsers() throws RemoteException;

    public List<String> getPairs() throws RemoteException;

    public List<String> getOldGames() throws RemoteException;

    public void addObserver(ServerMonitorListener observer) throws RemoteException;

    public void removeObserver(ServerMonitorListener observer) throws RemoteException;
}
