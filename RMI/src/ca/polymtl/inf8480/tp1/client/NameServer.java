/**
 * 
 */
package ca.polymtl.inf8480.tp1.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ca.polymtl.inf8480.tp1.server.Server;
import ca.polymtl.inf8480.tp1.shared.NameServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerObject;



/**
 * @author marcejthony
 *
 */
public class NameServer implements NameServerInterface {
    String loginRepartiteur ;
	String pwdRepartiteur ;
	static int port = 5001 ;
	static int rmiPort = 5001 ;
	private Set<ServerObject> listServers  = new HashSet<>() ;
	

	
public static void main(String[] args) {
		if ( args.length == 0 ) {
	        NameServer nameserver = new NameServer();
		    nameserver.run();           
		      }
	        else 
	        {
	        	sendErrorMessage();  
	        	System.exit(0);
	         }
	}
	

	public NameServer() {
		super();
	}

	

	 /**
    * Register nameserver to JAVA RMI for remote call
    */
	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		 String hostname = null;
			try {
				hostname = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
			System.out.println("IP Address to access the Name Server : " + hostname );
			
			try {
		 NameServerInterface stub = (NameServerInterface) UnicastRemoteObject.exportObject(this, port);

         
		 Registry registry = LocateRegistry.createRegistry(rmiPort);
		 registry.rebind("nameserver", stub);
			
			System.out.println("Name Server ready.");
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	
		
		
		
		
		
//-----------------------  Partie Serveur -----------------------	
	@Override
	public boolean verifyRepartiteur(String login, String password) throws RemoteException {
		// TODO Auto-generated method stub
		
		 if (login.equals(loginRepartiteur) && password.equals(pwdRepartiteur)) {
	            System.out.println( " Computing Server : Connected !"  );
	            return true;
	        }
	        return false;
	}
	

	@Override
/**
 * Adding each connected compute server to the list of available server for the repetiteur.
 */
	public boolean addServer(String hostname, int port, int cap ) throws RemoteException {
		System.out.println("Connected Compute Server infos : " + hostname +":"+ port );
		// TODO Auto-generated method stub
		ServerObject oneServer = new  ServerObject();
		oneServer.setServerIp(hostname);
		oneServer.setPortServer(port);
		oneServer.setCapServer(cap);
		
		listServers.add(oneServer);
		
		return false;
	}

	/**
	 * removing each down scompute server to the list
	 */
	@Override
	public boolean removeServer() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}


//-----------------------  Partie Repartiteur -----------------------
	/**
	 * creating the infos for the connnected repetiteur.
	 */
	@Override
	public void addOneRepartiteur(String login , String pwd) {
		this.loginRepartiteur = login;
		this.pwdRepartiteur = pwd ;
		System.out.println("Repartiteur connected to NameServer !");
	}
	
	
	/**
	 * Getting the list of all available compute server.
	 */
	@Override	public Set<ServerObject> getListServer() throws RemoteException {
		// TODO Auto-generated method stub
		return listServers;
	}
	
	
	 /**
	  * Send a message error to the user
	  * @return
	  */
	 private static void sendErrorMessage() {
		 // TODO Auto-generated method stub
		 System.out.println(" Error command or Syntax ! \n");
	 }

}
