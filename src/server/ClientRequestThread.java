package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.RFCNode;
import server.ClientNode;
import server.MyServer;

public class ClientRequestThread extends Thread {
	
	Socket socket=null;
	
	public ClientRequestThread(Socket socket) 
	{
		this.socket = socket;
	}

	@Override
	public void run() 
	{
		
		int portNo = 0;
		String hostName = "";
		String title = "";
		
		try 
		{
			DataInputStream fromClient = new DataInputStream(socket.getInputStream());
			DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
			String request = fromClient.readUTF();
			while(!request.equals("END"))
			{
				String response="";
				int flag=0;
				String l[] = request.split("<cr><lf>");
				
				for(int i=0;i<l.length;i=i+4)
				{
					
					int statusCode=0;
					String statusPhrase="";
					String requestType[]=l[i].split("<sp>");
					
					if(requestType[0].equals("ADD"))
					{
						String version = requestType[3];
						int rfcNumber = Integer.parseInt(requestType[2]);
						
						for(int h=i+1;h<i+4;h++)
						{
							String header[] = l[h].split("<sp>");
							
							if (header[0].equals("Host:")) 
							{
								hostName = header[1];
							}
							if (header[0].equals("Port:")) 
							{
								portNo = Integer.parseInt(header[1]);
							}
							if (header[0].equals("Title:")) 
							{
								title = header[1];
							}
						}
						
						response+=version+"<sp>";
						
						if(!version.equals("P2P-CI/1.0"))
						{
							statusCode=505;
							statusPhrase="P2P-CI Version Not Supported";
						}
						
						if (statusCode != 505) 
						{
							if (getClient(hostName) != null) 
							{
								flag = 1;
							}
							if (flag == 0 && addClient(portNo, hostName)) 
							{
								flag = 1;
							}
							if (flag == 1 && addRFC(rfcNumber, portNo, hostName, title)) 
							{
								statusCode = 200;
								statusPhrase = "OK";
							} else if (flag == 0) 
							{
								statusCode = 400;
								statusPhrase = "Bad Request";
							}
						}
						
						response += statusCode + "<sp>" + statusPhrase + "<cr><lf><cr><lf>RFC<sp>" + rfcNumber + "<sp>"
								+ title + "<sp>" + hostName + "<sp>" + portNo + "<cr><lf><cr><lf>";
						
					}
					
					if(requestType[0].equals("LOOKUP"))
					{
						String version = requestType[3];
						int rfcNumber = Integer.parseInt(requestType[2]);
						
						for(int h=i+1;h<i+4;h++)
						{
							String header[] = l[h].split("<sp>");
							
							if (header[0].equals("Host:")) 
							{
								hostName = header[1];
							}
							if (header[0].equals("Port:")) 
							{
								portNo = Integer.parseInt(header[1]);
							}
							if (header[0].equals("Title:")) 
							{
								title = header[1];
							}
						}
						
						response+=version+"<sp>";
						
						if(!version.equals("P2P-CI/1.0"))
						{
							statusCode=505;
							statusPhrase="P2P-CI Version Not Supported";
						}
						
						List<ClientNode> clients = null;
						if (statusCode != 505) 
						{
							clients = getClientsWithRFC(rfcNumber);
							if (clients.isEmpty()) 
							{
								statusCode = 404;
								statusPhrase = "Not Found";
							} else 
							{
								statusCode = 200;
								statusPhrase = "OK";
							}

						}
						
						response+=statusCode+"<sp>"+statusPhrase+"<cr><lf><cr><lf>";
						
						if(statusCode==200)
						{
							for(ClientNode client:clients)
							{
								response+="RFC "+rfcNumber+"<sp>"+title+"<sp>"+client.getHostname()+"<sp>"+client.getPort_number()+"<cr><lf>";
							}
							response+="<cr><lf>";
						}
						
					}
					
					if(requestType[0].equals("LIST"))
					{
						String version=requestType[2];
						
						for(int h=i+1;h<i+3;h++)
						{
							String header[] = l[h].split("<sp>");
							
							if (header[0].equals("Host:")) 
							{
								hostName = header[1];
							}
							if (header[0].equals("Port:")) 
							{
								portNo = Integer.parseInt(header[1]);
							}
						}
						
						response+=version+"<sp>";
						
						if(!version.equals("P2P-CI/1.0"))
						{
							statusCode=505;
							statusPhrase="P2P-CI Version Not Supported";
						}
						
						if(statusCode!=505)
						{
							String result = getRFCList();
							if(result.length()==16)
							{
								statusCode=404;
								statusPhrase="Not Found";
							}
							else
							{
								statusCode = 200;
								statusPhrase = "OK";
							}
							
							response += statusCode + "<sp>" + statusPhrase + result;
						}
						
					}
					
				}
				
				toClient.writeUTF(response);
				request = fromClient.readUTF();
				
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.stop();
		}
		finally
		{
			Iterator<ClientNode> clientIterator = MyServer.clients.iterator();
			while (clientIterator.hasNext()) 
			{
				ClientNode client = clientIterator.next();
				if (client.getPort_number() == portNo) 
				{
					clientIterator.remove();
				}
			}
			
			Iterator<RFCNode> RFCIterator = MyServer.rfc_list.iterator();
			ClientNode client = new ClientNode(portNo, hostName);
			while (RFCIterator.hasNext()) 
			{
				RFCNode rfc = RFCIterator.next();
				if (rfc.clientDetails.equals(client)) 
				{
					RFCIterator.remove();
				}
			}
		}
	}

	private String getRFCList() 
	{
		String result = "<cr><lf>";
		for (RFCNode rfc : MyServer.rfc_list) 
		{
			if (rfc.clientDetails == null) {
				MyServer.rfc_list.remove(rfc);
				continue;
			}
			result += "RFC<sp>" + rfc.getRfc_number() + "<sp>" + rfc.title + "<sp>" + rfc.clientDetails.getHostname() + "<sp>"
					+ rfc.clientDetails.getPort_number() + "<cr><lf>";
		}
		result += "<cr><lf>";
		return result;
	}

	private List<ClientNode> getClientsWithRFC(int rfcNumber) 
	{
		List<ClientNode> clients = new ArrayList<ClientNode>();
		
		for(RFCNode rfc:MyServer.rfc_list)
		{
			if(rfc.getRfc_number()==rfcNumber)
			{
				clients.add(rfc.clientDetails);
			}
		}
		
		return clients;
		
	}

	public boolean addRFC(int rfcNumber, int portNo, String hostName,String title) 
	{
		RFCNode rfc = new RFCNode(rfcNumber, title,new ClientNode(portNo, hostName));
		
		if(MyServer.rfc_list.contains(rfc))
		{
			return true;
		}
		
		return MyServer.rfc_list.add(rfc);
	}

	public boolean addClient(int portNo, String hostName) 
	{
		ClientNode client = new ClientNode(portNo, hostName);
		if (MyServer.clients.contains(client)) {
			return true;
		}
		
		return MyServer.clients.add(client);
	}

	public ClientNode getClient(String hostName) {
		
		for(ClientNode client:MyServer.clients)
		{
			if(client.getHostname().equals(hostName))
			{
				return client;
			}
		}
		return null;
	}

}
