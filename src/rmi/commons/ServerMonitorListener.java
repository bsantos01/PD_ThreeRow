package rmi.commons;

public interface ServerMonitorListener extends java.rmi.Remote {

    public void printPairs() throws java.rmi.RemoteException;

    //print LoggedUsers
    //print Pairs //inCreation //inGame
    //print history
}
