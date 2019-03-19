package ca.polymtl.inf8480.tp1.shared;

public class SvrDetails {
	private String srvIp ;
	private int svrPort ;
	private int svrCapacity ;
	private ServerInterface svrStub;
	/**
	 * @return the srvIp
	 */
	public String getSrvIp() {
		return srvIp;
	}
	/**
	 * @param srvIp the srvIp to set
	 */
	public void setSrvIp(String srvIp) {
		this.srvIp = srvIp;
	}
	/**
	 * @return the svrPort
	 */
	public int getSvrPort() {
		return svrPort;
	}
	/**
	 * @param svrPort the svrPort to set
	 */
	public void setSvrPort(int svrPort) {
		this.svrPort = svrPort;
	}
	/**
	 * @return the svrCapacity
	 */
	public int getSvrCapacity() {
		return svrCapacity;
	}
	/**
	 * @param svrCapacity the svrCapacity to set
	 */
	public  void setSvrCapacity(int svrCapacity) {
		this.svrCapacity = svrCapacity;
	}
	/**
	 * @return the svrStub
	 */
	public ServerInterface getSvrStub() {
		return svrStub;
	}
	/**
	 * @param svrStub the svrStub to set
	 */
	public void setSvrStub(ServerInterface svrStub) {
		this.svrStub = svrStub;
	}

}
