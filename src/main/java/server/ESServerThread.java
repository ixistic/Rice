package server;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;


public class ESServerThread extends Thread {

	private Socket client;
	private ExpertSystem expertSystem;

	public ESServerThread(Socket client, ExpertSystem expertSystem) {
		this.client = client;
		this.expertSystem = expertSystem;
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
			
			String[] splited = sb.toString().split("\r\n\r\nq=");
			String answer = null;
			if (splited.length > 1) {
				String question = splited[1];
				if (question.equals(".reset")){
					expertSystem.reInitiate();
				}
				question = URLDecoder.decode(question, "UTF-8");
				System.out.println(question);
				answer = expertSystem.getAnswer(question);
//				System.out.println(answer + "  ice");
			} else {
				answer = "ล้มเหลว";
			}
			
			String response = "HTTP/1.1 200 OK\r\nServer: Mapfap Server\r\nContent-Type: text/html\r\nAccess-Control-Allow-Origin: *\r\n\r\n";
			response += answer + "\r\n";

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
