package ca.polymtl.inf8480.tp1.shared;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface ServerInterface extends Remote {
	ArrayList<Integer> CalculOperation( ArrayList<Pair> listeOperations) throws RemoteException;

	 boolean openSession(String login, String pwd) throws RemoteException;


}
