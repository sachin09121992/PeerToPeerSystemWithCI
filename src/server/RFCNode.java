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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientDetails == null) ? 0 : clientDetails.hashCode());
		result = prime * result + rfc_number;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		RFCNode other = (RFCNode) obj;
		if (clientDetails == null) {
			if (other.clientDetails != null)
				return false;
		} else if (!clientDetails.equals(other.clientDetails))
			return false;
		if (rfc_number != other.rfc_number)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "RFCNode [rfc_number=" + rfc_number + ", title=" + title
				+ ", clientDetails=" + clientDetails + "]";
	}
	
}
