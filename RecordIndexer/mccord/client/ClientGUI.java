package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import communicator.DownloadBatchResult;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = -536053111717309667L;
	public String username;
	public String password;
	public String host;
	public String port;
	public DownloadBatchResult batch;
	
	
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
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new Center(this), lowerView);
		splitPane.setResizeWeight(0.75d);
		pane.add(splitPane, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
}
