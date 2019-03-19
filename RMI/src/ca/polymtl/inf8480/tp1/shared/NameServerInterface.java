/**
 * 
 */
package ca.polymtl.inf8480.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;


import ca.polymtl.inf8480.tp1.server.Server;

/**
 * @author marcejthony
 *
 */
public interface NameServerInterface extends Remote {
	public boolean verifyRepartiteur( String login, String password) throws RemoteException ;
	public boolean addServer (String hostname, int port, int cap )  throws RemoteException ;
	public boolean removeServer() throws RemoteException ;
	public Set<ServerObject> getListServer() throws RemoteException;
	public void addOneRepartiteur(String login, String pwd) throws RemoteException;
}
