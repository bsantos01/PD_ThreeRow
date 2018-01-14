package rmi.commons;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerMonitorListener extends Remote {

    public void printSeparator() throws RemoteException;

    public void printPairs() throws RemoteException;

    public void printUsers() throws RemoteException;

    public void printHistory() throws RemoteException;

    public void updatePrinter() throws RemoteException;

}
