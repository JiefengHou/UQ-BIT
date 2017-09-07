import java.io.*;
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
	
	
	public static void main(String[] args)  throws IOException {
		
		//The name server port number
		int NameServerPort = 0;
		//The catalog server port number
		int CatalogPort = 4200;
		//The server name
		String ServerName = "CatalogServer";
		
		//check command line argument is 1 or not, 
		//if not,print standard error and exit
		if(args.length != 1){
			System.err.println("Invalid command line argument for CatalogServer\n");
			System.exit(1);
		}

		//check the range of catalog server port number is from 1024 to 65535 or not
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
			//if CatalogServer cannot contact NameServer, 
			//it will print standard error and exit
			System.err.println("Cannot connect to name server located at " 
					+ NameServerPort  +"\n");
            System.exit(1);
        }
		
		//send the register message to NameServer
    	out.println(ServerName+" "+CatalogPort);
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
		try{
			// listen on CatalogServer port number
			serverSocket = new ServerSocket(CatalogPort);
		} catch (IOException e) {
			//if CatalogServer is unable to listen on its port number, 
			//it print standard error and exit
			System.err.println("CatalogServer unable to listen on given port\n");
            System.exit(1);
        }
        
		
		Socket connSocket = null;
		while(true)
		{
        	try {
        		System.out.println(
        				"CatalogServer waiting for incoming connections\n");
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
		    
		    //search the catalog file information
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
						Serverout.println(record);
						break;
					}
					if(!iterator.hasNext()){
						Serverout.println("no result is found!");
					}
				}
				//a keyword search returns all records with the keyword in the 
				//title or authors
				else {
					if(title.contains(request[1]) || author.contains(request[1])){
						i++;
						Serverout.println(record);	
					}
					
					if(!iterator.hasNext() && i == 0){
						Serverout.println("no result is found!");
					}					
				}
			}	    
		}
	}
}
