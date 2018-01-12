/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import rmi.commons.RemoteServiceInterface;
import rmi.commons.ServerMonitorListener;

/**
 *
 * @author simao
 */
public class RMIService extends UnicastRemoteObject implements RemoteServiceInterface, Runnable {

    private static ArrayList<ServerMonitorListener> observers;
    private String ipAndPort = "";

    public RMIService(String ip) throws RemoteException {
        this.observers = new ArrayList<ServerMonitorListener>();
        this.ipAndPort = ip;
    }

    public void notifyObservers() throws RemoteException {
        //System.out.println("Observadores: " + observers.size());
        for (ServerMonitorListener observer : observers) {
            //if (observer.wait())
            if (observer != null) {
                observer.printPairs();
            }
        }
    }

    @Override
    public void run() {
        try {
            Registry r;
            try {
                System.out.println("RMIService working @ port: " + Registry.REGISTRY_PORT);
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

                System.out.println("RMIService started!");

            } catch (RemoteException e) {
                System.out.println("RMIService already executing!");
                r = LocateRegistry.getRegistry();
            }

            RMIService directoryService = new RMIService(ipAndPort);
            r.bind("PD", directoryService);

            System.out.println("RMIService started...");

        } catch (RemoteException e) {
            System.out.println("RMIService RemoteException " + e);
            System.exit(1);
        } catch (Exception e) {
            System.out.println("RMIService Error " + e);
            System.exit(1);
        }
    }

    @Override
    public void addObserver(ServerMonitorListener observer) throws RemoteException {
        observers.add(observer);
        notifyObservers();
    }

    @Override
    public void removeObserver(rmi.commons.ServerMonitorListener observer) throws RemoteException {
        if (!observers.isEmpty()) {
            observers.remove(observer);
        }
    }

    @Override
    public List<String> getUsers() throws RemoteException {
        List<String> list = new ArrayList<>();
        list.add("String Server RMISERVICE");
        return list;
    }

    @Override
    public List<String> getPairs() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getOldGames() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
