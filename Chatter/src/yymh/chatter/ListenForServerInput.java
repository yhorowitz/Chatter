package yymh.chatter;
import java.io.IOException;

public class ListenForServerInput extends Thread{

	Client client;
	boolean listen = true;
	
	public ListenForServerInput(Client client) {
		this.client = client;
	}
	
	@Override
	public void run() {
				
		while (listen) {
			try {
				String msg = client.data.dis.readUTF();
				client.newMessage(msg);
				client.chat.updateChat();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
