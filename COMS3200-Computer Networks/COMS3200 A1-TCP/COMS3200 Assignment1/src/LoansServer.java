
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;
import java.util.*;


public class LoansServer {

	//Read the loans file
	@SuppressWarnings("resource")
	public static List<String> ReadFile() throws IOException {
		String line;
		List<String> LoansList  = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(
				new FileReader("loans-file.txt"));
		while ((line = reader.readLine()) != null) {
			String[] each = line.split(" ");
			LoansList.add(each[0]+" "+ each[1]+" "+each[2]);
		}
		return LoansList;
	}
	
	
	
	public static void main(String[] args)  throws IOException {
			
		//The NameServer port number
		int NameServerPort = 0;
		//The LoansServer port number;
		int LoansPort = 5200;
		//The server name
		String ServerName = "LoansServer";
		
		//check command line argument is 1 or not, if not
		//print standard error and exit
		if(args.length != 1){
			System.err.println("Invalid command line argument for LoanServer\n");
			System.exit(1);
		}

		//check the range of LoansServer port number is from 1024 to 65535 or not
		if(LoansPort  < 1024 || LoansPort  > 65535){
			System.err.println("Invalid port number for LoansServer\n");
			System.exit(1);
		}
		
		//check the argument is integer or not 
		Pattern pattern = Pattern.compile("[0-9]*");  
		//if argument is an integer, so check the range of this integer is 
		//from 1024 to 65535 or not
		if(pattern.matcher(args[0]).matches()){
			NameServerPort = Integer.parseInt(args[0]);
			if(NameServerPort < 1024 || NameServerPort > 65535){
				System.err.println(
						"Invalid command line argument for LoanServer\n");
				System.exit(1);
			}
		} else {
			System.err.println(
					"Invalid command line argument for LoanServer\n");
			System.exit(1);
		}
		
		//read the loans-file
		List<String> LoansList = ReadFile();
		
		
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;	
     
		try {
			// Connect to the process listening on NameServer 
			// port number on this host (localhost)
			clientSocket = new Socket("127.0.0.1", NameServerPort);
			// "true" means flush at end of line
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
            		new InputStreamReader(clientSocket.getInputStream()));
			
		} catch (Exception e) {
			//if LoansServer cannot contact NameServer, 
			//it will print standard error and exit
			System.err.println("Cannot connect to name server located at " 
					+ NameServerPort  +"\n");
            System.exit(1);
        }
		
		//send the register message to NameServer
    	out.println(ServerName+" "+LoansPort);
    	//receive a reply from NameServer
    	String reply = in.readLine();
    	if(reply.equals("accept")){
    		System.out.println("Sccessful register to NameServer\n");
    	}

    	//close the connection
        out.close();
        in.close();
        clientSocket.close(); 
        
		ServerSocket serverSocket = null;
		try {
			// listen on LoansServer port number
			serverSocket = new ServerSocket(LoansPort);
		} catch (IOException e) {
			//if LoansServer is unable to listen on its port number, 
			//it print standard error and exit
			System.err.println("LoansServer unable to listen on given port\n");
            System.exit(1);
        }
              
		Socket connSocket = null;
		while(true){
        	try {
        		System.out.println(
        				"LoansServer waiting for incoming connections\n");
        		// block, waiting for a conn. request
        		connSocket = serverSocket.accept();
        		// At this point, we have a connection
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
        	
		    // Now have a socket to use for communication
		    // Create a PrintWriter and BufferedReader for interaction with our  
        	// stream "true" means we flush the stream on newline
		    PrintWriter Serverout = new PrintWriter(
		    		connSocket.getOutputStream(), true);
		    BufferedReader Serverin = new BufferedReader(
		    		new InputStreamReader(connSocket.getInputStream()));
		    
		    String line;
		    //read a line from stream
		    line=Serverin.readLine();	 
		    //split the line to get request detail from QueryClient
		    String request[] = line.split(" ");
		    int i = 0;
		    
		    //search the loans file information
			for (Iterator<String> iterator = LoansList.iterator(); 
					iterator.hasNext();) {

				String record = iterator.next().toString();
				String[] item = record.split(" ");
				String userID = item[0];
				String bookID = item[1];
				String dueDate = item[2];
				//a request for the loans held by a specific user-id
				if(request[0].equals("L")){
					if(request[1].equals(userID)){
						i++;
						Serverout.println(record);
					}
					
					if(!iterator.hasNext() && i == 0){
						Serverout.println("no result is found!");
					}
				}				
				//a request for due-date of a specific book-id
				else
				{
					if(request[1].equals(bookID)){
						Serverout.println(record);	
						break;
					}
					
					if(!iterator.hasNext()){
						Serverout.println("no result is found!");
					}
				}
			}	    
		}
	}
}
