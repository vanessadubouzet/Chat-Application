package ChatClient;

import ChatServer.ChatServer;
import ChatServer.ChatServerIF;
import ChatClient.ClientRMIGUI;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class WelcomeGUI extends JFrame implements ActionListener {

    private JLabel titlelabel, instructionlabel, errorlabel;
    private JTextField usernametextField;
    private JButton startButton;
    String username, serverAddress;
    String[] arg = new String[3];
    java.util.List<String> users = new ArrayList<>();

    public WelcomeGUI() {
        super("Simple Chat Application");
        Container cont = getContentPane();
        cont.setLayout(new FlowLayout());

        cont.setBackground(new java.awt.Color(255, 246, 211));

        titlelabel = new JLabel("Welcome!", SwingConstants.CENTER);
        titlelabel.setFont(new java.awt.Font("Meiryo", Font.PLAIN, 30));
        titlelabel.setToolTipText("App Name");
        cont.add(titlelabel);

        ImageIcon icon = new ImageIcon("chatting.png");
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);

        instructionlabel = new JLabel();
        instructionlabel.setText("Please enter your name:");
        instructionlabel.setIcon(icon);
        instructionlabel.setHorizontalTextPosition(SwingConstants.CENTER);
        instructionlabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        cont.add(instructionlabel);

        usernametextField = new JTextField(20);
        cont.add(usernametextField);

        startButton = new JButton("Login");
        startButton.addActionListener(this);
        cont.add(startButton);

        setSize(275, 450);
        setLocation(600, 150);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            username = usernametextField.getText();
            if (username.length() != 0) {
                try {
                    this.dispose();
                    ClientRMIGUI chatapp = new ClientRMIGUI(username);
                    chatapp.setVisible(true);
                } catch (RemoteException ex) {
                    Logger.getLogger(WelcomeGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter your name to start", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        WelcomeGUI application = new WelcomeGUI();
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        application.setIconImage(icon);
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
