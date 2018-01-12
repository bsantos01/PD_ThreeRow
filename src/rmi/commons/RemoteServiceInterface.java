/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.commons;

import rmi.commons.ServerMonitorListener;
import java.rmi.Remote;

/**
 *
 * @author simao
 */
public interface RemoteServiceInterface extends Remote {

    public String getServerList() throws java.rmi.RemoteException;

    public void addObserver(ServerMonitorListener observer) throws java.rmi.RemoteException;

    public void removeObserver(ServerMonitorListener observer) throws java.rmi.RemoteException;
}
