import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Simple TCP Client that connects to TCPServer.java and receives the file sent.
 * The first command-line argument specifies where to save the file.
 * 
 * @author Jayson Rook (rookjc@miamioh.edu)
 */
public class TCPClient {
	private static final String ADDRESS = "localhost";
	private static final int PORT = 12345;
	
	/**
	 * Try connecting to the server at ADDRESS and PORT. If successful, store the
	 * data received to a file.
	 * 
	 * @param args single command line argument to specify destination file name
	 * @throws IOException if something goes wrong in closing streams or sockets
	 */
	public static void main(String[] args) throws IOException {
		// Make sure a destination file name is specified as a command-line argument
		if (args.length == 0) {
			System.err.println("Argument error; specify destination file name.");
			return;
		}
		String destFileName = args[0];
		
		Socket client = null;
		InputStream in = null;
		try {
			// Try connecting to server and receiving data
			client = new Socket(ADDRESS, PORT);
			in = client.getInputStream();
			receiveFile(client.getInputStream(), destFileName);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			// Close stream and socket, if either created successfully
			if (in != null)
				in.close();
			if (client != null)
				client.close();
		}
		
	}
	
	/**
	 * Copy data from a stream to a file
	 * 
	 * @param is the stream to receive data from
	 * @param fileName the file to store data in
	 */
	public static void receiveFile(InputStream is, String fileName) {
		Path filePath = FileSystems.getDefault().getPath(fileName);
		try {
			// Uses java.nio.file.Files.copy to handle copying from stream to path
			Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
