package client;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class HelpView extends JPanel {
	public HelpView(ClientGUI cg){
		super();
		
		JTabbedPane helpView = new JTabbedPane();
		helpView.setVisible(true);
		
		JPanel fieldHelp = new JPanel();
		helpView.addTab("Field Help", fieldHelp);
		helpView.setMnemonicAt(0, KeyEvent.VK_1);
		
		JPanel imageNavigation = new JPanel();
		helpView.addTab("Image Navigation", imageNavigation);
		helpView.setMnemonicAt(1, KeyEvent.VK_2);
	}
}
