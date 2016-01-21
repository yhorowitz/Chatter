package yymh.chatter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import java.util.Iterator;

public class Server {

	public static int nextUserId = 1;
	
	static Object lock = new Object();
	public static int port = 22222;
	public static String ip = "localhost";
	
	ServerSocket serverSocket;
	
	ArrayList<ClientData> clients;
	ArrayList<ChatLine> messages;
	
	Thread newClientListener;
	
	Server() {
		Server server = new Server();
		server.messages = new ArrayList<>();
		server.clients = new ArrayList<>();

		System.out.println("Server running...");
		
		server.newClientListener = new Thread(new ListenForNewClient(server));
		
		try {
			server.serverSocket = new ServerSocket(port, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.newClientListener.start();
		
		//remove clients that have left
		while (true) { 
			synchronized(lock) {
				for (ClientData client : server.clients) {
					if (client== null || client.socket.isClosed()) {
						client = null;
						server.clients.remove(client);
					}
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				
		}
	}
	
	public synchronized void  newMessage(ChatLine line) {
		messages.add(line);
		updateClients();
	}
	
	public void updateClients() {
		Iterator<ClientData> iterator = clients.iterator();
		ClientData data;
		while (iterator.hasNext()) {
			 data = iterator.next();
			
			if (data.socket.isClosed()) {
				
				iterator.remove();
			}
			else {
				try {
					data.dos.writeUTF(messages.get(messages.size() - 1).toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
}
