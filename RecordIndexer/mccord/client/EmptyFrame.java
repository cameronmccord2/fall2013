package client;

import javax.swing.JFrame;

public class EmptyFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5665082211161900101L;

	public EmptyFrame(){
		this.setTitle("Empty");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(100, 100);
		this.setSize(700, 500);
	}
}
