package RmiClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RmiClient {

    public static void main(String[] args) {

        try {
//            String registry = "localhost";
//            System.out.println("\nServices in registry\"//" + registry + ":" + Registry.REGISTRY_PORT + "\":");
//            String[] serviceList = Naming.list(registry);
//            for (int i = 0; i < serviceList.length; i++) {
//                StringTokenizer tokens = new StringTokenizer(serviceList[i], "/: ");
//                tokens.nextToken();
//                //OMITE O VALOR DO PORTO
//                String serviceName = tokens.nextToken();
//                System.out.println(serviceName);
//            }
//
            String url = "rmi://localhost/ServidorGestao";
            RemoteServiceInterface service = (RemoteServiceInterface) Naming.lookup(url);

            System.err.println("RMI: " + service.getTheTruth());
        } catch (NotBoundException ex) {
            System.out.println("RmiClient: NotBoundException" + ex);
        } catch (MalformedURLException ex) {
            System.out.println("RmiClient: Url Exception " + ex);
        } catch (RemoteException ex) {
            System.out.println("RmiClient: RemoteException " + ex);
        } catch (Exception ex) {
            System.out.println("RmiClient: Exception " + ex);
        }
    }

}
