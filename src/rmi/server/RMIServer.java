/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.server;

import java.rmi.RemoteException;

/**
 *
 * @author simao
 */
public class RMIServer {

    private static RMIService rmiService;

    public static void main(String[] args) {

        try {
            rmiService = new RMIService();
            rmiService.run();
        } catch (RemoteException ex) {
            System.out.println("Erro ao iniciar o servico RMI! " + ex);
        }
    }

}
