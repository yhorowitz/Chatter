package yymh.chatter;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ListenForNewClient implements Runnable {

	Server server;
	
	ListenForNewClient(Server server) {
		this.server = server;
	}
	
	
	@Override
	public void run() {

		while (true) {
			Socket socket = null;
			try {
				socket = server.serverSocket.accept();
				ClientData client = new ClientData(socket);
				synchronized(Server.lock){
					server.clients.add(client);
				}
				client.listenForClient = new ListenForClientInput(server, client);
				client.listenForClient.start();	
				System.out.println("Client #" + client.userId + " has connected to server");
				server.newMessage(new ChatLine("system", "User " + client.userId + " has joined chat.", new Date()));
				
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
