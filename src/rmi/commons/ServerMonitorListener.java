package rmi.commons;

import java.rmi.RemoteException;

public interface ServerMonitorListener extends java.rmi.Remote {

    public void printPairs() throws RemoteException;

    public void printUsers() throws RemoteException;

    public void printHistory() throws RemoteException;

    //print LoggedUsers
    //print Pairs //inCreation //inGame
    //print history
}
