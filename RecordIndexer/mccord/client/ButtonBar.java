package client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonBar extends JPanel {

	private static final long serialVersionUID = 6247849090182044473L;

	public ButtonBar(ClientGUI cg){
		super();
		
//		JPanel buttonBar = new JPanel();
		this.setSize(new Dimension(this.getSize().width, 30));
		this.setPreferredSize(new Dimension(this.getSize().width, 30));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		this.setLayout(new GridBagLayout());
		
		
		JButton zoomInButton = new JButton("Zoom In");
		zoomInButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse click zoom in");
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		
		JButton zoomOutButton = new JButton("Zoom Out");
		zoomOutButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse click zoom out");
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		
		JButton invertImageButton = new JButton("Invert Image");
		invertImageButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse click invert image");
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		
		JButton toggleHighlightsButton = new JButton("Toggle Highlights");
		toggleHighlightsButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse click toggle highlights");
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		
		JButton saveButton = new JButton("Save");
		saveButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse click save");
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		
		JButton submitButton = new JButton("Submit");
		submitButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse click submit");
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(zoomInButton, c);
		c.gridx++;
		this.add(zoomOutButton, c);
		c.gridx++;
		this.add(invertImageButton, c);
		c.gridx++;
		this.add(toggleHighlightsButton, c);
		c.gridx++;
		this.add(saveButton, c);
		c.gridx++;
		this.add(submitButton, c);
		this.setVisible(true);
		
//		this.add(buttonBar);
//		this.setVisible(true);
		System.out.println("button bar ran");
	}
}
