package rmi.client;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import rmi.commons.RemoteServiceInterface;
import rmi.commons.ServerMonitorListener;

public class RMIClient extends UnicastRemoteObject implements ServerMonitorListener, Runnable {

    private String addr;

    RemoteServiceInterface rmiService;

    public RMIClient(String string) throws RemoteException {
        this.addr = string;

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

    @Override
    public void printPairs() throws RemoteException {

        try {
            List<String> users = new ArrayList<>();
            users.add("uno uno uno");

            if (rmiService.getUsers() != null) {
                users.addAll(rmiService.getUsers());
            }

            for (String str : users) {
                System.err.println("Monitor: " + str);
            }
        } catch (Exception e) {
            System.out.println("rmi.client.RMIClient.printPairs() " + e);
        }
    }

    public void terminate() throws RemoteException {
        rmiService.removeObserver(this);
    }

}
