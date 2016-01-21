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
		
		this.chat = new ChatGUI(this);
		try {
			this.data = new ClientData(Server.ip, Server.port);
			this.data.listenForServer = new ListenForServerInput(this);
			this.data.listenForServer.start();
			this.chat.name.setText("Your are user #" + this.data.userId);
			
		} catch (IOException e){
			this.chat.messages.setText("Error connecting to chat");
			e.printStackTrace();
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					Client.this.data.dos.writeUTF("EXIT_CODE_01234");
					Client.this.data.listenForServer.listen = false;
					Client.this.data.listenForServer.interrupt();
					Client.this.data.nullAllValues();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
