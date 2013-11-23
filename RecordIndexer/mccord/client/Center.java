package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Center extends JPanel {
	
	private ClientGUI cg;
	private JLabel picLabel;
	private ArrayList<DrawingShape> shapes;
	private static BufferedImage NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	private DrawingComponent component;
	
	public Center(ClientGUI cg){
		super();
		this.cg = cg;
//		this.setVisible(true);
		this.setBackground(Color.gray);
		this.setLayout(new BorderLayout());
		this.shapes = new ArrayList<DrawingShape>();
	}

	public void loadImage(String imageUrl) {
		this.removeAll();
		this.component = new DrawingComponent(imageUrl, 0, cg.batch.getFirstYCoor(), cg.batch.getFields().get(1).getWidth(), cg.batch.getRecordHeight());   
		this.component.setVisible(true);
		this.add(component);
//		this.setBackground(Color.cyan);
		this.component.setVisible(true);
//		this.setLocation(50, 50);
		
//		this.shapes = new ArrayList<DrawingShape>();
//		System.out.println("getting image");
//		BufferedImage myPicture = this.loadImageFromURL(imageUrl);
//		
//		System.out.println("image: " + myPicture.getHeight() + ", " + myPicture.getWidth());
//		
//		this.shapes.add(new DrawingImage(myPicture, new Rectangle2D.Double(50, 50, myPicture.getWidth(), myPicture.getHeight())));
		
		
//		int type = myPicture.getType() == 0? BufferedImage.TYPE_INT_ARGB : myPicture.getType();
//		BufferedImage resizedImage = new BufferedImage(600, 600, type);
//		Graphics2D g = resizedImage.createGraphics();
//		g.drawImage(myPicture, 0, 0, 600, 600, null);
//		g.dispose();
//		picLabel = new JLabel(new ImageIcon(resizedImage));
//		picLabel.setPreferredSize(new Dimension(600, 600));
//		picLabel.setBackground(Color.red);
//		picLabel.setVisible(true);
//		
//		this.removeAll();
//		
//		JPanel imageFrame = new JPanel();
//		imageFrame.add(picLabel);
//		imageFrame.setBackground(Color.pink);
//		imageFrame.setVisible(true);
//		imageFrame.setPreferredSize(new Dimension(600, 600));
//		
//		this.add(imageFrame, BorderLayout.CENTER);
//		this.setVisible(true);
//		
//		System.out.println("added it to pane");
	}
	
	private WindowAdapter windowAdapter = new WindowAdapter() {

		@Override
		public void windowActivated(WindowEvent e) {
			return;
		}

		@Override
		public void windowClosed(WindowEvent e) {
			return;
		}

		@Override
		public void windowClosing(WindowEvent e) {
			return;
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			return;
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			return;
		}

		@Override
		public void windowGainedFocus(WindowEvent e) {
			component.requestFocusInWindow();
		}

		@Override
		public void windowIconified(WindowEvent e) {
			return;
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			return;
		}

		@Override
		public void windowOpened(WindowEvent e) {
			return;
		}

		@Override
		public void windowStateChanged(WindowEvent e) {
			return;
		}
		
	};

	public void adjustSquarePosition(int x, int y) {
		System.out.println("adjust sqare position center");
		this.component.adjustSquarePosition(x, y);
	}
	
//	private BufferedImage loadImageFromURL(String imageUrl) {
//		try {
//			return ImageIO.read(new URL(imageUrl));
//		}catch (IOException e) {
//			return NULL_IMAGE;
//		}
//	}
//
//	@Override
//	protected void paintComponent(Graphics g) {
//		System.out.println("paint component hit");
//		super.paintComponent(g);
//		
//		Graphics2D g2 = (Graphics2D)g;
//		drawBackground(g2);
//		drawShapes(g2);
//	}
//	
//	private void drawBackground(Graphics2D g2) {
//		g2.setColor(getBackground());
//		g2.fillRect(0,  0, getWidth(), getHeight());
//	}
//
//	private void drawShapes(Graphics2D g2) {
//		for (DrawingShape shape : this.shapes) {
//			shape.draw(g2);
//		}
//	}
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	interface DrawingShape {
//		void draw(Graphics2D g2);
//	}
//	
//	class DrawingImage implements DrawingShape {
//
//		private BufferedImage image;
//		private Rectangle2D rect;
//		
//		public DrawingImage(BufferedImage image, Rectangle2D rect) {
//			this.image = image;
//			this.rect = rect;
//		}
//
//		@Override
//		public void draw(Graphics2D g2) {
//			Rectangle2D bounds = rect.getBounds2D();
//			g2.drawImage(image, (int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getMaxX(), (int)bounds.getMaxY(),
//							0, 0, image.getWidth(null), image.getHeight(null), null);
//		}	
//	}
//	
//	class DrawingRect implements DrawingShape {
//
//		private Rectangle2D rect;
//		private Color color;
//		
//		public DrawingRect(Rectangle2D rect, Color color) {
//			this.rect = rect;
//			this.color = color;
//		}
//
//		@Override
//		public void draw(Graphics2D g2) {
//			g2.setColor(color);
//			g2.fill(rect);
//			// OR g2.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
//		}	
//	}
}
