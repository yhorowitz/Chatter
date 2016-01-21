package yymh.chatter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChatGUI extends JFrame{
	
	JLabel name = new JLabel();
	JTextArea messages = new JTextArea(22, 75);
	JTextField newMessage = new JTextField("");
	
	JScrollPane messagesScroll = new JScrollPane(messages);
	
	JPanel buttonPanel = new JPanel();
	JButton sendButton = new JButton("Send");
	JButton exitButton = new JButton("Exit");
	
	JPanel mainPanel = new JPanel();
	Client client;
	
	public void sendMessageToServer() {
		if (!messages.getText().equals("")){
			try {
				client.data.dos.writeUTF(newMessage.getText());
				newMessage.setText("");
				newMessage.requestFocusInWindow();
				messagesScroll.getVerticalScrollBar().setValue(messagesScroll.getVerticalScrollBar().getMaximum());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	ChatGUI(Client client) {
		this.client = client;
		
		this.setResizable(false);
		this.setTitle("Chatter");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		messages.setBackground(Color.GRAY);
		messages.setForeground(Color.WHITE);
		messages.setEditable(false);
		
		messagesScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		messagesScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		messagesScroll.setPreferredSize(new Dimension(600, 400));
		newMessage.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessageToServer();
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		
		
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (newMessage.getText() != null || !newMessage.getText().equals("")) {
						sendMessageToServer();
				}
					
			}
			
		});
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		buttonPanel.add(sendButton);
		buttonPanel.add(exitButton);
		
		mainPanel.add(name);
		mainPanel.add(messagesScroll);
		mainPanel.add(newMessage);
		mainPanel.add(buttonPanel);
		
		this.add(mainPanel);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		newMessage.requestFocusInWindow();

	}
	
	public void updateChat() {
		messages.setText("");
		
		for (String line : client.messages) {
			messages.append(line + "\n");
		}
	}
	
}
