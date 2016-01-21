package yymh.chatter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientData{

	int userId;
	static Object lock = new Object();
	
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	BufferedReader reader;
	PrintWriter writer;
	ListenForClientInput listenForClient = null;
	ListenForServerInput listenForServer = null;
	
	//to be used to avoid memory leaks
	public void nullAllValues() {
		try {
			socket.close();
			socket = null;
			dis.close();
			dos.close();
			reader.close();
			writer.close();
			listenForClient = null;
			listenForServer = null;
			lock = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//used by server
	ClientData(Socket socket) throws IOException {
		this.socket = socket;
		this.dis = new DataInputStream(this.socket.getInputStream());
		this.dos = new DataOutputStream(this.socket.getOutputStream());
		synchronized(lock) {
			this.userId = Server.nextUserId;
			Server.nextUserId++;			
		}
		this.dos.writeInt(userId);

		this.reader = new BufferedReader(new InputStreamReader(this.dis));
		this.writer = new PrintWriter(dos, true);

	}
	
	//used by clients to establish a connection
	ClientData(String ip, int port) throws IOException{
		socket = new Socket(ip, port);
		this.dis = new DataInputStream(this.socket.getInputStream());;
		this.dos = new DataOutputStream(this.socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(this.dis));
		writer = new PrintWriter(dos, true);

		synchronized(lock) {
			this.userId = dis.readInt();
		}
	}
	
}
