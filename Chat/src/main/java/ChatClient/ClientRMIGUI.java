package ChatClient;

import ChatServer.ChatServer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;

public final class ClientRMIGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel textPanel, privtextPanel, inputPanel, emojiPanel;
    private JTextField textField;
    private JButton emoji1, emoji2, emoji3, emoji4, emoji5, emoji6, privemoji1, privemoji2, privemoji3, privemoji4, privemoji5, privemoji6;
    private String name, message;
    private Font verandaFont = new Font("Veranda", Font.BOLD, 14);
    private Border blankBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);//top,r,b,l
    private ChatClient chatClient;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private boolean isPrivateChat, isGroupChat;

    protected JTextArea userArea;
    protected JPanel cards;
    public static JTextPane textArea, privtextArea;
    protected JScrollPane scrollPane, privscrollPane;
    protected JFrame frame;
    protected JButton privateMsgButton, startButton, sendButton, logoutButton;
    protected JButton groupChat, privateChat;
    protected JPanel clientPanel, userPanel;
    protected CardLayout cl; 

    public static void main(String args[]) {
        //set the look and feel to 'Nimbus'
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
    }//end main

    //GUI Constructor
    public ClientRMIGUI(String username) throws RemoteException {
        frame = new JFrame("Client Chat Console");
        this.name = username;

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (chatClient != null) {
                    try {
                        sendMessage("Bye all, I am leaving");
                        chatClient.serverIF.leaveChat(name);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });
        JPanel outerPanel = new JPanel(new BorderLayout());
        Container c = getContentPane();
        
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outerPanel.setBackground(new java.awt.Color(255, 246, 211));
        outerPanel.add(getTextPanel(), BorderLayout.CENTER);
        outerPanel.add(getInputPanel(), BorderLayout.SOUTH);
        
        c.setLayout(new BorderLayout());
        c.add(outerPanel, BorderLayout.CENTER);
        c.add(getUsersPanel(), BorderLayout.WEST);
        c.add(getChatButton(), BorderLayout.NORTH);
        c.add(getEmojiPanel(), BorderLayout.SOUTH);

        frame.add(c);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setLocation(150, 150);
        textField.requestFocus();

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
        
        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        frame.setIconImage(icon);

        if (name.length() != 0) {
            frame.setTitle(name + "'s console ");
            textField.setText("");
            textArea.setText(name + " connecting to chat...\n");
            getConnected(name);
            if (!chatClient.connectionProblem) {
                sendButton.setEnabled(true);
            }
        }
    }

    //Method to set up the JPanel to display the chat text
    public JPanel getTextPanel() {
        String welcome = "Welcome!";
        textArea = new JTextPane();
        textArea.setText(welcome);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(verandaFont);

        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(120, 300));

        JScrollPane scrollPane = new JScrollPane(textArea);

        textPanel = new JPanel(new BorderLayout(4, 4));
        textPanel.add(scrollPane, BorderLayout.SOUTH);
        textPanel.setBackground(new java.awt.Color(255, 246, 211));

        textPanel.setFont(new Font("Veranda", Font.PLAIN, 14));
        return textPanel;
    }

        public JPanel getChatButton() {
        inputPanel = new JPanel(new GridLayout(1, 2));
        inputPanel.setBorder(blankBorder);
        inputPanel.setBackground(new java.awt.Color(255, 246, 211)); //color yellow

        privateChat = new JButton("Private Chat");
        privateChat.setBackground(new java.awt.Color(238,206,205)); //color pink
        privateChat.addActionListener(this);

        groupChat = new JButton("Group Chat");
        groupChat.setBackground(new java.awt.Color(235, 235, 235)); // color gray
        groupChat.addActionListener(this);

        inputPanel.add(privateChat);
        inputPanel.add(groupChat);

        return inputPanel;
    }
        
    // Method to build the panel with input field
    public JPanel getInputPanel() {

        inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;

        inputPanel.setBorder(blankBorder);
        inputPanel.setBackground(new java.awt.Color(255, 246, 211));

        textField = new JTextField();
        textField.setFont(verandaFont);
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        sendButton = new JButton("Send");
        sendButton.setBackground(new java.awt.Color(235, 235, 235));
        sendButton.addActionListener(this);

        privateMsgButton = new JButton("Send PM");
        privateMsgButton.setBackground(new java.awt.Color(238,206,205));
        privateMsgButton.addActionListener(this);

        c1.gridx = 1;
        c1.gridy = 0;

        inputPanel.add(textField, c);
        inputPanel.add(sendButton, c1);
        inputPanel.add(privateMsgButton, c1);

        return inputPanel;
    }
    
    //TRY
    public JPanel getEmojiPanel() {

        inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints c3 = new GridBagConstraints();
        c3.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints c4 = new GridBagConstraints();
        c4.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints c5 = new GridBagConstraints();
        c5.fill = GridBagConstraints.HORIZONTAL;

        inputPanel.setBorder(blankBorder);
        inputPanel.setBackground(new java.awt.Color(255, 246, 211));

        ImageIcon emo1 = new ImageIcon("emoji1.png");
        emoji1 = new JButton("", emo1);
        emoji1.setForeground(Color.BLACK);
        emoji1.setBackground(Color.WHITE);
        emoji1.addActionListener(this);

        ImageIcon emo2 = new ImageIcon("emoji2.png");
        emoji2 = new JButton("", emo2);
        emoji2.setForeground(Color.BLACK);
        emoji2.setBackground(Color.WHITE);
        emoji2.addActionListener(this);

        ImageIcon emo3 = new ImageIcon("emoji3.png");
        emoji3 = new JButton("", emo3);
        emoji3.setForeground(Color.BLACK);
        emoji3.setBackground(Color.WHITE);
        emoji3.addActionListener(this);

        ImageIcon emo4 = new ImageIcon("emoji4.png");
        emoji4 = new JButton("", emo4);
        emoji4.setForeground(Color.BLACK);
        emoji4.setBackground(Color.WHITE);
        emoji4.addActionListener(this);

        ImageIcon emo5 = new ImageIcon("emoji5.png");
        emoji5 = new JButton("", emo5);
        emoji5.setForeground(Color.BLACK);
        emoji5.setBackground(Color.WHITE);
        emoji5.addActionListener(this);

        ImageIcon emo6 = new ImageIcon("emoji6.png");
        emoji6 = new JButton("", emo6);
        emoji6.setForeground(Color.BLACK);
        emoji6.setBackground(Color.WHITE);
        emoji6.addActionListener(this);
        
        //PRIVATE EMOJI
        ImageIcon privemo1 = new ImageIcon("emoji1.png");
        privemoji1 = new JButton("", emo1);
        privemoji1.setForeground(Color.BLACK);
        privemoji1.setBackground(Color.WHITE);
        privemoji1.addActionListener(this);
        privemoji1.setVisible(false);
        
        ImageIcon privemo2 = new ImageIcon("emoji2.png");
        privemoji2 = new JButton("", emo2);
        privemoji2.setForeground(Color.BLACK);
        privemoji2.setBackground(Color.WHITE);
        privemoji2.addActionListener(this);
        privemoji2.setVisible(false);
        
        ImageIcon privemo3 = new ImageIcon("emoji3.png");
        privemoji3 = new JButton("", emo3);
        privemoji3.setForeground(Color.BLACK);
        privemoji3.setBackground(Color.WHITE);
        privemoji3.addActionListener(this);
        privemoji3.setVisible(false);

        ImageIcon privemo4 = new ImageIcon("emoji4.png");
        privemoji4 = new JButton("", emo4);
        privemoji4.setForeground(Color.BLACK);
        privemoji4.setBackground(Color.WHITE);
        privemoji4.addActionListener(this);
        privemoji4.setVisible(false);

        ImageIcon privemo5 = new ImageIcon("emoji5.png");
        privemoji5 = new JButton("", emo5);
        privemoji5.setForeground(Color.BLACK);
        privemoji5.setBackground(Color.WHITE);
        privemoji5.addActionListener(this);
        privemoji5.setVisible(false);

        ImageIcon privemo6 = new ImageIcon("emoji6.png");
        privemoji6 = new JButton("", emo6);
        privemoji6.setForeground(Color.BLACK);
        privemoji6.setBackground(Color.WHITE);
        privemoji6.addActionListener(this);
        privemoji6.setVisible(false);

        c.gridx = 0;
        c.gridy = 0;
        
        c1.gridx = 1;
        c1.gridy = 0;
        
        c2.gridx = 2;
        c2.gridy = 0;
        
        c3.gridx = 3;
        c3.gridy = 0;
        
        c4.gridx = 4;
        c4.gridy = 0;
        
        c5.gridx = 5;
        c5.gridy = 0;

        inputPanel.add(emoji1, c);
        inputPanel.add(emoji2, c1);
        inputPanel.add(emoji3, c2);
        inputPanel.add(emoji4, c3);
        inputPanel.add(emoji5, c4);
        inputPanel.add(emoji6, c5);
        
        inputPanel.add(privemoji1, c);
        inputPanel.add(privemoji2, c1);
        inputPanel.add(privemoji3, c2);
        inputPanel.add(privemoji4, c3);
        inputPanel.add(privemoji5, c4);
        inputPanel.add(privemoji6, c5);

        return inputPanel;
    }
    
    //Method with a call to the button panel building method
    public JPanel getUsersPanel() {

        userPanel = new JPanel(new BorderLayout());
        String userStr = " Online Users      ";

        JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
        userPanel.setBackground(new java.awt.Color(255, 246, 211));

        userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));

        String[] noClientsYet = {"No other users"};
        setClientPanel(noClientsYet);

        clientPanel.setFont(verandaFont);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        userPanel.add(userLabel, BorderLayout.NORTH);
        userPanel.add(logoutButton, BorderLayout.SOUTH);
        userPanel.setBorder(blankBorder);

        return userPanel;
    }

    //Populate current user panel with a selected list of currently connected users
    public void setClientPanel(String[] currClients) {
        clientPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<String>();

        for (String s : currClients) {
            listModel.addElement(s);
        }
        if (currClients.length > 1) {
            privateMsgButton.setEnabled(true);
            privateChat.setEnabled(true);
        }

        //Create the list and put it in a scroll pane.
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(8);
        list.setFont(verandaFont);
        JScrollPane listScrollPane = new JScrollPane(list);

        clientPanel.add(listScrollPane, BorderLayout.CENTER);
        userPanel.add(clientPanel, BorderLayout.CENTER);
    }

    //Action handling on the buttons	 
    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            //get text and clear textField
            if (e.getSource() == sendButton) {
                message = textField.getText();
                textField.setText("");
                sendMessage(message);
                System.out.println("Sending message : " + message);
            }

            //send a private message, to selected users
            if (e.getSource() == privateMsgButton) {
                int[] privateList = list.getSelectedIndices();

                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selected index :" + privateList[i]);
                }

                message = textField.getText();
                textField.setText("");
                sendPrivate(privateList);
            }

            //logout
            if (e.getSource() == logoutButton) {
                if (chatClient != null) {
                    sendMessage("Bye all, I am leaving");
                    chatClient.serverIF.leaveChat(name);
                }
                System.exit(0);
            }
            
            //GROUP EMOJI BUTTON
            if (e.getSource() == emoji1) {
                sendEmoji(new ImageIcon("emoji1.png"));
                System.out.println("Sending message : emoji1"); 
            }

            if (e.getSource() == emoji2) {
                sendEmoji(new ImageIcon("emoji2.png"));
                System.out.println("Sending message : emoji2");
            }

            if (e.getSource() == emoji3) {
                sendEmoji(new ImageIcon("emoji3.png"));
                System.out.println("Sending message : emoji3");
            }

            if (e.getSource() == emoji4) {
                sendEmoji(new ImageIcon("emoji4.png"));
                System.out.println("Sending message : emoji4");
            }

            if (e.getSource() == emoji5) {
                sendEmoji(new ImageIcon("emoji5.png"));
                System.out.println("Sending message : emoji5");
            }

            if (e.getSource() == emoji6) {
                sendEmoji(new ImageIcon("emoji6.png"));
                System.out.println("Sending message : emoji6");
            }

            
            //PRIVATE EMOJI BUTTON
            if (e.getSource() == privemoji1) {
                System.out.println("Sending message : emoji1"); 
                int[] privateList = list.getSelectedIndices();
                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selected index :" + privateList[i]);
                } 
                sendPrivateIcon(privateList, new ImageIcon("emoji1.png"));
            }

            if (e.getSource() == privemoji2) {
                System.out.println("Sending message : emoji2"); 
                int[] privateList = list.getSelectedIndices();
                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selected index :" + privateList[i]);
                } 
                sendPrivateIcon(privateList, new ImageIcon("emoji2.png"));
            }

            if (e.getSource() == privemoji3) {
                System.out.println("Sending message : emoji3"); 
                int[] privateList = list.getSelectedIndices();
                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selected index :" + privateList[i]);
                } 
                sendPrivateIcon(privateList, new ImageIcon("emoji3.png"));
            }

            if (e.getSource() == privemoji4) {
                System.out.println("Sending message : emoji4"); 
                int[] privateList = list.getSelectedIndices();
                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selected index :" + privateList[i]);
                } 
                sendPrivateIcon(privateList, new ImageIcon("emoji4.png"));
            }

            if (e.getSource() == privemoji5) {
                System.out.println("Sending message : emoji5"); 
                int[] privateList = list.getSelectedIndices();
                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selected index :" + privateList[i]);
                } 
                sendPrivateIcon(privateList, new ImageIcon("emoji5.png"));
            }

            if (e.getSource() == privemoji6) {
                System.out.println("Sending message : emoji6"); 
                int[] privateList = list.getSelectedIndices();
                for (int i = 0; i < privateList.length; i++) {
                    System.out.println("selected index :" + privateList[i]);
                } 
                sendPrivateIcon(privateList, new ImageIcon("emoji6.png"));
            }
            
            if (e.getSource() == privateChat) {
                sendButton.setVisible(false);
                emoji1.setVisible(false);
                emoji2.setVisible(false);
                emoji3.setVisible(false);
                emoji4.setVisible(false);
                emoji5.setVisible(false);
                emoji6.setVisible(false);
                
                
                privemoji1.setVisible(true);
                privemoji2.setVisible(true);
                privemoji3.setVisible(true);
                privemoji4.setVisible(true);
                privemoji5.setVisible(true);
                privemoji6.setVisible(true);
                privateMsgButton.setVisible(true);
            }

            if (e.getSource() == groupChat) {
                sendButton.setVisible(true);
                emoji1.setVisible(true);
                emoji2.setVisible(true);
                emoji3.setVisible(true);
                emoji4.setVisible(true);
                emoji5.setVisible(true);
                emoji6.setVisible(true);
                
                privemoji1.setVisible(false);
                privemoji2.setVisible(false);
                privemoji3.setVisible(false);
                privemoji4.setVisible(false);
                privemoji5.setVisible(false);
                privemoji6.setVisible(false);
                privateMsgButton.setVisible(false);
            }

        } catch (RemoteException remoteExc) {
            remoteExc.printStackTrace();
        }

    }

    //Send a message, to be relayed to all chatters
    private void sendMessage(String chatMessage) throws RemoteException {
        chatClient.serverIF.updateChat(name, chatMessage);
    }

    //Send a emoji, to be relayed to all chatter
    private void sendEmoji(ImageIcon emoji) throws RemoteException {
        chatClient.serverIF.updateChatIcon(name, emoji);
    }

    //Send a message, to be relayed, only to selected chatters
    private void sendPrivate(int[] privateList) throws RemoteException {
        LocalTime localTime = LocalTime.now();
        var time = localTime.toString();
        time = time.substring(0, 5);
        String privateMessage = "(" + time + ") [PM from " + name + "] :" + message + "\n";
        chatClient.serverIF.sendPM(privateList, privateMessage);
    }
    
    //Send a privateEmoji, to be relayed, only to selected characters 
    private void sendPrivateIcon(int[] privateList, ImageIcon emoji) throws RemoteException {
        LocalTime localTime = LocalTime.now();
        var time = localTime.toString();
        time = time.substring(0, 5);
        String privateMessage = "(" + time + ") [PM from " + name + "] : ";
        chatClient.serverIF.sendIconPM(privateList, privateMessage, emoji);
    }
    
    //Make the connection to the chat server
    private void getConnected(String userName) throws RemoteException {
        //remove whitespace and non word characters to avoid malformed url
        String cleanedUserName = userName.replaceAll("\\s+", "_");
        cleanedUserName = userName.replaceAll("\\W+", "_");
        try {
            chatClient = new ChatClient(this, cleanedUserName);
            chatClient.startClient();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
