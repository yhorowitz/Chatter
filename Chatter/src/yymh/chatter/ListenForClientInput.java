package yymh.chatter;
import java.io.IOException;
import java.util.Date;

public class ListenForClientInput extends Thread {

	ClientData data;
	Server server;
	
	public ListenForClientInput(Server server, ClientData data) {
		this.server = server;
		this.data = data;
	}
	
	@Override
	public void run() {
		
		String input;
		
		while (true) {
			try {

				input = data.dis.readUTF();
				ChatLine line = null;
				
				if (input.equals("EXIT_CODE_01234")) {
					line = new ChatLine("system", "User " + data.userId + " has left the chat", new Date());
					server.newMessage(line);
					
					data.nullAllValues();

					break;
				}
				else {
					line = new ChatLine("User " + data.userId, input, new Date());
				}
					
				server.newMessage(line);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
