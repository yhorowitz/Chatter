package yymh.chatter;

import java.io.IOException;
import java.util.ArrayList;

public class Client {
	public static int nextNum = 1;
	
	public static final int MAX_NUM_OF_MESSAGES = 200;
	
	ClientData data;
	ArrayList<String> messages = new ArrayList<>();
	ChatGUI chat;
	
	public void newMessage(String msg) {
		while (messages.size() > MAX_NUM_OF_MESSAGES) {
			messages.remove(0);
		}
		
		messages.add(msg);
	}
	
	Client() {
		Client client = new Client();
		
		client.chat = new ChatGUI(client);
		try {
			client.data = new ClientData(Server.ip, Server.port);
			client.data.listenForServer = new ListenForServerInput(client);
			client.data.listenForServer.start();
			client.chat.name.setText("Your are user #" + client.data.userId);
			
		} catch (IOException e){
			client.chat.messages.setText("Error connecting to chat");
			e.printStackTrace();
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					client.data.dos.writeUTF("EXIT_CODE_01234");
					client.data.listenForServer.listen = false;
					client.data.listenForServer.interrupt();
					client.data.nullAllValues();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
