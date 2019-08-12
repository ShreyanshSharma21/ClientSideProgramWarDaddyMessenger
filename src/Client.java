import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;


public class Client extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIp;
	private Socket connection;
	
	public Client(String host)
	{
		super("client part");
		serverIp = host;
		userText = new JTextField();
	    userText.setEditable(false);
	    userText.addActionListener(
	    		new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
						
						
					}
				}
	    		
	    		
	    		);
	    add (userText, BorderLayout.NORTH);
	    chatWindow = new JTextArea();
	    add(new JScrollPane(chatWindow),BorderLayout.CENTER);
	    setSize(500,500);
	    setVisible(true);
	    
	}
	
	public void startRunning()
	{
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException) {
			showMessage("\n client terminated connection");
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}finally {
			closeCrap();
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException
	{
		showMessage("Attempting Connection ... \n");
		connection = new Socket(InetAddress.getByName(serverIp), 6789);
		showMessage("Connected to :" + connection.getInetAddress().getHostName());
	}
	
	//setup streams to send and receive message
	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n dude your streams are now good to go \n");
		
		
	}
	
	//while chatting with server
	
	private void whileChatting() throws IOException
	{
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n	" + message);
				
			}catch(ClassNotFoundException classNotFoundException) {
				
				showMessage("\n i dunoo what the fuck just happened	");
				
				
			}
		}while(!message.equals("SERVER - END"));
	}
	
	//close the stream of sockets
	
	private void closeCrap() {
		showMessage("\n close everything ...");
		ableToType(false);
		try {
			
			output.close();
			input.close();
			connection.close();
			
		}catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
		
		
	}
	
	//send messages to the server 
	
	private void sendMessage(String message)
	{
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\n CLIENT - " + message);
		}catch(IOException ioException) {
			chatWindow.append("\n something messed up");
		}
	}
	
	//change or update chat window 
	
	private void showMessage(final String m)
	{
		 SwingUtilities.invokeLater(
				 new Runnable() {

					@Override
					public void run() {
						chatWindow.append(m);
						
					}
				 }	 );
	}
	
	// gives user permission to type 
	private void ableToType(final boolean tof) {
		 
		SwingUtilities.invokeLater(
				new Runnable() {
					
						public void run() {
						userText.setEditable(tof);
						
				}
			 }
		);
	}
}
