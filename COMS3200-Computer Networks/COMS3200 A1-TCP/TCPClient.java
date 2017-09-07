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

import java.io.*;
import java.net.*;
public class TCPClient {
    
    public static void main(String[] args) throws IOException {
        
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        
        try {
            // Connect to the process listening on port 9000 on this host (localhost)
            clientSocket = new Socket("127.0.0.1", 9000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            // "true" means flush at end of line
            in = new BufferedReader(
		    new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create a buffered reader to read from standard input
        System.out.println("Please input your message:");
        BufferedReader stdin = new BufferedReader(
		new InputStreamReader(System.in));
        
        String userInput;
        
        while((userInput = stdin.readLine()) != null) {
            // Send the line to server
            out.println(userInput);
            // when Client exit
			if (userInput.equalsIgnoreCase("exit")) {
				System.out.println("Client exit !");
				break;
			}
            // Wait for a reply
            String reply = in.readLine();
            // Print out the reply to standard output
            System.out.println("Reply: " + reply);
        }
        
        // Close everything
        out.close();
        in.close();
        stdin.close();
        clientSocket.close();
    }
    
}
