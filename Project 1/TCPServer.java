import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

// Simple server that sends a welcome message to a single TCP client.
public class TCPServer {
	private static final String SOURCE_FILE_NAME = "testt.txt";
	public static void main(String[] args) throws IOException {
		// Don't run the server if the source file doesn't exist
		if (!verifySourceExists(SOURCE_FILE_NAME))
			throw new FileNotFoundException("File '" + SOURCE_FILE_NAME + "' not found; server aborted.");
		
		ServerSocket welcomeSocket = new ServerSocket(12345);
		Socket helpClient = welcomeSocket.accept();
		sendFile(SOURCE_FILE_NAME, helpClient.getOutputStream());
		
		/*PrintWriter out = new PrintWriter(helpClient.getOutputStream());
		out.println("Hi, there. Welcome to CSE283");
		out.close();*/
		helpClient.close();
		welcomeSocket.close();
	}
	
	// Copy the file at path fileName into the output stream
	public static void sendFile(String fileName, OutputStream os) {
		Path filePath = FileSystems.getDefault().getPath(fileName);
		try {
			Files.copy(filePath, os);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean verifySourceExists(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}
}
