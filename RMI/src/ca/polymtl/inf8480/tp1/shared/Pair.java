/**
 * 
 */
package ca.polymtl.inf8480.tp1.shared;

import java.io.Serializable;

/**
 * @author marcejthony
 *
 */
public class Pair implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -833560037685666767L;
	private String operation_;
	private int operande_;
	 String operation;
	 int operande ;

	 
	public Pair(String operation, int operande)
	{
		operation_ = operation;
		operande_ = operande;
	}
	
	// fonctions get, set
	public void setOperation (String operation)
	{
		operation_ = operation;
	}
	
	public void setOperande (int operande)
	{
		operande_ = operande;
	}
	
	public String getOperation()
	{
		return operation_;
	}
	
	public int getOperande()
	{
		return operande_;
	}
		
	public void print()
	{
		System.out.println( operation_ + " " + Integer.toString(operande_));
	}

}
