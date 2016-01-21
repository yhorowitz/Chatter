package yymh.chatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Server {

	public static int nextUserId = 1;
	
	static Object lock = new Object();
	public static int port = 22222;
	public static String ip = "24.186.209.128";
	//public static String ip = "192.168.1.1";
	
	ServerSocket serverSocket;
	
	ArrayList<ClientData> clients;
	ArrayList<ChatLine> messages;
	
	Thread newClientListener;
	
	Server() {
		this.messages = new ArrayList<>();
		this.clients = new ArrayList<>();
		
		this.newClientListener = new Thread(new ListenForNewClient(this));
		
		try {
			this.serverSocket = new ServerSocket(port, 10);
			
			JFrame window = new JFrame("Server Connected");
			JPanel mainPanel = new JPanel();
			JLabel label = new JLabel("The Chatter Server Is Running");
			JButton shutOffButton = new JButton("Shut Down Server");
			
			shutOffButton.addActionListener(new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
				
			});
			
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.add(label);
			mainPanel.add(shutOffButton);
			window.add(mainPanel);
			window.pack();
			window.setResizable(false);
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is already an instance of the Chatter server running", "Error", JOptionPane.ERROR_MESSAGE);
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
