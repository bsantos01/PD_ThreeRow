/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.monitor;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import rmi.commons.RemoteServiceInterface;
import rmi.commons.ServerMonitorListener;

/**
 *
 * @author simao
 */
public class ServerMonitor extends UnicastRemoteObject implements ServerMonitorListener {

    private static RemoteServiceInterface serverMonitor;

    public ServerMonitor() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException {
        ServerMonitor observer = new ServerMonitor();

        try {
            String registration = "rmi://localhost/PD";
            Remote remoteService = Naming.lookup(registration);
            serverMonitor = (RemoteServiceInterface) remoteService;
            serverMonitor.addObserver(observer);
        } catch (NotBoundException e) {
            System.out.println("Não existe servico disponivel! ");
        } catch (RemoteException e) {
            System.out.println("Erro no RMI: " + e);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }

    public void updateView(String s) {
        try {
            Runtime.getRuntime().exec("cls");
        } catch (IOException ex) {
        }
        System.out.println(s);
    }

    @Override
    public void printServers() throws RemoteException {
        System.err.println("printServers in Monitor");
    }
}
