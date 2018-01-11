package RmiClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClientInterfaceImpl extends UnicastRemoteObject implements RmiClientInterface {

    public RmiClientInterfaceImpl() throws RemoteException {

    }

}
