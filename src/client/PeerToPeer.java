package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class PeerToPeer implements Runnable 
{

	ServerSocket serverSocket;
	String folderName;
	
	public PeerToPeer(ServerSocket serverSocket, String folderName) 
	{
		this.serverSocket=serverSocket;
		this.folderName=folderName;
	}
	
	@Override
	public void run() 
	{
		Socket clientSocket = null;
		while (true) 
		{
			try 
			{
				clientSocket = serverSocket.accept();
				Thread P2PClientThread = new Thread(new PeerToPeerClientThread(clientSocket, folderName));
			    P2PClientThread.start();	
			} 
			catch (IOException e) 
			{
				System.out.println(e);
			}
		}
	}

}
