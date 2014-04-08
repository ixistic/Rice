package server;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public Server(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("Server is running at port " + port);
			while (true) {
				Socket client = server.accept();
				(new ServerThread(client)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
