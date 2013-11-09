package client;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import client.login.LoginGUI;
// jframe, jdialog, jpanel, jsplitframe, jcomponent-the thing to extend for custom, jtextfield, jpasswordfield, jlist, jscrollpane, jlabel, jbutton, bufferedImage, image
// events: mousePressed(MouseEvent event), mouseReleased, mouseClicked
//   listeners: mouseListener, keyPressListener, mouseMotionListener, actionListener(button), windowListener, windowStateListener, mouseWheelListener
// layout managers: flowLayout, gridBagLayout, boxLayout
public class SearchGUI extends JFrame {
	
	private static final long serialVersionUID = 4888473675568560947L;

	public SearchGUI(){
		super("GUI's init");
		this.setPreferredSize(new Dimension(270, 360));
		this.setMinimumSize(new Dimension(100, 100));
		
//		this.getContentPane().add(STUFF);
		this.setLocationRelativeTo(null);// this sets relative to center of the jframe
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {		
			public void run() {
				LoginGUI loginGui = new LoginGUI();
				loginGui.setVisible(true);
//				// Create the frame window object
//				SimpleFrame frame = new SimpleFrame();
//
//				// Make the frame window visible
//				frame.setVisible(true);
			}
		});
	}
}
