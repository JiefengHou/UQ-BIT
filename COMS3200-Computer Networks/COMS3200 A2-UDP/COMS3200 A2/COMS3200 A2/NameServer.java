
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class NameServer {
	
	public static void main(String[] args)  throws IOException {
		
		//This is name server port number
		int PortNumber = 0;
		//This is list of list to store server detail
		String[][] ServerList = new String[2][3];
		
		//check command line argument is 1 or not, 
		//if not,print standard error and exit
		if (args.length != 1){
			System.err.println("Invalid command line argument for Name Server\n");
			System.exit(1);
		}
		
		//check the argument is integer or not 
		Pattern pattern = Pattern.compile("[0-9]*");   
		//if argument is an integer, so check the range of this 
		//integer is from 1024 to 65535 or not
		if (pattern.matcher(args[0]).matches()){
			PortNumber = Integer.parseInt(args[0]);
			if (PortNumber < 1024 || PortNumber > 65535){
				System.err.println(
						"Invalid command line argument for Name Server\n");
				System.exit(1);
			}
		} else {
			System.err.println("Invalid command line argument for Name Server\n");
			System.exit(1);
			}
		
		try{
			// construct datagram socket
			DatagramSocket serverSocket = new DatagramSocket(PortNumber);
			System.out.println("Name Server is activated.\n");
			// waiting for incoming messages
			while (true) {	
				// set buffers
				byte[] receiveData = new byte[1024];
				byte[] sendData = new byte[1024];
				
				// receive message from client
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				serverSocket.receive(receivePacket);
				
				//get string of msg from client
				String msg = new String(receivePacket.getData());
				//get client ip
				InetAddress IPAddress = receivePacket.getAddress();
				//get client port number
				int port = receivePacket.getPort();
				
				String request[] = msg.split(" ");
							
			    //if the message is register message, store the sever detail 
			    //to ServerList
				if(request[0].equals("CatalogServer") 
			    		|| request[0].equals("LoansServer")){
			    	
					System.out.println("Message from "+request[0]+" Server:\n" 
							+ msg+"\n");
					
			    	if(ServerList[0][0] == null){
		    			ServerList[0][0] = request[0];
		    			ServerList[0][1] = request[1];
		    			ServerList[0][2] = 
		    					IPAddress.getHostAddress();
		    			sendData = "accept".getBytes();
		    		} else {
		    			if(ServerList[0][0].equals(request[0]))
		    			{
			    			ServerList[0][0] = request[0];
			    			ServerList[0][1] = request[1];
			    			ServerList[0][2] = 
			    					IPAddress.getHostAddress();	
			    			sendData = "accept".getBytes();
		    			} else {
			    			ServerList[1][0] = request[0];
			    			ServerList[1][1] = request[1];
			    			ServerList[1][2] = 
			    					IPAddress.getHostAddress();	
			    			sendData = "accept".getBytes();
		    			}    			
		    		}
					// send the message back to the client 
					DatagramPacket sendPacket = new DatagramPacket(sendData, 
							sendData.length,IPAddress, port);
					serverSocket.send(sendPacket);
			    }
			    
			    /*if message is lookup message, name server will search server list, 
			     *if server cannot be found, name server will reply an error 
			     *message.
			     *
			     *Search loans server information in server list
			     */
				else if(request[0].equals("L") || request[0].equals("D")){
			    	
					System.out.println("Message from Client:\n" + msg+"\n");
			    	if(ServerList[0][0] == null || 
			    			(!ServerList[0][0].equals("LoansServer") 
			    					&& ServerList[1][0] == null)){
			    		sendData = 
			    				("Error: Process has not registered with the "
			    						+ "Name Server\n").getBytes();
			    	} else {
			    		
			    		if(ServerList[0][0].equals("LoansServer")){
			    			sendData = (ServerList[0][0] +" "+ ServerList[0][1] 
			    					+" ").getBytes();
			    			
			    		} else sendData = (ServerList[1][0]+" "+ServerList[1][1] 
			    				+" ").getBytes();
			    	}	
					// send the message back to the client 
					DatagramPacket sendPacket = new DatagramPacket(sendData, 
							sendData.length,IPAddress, port);
					serverSocket.send(sendPacket);
			    } 
				//Search catalog server information in server list
				else if(request[0].equals("C") || request[0].equals("K")){
			    	
					System.out.println("Message from Client:\n" + msg+"\n");
					if(ServerList[0][0] == null || 
			    			(!ServerList[0][0].equals("CatalogServer") 
			    					&& ServerList[1][0] == null)){
			    		sendData = 
			    				("Error: Process has not registered with the "
			    				+ "Name Server\n").getBytes();
			    	} else {
			    		if(ServerList[0][0].equals("CatalogServer"))
			    		{
			    			sendData = (ServerList[0][0] +" "+ ServerList[0][1] 
			    					+" ").getBytes();
			    		}
			    		else sendData = (ServerList[1][0] +" "+ServerList[1][1] 
			    				+" ").getBytes();
			    	}	
					
					// send the message back to the client 
					DatagramPacket sendPacket = new DatagramPacket(sendData, 
							sendData.length,IPAddress, port);
					serverSocket.send(sendPacket);	
			    } 
			    
				//If NameServer receive invalid messages, the connection will 
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
