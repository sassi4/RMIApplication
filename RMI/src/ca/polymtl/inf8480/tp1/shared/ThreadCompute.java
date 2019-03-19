/**
 *
 */
package ca.polymtl.inf8480.tp1.shared;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author marcejthony
 *
 */
public class ThreadCompute extends Thread{


    public ServerInterface svrStub;
    public ArrayList<Integer> lstRst = new ArrayList<Integer>() ;


    public ArrayList<Integer> getLstRst() {
		return lstRst;
	}



	/**
	 * @param svrStub
	 * @param lstOps
	 */
	public ThreadCompute(ServerInterface svrStub, ArrayList<Pair> lstOps) {
		super();
		this.svrStub = svrStub;
		this.lstOps = lstOps;
	}



	public ArrayList<Pair> lstOps = new ArrayList<Pair>();
    public ArrayList<Pair> lstOpsToDo = new ArrayList<Pair>();

    public ThreadCompute() {
		// TODO Auto-generated constructor stub
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		try
		{
			 System.out.println("Thread started ...");


			do
			{
				lstRst = svrStub.CalculOperation(lstOps);

			}while (lstRst == null);

			if 	(lstRst== null)
			{

			}

		}
		catch (RemoteException e)
		{
			lstOpsToDo.addAll(lstOps);
			System.out.println("ThreadCompute Error : " + e.getMessage());
		}

	}

}
