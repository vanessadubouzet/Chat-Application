# COSC 318 - Network Programming Final Project
This project was created by Rao Fu and Vanessa Dubouzet

## General Info
This project is a simple chat application that uses Remote Method Invocation (RMI). 
No further improvement was made in this project since the project demo presentation last April 13, 2023. 

### Project Information - how it works  
- To start this project, you need to run the ChatServer.java file
- After that, you can now run the WelcomeGUI.java file. 
	- By running this file, you will be greeted by the Welcome Frame (Welcome Frame.jpg) 
	- In this Frame, you will be asked to input your name before joining the chat server. 
	- We handled validations (Welcome Frame - Validation.jpg) when a user doesn't enter a name
	- Once logged in, you will be greeted by the Main Chat Frame (Main Chat Frame.jpg) 
- The Main Chat Frame is where all the chat activities are held. 
	- 11 Buttons: Private Chat, Group Chat, Logout, Send, SendPM, Emojies(6)
	- 1 TextField
- To send a private message:
	- Click on the Private Chat button
	- Select the user you want to send the message to, on the Online User column
	- Type your message in the TextField and click the SendPM button / click on any emoji button
- To send a group message:
	- Click on the Group Chat button
	- Type your message in the TextField and click the Send button / click on any emoji button
- To disconnect / logout 
	- Click on the logout button. 
