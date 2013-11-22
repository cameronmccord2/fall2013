package client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Center extends JPanel {
	public Center(ClientGUI cg){
		super();
		
		JPanel centerView = new JPanel();
//		centerView.setPreferredSize(new Dimension(600, 400));
		centerView.setVisible(true);
		centerView.setBackground(Color.BLUE);
		
		this.add(centerView);
		
	}
}
