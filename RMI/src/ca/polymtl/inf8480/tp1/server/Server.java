package ca.polymtl.inf8480.tp1.server;

import ca.polymtl.inf8480.tp1.shared.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;



public class Server implements ServerInterface {
	 static int  port_  ;
	static int  cap_  ;
	 static int  malicePctg_  ;
	 static String  nameServerHostname ;
	 private NameServerInterface localNameServerStub ; // nameserver

	public static void main(String[] args) {

		if (checkArgs(args)) {
	        Server server = new Server();
		    server.run();
		      }
	        else
	        {
	        	sendErrorMessage();
	        	System.exit(0);
	         }
	}


	public Server() {
		super();
	}

	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		//---------------------------------
		 localNameServerStub = loadNameServerStub(nameServerHostname); //nameserver ip
		 String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		 try {
			localNameServerStub.addServer(hostname, port_, cap_);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 //--------------------------------------
		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, port_);

			Registry registry = LocateRegistry.createRegistry(port_);;
			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}


	   /**
	    * Verify session info of the repartiteur to nameServer.
	    * @param login
	    * @param pwd
	    * @return
	    */
	@Override
	public boolean openSession(String login , String pwd) {

		try {
			if (localNameServerStub.verifyRepartiteur(login, pwd)) {
				return true;
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

/**
 *
 * @param args
 * @return
 */
	public static boolean checkArgs(String[] args)
	{
		boolean result ;
		if  (args.length != 7 )
		{
			sendErrorMessage();
			result = false;
		}
		else
		{
			switch (args[0])
			{
				case "-m" :
					switch (args[2])
					{
						case "-q" :
							switch (args[4])
							{
								case "-p" :
									port_ = Integer.parseInt(args[5]);
									cap_ = Integer.parseInt(args[3]);
									malicePctg_ = Integer.parseInt(args[1]);
									nameServerHostname = args[6];
									result = true;
									System.out.println("NameServer IP = "+nameServerHostname);
									System.out.println("Capacity = "+ Integer.toString(cap_) + " operations");
									System.out.println("Malice = "+ Integer.toString(malicePctg_) + "%");
									System.out.println("port = "+ Integer.toString(port_));
								break;

								default:
									sendErrorMessage() ;
									result = false;
								break;
							}
						break;

						default:
							sendErrorMessage() ;
							result = false;
						break;
					}

				break;

				default:
					sendErrorMessage() ;
					result = false;
				break;
			}
		}
		return result;
	}



	/**
	 * Compute the % of refusal operation.
	 * @param Ui
	 * @param Qi
	 * @return
	 */
	public static float txRefus(int Ui, int Qi)
	{
				return  ( ((float)(Ui-Qi)/ (float)(5*Qi) )) ;
	}

	/**
	 * Check refusal ok.
	 * @param txRefus
	 * @return
	 */
	public static boolean RefusalOK (int txRefus )
	{
		Random randGen = new Random();
		int randInt = randGen.nextInt(100);

		if ( randInt <= txRefus)
			return true;
		else
			return false;
	}

	@Override
	public ArrayList<Integer> CalculOperation(ArrayList<Pair> listeOperations) throws RemoteException {
		// TODO Auto-generated method stub
		ArrayList<Integer> aList= new ArrayList<Integer>();

		// compute the % of refusal to make a compute.
		int txRefuse = (int) (txRefus(listeOperations.size(),cap_)*100);
		if (RefusalOK(txRefuse)) {
			System.out.println( "Operation refuse !"  );
					return null ;
		}
		else
		{
		Operations Op = new Operations();
		for (Pair oneP : listeOperations)
		{
			switch ( oneP.getOperation() )
			{
				case "pell" :

					if (!RefusalOK (malicePctg_))
					{
						aList.add(Op.pell(oneP.getOperande()));
						System.out.println("Operation pell of :" + oneP.getOperande() + " in Correct mode.\n ----> " + Integer.toString(Op.pell(oneP.getOperande())) );
					}
					else
					{
						// Adding a false number between 1 - 100 in the result.
						Random randGen = new Random();
						int randInt = randGen.nextInt(100);
						aList.add( Op.pell(oneP.getOperande()) + randInt );
						System.out.println("Operation pell of: " + oneP.getOperande() + " in Incorrect mode.\n ----> " + Integer.toString(Op.pell(oneP.getOperande()) + randInt) );
					}
				break;

				case "prime" :
			if (!RefusalOK (malicePctg_))
					{
						aList.add(Op.prime(oneP.getOperande()));
						System.out.println("Operation prime of: " + oneP.getOperande() + " in Correct mode.\n ----> " + Integer.toString(Op.prime(oneP.getOperande())) );
					}
					else
					{
						//  Adding a false number between 1 - 100 in the result.
						Random randGen = new Random();
						int randInt = randGen.nextInt(100);
						aList.add( Op.prime(oneP.getOperande()) + randInt );
						System.out.println("Operation prime of: " + oneP.getOperande() + " in Incorrect mode.\n ----> "  + Integer.toString(Op.prime(oneP.getOperande()) + randInt) );
					}

				break;
			}
		}

		return aList;
		}
		}


	/**
	 * Send a message error to the user
	 * @return
	 */
	private static void sendErrorMessage() {
		// TODO Auto-generated method stub
		System.out.println(" Error command or Syntax ! \n");
		System.out.println(" Ex. de commande :");
		System.out.println("./server -m 30 -q 4 -p 5002");
		System.out.println(" m : taux de malice en %, q: capacité d'operations, p: numero de port (not 5001)");
	}




	 /**
	  *
	  * @param nameserverHost
	  * @return
	  */
	 private NameServerInterface loadNameServerStub(String nameserverHost) {
		 NameServerInterface stubNameServer = null;

		 try {
			 Registry registry = LocateRegistry.getRegistry(nameserverHost,5001);
			 stubNameServer = (NameServerInterface) registry.lookup("nameserver");
		 } catch (NotBoundException e) {
			 System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		 } catch (AccessException e) {
			 System.out.println("Erreur: " + e.getMessage());
		 } catch (RemoteException e) {
			 System.out.println("Erreur: " + e.getMessage());
		 }
		 return stubNameServer;
	 }




}
