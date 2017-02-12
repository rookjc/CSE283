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
	private static final String SOURCE_FILE_NAME = "video.mp4";
	private static final int PORT = 12345;
	
	// The socket associated with an instance (a single connected client)
	private Socket socket;
	
	
	public static void main(String[] args) {
		// Don't run the server if the source file doesn't exist
		if (!verifySourceExists(SOURCE_FILE_NAME)) {
			System.err.println("File '" + SOURCE_FILE_NAME + "' not found; server aborted.");
			return;
		}

		try {
			ServerSocket welcomeSocket = new ServerSocket(PORT);
			while (true) {
				Socket helpClient = welcomeSocket.accept();
				System.out.println("Connection!");
				new Thread(new TCPServer(helpClient)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		OutputStream out = null;
		try {
			out = this.socket.getOutputStream();
			sendFile(SOURCE_FILE_NAME, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public TCPServer(Socket helpClient) {
		this.socket = helpClient;
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
