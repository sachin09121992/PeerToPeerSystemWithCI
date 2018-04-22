package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;


public class MyClient 
{
	public static final String client_location ="E:\\clientFolders\\";

	public static void main(String[] args) throws UnknownHostException 
	{
		
		Random random = new Random();
		int port = random.nextInt((49150 - 1025) + 1) + 1025;
		ServerSocket serverSocket = null;
		System.out.println("Assign a folder number to this client: ");
		Scanner scanner = new Scanner(System.in);
		Integer foldernumber = new Integer(scanner.nextInt());
		String folderName = client_location + foldernumber.toString() + "\\";	
		System.out.println("What is server's IP address?");
		String serverIP = scanner.next();
		try 
		{
			serverSocket = new ServerSocket(port);
		} 
		catch (IOException e) 
		{
			System.out.println(e);
		}
		
		Thread clientThread = new Thread(new ClientToServer(port, folderName, serverIP));
		clientThread.start();
		Thread peerThread = new Thread(new PeerToPeer(serverSocket, folderName));
		peerThread.run();
	}
}
