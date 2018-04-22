package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class MyServer {

	public static volatile List<ClientNode> clients = new LinkedList<ClientNode>();
	public static volatile List<RFCNode> rfc_list = new LinkedList<RFCNode>();
	
	public static void main(String[] args) 
	{
		ServerSocket serverSocket = null;
		try 
		{
			serverSocket = new ServerSocket(7734);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		Socket clientSocket = null;
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				ClientRequestThread clientRequestThread = new ClientRequestThread(clientSocket);
				clientRequestThread.start();

			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	
}
