/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.server;

import rmi.commons.RemoteServiceInterface;
import rmi.commons.ServerMonitorListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author simao
 */
public class RMIService extends UnicastRemoteObject implements RemoteServiceInterface, Runnable {

    private static ArrayList<ServerMonitorListener> observers;

    public RMIService() throws RemoteException {
        observers = new ArrayList<ServerMonitorListener>();
    }

    public void notifyObservers() throws RemoteException {
        //System.out.println("Observadores: " + observers.size());
        for (ServerMonitorListener observer : observers) {
            //if (observer.wait())
            if (observer != null) {
                observer.printServers();
            }
        }
    }

    @Override
    public void run() {
        try {
            Registry r;
            try {
                System.out.println("Tentativa de lancamento do registry no porto "
                        + Registry.REGISTRY_PORT + "...");
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                System.out.println("Registry lancado!");

            } catch (RemoteException e) {
                System.out.println("Registry provavelmente ja' em execucao!");
                r = LocateRegistry.getRegistry();
            }

            RMIService directoryService = new RMIService();

            System.out.println("Servico RMI criado ");

            r.bind("PD", directoryService);

            System.out.println("Servico DirectoryService registado no registry...");

        } catch (RemoteException e) {
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Erro - " + e);
            System.exit(1);
        }
    }

    @Override
    public String getServerList() throws RemoteException {
        return "String Server RMISERVICE";
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

}
