package ca.polymtl.inf8480.tp1.client;

import ca.polymtl.inf8480.tp1.shared.*;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Set;



public class Client {
	private static  long start_Time = 0;
	private static  long end_Time = 0;
	private static int nbOps =0 ;

	private static boolean secure_OK;
	private static ArrayList<SvrDetails> svrs = new ArrayList<SvrDetails>();
	private static ArrayList<SvrDetails> svrTmp = new ArrayList<SvrDetails>();
	String login = "user1",  pwd = "12345" ;
	public static volatile ArrayList<Integer> resultList= new ArrayList<Integer>();
	  boolean repartiteerConnect = false ;


	public static void main(String[] args) throws RemoteException, IOException {
		if (chkArgs(args)) {
			Client client = new Client();
			client.run(args);
		}
		else
			sendErrorMessage();
		System.exit(0);
	}


	/**
	 *
	 * @param args
	 * @return
	 */
	 private static boolean chkArgs(String[] args) {

		 if  (args.length == 0 )
		 {
			 System.out.println(" You must enter the path of the file operations. ");
			 return false;
		 }
		 else if (args.length >2)
		 {
			 System.out.println("Too many params.");
			 return false;
		 }
		 else
		 {
			 if (args.length == 2)
			 {
				 switch (args[1])
				 {
				 case "-u" :
					 secure_OK = false;
					 break;
				 case "-s" :
					 secure_OK = true;
					 break;
				 default:
					 System.out.println(" Incorrect mode, use : -u ou -s");
					 return false;

				 }
			 }
			 else
				 secure_OK = true ;

			 File file = new File(args[0]);
			 if( file.exists() && !file.isDirectory() )
				 return true;
			 else
			 {
				 System.out.println("Unable to find the file");
				 return false;
			 }
		 }
	 }
	 /**
	  * Displays the result of the operations.
	  * @param results
	  */
	 public static void  displayResult(ArrayList<Integer> results) {
		 String resultat = null;
		 int solution = 0;
		 boolean first = false;

		 for (int i : results)
		 {
			 solution = (solution + (i%5000)) % 5000;
			 if (first)
			 {
				 resultat = "( " + resultat + " + " + Integer.toString(i) + " ) % 5000 ";
			 }
			 else
			 {
				 resultat = Integer.toString(i) + " % 5000 " ;
				 first = true;
			 }
		 }
		 resultat = resultat + " = " + Integer.toString(solution);
		 System.out.println(resultat);

	 }

	 private ServerInterface localServerStub = null;
	 private NameServerInterface localNameServerStub = null;


