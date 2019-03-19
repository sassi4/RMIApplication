/**
 * 
 */
package ca.polymtl.inf8480.tp1.shared;

import java.io.Serializable;

/**
 * @author marcejthony
 *
 */
public class ServerObject implements Serializable  {
	String serverIp ;
	int portServer ;
	 private static final long serialVersionUID = 1L;
	int capServer;
	
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public int getPortServer() {
		return portServer;
	}
	public void setPortServer(int portServer) {
		this.portServer = portServer;
	}
	public int getCapServer() {
		return capServer;
	}
	public void setCapServer(int capServer) {
		this.capServer = capServer;
	}

}
