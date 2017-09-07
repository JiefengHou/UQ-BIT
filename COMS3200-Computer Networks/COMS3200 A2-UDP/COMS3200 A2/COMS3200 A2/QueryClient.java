import java.util.regex.*;
import java.io.*;
import java.net.*;


/*
 * This a set timeout function. if timeout,it will return false, else, return 
 * true. set timeout is 3 seconds
 */
class timeoutThread extends Thread {
	
	boolean receiveResponse = true;
		
	public boolean checkReceive(){
		return receiveResponse;
	}
	
	public void run(){
		try{
			//Set thread sleep 3 seconds
			Thread.sleep(3000);
			receiveResponse = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
				
}

public class QueryClient {
	
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
		// set buffers
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
	
	
	public static void main(String[] args) throws Exception {
	

		//The NameServer port number
		int NameServerPort = 0;
		//The request type is a single letter
		String RequestType = null;
		//The request keyword is a string or integer
		String RequestKeyword = null;
		
		
		//check command line argument is 3 or not, if not,
		//print standard error and exit
		if(args.length != 3){
			System.err.println("Invalid command line arguments\n");
			System.exit(1);
		}
		
		//check the first argument (NameServer port) is integer or not 
		Pattern pattern = Pattern.compile("[0-9]*");  
		//if argument is an integer, so check the range of this integer is 
		//from 1024 to 65535 or not
		if(pattern.matcher(args[0]).matches()){
			NameServerPort = Integer.parseInt(args[0]);
			if(NameServerPort < 1024 || NameServerPort > 65535)
			{
				System.err.println("Invalid command line arguments\n");
				System.exit(1);
			}
		} else {
			System.err.println("Invalid command line arguments\n");
			System.exit(1);
		}
		
		//Check the second argument (request type) is required or not
		if(args[1].equals("L") || args[1].equals("D") || 
				args[1].equals("C") || args[1].equals("K")){
			RequestType = args[1];
		} else {
			System.err.println("Invalid command line arguments\n");
			System.exit(1);
		}
		
		//The third argument is request keyword
		RequestKeyword = args[2];
			
				
		String reply=null; 
		Double x; 
		boolean receiveResponse = false; 
		int tries = 1;
		int maxtries = 5;

		// construct datagram socket
		DatagramSocket clientSocket = new DatagramSocket();
		
		/*
		 * send the packet to target server when random number is >=0.5, if not, 
		 * get a new random number until this random number >=0.5.
		 * 
		 * send a lookup msg to NameServer, and create in set timeout function in
		 * new thread. If timeout, the client will re-send packet to NameServer.
		 * 
		 * receive the port number and name of target server from NameServer
		 * 
		 * If receive packet is timeout from NameServer is more than 5 times,
		 * it means client cannot contact NameServer
		 */
		do{
			x=Math.random();	
			if(x>=0.5){
				//send data to NameServer
				sendData(clientSocket,RequestType+" ",NameServerPort);
				System.out.println("Send to NameServer successfully");
				
				//create a set timeout in new thread
				timeoutThread Timer = new timeoutThread();
				Timer.start();
				
				//receive data from NameServer
				reply = receiveDate(clientSocket);
				
				//check receive data from NameServer is timeout or not
				receiveResponse = Timer.checkReceive();
				if(receiveResponse){
					/*
					 * If required server cannot be found in NameServer,
					 * print standard error and exit
					 */
					String ServerInfor[] = reply.split(" ");
			        if(ServerInfor.length != 3) {		
			        	System.err.println(reply +"\n");
			        	System.exit(1);     	
			        } else {
			        	//print result
			        	System.out.println("Result from NameServer: "+reply
			        			+"\n");
			        }
				} else {
					//re-send packet if tries is less than 5 and receive timeout
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
					
			} else {
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
		} while(receiveResponse == false);
		
		// close up
		clientSocket.close();
		
		
			
		String ServerInfor[] = reply.split(" ");
		
    	// construct datagram socket
    	DatagramSocket ClientSocket = new DatagramSocket();
    	
    	//Get the CatalogServer/LoansServer port number from reply
    	int TargetServerPort = Integer.parseInt(ServerInfor[1]);
    	
    	tries = 1;
    	receiveResponse = false;
         	
		/*
		 * send the packet to target server when random number is >=0.5, if not, 
		 * get a new random number until this random number >=0.5.
		 * 
		 * send a lookup msg to target server, and create in set timeout 
		 * function in new thread. If timeout, the client will re-send 
		 * packet to target server.
		 * 
		 * receive the result from target server
		 * 
		 * If receive packet is timeout from TargetServer is more than 5 times,
		 * it means client cannot contact TargetServer 
		 */
    	do{
			x=Math.random();	
			if(x>=0.5){
	        	//send data to target server
				sendData(ClientSocket,RequestType+" "+RequestKeyword+" ",
	        			TargetServerPort);
				System.out.println("Send to " + ServerInfor[0] 
						+ " successfully");
				
				//create a set timeout in new thread
				timeoutThread Timer = new timeoutThread();
				Timer.start();
				
				//receive data from target server
				reply = receiveDate(ClientSocket);
				
				//check receive data from target server is timeout or not
				receiveResponse = Timer.checkReceive();
				if(receiveResponse){
					//print the result
					System.out.println("Result from "+ServerInfor[0]+":");
					System.out.println(reply);
		        	do {
		        		reply = receiveDate(ClientSocket);
		        		if(reply != null){
		        			System.out.println(reply);
		        		}        		  
		        	} while(reply != null);
				} else {
					//re-send packet if tries is less than 5 and receive timeout
					System.out.println("Receive from NameServer timeout, " 
							+ (maxtries - tries) + " more tries...");
					tries++;
					//print standard error and exit if cannot contact
					//TargetServer
					if(tries > maxtries) {
						System.err.println("Cannot contact"+ServerInfor[0]
								+"\n");
						System.exit(1);
					}						
					System.out.println("try to re-send...\n");
				}
			}
			else {
				//re-send packet if tries is less than 5 and loss packet
				System.out.println("Send to " + ServerInfor[0] + " failed: "
						+ "Loss packet," + (maxtries - tries)+" more tries...");
				tries++;	
				//print standard error and exit if cannot contact
				//NameServer
				if(tries > maxtries) {
					System.err.println("Cannot contact"+ ServerInfor[0] +"\n");
					System.exit(1);
				}
				System.out.println("try to re-send...\n");
				
			}
		} while(receiveResponse == false); 
    	
		// close up
		ClientSocket.close();
    	
	}
}
