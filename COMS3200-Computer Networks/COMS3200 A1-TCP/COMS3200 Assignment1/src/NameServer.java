
import java.io.*;
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
		
		
		ServerSocket serverSocket = null;
		try {
			// listen on given name server port number
			serverSocket = new ServerSocket(PortNumber);	
		} catch (IOException e) {
			//if name server is unable to listen on given port number, 
			//it print standard error and exit
			System.err.println(
					"Cannot listen on given port number " + PortNumber +"\n");
            System.exit(1);
        }
		
		
		Socket connSocket = null;
		while(true)
		{
        	try {
        		System.out.println(
        				"Name Server waiting for incoming connections ...\n");
        		// block, waiting for a conn. request
        		connSocket = serverSocket.accept();
        		// At this point, we have a connection
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
        	
		    // Now have a socket to use for communication
		    // Create a PrintWriter and BufferedReader for interaction with our  
        	// stream "true" means we flush the stream on newline
		    PrintWriter out = new PrintWriter(connSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
		    		new InputStreamReader(connSocket.getInputStream()));
		
		    
		    String line;
		    //read a line from stream
		    line=in.readLine();	    	
		    //split the line to get request detail
		    String request[] = line.split(" ");
		    //System.out.println("Message from client: " + request[0] +"\n");
		    
		    //if the message is register message, store the sever detail 
		    //to ServerList
		    if(request[0].equals("CatalogServer") 
		    		|| request[0].equals("LoansServer")){

	    		if(ServerList[0][0] == null){
	    			ServerList[0][0] = request[0];
	    			ServerList[0][1] = request[1];
	    			ServerList[0][2] = 
	    					connSocket.getInetAddress().getHostName();
	    			out.println("accept");
	    		} else {
	    			if(!ServerList[0][0].equals(request[0]))
	    			{
		    			ServerList[1][0] = request[0];
		    			ServerList[1][1] = request[1];
		    			ServerList[1][2] = 
		    					connSocket.getInetAddress().getHostName();	
		    			out.println("accept");
	    			}    			
	    		}	
		    }
		    
		    /*if message is lookup message, name server will search server list, 
		     *if server cannot be found,
		     *name server will reply an error message */
		    if(request[0].equals("L") || request[0].equals("D")){
		    	if(ServerList[0][0] == null || 
		    			(!ServerList[0][0].equals("LoansServer") 
		    					&& ServerList[1][0] == null)){
		    		out.println(
		    				"Error: Process has not registered with the Name Server\n");
		    	} else {
		    		if(ServerList[0][0].equals("LoansServer")){
		    			out.println(ServerList[0][0] +" "+ ServerList[0][1]);
		    		} else out.println(ServerList[1][0] +" "+ ServerList[1][1]);
		    	}			    	
		    }
		    
		    if(request[0].equals("C") || request[0].equals("K")){
		    	if(ServerList[0][0] == null || 
		    			(!ServerList[0][0].equals("CatalogServer") 
		    					&& ServerList[1][0] == null)){
		    		out.println(
		    				"Error: Process has not registered with the Name Server\n");
		    	} else {
		    		if(ServerList[0][0].equals("CatalogServer"))
		    		{
		    			out.println(ServerList[0][0] +" "+ ServerList[0][1]);
		    		}
		    		else out.println(ServerList[1][0] +" "+ ServerList[1][1]);
		    	}	
		    }
		    //if the client sends a rubbish data , 
		    //name server will close connection
		    else
		    {
			    out.close();
			    in.close();
			    connSocket.close();		    	
		    }
		}
	}
}
