import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

// Simple server that sends a welcome message to a single TCP client.
public class TCPServer {
	private static final String SOURCE_FILE_NAME = "test.txt";
	private static final int PORT = 12345;
	
	public static void main(String[] args) throws IOException {
		// Don't run the server if the source file doesn't exist
		if (!verifySourceExists(SOURCE_FILE_NAME))
			System.err.println("File '" + SOURCE_FILE_NAME + "' not found; server aborted.");
		
		ServerSocket welcomeSocket = null;
		Socket helpClient = null;
		OutputStream out = null;
		try {
			welcomeSocket = new ServerSocket(PORT);
			helpClient = welcomeSocket.accept();
			out = helpClient.getOutputStream();
			sendFile(SOURCE_FILE_NAME, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
			if (helpClient != null)
				helpClient.close();
			if (welcomeSocket != null)
				welcomeSocket.close();
		}
		
	}
	
	// Copy the file at path fileName into the output stream
	public static void sendFile(String fileName, OutputStream os) {
		Path filePath = FileSystems.getDefault().getPath(fileName);
		try {
			Files.copy(filePath, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean verifySourceExists(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}
}
