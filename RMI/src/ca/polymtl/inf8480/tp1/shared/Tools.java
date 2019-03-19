package ca.polymtl.inf8480.tp1.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

//import javax.xml.*;


@SuppressWarnings("unused")
public class Tools {



	public static ArrayList<Pair> readOps(String filePath)
	{
		String line;
		String operation;
		int operande;
		ArrayList<Pair> lstOps_ = new ArrayList<Pair> ();
		try (
			InputStream fis = new FileInputStream(filePath);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);)
		{
				while ((line = br.readLine()) != null)
				{
					 String[] words = line.split(" ");
					 operation = words[0];
					 operande = Integer.parseInt(words[1]);
					 // ajouter l'operande et l'operation a la liste des operations
					 lstOps_.add(new Pair (operation,operande));

				}
				return lstOps_;
		}
		catch(FileNotFoundException exception)
		{
			System.out.println("The file " + filePath + " was not found.");
			return null;
		}
		catch(IOException exception)
		{
			System.out.println(exception);
			return null;
		}
	}


	public static ArrayList<SvrDetails> readConfigs (String configFilePath)
	{
		ArrayList<SvrDetails> Servers = new ArrayList<SvrDetails> ();

		Scanner read = null;
		boolean comment = false;

		try
		{
			read = new Scanner (new File(configFilePath));

			String line;
			while (read.hasNextLine())
			{
				line = read.nextLine();

				if (!line.isEmpty())
				{
					// test if there is a comment
					if (!line.substring(0,2).matches("##"))
					{

						String[] strArray = line.split(";");

						// only 3 parameter (IP, port & capacity)
						if (strArray.length == 3)
						{
							SvrDetails SD = new SvrDetails();
							SD.setSrvIp(strArray[0]);
							SD.setSvrPort(Integer.parseInt( strArray[1]));
							SD.setSvrCapacity(Integer.parseInt(strArray[2] ));
							Servers.add(SD);

							System.out.println("Server IP =  " + SD.getSrvIp() + "\n" +
							"Port = " +  Integer.toString(SD.getSvrPort()) + "\n" +
						 "Capacity = " +  Integer.toString(SD.getSvrCapacity()) + "\n"
							 );
						}
					}
				}

			}
			read.close();
		}
		catch (IOException e)
		{
			System.err.println("Can not open the file : " + configFilePath);
		}
		finally
		{
			if (read != null)
			{
				read.close();
			}
		}

		return Servers;
	}

	public static ArrayList<Pair> splitL(ArrayList<Pair> listOps, int first, int last)
	{
		ArrayList<Pair> sousList= new ArrayList<Pair>();
		int i=0;
		for(Pair x :listOps)
		{
			if(i>=first)
			{
				sousList.add(x);
				if (i==last)
					break;
			}
			i++;
		}
		return sousList;
	}




}

