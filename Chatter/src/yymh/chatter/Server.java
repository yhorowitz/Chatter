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
		this.messages = new ArrayList<>();
		this.clients = new ArrayList<>();

		System.out.println("Server running...");
		
		this.newClientListener = new Thread(new ListenForNewClient(this));
		
		try {
			this.serverSocket = new ServerSocket(port, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.newClientListener.start();
		
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
