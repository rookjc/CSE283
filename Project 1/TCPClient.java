import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

// Simple client that connects to a TCP server and prints out everything received.
public class TCPClient {
	private static final int PORT = 12345;
	
	public static void main(String[] args) throws IOException {
		String destFileName = (args.length > 0 ? args[0] : "test2.txt");
		
		Socket client = null;
		InputStream in = null;
		try {
			client = new Socket("localhost", PORT);
			in = client.getInputStream();
			recieveFile(client.getInputStream(), destFileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				in.close();
			if (client != null)
				client.close();
		}
		
	}
	
	public static void recieveFile(InputStream is, String fileName) {
		Path filePath = FileSystems.getDefault().getPath(fileName);
		try {
			Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
