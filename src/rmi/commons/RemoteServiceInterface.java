package rmi.commons;

import java.rmi.Remote;
import java.util.List;

public interface RemoteServiceInterface extends Remote {

    public List<String> getUsers() throws java.rmi.RemoteException;

    public List<String> getPairs() throws java.rmi.RemoteException;

    public List<String> getOldGames() throws java.rmi.RemoteException;

    public void addObserver(ServerMonitorListener observer) throws java.rmi.RemoteException;

    public void removeObserver(ServerMonitorListener observer) throws java.rmi.RemoteException;
}
