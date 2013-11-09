package client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class SimpleFrame extends JFrame {
	public SimpleFrame(){
		this.setTitle("Simple");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(new JLabel("Hey!"), BorderLayout.NORTH);
		this.add(new JTextArea(3, 10), BorderLayout.CENTER);
		
		this.setLocation(100, 100);
		
		this.pack();
	}
}
