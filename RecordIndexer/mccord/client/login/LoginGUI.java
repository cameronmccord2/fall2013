package client.login;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import models.FailedResult;
import models.FalseResult;
import server.ClientCommunicator;

import communicator.ValidateUserParams;
import communicator.ValidateUserResult;

import client.TextPrompt;
import client.TextPrompt.Show;

public class LoginGUI extends JFrame{

	private static final long serialVersionUID = -3795015357219531829L;
	private JTextField username;
	private JTextField password;
	private JTextField host;
	private JTextField port;
	
	public LoginGUI(){
		super("Login GUI");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension panelSize = new Dimension(400, 100);
		this.setSize(panelSize);
		this.setMinimumSize(panelSize);
		this.setMaximumSize(panelSize);
		Container pane = this.getContentPane();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.setLayout(new GridBagLayout());
		
		username = new JTextField();
		TextPrompt usernameTextPrompt = new TextPrompt("Username", username);
		usernameTextPrompt.changeAlpha(.75f);
		usernameTextPrompt.setShow(Show.FOCUS_LOST);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(username, c);

		password = new JPasswordField();
		TextPrompt passwordTextPrompt = new TextPrompt("Password", password);
		passwordTextPrompt.changeAlpha(.75f);
		passwordTextPrompt.setShow(Show.FOCUS_LOST);
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(password, c);
		
		
		host = new JTextField();
		TextPrompt hostPrompt = new TextPrompt("Host", host);
		hostPrompt.changeAlpha(.75f);
		hostPrompt.setShow(Show.FOCUS_LOST);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(host, c);
		
		port = new JTextField();
		TextPrompt portPrompt = new TextPrompt("port", port);
		portPrompt.changeAlpha(.75f);
		portPrompt.setShow(Show.FOCUS_LOST);
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 1;
		pane.add(port, c);
		
		JButton loginButton = new JButton("Login");
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 0;
		loginButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(username.getText().length() > 0 && password.getText().length() > 0 && host.getText().length() > 0 && port.getText().length() > 0) 
					validateUser(username.getText(), password.getText(), host.getText(), port.getText());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
		});
		pane.add(loginButton, c);
		
		this.pack();
	}
	
	private void validateUser(String username, String password, String host, String port){
		System.out.println("validating user");
		ClientCommunicator cc = new ClientCommunicator();
		ValidateUserParams var = new ValidateUserParams();
		var.setUsername(username);
		var.setPassword(password);
		try {
			Object response = cc.validateUser(var, host, port);
			if(response instanceof ValidateUserResult){
				this.setVisible(false);
			}else if(response instanceof FailedResult){
//				result = ((FailedResult)response).toString();
			}else if(response instanceof FalseResult){
				
			}
//				result = ((FalseResult)response).toString();
//			getView().setResponse(result);
		} catch (Exception e) {
//			result = "FAILED";
		}finally{
//			getView().setResponse(result);
		}
	}
}
