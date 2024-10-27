import java.io.*;
import java.net.*;

public class client {
	public static void main(String[]args) throws IOException {
		int portNumber = 8001;
		String hostName = ("127.0.0.1");
		
		System.out.println("Connecting...");
		Socket s = new Socket( hostName, portNumber );
		if ( s.isConnected() ) System.out.println( "Connected to " + s.getInetAddress() + ":" + s.getPort() + " from " + s.getLocalAddress() + ":" + s.getLocalPort() );
		
		PrintWriter out = new PrintWriter( s.getOutputStream(), true );
        BufferedReader in = new BufferedReader( new InputStreamReader( s.getInputStream() ) );

        //reads keyboard
        BufferedReader userInput = new BufferedReader( new InputStreamReader(System.in) );

        String message = "";
        while ( true ) {
        	System.out.print( "send: " );
        	message = userInput.readLine();	//reads keyboard until user hits a newline
            out.println( message );
            
            if ( message.equals( "CLOSECONNECTION" ) ) {
            	s.close();
            	System.exit(0);
            }
        }
		
	}

}


