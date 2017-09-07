import java.util.regex.*;
import java.io.*;
import java.net.*;

public class QueryClient {
	
	public static void main(String[] args) throws IOException {
	
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
		
		
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;	

		try {
			// Connect to the process listening on NameServer port 
			//number on this host (localhost)
			clientSocket = new Socket("127.0.0.1", NameServerPort);
			// "true" means flush at end of line
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
            		new InputStreamReader(clientSocket.getInputStream()));

			
		} catch (Exception e) {
			System.err.println("Cannot connect to name server located at " 
					+ NameServerPort +"\n");
            System.exit(1);
        }
		
		//Send a lookup message to NameServer 
    	out.println(RequestType);
    	//Receive a reply from NameServer
    	String reply = in.readLine();
    	//Get the required server information from NameServer
        String ServerInfor[] = reply.split(" ");
        
        //If required server cannot be found in NameServer, 
        //print standard error and exit
        if(ServerInfor.length != 2) {		
        	System.err.println(reply +"\n");
        	System.exit(1);        	
        }
        //If required server can be found in NameServer, 
        //try to connect CatalogServer/LoansServer
        else {
            //Get the CatalogServer/LoansServer port number from reply
        	int TargetServerPort = Integer.parseInt(ServerInfor[1]);
            Socket ClientSocket = null;
            PrintWriter ClientOut = null;
            BufferedReader ClientIn = null;
            
    		try {
    			// Connect to the process listening on TargetServer port 
    			// number on this host (localhost)
    			ClientSocket = new Socket("127.0.0.1", TargetServerPort);
    			// "true" means flush at end of line
    			ClientOut = new PrintWriter(ClientSocket.getOutputStream(), true);
    			ClientIn = new BufferedReader(
    					new InputStreamReader(ClientSocket.getInputStream()));
    			
    		} catch (Exception e) {
    			//if cannot connect LoansServer, print standard error and exit
    			if(RequestType.equals("L") || RequestType.equals("D")) {
    				System.err.println(
    						"QueryClient unable to connect to LoansServer\n");
    				System.exit(1); 
    			}
    			//if cannot connect CatalogServer, print standard error and exit
    			else
    			{
        			System.err.println(
        					"QueryClient unable to connect to CatalogServer\n");
                    System.exit(1);   				
    			}
            }      
            
    		//Send request type and keyword to TargetServer to search information
    		ClientOut.println(RequestType+" "+RequestKeyword);
    		
    		//Receive result from TargetServer
    		String response;
    		while((response = ClientIn.readLine()) != null) {
    			System.out.println(response);
    		} 
        }
	}
}
