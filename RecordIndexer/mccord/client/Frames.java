package client;

import java.awt.EventQueue;

public class Frames {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				EmptyFrame frame = new EmptyFrame();
				
				frame.setVisible(true);
			}
		});

	}

}
