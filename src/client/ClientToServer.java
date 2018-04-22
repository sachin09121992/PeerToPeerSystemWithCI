package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientToServer implements Runnable {

	
	int portNumber;
	String folderName;
	String serverIP;
	String ipAddress;
	
	public ClientToServer(int portNumber,String folderName,String serverIP) 
	{
		this.portNumber=portNumber;
		this.folderName=folderName;
		this.serverIP=serverIP;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() 
	{
		Socket client=null;
		
		try 
		{
			client = new Socket(serverIP, 7734);
			ipAddress = client.getLocalAddress().getHostAddress();
			
			System.out.println("Welcome to Peer to Peer CI System!!");
			System.out.println("Please see the menu below and tell us how we can help you??");
			Scanner scanner=new Scanner(System.in);
			
			int choice=0;
			
			DataInputStream fromServer = new DataInputStream(client.getInputStream());
			DataOutputStream toServer = new DataOutputStream(client.getOutputStream());
			
			String request=null;
			
			File folder = new File(folderName);
			File[] listOfFiles = folder.listFiles();
			System.out.println("no of files:"+listOfFiles.length);
			
			for (int i = 0; i < listOfFiles.length; i++) 
			{
				System.out.println("Adding files initially");
				Integer rfcNo = Integer.parseInt(listOfFiles[i].getName().substring(3, (int) (listOfFiles[i].getName().length() - 4)));
				request = "ADD<sp>RFC<sp>" + rfcNo + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
						+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + listOfFiles[i].getName()
						+ "<cr><lf>";
				toServer.writeUTF(request);
				toServer.flush();
				fromServer.readUTF();
			}
			
			while(choice<=5)
			{
				System.out.println("1) Enter 1 to add an RFC to server");
				System.out.println("2) Enter 2 to lookup an RFC from server");
				System.out.println("3) Enter 3 to get the list of all the RFC's");
				System.out.println("4) Enter 4 Choose a peer to download an RFC from");
				System.out.println("5) Enter 5 to Quit the system");
				System.out.println("Enter your choice:");
				choice = scanner.nextInt();
				
				switch(choice)
				{
				
					case 1:System.out.println("Provide the RFC number which you want to add to server:");
						   int rfcNumber = scanner.nextInt();
						   request = "ADD<sp>RFC<sp>" + rfcNumber + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
									+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + "rfc" + rfcNumber + ".txt"
									+ "<cr><lf>";
						   toServer.writeUTF(request);
						   System.out.println(fromServer.readUTF());
						   break;
					
					case 2:System.out.println("Provide the RFC number which you want to lookup from server:");
					   	   int rfcNumberLookup = scanner.nextInt();
					       request = "LOOKUP<sp>RFC<sp>" + rfcNumberLookup + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
									+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + "rfc" + rfcNumberLookup + ".txt"
									+ "<cr><lf><cr><lf>";
					       toServer.writeUTF(request);
					       System.out.println(fromServer.readUTF());
					       break;
					   
					case 3:System.out.println("The list of all the available RFC's is as follows:");
						   request = "LIST<sp>ALL<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress + "<cr><lf>Port:<sp>" + portNumber
							         + "<cr><lf>";
					       toServer.writeUTF(request);
					       System.out.println(fromServer.readUTF());
					       break;
					       
					case 4:System.out.println("Please provide the RFC number that you want:");
					       int rfcNumberRequired = scanner.nextInt();
					       System.out.println("Please provide the hostname of peer from which you want this rfc:");
					       String hostname = scanner.next();
					       System.out.println("Please provide the port number of peer from which you want this rfc:");
					       int port = scanner.nextInt();
					       request = "GET<sp>RFC<sp>" + rfcNumberRequired + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + hostname + "<sp>Port:<sp>"
									+ port + "<cr><lf>OS:<sp>Mac<sp>OS<sp>10.4.1<cr><lf>";
					       Socket peer = new Socket(hostname,port);
					       DataOutputStream toPeer = new DataOutputStream(peer.getOutputStream());
					       DataInputStream fromPeer = new DataInputStream(peer.getInputStream());
					       toPeer.writeUTF(request);
					       String output = fromPeer.readUTF();
						   List<String> response = Arrays.asList(output.split("<cr><lf>"));
						   String data = response.get(response.size() - 1);
						   try 
						   {
								PrintWriter writer = new PrintWriter(folderName + "rfc" + rfcNumberRequired + ".txt", "UTF-8");
								writer.println(data);
								writer.close();
								request = "ADD<sp>RFC<sp>" + rfcNumberRequired + "<sp>P2P-CI/1.0<cr><lf>Host:<sp>" + ipAddress
										+ "<cr><lf>Port:<sp>" + portNumber + "<cr><lf>Title:<sp>" + "rfc" + rfcNumberRequired + ".txt"
										+ "<cr><lf>";
								toServer.writeUTF(request);
								System.out.println(fromServer.readUTF());
						   } 
						   catch (IOException e) 
						   {
								e.printStackTrace();
						   }
						   peer.close();
						   break;
					       
					case 5:client.close();
						   Thread.currentThread().stop();
						   break;
						
					default:System.out.println("The option that you have choosen does not exists!!");
					        System.out.println("Please select a valid choice:");
					        choice = scanner.nextInt();
					        break;
				}
				
			}
			
			scanner.close();
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}

}
