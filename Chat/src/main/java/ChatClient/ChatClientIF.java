package ChatClient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.ImageIcon;

public interface ChatClientIF extends Remote{

	public void messageFromServer(String message) throws RemoteException;

        public void iconFromServer(ImageIcon message) throws RemoteException;
        
	public void updateUserList(String[] currentUsers) throws RemoteException;

}
