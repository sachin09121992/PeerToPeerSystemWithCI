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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + port_number;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientNode other = (ClientNode) obj;
		if (hostname == null) {
			if (other.hostname != null)
				return false;
		} else if (!hostname.equals(other.hostname))
			return false;
		if (port_number != other.port_number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientNode [hostname=" + hostname + ", port_number="
				+ port_number + "]";
	}
	
	

}
