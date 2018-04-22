package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PeerToPeerClientThread implements Runnable 
{
	
	Socket socket=null;
	String folderName=null;
	
	public PeerToPeerClientThread(Socket socket, String folderName) 
	{
		super();
		this.socket = socket;
		this.folderName = folderName;
	}



	@Override
	public void run() 
	{
		int portNo = 0;
		String hostName = "";
		String os = "Mac OS 10.2.1";
		try 
		{
			DataInputStream fromClient = new DataInputStream(socket.getInputStream());
			DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
			String request = fromClient.readUTF();
			System.out.println(request);
			String response = "";
			String[] s = request.split("<cr><lf>");

			int statusCode = 0;
			String statusPhrase = "";
			String data = "";
			boolean fileFound = false;
			
			long lastModified=0L;
			double contentLength=0.0; 
			
			String[] type = s[0].split("<sp>");
			
			if (type[0].equalsIgnoreCase("GET")) 
			{
				String version = type[3];
				int rfcNumber = Integer.parseInt(type[2]);

				response += version + "<sp>";

				if (!version.equals("P2P-CI/1.0")) 
				{
					statusCode = 505;
					statusPhrase = "P2P-CI Version Not Supported";
				}
				
				if (statusCode != 505) 
				{
					File folder = new File(this.folderName);
					File[] listOfFiles = folder.listFiles();
					for (int i = 0; i < listOfFiles.length; i++)
					{
						if (listOfFiles[i].getName().equals("rfc" + rfcNumber + ".txt")) 
						{
							fileFound = true;					
							FileReader f = new FileReader(listOfFiles[i].getPath());
							BufferedReader buff = new BufferedReader(f);
							String line = "";
							while ((line = buff.readLine()) != null) 
							{
								data += line + "\n";
							}
							lastModified = listOfFiles[i].lastModified();
							contentLength = listOfFiles[i].length();
							break;
						}

					}
					
					if(fileFound)
					{
						statusCode = 200;
						statusPhrase = "OK";
					}
					else
					{
						statusCode = 404;
						statusPhrase = "Not Found";
					}
					
				}

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				
				response += statusCode + "<sp>" + statusPhrase + "<cr><lf><cr><lf>Date:<sp>" + dateFormat.format(date) + "<cr><lf>OS:<sp>"
						+ os + "<cr><lf><cr><lf>Last-Modified:<sp>" + lastModified + "<cr><lf><cr><lf>Content-Length:<sp>"
						+ contentLength + "<cr><lf><cr><lf>Content-Type:<sp>text/plain<cr><lf><cr><lf>" + data;
			
			}

			toClient.writeUTF(response);
			socket.close();

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
