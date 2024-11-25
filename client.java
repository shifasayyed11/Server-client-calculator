package server_client_calculator;

import java.io.*;
import java.net.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client {
    public static void main(String[] args) throws IOException {
        int portNumber = 8001;

        String hostName = "127.0.0.1";

        System.setProperty("javax.net.ssl.trustStore", "./truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "1qaz2wsx");

        SSLSocketFactory factory = ( SSLSocketFactory ) SSLSocketFactory.getDefault();

        System.out.println("Connecting...");
        try (SSLSocket s = (SSLSocket) factory.createSocket( hostName, portNumber )) {
            if (s.isConnected()) {
                s.startHandshake(); // the SSL handshake
                System.out.println("Connected to " + s.getInetAddress() + ":" + s.getPort() +
                        " from " + s.getLocalAddress() + ":" + s.getLocalPort());
            }

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Optional handshake process
            System.out.print("Enter handshake phrase: ");
            String handshakePhrase = userInput.readLine();
            out.println(handshakePhrase);

            String handshakeResponse = in.readLine();
            if (!"OK".equals(handshakeResponse)) {
                System.out.println("Handshake failed. Exiting.");
                s.close();
                return;
            }
            System.out.println("Handshake successful!");

            // Main loop for calculator functionality
            while (true) {
                System.out.println("Choose an operation (ADD, SUBTRACT, MULTIPLY, DIVIDE) or type CLOSECONNECTION to exit:");
                String operation = userInput.readLine().toUpperCase();

                if ("CLOSECONNECTION".equals(operation)) {
                    out.println(operation);
                    System.out.println("Closing connection...");
                    break;
                }

                // Validate operation
                if (!operation.matches("ADD|SUBTRACT|MULTIPLY|DIVIDE")) {
                    System.out.println("Invalid operation. Please enter ADD, SUBTRACT, MULTIPLY, or DIVIDE.");
                    continue;
                }
                out.println(operation);

                // Prompt for the two numbers
                System.out.print("Enter the first number: ");
                String num1 = userInput.readLine();
                System.out.print("Enter the second number: ");
                String num2 = userInput.readLine();

                // Send numbers to server
                out.println(num1);
                out.println(num2);

                // Receive and display the result
                String result = in.readLine();
                System.out.println("Result: " + result);
            }
        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
