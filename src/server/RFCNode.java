package server;

public class RFCNode {

	int rfc_number;
	String title;
	ClientNode clientDetails;
	
	public RFCNode(int rfc_number, String title, ClientNode clientDetail) 
	{
		this.rfc_number = rfc_number;
		this.title = title;
		this.clientDetails = clientDetail;
	}

	public int getRfc_number() {
		return rfc_number;
	}

	public String getTitle() {
		return title;
	}

	public ClientNode getHostname() {
		return clientDetails;
	}

	@Override
	public String toString() {
		return "RFCNode [rfc_number=" + rfc_number + ", title=" + title
				+ ", clientDetails=" + clientDetails + "]";
	}
	
}
