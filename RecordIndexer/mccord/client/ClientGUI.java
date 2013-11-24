package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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

import client.login.LoginGUI;
import server.ClientCommunicator;
import models.FailedResult;
import models.Fields;
import communicator.DownloadBatchResult;
import communicator.SubmitBatchParams;
import communicator.SubmitBatchResult;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = -536053111717309667L;
	public String username;
	public String password;
	public String host;
	public String port;
	public DownloadBatchResult batch;
	public JMenuItem downloadBatchMenuItem;
	private DownloadBatchGUI downloadBatchGUI;
	private EntryView entryView;
	private HelpView helpView;
	private Center center;
	private JSplitPane lowerView;
	private JSplitPane mainSplitPane;
	private JMenuBar menuBar;
	private LoginGUI lg;
	
	
	public ClientGUI(String username, String password, String host, String port, LoginGUI lg){
		super("Client GUI");
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.lg = lg;
		
		this.downloadBatchGUI = new DownloadBatchGUI(this);

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
		
		this.entryView = new EntryView(this);
		this.helpView = new HelpView(this);
		lowerView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.entryView, this.helpView);
		lowerView.setResizeWeight(0.5d);
		lowerView.setVisible(true);
		
		this.center = new Center(this);
		mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.center, lowerView);
		mainSplitPane.setResizeWeight(0.75d);
		mainSplitPane.setVisible(true);
		pane.add(mainSplitPane, BorderLayout.CENTER);
		
		
		// menu
		//Where the GUI is created:
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
			public void mouseReleased(MouseEvent e) {
				System.out.println("download batch clicked");
				openDownloadBatchGUI();
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		menu.add(downloadBatchMenuItem);

		menuItem = new JMenuItem("Logout", KeyEvent.VK_B);
		menuItem.addMouseListener(new MouseListener(){
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("logout clicked");
				save();
				logout();
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Exit", KeyEvent.VK_B);
		menuItem.addMouseListener(new MouseListener(){
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("exit clicked");
				save();
				System.exit(1);
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		menu.add(menuItem);

		
		this.setJMenuBar(menuBar);
		
		this.setVisible(true);
	}
	
	public void openDownloadBatchGUI(){
		if(this.batch != null)
			return;
		System.out.println("opening download batch gui");
		downloadBatchGUI.setVisible(true);
	}
	
	public void closeDownloadBatchGUI(){
		downloadBatchGUI.setVisible(false);
	}
	
	public void setDownloadBatchEnabledTo(boolean bool){
		this.downloadBatchMenuItem.setEnabled(bool);
	}
	
	public void downloadedBatch(DownloadBatchResult result){
		System.out.println("got downloaded batch in gui");
		this.batch = result;
		ArrayList<Fields> newFields = new ArrayList<Fields>();
		Fields field = new Fields();
		field.setTitle("Row Number");
		newFields.add(field);
		newFields.addAll(this.batch.getFields());
		this.batch.setFields(newFields);
		this.entryView.populateTabs(this);
		this.helpView.populateTabs(this);
		this.center.loadImage(result.getImageUrl());
		this.mainSplitPane.setDividerLocation(0.61d);
		this.setDownloadBatchEnabledTo(false);
	}

	public void changeSelectedCell(int selectedColumn, int selectedRow) {
		this.center.updateLastColumn(selectedColumn);
		this.helpView.changeSelectedField(selectedColumn);
		if(selectedColumn != 0){
			System.out.println("change selected cell, col: " + selectedColumn + ", row: " + selectedRow);
			Integer x = this.batch.getFields().get(selectedColumn).getXcoor();
			Integer y = selectedRow * this.batch.getRecordHeight() + this.batch.getFirstYCoor();
			Integer width = this.batch.getFields().get(selectedColumn).getWidth();
			System.out.println("x: " + x + ", y: " + y + ", width: " + width);
			this.center.adjustSquarePosition(x, y, width, this.batch.getRecordHeight());
		}
	}

	public void zoomIn() {
		this.center.zoomIn();
	}

	public void zoomOut() {
		this.center.zoomOut();
	}

	public void invert() {
		this.center.invert();
	}

	public void toggleHighlights() {
		this.center.toggleHighlights();
	}

	public void save() {
		// TODO Auto-generated method stub
		
	}

	public void submit() {
		SubmitBatchParams sbp = new SubmitBatchParams();
		sbp.setFieldValues(this.entryView.getFieldValuesForSubmit());
		sbp.setPassword(this.password);
		sbp.setUsername(this.username);
		sbp.setBatchId(this.batch.getBatchId());
		SubmitBatchResult r = this.submitBatch(sbp);
		if("TRUE".equalsIgnoreCase(r.getResult())){
			System.out.println("goodly submitted a batch");
			this.lg.resetView();
		}else{
			System.out.println("expected TRUE, got : " + r.getResult());
		}
	}
	
	public void logout() {
		this.lg.logout();
	}
	
	public void quitProgram() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("finally")
	private SubmitBatchResult submitBatch(SubmitBatchParams sbp) {
		ClientCommunicator cc = new ClientCommunicator();
		SubmitBatchResult result = null;
		try {
			Object response = cc.submitBatch(sbp, this.host, this.port);
			if(response instanceof SubmitBatchResult){
				result = (SubmitBatchResult)response;
				System.out.println(result.getResult());
			}else if(response instanceof FailedResult){
				System.out.println("submit batch failed result");
				result = new SubmitBatchResult();
				result.setResult("FAILED");
//				result = ((FailedResult)response).toString();
			}else
				System.out.println("submit batch something else");
//				result = "something else" + response.getClass();
		} catch (Exception e) {
			System.out.println("FAILED");
//			result = "FAILED";
		}finally{
			return result;
		}
	}
}
