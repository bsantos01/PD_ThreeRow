/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.client;

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
public class RMIClient extends UnicastRemoteObject implements ServerMonitorListener, Runnable {

    private String addr;

    RemoteServiceInterface rmiService;
    String output = "Printing servers";

    public RMIClient() throws RemoteException {
        this.addr = "localhost";

    }

    @Override
    public void run() {
        try {
            String registration = "rmi://" + addr + "/PD";
            Remote remoteService = Naming.lookup(registration);
            rmiService = (RemoteServiceInterface) remoteService;
            rmiService.addObserver(this);

        } catch (NotBoundException e) {
            System.out.println("Não existe servico disponivel! ");
        } catch (RemoteException e) {
            System.out.println("Erro no RMI: " + e);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }

    public RemoteServiceInterface getService() {
        return rmiService;
    }

    public void terminate() throws RemoteException {
        rmiService.removeObserver(this);
    }

    @Override
    public void printServers() throws RemoteException {
        System.out.println(output);

    }
}
