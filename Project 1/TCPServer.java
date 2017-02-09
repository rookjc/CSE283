import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// Simple server that sends a welcome message to a single TCP client.
public class TCPServer {
	
	public static void main(String[] args) throws IOException {
		ServerSocket welcomeSocket = new ServerSocket(12345);
		Socket helpClient = welcomeSocket.accept();
		PrintWriter out = new PrintWriter(helpClient.getOutputStream());
		out.println("Hi, there. Welcome to CSE283");
		out.close();
		helpClient.close();
		welcomeSocket.close();
	}

}
