package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import communicator.DownloadBatchResult;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = -536053111717309667L;
	public String username;
	public String password;
	public String host;
	public String port;
	public DownloadBatchResult batch;
	public JMenuItem downloadBatchMenuItem;
	
	
	public ClientGUI(String username, String password, String host, String port){
		super("Client GUI");
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension panelSize = new Dimension(1200, 800);
		this.setSize(panelSize);
//		this.setMinimumSize(panelSize);
//		this.setMaximumSize(panelSize);
		Container pane = this.getContentPane();
//		GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.HORIZONTAL;
		pane.setLayout(new BorderLayout());
		
		pane.add(new ButtonBar(this), BorderLayout.PAGE_START);
		
		JSplitPane lowerView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new EntryView(this), new HelpView(this));
		lowerView.setResizeWeight(0.5d);
		lowerView.setVisible(true);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new Center(this), lowerView);
		splitPane.setResizeWeight(0.75d);
		splitPane.setVisible(true);
		pane.add(splitPane, BorderLayout.CENTER);
		
		
		// menu
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);

		//a group of JMenuItems
		downloadBatchMenuItem = new JMenuItem("Download Batch", KeyEvent.VK_T);
		downloadBatchMenuItem.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		menu.add(downloadBatchMenuItem);

		menuItem = new JMenuItem("Logout", KeyEvent.VK_B);
		menuItem.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Exit", KeyEvent.VK_B);
		menuItem.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		menu.add(menuItem);

		
		this.setJMenuBar(menuBar);
		
		this.setVisible(true);
	}
	
	public void setDownloadBatchEnabledTo(boolean bool){
		this.downloadBatchMenuItem.setEnabled(bool);
	}
	
	public void downloadedBatch(DownloadBatchResult result){
		
	}
}
