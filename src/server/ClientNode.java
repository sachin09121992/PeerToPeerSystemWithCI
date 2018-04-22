package server;

public class ClientNode 
{
	private String hostname;
	private int port_number;
	
	public ClientNode(int port_number,String hostname)
	{
		this.hostname = hostname;
		this. port_number = port_number;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort_number() {
		return port_number;
	}

	@Override
	public String toString() {
		return "ClientNode [hostname=" + hostname + ", port_number="
				+ port_number + "]";
	}
	
	

}
