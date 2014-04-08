package server;
import java.net.ServerSocket;
import java.net.Socket;

public class ESServer {

	public ESServer(int port, ExpertSystem expertSystem) {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			System.out.println("Server is running at port " + port);
			while (true) {
				Socket client = server.accept();
				(new ESServerThread(client, expertSystem)).start();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
