package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import models.FailedResult;
import server.ClientCommunicator;
import communicator.GetSampleImageParams;
import communicator.GetSampleImageResult;

public class SampleImage extends JFrame {

	private static final long serialVersionUID = -5406111254112561349L;
	private ClientGUI cg;
	
	public SampleImage(String projectTitle, Integer projectId, ClientGUI cg){
		super("Sample image from " + projectTitle);
		this.cg = cg;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension panelSize = new Dimension(600, 700);
		this.setSize(panelSize);
		this.setMinimumSize(panelSize);
		this.setMaximumSize(panelSize);
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		
		try {
			System.out.println("getting image");
			GetSampleImageResult result = this.getSampleImage(projectId);
			BufferedImage myPicture = ImageIO.read(new URL(result.getUrl()));
			
			System.out.println("image: " + myPicture.getHeight() + ", " + myPicture.getWidth());
			
        	
        	
        	int type = myPicture.getType() == 0? BufferedImage.TYPE_INT_ARGB : myPicture.getType();
        	BufferedImage resizedImage = new BufferedImage(600, 600, type);
        	Graphics2D g = resizedImage.createGraphics();
        	g.drawImage(myPicture, 0, 0, 600, 600, null);
        	g.dispose();
        	JLabel picLabel = new JLabel(new ImageIcon(resizedImage));
			picLabel.setPreferredSize(new Dimension(600, 600));
        	picLabel.setVisible(true);
        	pane.add(picLabel, BorderLayout.CENTER);
        	
        	JButton closeButton = new JButton("Close");
        	closeButton.addMouseListener(new MouseListener(){
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				System.out.println("mouse click close");
    				setVisible(false);
    			}
    			@Override public void mousePressed(MouseEvent e) {}
    			@Override public void mouseReleased(MouseEvent e) {}
    			@Override public void mouseEntered(MouseEvent e) {}
    			@Override public void mouseExited(MouseEvent e) {}
    		});
    		
    		pane.add(closeButton, BorderLayout.PAGE_END);
        	
//        	centerPanel.setVisible(false);
//        	pane.remove(centerPanel);
//        	centerPanel = new JPanel();
////        	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
//        	centerPanel.setPreferredSize(new Dimension(600, 600));
//        	centerPanel.setVisible(true);
//        	centerPanel.add(picLabel);
//        	pane.add(centerPanel, BorderLayout.CENTER);
        	System.out.println("added it to pane");
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			pane.setVisible(true);
		}
	}
	
	@SuppressWarnings("finally")
	private GetSampleImageResult getSampleImage(Integer projectId) {
		ClientCommunicator cc = new ClientCommunicator();
		GetSampleImageParams var = new GetSampleImageParams();
		var.setUsername(this.cg.username);
		var.setPassword(this.cg.password);
		var.setProjectId(projectId);
		GetSampleImageResult result = null;
		try {
			Object response = cc.getSampleImage(var, this.cg.host, this.cg.port);
			if(response instanceof GetSampleImageResult){
				((GetSampleImageResult)response).setUrl("http://" + this.cg.host + ":" + this.cg.port + "/" + ((GetSampleImageResult)response).getUrl());
				result = (GetSampleImageResult)response;
			}else if(response instanceof FailedResult){
				System.out.println("failed to get sample image");
//				result = ((FailedResult)response).toString();
			}else
				System.out.println("something else get sample image");
//				result = "something else" + response.getClass();
		} catch (Exception e) {
			System.out.println("exception");
			e.printStackTrace();
		}finally{
			return result;
		}
	}
}
