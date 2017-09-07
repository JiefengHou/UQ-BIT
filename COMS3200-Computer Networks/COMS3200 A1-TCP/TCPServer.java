/**
 * 
 * @author Mingyang Zhong
 * Feb. 2014
 * the University of Queensland
 * Code example for course: COMS3200
 * 
 This is simple network program based on Java-IO, TCP blocking mode and single thread. 
 The TCPClient reads inputs from the keyboard then sends it to TCPServer.
 The TCPServer reads packets from the socket channel and convert it to upper case, and then sends back to TCPClient. 
 The program assumes that the data in any received packets will be in string form.
 Typing 'exit' will close the program.
 * 
 */

import java.net.*;
import java.io.*;

public class TCPServer {
	
    @SuppressWarnings("resource")
	public static void main(String[] args)  throws IOException {
        ServerSocket serverSocket = null;
        try {
            // listen on port 9000
            serverSocket = new ServerSocket(9000);
			System.out.println("<TCPServer> Server is activated, listening on port: 9000");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket connSocket = null;
        while(true) {
        	try {
				// block, waiting for a conn. request
				connSocket = serverSocket.accept();
				// At this point, we have a connection
				System.out.println("Connection accepted from: " + connSocket.getInetAddress().getHostName());
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    // Now have a socket to use for communication
		    // Create a PrintWriter and BufferedReader for interaction with our stream "true" means we flush the stream on newline
		    PrintWriter out = new PrintWriter(connSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
		    String line;
		    // Read a line from the stream - until the stream closes
		    while ((line=in.readLine()) != null) {
		    	System.out.println("Message from client: " + line);
				// Client can close the connection by sending "exit"
				if(line.equalsIgnoreCase("exit")) {
				    break;
				}
				// Perform the job of the server - convert string to uppercase and return it
				line = line.toUpperCase();
				out.println(line);
		    }
		    
		    System.out.println("Client " + connSocket.getInetAddress().getHostName() +" finish up");
		    out.close();
		    in.close();
		    connSocket.close();
        }
        // serverSocket.close();
    }
    
}