	 public Client() {
		 super();

		 if (System.getSecurityManager() == null) {
			 System.setSecurityManager(new SecurityManager());
		 }

         // call nameServer stub function
		 localNameServerStub = loadNameServerStub("127.0.0.1"); //TODO: change to the nameserver ip.
		 try {
			localNameServerStub.addOneRepartiteur(login, pwd);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	 }


	 /**
	  *
	  * @param hostname
	  * @param port
	  * @return
	  */
	 private ServerInterface loadServerStub(String hostname, int port) {
		 ServerInterface stub = null;

		 try {

			 Registry registry = LocateRegistry.getRegistry(hostname, port);
			 stub = (ServerInterface) registry.lookup("server");
		 } catch (NotBoundException e) {
			 System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		 } catch (AccessException e) {
			 System.out.println("Erreur: " + e.getMessage());
		 } catch (RemoteException e) {
			 System.out.println("Erreur: " + e.getMessage());
		 }
		 return stub;
	 }




	 private void run(String[] args) throws RemoteException, IOException {

         Set<ServerObject> tmp = localNameServerStub.getListServer();

          if (!tmp.isEmpty())
          {
        	  for (ServerObject one : tmp) {
        		  SvrDetails svr = new SvrDetails();
        			 svr.setSrvIp(one.getServerIp());
        			 svr.setSvrPort(one.getPortServer());
        			 svr.setSvrCapacity(one.getCapServer());

        			 svrTmp.add(svr);
        	  }

          }

		 // Empty list to get the operation list from the server
		 ArrayList<Pair> listOp = new ArrayList<Pair> ();

		 // Fill the list with the list of operation.
		 // args[0] :  path to the ops file.
		 listOp =Tools.readOps(args[0]);

		 //Connect to the server stub
		 for (SvrDetails oneDS: svrTmp )
		 {
			 oneDS.setSvrStub(loadServerStub(oneDS.getSrvIp(),oneDS.getSvrPort() ));


			 //TODO: add condition here !
			 try {

		 		 repartiteerConnect =  openSessionRepartiteur(login, pwd, oneDS.getSvrStub());

		 		}
		 		 // Opening a session on the server.
		 	    catch (Exception e) {
		 		// TODO: handle exception
		 		   System.out.println("Error: " + e.getMessage());
		 	   	}
			 svrs.add(oneDS);

		 }

		 if (!repartiteerConnect) {
			 System.out.println("Error: Login Repartieur ! ");
		 }
		 else {

			// Start counting
			start_Time = System.nanoTime();
			// tracking threads
		 ArrayList<Thread> lstThreads = new ArrayList<Thread>();
		 ArrayList<ThreadCompute> lstThreadsCompute = new ArrayList<ThreadCompute>();


		 if (secure_OK){

			 nbOps= listOp.size();
			 int idx=0;
			 boolean lastOp=false;

			 ArrayList<Pair> listToSend= new ArrayList<Pair>();

			 while (idx < nbOps) {
				 for (SvrDetails s : svrs)
				 {
					 if (s.getSvrStub()!=null)
					 {
						 int listSize;
						 if( (idx+s.getSvrCapacity())< nbOps)
						 {
							 listSize=s.getSvrCapacity();
						 }
						 else
						 {
							 listSize=nbOps+idx;
							 lastOp=true;
						 }

						 listToSend=Tools.splitL(listOp,idx, idx+ listSize-1);
						 idx+= listSize;
						 ThreadCompute thCompute= new ThreadCompute(s.getSvrStub(),listToSend);
						 lstThreadsCompute.add(thCompute);
						 Thread th=new Thread(thCompute);
						 lstThreads.add(th);
						 th.start();

						 if(lastOp)
							 break;
					 }
				 }
			 }

			 int nbElement=0;

			 int taskIdx=0;

			 for( Thread th: lstThreads)
			 {
				 try {
					 th.join();
					 ThreadCompute TC= lstThreadsCompute.get(taskIdx++);
					 synchronized (resultList) {
						 if(TC.lstRst !=null)
						 {
							 for( int i : TC.lstRst)
							 {
								 nbElement++;
								 resultList.add(i);
							 }
						 }
					 }


				 } catch (InterruptedException e) {
					 // TODO: handle exception
					 System.out.println("Error Thread "+ th.getId());
				 }
			 }

			 if (!resultList.isEmpty()) {
				 displayResults(resultList);
				 callStats();
			 }else{
				 System.out.println("List Result is empty - Nothing to display.");
			 }

		 }else {
			 System.out.println("NON SECURISE !!!!");
			 if (svrTmp.size() < 2) {
		            System.out.println("Not enough server. Must have minimum 2 servers!");
		            return;
		        }else {
		        	// -----------------------------------------
		        	
		        	 nbOps= listOp.size();
					 int idx=0;
					 boolean lastOp=false;
                     boolean matchResult = false ;
					 ArrayList<Pair> listToSend= new ArrayList<Pair>();

					 while (idx < nbOps) {
						// for (SvrDetails s : svrs)
						for (int i = 0 ; i < svrs.size()-1;i++)
						 {
							 if (svrs.get(i).getSvrStub()!=null)
							 {
								 int listSize;
								 if( (idx+svrs.get(i).getSvrCapacity())< nbOps)
								 {
									 listSize=svrs.get(i).getSvrCapacity();
								 }
								 else
								 {
									 listSize=nbOps+idx;
									 lastOp=true;
								 }

								 listToSend=Tools.splitL(listOp,idx, idx+ listSize-1);
								 idx+= listSize;
								 
								 ThreadCompute thCompute= new ThreadCompute(svrs.get(i).getSvrStub(),listToSend);
								 lstThreadsCompute.add(thCompute);
								 Thread th=new Thread(thCompute);
								 lstThreads.add(th);
								 
								 ThreadCompute thCompute1= new ThreadCompute(svrs.get(i+1).getSvrStub(),listToSend);
								 lstThreadsCompute.add(thCompute1);
								 Thread th1=new Thread(thCompute1);
								 lstThreads.add(th1);
								 th1.start();  
								 th.start();
								 
								 
								if (thCompute.getLstRst().equals(thCompute1.getLstRst())) {
									
									System.out.println( "same result  !");
									matchResult = true ;
								}
								 

								 if(lastOp)
									 break;
							 }
						 }
					 }

					 int nbElement=0;

					 int taskIdx=0;

					 for( Thread th: lstThreads)
					 {
						 try {
							 th.join();
							 ThreadCompute TC= lstThreadsCompute.get(taskIdx++);
							 synchronized (resultList) {
								 if(TC.lstRst !=null)
								 {
									 for( int i : TC.lstRst)
									 {
										 nbElement++;
										 resultList.add(i);
									 }
								 }
							 }


						 } catch (InterruptedException e) {
							 // TODO: handle exception
							 System.out.println("Error Thread "+ th.getId());
						 }
					 }
		        	
		        	
		        	
		        	
		        	
		        	//-----------------------------------------
		        	
		        	
		        
		        }

		 }
		 }
	 }


	 /**
		* Displays the stats of the operations
	  */
	 private void callStats() {
		//Displays quantity of time taken to execute the ops
		end_Time = System.nanoTime();
		float tpEx = (float)(end_Time - start_Time)/1000000;
		System.out.println("\nTp d'execution : "+ Float.toString(tpEx) + " ms");
		System.out.println("Tp d'execution : "+ Float.toString(tpEx/1000) + " s");

		// Displays the quantity of done ops.
		System.out.println("Qté d'ops = "+ Integer.toString(nbOps)+" opérations.\n");
	}


	/**
	  * Displays results with modulo 5000
	  * @param listRslts
	  */
	 public static void displayResults (ArrayList<Integer> listRslts)
	 {
		 String result = null;
		 int sln = 0;
		 boolean fst = false;

		 for (int oneResult : listRslts)
		 {
			 sln = (sln + (oneResult%5000)) % 5000;
			 if (fst)
			 {
				 result = "( " + result + " + " + Integer.toString(oneResult) + " ) % 5000 ";
			 }
			 else
			 {
				 result = Integer.toString(oneResult) + " % 5000 " ;
				 fst = true;
			 }
		 }
		 //result = result + " = " + Integer.toString(sln); // uncomment to display all operations.
		 result = "Result  = " + Integer.toString(sln); // comment to display all operations.

		 System.out.println(result);
	 }






/**
 * Opening a session from the server.
 * @param login_
 * @param pwd_
 * @param serverInterface
 * @return
 */
	 private boolean openSessionRepartiteur(String login_, String pwd_, ServerInterface oneStub) {
		 try {
			if (oneStub.openSession(login_, pwd_)) {
				 System.out.println(" Repartiteur connected to Server  !");
			 }
			 else {
				 System.out.println(" Not connected !");
				 System.exit(0);

			 }
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 return true;
	 }



	 /**
	  * Send a message error to the user
	  * @return
	  */
	 private static void sendErrorMessage() {
		 // TODO Auto-generated method stub
		 System.out.println(" Error command or Syntax ! \n");
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
