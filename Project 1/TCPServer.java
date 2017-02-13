import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple TCP Server that sends a file to connecting clients. Implements the
 * Runnable interface and multi-threading to service multiple clients at a time.
 * 
 * @author Jayson Rook (rookjc@miamioh.edu)
 */
public class TCPServer implements Runnable {
	private static final String SOURCE_FILE_NAME = "data.txt";
	private static final int PORT = 12345;
	
	// The socket associated with an instance (a single connected client)
	private Socket socket;
	
	/**
	 * Creates a welcome socket, that stays open as the server loops forever.
	 * Whenever a client connects, it is served (sent the file) from a new thread.
	 * 
	 * @param args No command-line arguments used
	 */
	public static void main(String[] args) {
		// Don't run the server if the source file doesn't exist
		if (!verifySourceExists(SOURCE_FILE_NAME)) {
			System.err.println("File '" + SOURCE_FILE_NAME + "' not found; server aborted.");
			return;
		}

		try {
			@SuppressWarnings("resource")	// welcomeSocket never closes due to infinite loop
			ServerSocket welcomeSocket = new ServerSocket(PORT);
			// Loop forever, each time a new client connects
			while (true) {
				Socket helpClient = welcomeSocket.accept();
				// Process the file transfer in another thread
				new Thread(new TCPServer(helpClient)).start();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		
	}
	
	/**
	 * Sends the file at SOURCE_FILE_NAME to the this.socket stream
	 */
	@Override
	public void run() {
		OutputStream out = null;
		try {
			out = this.socket.getOutputStream();
			sendFile(SOURCE_FILE_NAME, out);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			// Try to close output stream if it exists
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
			// Try to close this.socket
			try {
				this.socket.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
		
	} // end run method
	
	/**
	 * Constructor to create a Runnable instance, for threading
	 * 
	 * @param helpClient the socket this instance will serve
	 */
	public TCPServer(Socket helpClient) {
		this.socket = helpClient;
	}
	
	/**
	 * Copy the file at path fileName into the output stream
	 * 
	 * @param fileName the name of the file to send
	 * @param os the stream to send the file into
	 */
	public static void sendFile(String fileName, OutputStream os) {
		Path filePath = FileSystems.getDefault().getPath(fileName);
		try {
			// Uses java.nio.file.Files.copy to handle copying from path to stream
			Files.copy(filePath, os);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Determine whether the file given by fileName exists
	 * @param fileName the file to check for
	 * @return true iff the file exists
	 */
	public static boolean verifySourceExists(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}

}
