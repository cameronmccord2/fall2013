package client;

import java.awt.Dimension;

import javax.swing.JFrame;
// jframe, jdialog, jpanel, jsplitframe, jcomponent-the thing to extend for custom, jtextfield, jpasswordfield, jlist, jscrollpane, jlabel, jbutton, bufferedImage, image
// events: mousePressed(MouseEvent event), mouseReleased, mouseClicked
//   listeners: mouseListener, keyPressListener, mouseMotionListener, actionListener(button), windowListener, windowStateListener, mouseWheelListener
// layout managers: flowLayout, gridBagLayout, boxLayout
public class SearchGUI extends JFrame {
	
	public SearchGUI(){
		super("GUI's init");
		this.setPreferredSize(new Dimension(270, 360));
		this.setMinimumSize(new Dimension(100, 100));
		
		this.getContentPane().add(STUFF);
		this.setLocationRelativeTo(null);// this sets relative to center of the jframe
	}
}
