import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;
import java.util.*;


public class CatalogServer {

	//Read the catalog file
	@SuppressWarnings("resource")
	public static List<String> ReadFile() throws IOException {
		String line;
		List<String> CatalogList = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(
				new FileReader("catalog-file.txt"));
		while ((line = reader.readLine()) != null){
			String[] each = line.split("( \")");
			CatalogList.add(each[0]+ " \""+each[1]+" \""+each[2]);
		}
		return CatalogList;
	}
	
	/*
	 * send packet to target server
	 */
	public static void sendData(DatagramSocket Socket,String msg, int port) 
			throws Exception {   
		
		// set server's ip address
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");		
		// set buffers
		byte[] sendData = new byte[1024];			
		sendData = msg.getBytes();	
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
				IPAddress, port);
		
		try{			
			// send the message to server	
			Socket.send(sendPacket);	
			
		} catch(Exception e){  
            e.printStackTrace();  
		}
    } 
	
	
	/*
	 * receive packet to target server and return the string of received data
	 */
	public static String receiveDate(DatagramSocket Socket) throws Exception {   
		
		String reply=null;
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, 
				receiveData.length);
		
		try{
			// receive reply message from server
			Socket.receive(receivePacket);
			reply = new String(receivePacket.getData());
			
		} catch(Exception e){  
            e.printStackTrace();  
		}	
		return reply;
    } 
	
	
	public static void main(String[] args)  throws Exception {
		
		//The name server port number
		int NameServerPort = 0;
		//The catalog server port number
		int CatalogPort = 0;
		//The server name
		String ServerName = "CatalogServer";
		
		try{
			// construct datagram socket, and allocated port number by system
			DatagramSocket serverSocket = new DatagramSocket(0);
			//get the port random port number
			CatalogPort = serverSocket.getLocalPort(); 
			
			//check command line argument is 1 or not, 
			//if not,print standard error and exit
			if(args.length != 1){
				System.err.println("Invalid command line argument for "
						+ "CatalogServer\n");
				System.exit(1);
			}
	
			//check the range of catalog server port number is from 1024 to 
			//65535 or not
			if(CatalogPort  < 1024 || CatalogPort  > 65535){
				System.err.println("Invalid port number for CatalogServer\n");
				System.exit(1);
			}
			
			//check the argument is integer or not 
			Pattern pattern = Pattern.compile("[0-9]*");  		
			//if argument is an integer, so check the range of this 
			//integer is from 1024 to 65535 or not
			if(pattern.matcher(args[0]).matches()){
				NameServerPort  = Integer.parseInt(args[0]);
				if(NameServerPort  < 1024 || NameServerPort  > 65535){
					System.err.println(
							"Invalid command line argument for CatalogServer\n");
					System.exit(1);
				}
			} else {
				System.err.println(
						"Invalid command line argument for CatalogServer\n");
				System.exit(1);
			}
			
			//read the catalog-file
			List<String> CatalogList = ReadFile();
			
			
			String reply=null;
			Double x;
			boolean receiveResponse = false;
			int tries = 1;
			int maxtries = 5;
		
			// construct datagram socket
			DatagramSocket clientSocket = new DatagramSocket();
			
			/*
			 * send the packet to target server when random number is >=0.5, 
			 * if not, get a new random number until this random number >=0.5.
			 * 
			 * send a register msg to NameServer, and create in set timeout 
			 * function in new thread. If timeout, the client(catalog server) 
			 * will re-send packet to NameServer.
			 * 
			 * receive accept msg from NameServer
			 * 
			 * If receive packet is timeout from NameServer is more than 5 times,
			 * it means client(catalog server) cannot contact NameServer
			 */
			do{
				x=Math.random();	
				if(x>=0.5){	
					sendData(clientSocket,ServerName+" "+CatalogPort+" ",
							NameServerPort);
					System.out.println("Send to NameServer successfully");
					timeoutThread Timer = new timeoutThread();
					Timer.start();
					reply = receiveDate(clientSocket);
					receiveResponse = Timer.checkReceive();
					if(receiveResponse){
						//print result
						System.out.println("Result from NameServer: "+reply);
					} else {
						//re-send packet if tries is less than 5
						System.out.println("Receive from NameServer timeout, " 
								+ (maxtries - tries) + " more tries...");
						tries++;
						//print standard error and exit if cannot contact
						//NameServer
						if(tries > maxtries) {
							System.err.println("Cannot contact NameServer\n");
							System.exit(1);
						}
						System.out.println("try to re-send...\n");
					}
				}
				else {
					//re-send packet if tries is less than 5 and loss packet
					System.out.println("Send to NameServer failed: Loss packet, "
							+ (maxtries - tries) + " more tries...");
					tries++;	
					//print standard error and exit if cannot contact
					//NameServer
					if(tries > maxtries) {
						System.err.println("Cannot contact NameServer\n");
						System.exit(1);
					}
					System.out.println("try to re-send...\n");
				}
			} while(receiveResponse==false);
			
			// close up
			clientSocket.close();
		
			
			
			System.out.println("Catalog Server is activated.\n");
			
			// waiting for incoming messages
			while (true) {
				// set buffers
				byte[] receiveData = new byte[1024];
				byte[] sendData = new byte[1024];
				
				// receive message from client
				DatagramPacket receivePacket = new DatagramPacket(receiveData, 
						receiveData.length);
				serverSocket.receive(receivePacket);
				
				// print the message
				String msg = new String(receivePacket.getData());
				System.out.println("Message from Client: \n" + msg+"\n");
				
				// get the port of the client
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				
				String request[] = msg.split(" ");			
				
				//If the request type in msg is C or K
				if(request[0].equals("C") || request[0].equals("K")) {
				    int i = 0;
				    
					for (Iterator<String> iterator = CatalogList.iterator(); 
							iterator.hasNext();) {
						String record = iterator.next().toString();
						String[] item = record.split("( \")");
						String bookID = item[0];
						String title = item[1].replaceAll("\"", "");
						String author = item[2].replaceAll("\"", "");
						//a search by book-id returns the matching record
						if(request[0].equals("C")){
							if(request[1].equals(bookID)){
								sendData = record.getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										sendData,sendData.length,IPAddress,port);				
								serverSocket.send(sendPacket);
								break;
							}
							if(!iterator.hasNext()){
								sendData = ("no result is found!").getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										sendData,sendData.length,IPAddress,port);			
								serverSocket.send(sendPacket);
							}
						}
						//a keyword search returns all records with the keyword  
						//in the title or authors
						else {
							if(title.contains(request[1]) || 
									author.contains(request[1])){
								i++;
								sendData = record.getBytes();	
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										sendData,sendData.length,IPAddress,port);	
								serverSocket.send(sendPacket);	
							}
							
							if(!iterator.hasNext() && i == 0){
								sendData = ("no result is found!").getBytes();
								// send the message back to the client 
								DatagramPacket sendPacket = new DatagramPacket(
										sendData,sendData.length,IPAddress,port);					
								serverSocket.send(sendPacket);	
							}
						}
					}	
				} 
				
				//If CatalogServer receive invalid messages, the connection will 
				//close
				else {
					serverSocket.close();
				}

			}
		} catch(Exception e){  
			//print standard error and exit if port number has been used
			System.err.println("The port number has been used\n");
			System.exit(1);   
		}	

	}
}
