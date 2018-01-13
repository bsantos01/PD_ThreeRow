/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.server;

import Server.DBhandler;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
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
    private DBhandler database;

    private List<String> pairs;
    private List<String> users;
    private List<String> oldGames;

    public RMIService(String ip) throws RemoteException {
        this.observers = new ArrayList<ServerMonitorListener>();

        this.ipAndPort = ip;

        this.pairs = new ArrayList<>();
        this.users = new ArrayList<>();
        this.oldGames = new ArrayList<>();

        this.database = new DBhandler(ip);
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

                System.out.println("RMIService starting...");

            } catch (RemoteException e) {
                System.out.println("RMIService already executing!");
                r = LocateRegistry.getRegistry();
            }

            RMIService directoryService = new RMIService(ipAndPort);
            r.bind("PD", directoryService);

            System.out.println("RMIService started!");

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

        try {
            if (database.getUsersLogged() != null) {
                users.addAll(database.getUsersLogged());
            } else {
                users.add("No users are On!");
            }
        } catch (SQLException ex) {
            System.out.println("RMIService: SQLException " + ex);
        } catch (Exception ex) {
            System.out.println("RMIService: Exception " + ex);
            //users.add("No users are On!");
        }
        return users;

    }

    @Override
    public List<String> getPairs() throws RemoteException {
        try {
            if (database.getPairs() != null) {
                pairs.addAll(database.getPairs());
            } else {
                pairs.add("No users are On!");
            }
        } catch (SQLException ex) {
            System.out.println("RMIService: SQLException " + ex);
        } catch (Exception ex) {
            System.out.println("RMIService: Exception " + ex);
        }
        return pairs;
    }

    @Override
    public List<String> getOldGames() throws RemoteException {
        try {
            if (database.getPairs() != null) { //getGames..
                oldGames.addAll(database.getPairs());
            } else {
                oldGames.add("No users are On!");
            }
        } catch (SQLException ex) {
            System.out.println("RMIService: SQLException " + ex);
        } catch (Exception ex) {
            System.out.println("RMIService: Exception " + ex);
        }
        return oldGames;
    }

}
