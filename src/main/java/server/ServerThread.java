package server;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;

public class ServerThread extends Thread {

	private Socket client;

	public ServerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			
			String incomingIP = client.getInetAddress().toString();
			int incomingPort = client.getPort();
			System.out.println(">> " + incomingIP + ":" + incomingPort);
			InputStream in = client.getInputStream();
			OutputStream out = client.getOutputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "UTF-8"));
			
			for (int i = 0; i < 2000; i++) {      
			      boolean ready = reader.ready();
			     if (ready)  
			        break;  
			    try {  
			        Thread.sleep(1);  
			    } catch (InterruptedException e) {}  
			}
			
			StringBuilder sb = new StringBuilder();
			while (reader.ready()) {
				sb.append((char) reader.read());
//				System.out.print((char) reader.read());
			}
			String data = sb.toString().split("\r\n\r\n")[1];
			data = URLDecoder.decode(data, "UTF-8");
			System.out.println(data);

			String response = "";
			response += "HTTP/1.1 200 OK\r\n";
			response += "Server: Mapfap Server\r\n";
			response += "Content-Type: text/html\r\n";
			response += "Access-Control-Allow-Origin: *\r\n";
			response += "\r\n";
			response += data;
			response += "\r\n";

			byte[] bytes = response.getBytes();
			out.write(bytes);
			out.flush();
			in.close();
			out.close();
			System.out.println(">> Connection closed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
