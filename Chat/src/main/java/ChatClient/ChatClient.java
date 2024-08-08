package ChatClient;

import static ChatClient.ClientRMIGUI.textArea;
import ChatServer.ChatServerIF;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;


public class ChatClient  extends UnicastRemoteObject implements ChatClientIF {
	/**
	 *
	 */
	private static final long serialVersionUID = 7468891722773409712L;
	ClientRMIGUI chatGUI;
	private String hostName = "localhost";
	private String serviceName = "GroupChatService";
	private String clientServiceName;
	private String name;
	protected ChatServerIF serverIF;
	protected boolean connectionProblem = false;


	/**
	 * class constructor,
	 * note may also use an overloaded constructor with
	 * a port no passed in argument to super
	 * @throws RemoteException
	 */
	public ChatClient(ClientRMIGUI aChatGUI, String userName) throws RemoteException {
		super();
		this.chatGUI = aChatGUI;
		this.name = userName;
		this.clientServiceName = "ClientListenService_" + userName;
	}

	/**
	 * Register our own listening service/interface
	 * lookup the server RMI interface, then send our details
	 * @throws RemoteException
	 */
	public void startClient() throws RemoteException {
		String[] details = {name, hostName, clientServiceName};

		try {
			Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
			serverIF = ( ChatServerIF )Naming.lookup("rmi://" + hostName + "/" + serviceName);
		}
		catch (ConnectException  e) {
			JOptionPane.showMessageDialog(
					chatGUI.frame, "The server seems to be unavailable\nPlease try later",
					"Connection problem", JOptionPane.ERROR_MESSAGE);
			connectionProblem = true;
			e.printStackTrace();
		}
		catch(NotBoundException | MalformedURLException me){
			connectionProblem = true;
			me.printStackTrace();
		}
		if(!connectionProblem){
			registerWithServer(details);
		}
		System.out.println("Client Listen RMI Server is running...\n");
	}


	/**
	 * pass our username, hostname and RMI service name to
	 * the server to register out interest in joining the chat
	 * @param details
	 */
	public void registerWithServer(String[] details) {
		try{
			serverIF.passIDentity(this.ref);
			serverIF.registerListener(details);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//=====================================================================
        //GROUP CHAT 
	@Override
	public void messageFromServer(String message) throws RemoteException {
            try {
                System.out.println( message );
                chatGUI.textArea.getDocument().insertString(chatGUI.textArea.getDocument().getLength(), message, null);
                chatGUI.textArea.setCaretPosition(chatGUI.textArea.getDocument().getLength());
            } catch (BadLocationException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        
        public void iconFromServer(ImageIcon emoji) throws RemoteException {
		System.out.println( emoji );
                chatGUI.textArea.insertIcon(emoji);
		chatGUI.textArea.setCaretPosition(chatGUI.textArea.getDocument().getLength());
	}
    
	/**
	 * A method to update the display of users
	 * currently connected to the server
	 */
	@Override
	public void updateUserList(String[] currentUsers) throws RemoteException {

		if(currentUsers.length < 2){
			chatGUI.privateMsgButton.setEnabled(false);
                        chatGUI.privateChat.setEnabled(false);
		}
		chatGUI.userPanel.remove(chatGUI.clientPanel);
		chatGUI.setClientPanel(currentUsers);
		chatGUI.clientPanel.repaint();
		chatGUI.clientPanel.revalidate();
	}
}//end class













