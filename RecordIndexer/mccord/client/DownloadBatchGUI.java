package client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import client.TextPrompt.Show;

public class DownloadBatchGUI extends JFrame {
	
	private void viewSample(){
		
	}
	
	private void cancel(){
		
	}
	
	private void download(){
		
	}

	public DownloadBatchGUI(ClientGUI cg){
		super("Download Batch");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension panelSize = new Dimension(400, 200);
		this.setSize(panelSize);
		this.setMinimumSize(panelSize);
		this.setMaximumSize(panelSize);
		Container pane = this.getContentPane();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.setLayout(new GridBagLayout());
		
		JLabel label = new JLabel("Project: ");
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		label.setVisible(true);
		pane.add(label);
		
		JComboBox projectList = new JComboBox();// add array of things here
		projectList.setSelectedIndex(0);
		projectList.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(projectList);
		
		JButton viewSampleButton = new JButton("View Sample");
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 0;
		viewSampleButton.setVisible(true);
		viewSampleButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				viewSample();// success pane
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		pane.add(viewSampleButton);
		
		JButton cancelButton = new JButton("Cancel");
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		cancelButton.setVisible(true);
		cancelButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				cancel();// success pane
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		pane.add(cancelButton);
		
		JButton downloadButton = new JButton("Cancel");
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		downloadButton.setVisible(true);
		downloadButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				download();// success pane
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		pane.add(downloadButton);
	}
}
