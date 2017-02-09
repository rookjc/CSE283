import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

// Simple client that connects to a TCP server and prints out everything received.
public class TCPClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket("localhost", 12345);
		Scanner input = new Scanner(client.getInputStream());
		while (input.hasNextLine())
			System.out.println(input.nextLine());
		input.close();
		client.close();
	}

}
