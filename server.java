import java.io.*;
import java.net.*;

public class server {
    public static void main( String[] args ) throws IOException {

        int portNumber = 8001;

        //it needs to be a socket that awaits and accepts connections
        ServerSocket ss = new ServerSocket( portNumber );

        //sockets just like the client
        System.out.println( "Waiting for connections..." );

        Socket s = ss.accept();
        if ( s.isConnected() ) System.out.println( "Connected: " + s.getInetAddress() + ":" + s.getPort() );

        BufferedReader in = new BufferedReader( new InputStreamReader( s.getInputStream() ) );

        String message = "";
        while ( true ) {
            message = in.readLine();
            message = message.toUpperCase();
            System.out.println( "recv: " + message );
            // Calculator goes here
            if (message.equals("HELLO WORLD"))
            {
                System.out.println("Hello World message received.");
            }
            else if ( message.equals( "CLOSECONNECTION" ) ) {
                s.close();
                ss.close();
                System.exit(0);
            }
        }
    }
}
