import java.io.*;
import java.net.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;


public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 8001;

        //tell the execution environment where the keystore (and the certificate) is
        System.setProperty( "javax.net.ssl.keyStore", "./keystore.jks" );
        System.setProperty( "javax.net.ssl.keyStorePassword", "1qaz2wsx" );

        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try (SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket( portNumber )) {
            serverSocket.setEnabledProtocols( new String[]{"TLSv1.3", "TLSv1.2"} );
            System.out.println("Server is running on port " + portNumber + "...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    // Handshake verification
                    String handshake = in.readLine();
                    if ("HELLO".equals(handshake)) {  // assuming "HELLO" as handshake phrase
                        out.println("OK");
                    } else {
                        out.println("Handshake failed");
                        continue;
                    }

                    // Main loop for handling operations
                    while (true) {
                        String operation = in.readLine();
                        if ("CLOSECONNECTION".equals(operation)) {
                            System.out.println("Client disconnected.");
                            break;
                        }

                        // Check for valid operation
                        if (!operation.matches("ADD|SUBTRACT|MULTIPLY|DIVIDE")) {
                            out.println("Invalid operation. Please enter ADD, SUBTRACT, MULTIPLY, or DIVIDE.");
                            continue;
                        }

                        // Receive numbers
                        String num1Str = in.readLine();
                        String num2Str = in.readLine();

                        // Parse numbers and handle errors
                        double num1, num2;
                        try {
                            num1 = Double.parseDouble(num1Str);
                            num2 = Double.parseDouble(num2Str);
                        } catch (NumberFormatException e) {
                            out.println("Invalid numbers. Please enter numeric values.");
                            continue;
                        }

                        // Perform calculation
                        double result = 0;
                        switch (operation) {
                            case "ADD":
                                result = num1 + num2;
                                break;
                            case "SUBTRACT":
                                result = num1 - num2;
                                break;
                            case "MULTIPLY":
                                result = num1 * num2;
                                break;
                            case "DIVIDE":
                                if (num2 != 0) {
                                    result = num1 / num2;
                                } else {
                                    out.println("Error: Division by zero is undefined.");
                                    continue;
                                }
                                break;
                        }

                        // Send result to client
                        out.println("Result: " + result);
                    }
                } catch (IOException e) {
                    System.out.println("Connection error: " + e.getMessage());
                }
            }
        }
    }
}
