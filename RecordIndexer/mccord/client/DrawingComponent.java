package client;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import java.util.*;
import java.io.*;
import java.net.URL;


public class DrawingComponent extends JComponent {

	private static final long serialVersionUID = -7538117157602255713L;

	private static BufferedImage NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
//	private ArrayList<DrawingShape> shapes;
	private ArrayList<DrawingShape> dragShapes;
	private Point2D lastPoint;
	private DrawingShape square;
	private DrawingShape imageShape;
	private BufferedImage image;
	private boolean squareOn;
	private boolean invertOn;
	private Integer lastColumn;
	private BufferedImage originalImage;
	private double scale;
//	private Font font;
	
	public DrawingComponent(String imageUrl, Integer x, Integer y, Integer width, Integer height) {
		System.out.println("drawing componnt hit");
		this.lastColumn = 1;
		this.squareOn = true;
		this.scale = 1.0;
		this.invertOn = false;
		
//		shapes = new ArrayList<DrawingShape>();
		dragShapes = new ArrayList<DrawingShape>();
		
//		font = new Font("SansSerif", Font.PLAIN, 72);
		this.originalImage = loadImage(imageUrl);
		this.image = this.originalImage;
		
		this.setBackground(new Color(178, 223, 210));
		this.setPreferredSize(new Dimension(600, 600));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		this.addKeyListener(keyAdapter);
		
		Integer leftPad = (1200 - image.getWidth(null))/2;
		Integer topPad = (800 - image.getHeight(null))/2;
		this.imageShape = new DrawingImage(image, new Rectangle2D.Double(leftPad, topPad, image.getWidth(null), image.getHeight(null)));
		this.drawSquare(x + leftPad + 10000, y + topPad + 10000, width, height);// move it off screen initially
//		Image spongebob = loadImage("spongebob.jpg");
//		shapes.add(new DrawingImage(spongebob, new Rectangle2D.Double(50, 250, spongebob.getWidth(null) * 2, spongebob.getHeight(null) * 2)));
		
		
//		shapes.add(new DrawingLine(new Line2D.Double(400, 400, 600, 600), new Color(255, 0, 0, 64)));
		
//		createTextShapes();
		
		this.setVisible(true);
	}
	
	public void drawSquare(Integer x, Integer y, Integer width, Integer height){
		square = new DrawingRect(new Rectangle2D.Double(x, y, width, height), new Color(210, 180, 140, 192));
	}
	
//	private void createTextShapes() {
//		String text1 = "Width: " + this.getWidth();
//		shapes.add(new DrawingText(text1, font, Color.BLACK, new Point2D.Float(200, 200)));
//		
//		String text2 = "Height: " + this.getHeight();
//		shapes.add(new DrawingText(text2, font, Color.BLACK, new Point2D.Float(200, 400)));
//	}
	
//	private void updateTextShapes() {
//		for (DrawingShape shape : shapes) {
//			if (shape instanceof DrawingText) {
//				DrawingText textShape = (DrawingText)shape;
//				if (textShape.getText().startsWith("Width:")) {
//					textShape.setText("Width: " + this.getWidth());
//				}
//				else if (textShape.getText().startsWith("Height:")) {
//					textShape.setText("Height: " + this.getHeight());
//				}
//			}
//		}
//	}
	
	
	
	private void adjustShapePositions(double dx, double dy) {
		this.imageShape.adjustPosition(dx, dy);
		this.square.adjustPosition(dx, dy);
		this.repaint();
	}
	
	private BufferedImage loadImage(String imageUrl) {
		try {
			return ImageIO.read(new URL(imageUrl));
		}
		catch (IOException e) {
			return NULL_IMAGE;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
//		System.out.println("paint components hit");
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		this.imageShape.draw(g2);
		if(this.squareOn && this.lastColumn != 0){
			this.square.draw(g2);
			System.out.println("drew square**********************");
		}else
			System.out.println("didnt draw square: " + this.squareOn + ", " + this.lastColumn);
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {
			
			int dx = e.getX() - (int)lastPoint.getX();
			int dy = e.getY() - (int)lastPoint.getY();
			
			for (DrawingShape s : dragShapes) {
				s.adjustPosition(dx, dy);
			}
			
			lastPoint = new Point2D.Double(e.getX(), e.getY());
			
			DrawingComponent.this.repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			dragShapes.clear();
			dragShapes.add(imageShape);
			dragShapes.add(square);
			
			lastPoint = new Point2D.Double(e.getX(), e.getY());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			dragShapes.clear();
			lastPoint = null;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int scrollAmount = 0;
			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				scrollAmount = e.getUnitsToScroll();
			} else {
				scrollAmount = e.getWheelRotation();
			}
			
			int dx = 0;
			int dy = 0;
			if (e.isShiftDown()) {
				dx = scrollAmount;
				dy = 0;
			}
			else {
				dx = 0;
				dy = scrollAmount;
			}
			
			adjustShapePositions(dx, dy);
		}	
	};
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {
		@Override
		public void componentHidden(ComponentEvent e) {
			return;
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			return;
		}

		@Override
		public void componentResized(ComponentEvent e) {
			return;
		}

		@Override
		public void componentShown(ComponentEvent e) {
			return;
		}	
	};

	private KeyAdapter keyAdapter = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				adjustShapePositions(-1, 0);
			}else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				adjustShapePositions(1, 0);
			}else if (e.getKeyCode() == KeyEvent.VK_UP) {
				adjustShapePositions(0, -1);
			}else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				adjustShapePositions(0, 1);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			return;
		}
		@Override
		public void keyTyped(KeyEvent e) {
			return;
		}	
	};

	public void hideSquare(Integer lastColumn) {
		this.lastColumn = lastColumn;
		System.out.println("square is hidden");
		this.squareOn = false;
		this.repaint();
	}

	public void showSquare(Integer lastColumn) {
		this.lastColumn = lastColumn;
		System.out.println("showing square");
		this.squareOn = true;
		this.repaint();
	}

	public void toggleHighlights() {
		this.squareOn = !this.squareOn;
		this.repaint();
	}

	public void zoomIn() {
		System.out.println("zoom in hit");
		this.scale *= 1.1d;
		this.zoom();
//		this.repaint();
	}

	public void zoomOut() {
		System.out.println("zoom out hit");
		this.scale *= 0.9d;
		this.zoom();
//		this.repaint();
	}
	
	private void zoom(){
		if(this.invertOn)
			this.image = this.invertImage(this.originalImage);
		else
			this.image = this.originalImage;
		
		AffineTransform transform = AffineTransform.getScaleInstance(this.scale, this.scale);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		this.image = op.filter(this.image, null);
//		Integer x = this.imageShape.getX()
//		Integer y = 
//		Integer width = 
//		Integer height = 
		Integer dx = (int) ((this.imageShape.getWidth() - this.image.getWidth())/2);
		Integer dy = (int) ((this.imageShape.getHeight() - this.image.getHeight())/2);
		this.imageShape = new DrawingImage(this.image, new Rectangle2D.Double(this.imageShape.getX(), this.imageShape.getY(), this.image.getWidth(), this.image.getHeight()));
		this.imageShape.adjustPosition(dx, dy);
		Integer squareX = (int) (this.square.getX() + dx);
		Integer squareY = (int) (this.square.getY() + dy);
		Integer squareWidth = (int) (this.square.getWidth() - dx);
		Integer squareHeight = (int) (this.square.getHeight() - dy);
		this.square.setPosition(squareX, squareY, squareWidth, squareHeight);
		this.repaint();
	}
	
	public void adjustSquarePosition(double x, double y, Integer width, Integer height){
		System.out.println("adjust square position drawing component, x: " + x + ", y: " + y);
		Integer leftPad = (1200 - image.getWidth(null))/2;
		Integer topPad = (800 - image.getHeight(null))/2;
		System.out.println(leftPad + " " + topPad);
		this.square.setPosition(x + leftPad, y + topPad, width, height);
		this.zoom();
	}
	
	private BufferedImage invertImage(final BufferedImage src) {
		RescaleOp op = new RescaleOp(-1.0f, 255f, null);
		BufferedImage negative = op.filter(src, null);
		return negative;
	}

	public void invert() {
		this.invertOn = !this.invertOn;
		System.out.println("invert hit");
		this.zoom();
	}

	public void updateLastColumn(int selectedColumn) {
		this.lastColumn = selectedColumn;
		this.repaint();
	}
}


interface DrawingShape {
	boolean contains(Graphics2D g2, double x, double y);
	double getHeight();
	double getWidth();
	void adjustPosition(double dx, double dy);
	void draw(Graphics2D g2);
	void setPosition(double x, double y, Integer width, Integer height);
	double getX();
	double getY();
}


class DrawingRect implements DrawingShape {

	private Rectangle2D rect;
	private Color color;
	
	public DrawingRect(Rectangle2D rect, Color color) {
		this.rect = rect;
		this.color = color;
	}

	@Override
	public boolean contains(Graphics2D g2, double x, double y) {
		return rect.contains(x, y);
	}

	@Override
	public void adjustPosition(double dx, double dy) {
		rect.setRect(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());	
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(rect);
		// OR g2.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
	}

	@Override
	public void setPosition(double x, double y, Integer width, Integer height) {
		rect.setRect(x, y, width, height);
	}

	@Override
	public double getX() {
		return this.rect.getX();
	}

	@Override
	public double getY() {
		return this.rect.getY();
	}

	@Override
	public double getHeight() {
		return this.rect.getHeight();
	}

	@Override
	public double getWidth() {
		return this.rect.getWidth();
	}	
}


class DrawingImage implements DrawingShape {

	private Image image;
	private Rectangle2D rect;
//	private double scale;
	
	public DrawingImage(BufferedImage image, Rectangle2D rect) {
		this.image = image;
		this.rect = rect;
//		this.scale = 1.0;
	}
	
	public double getX(){
		return this.rect.getX();
	}
	
	public double getY(){
		return this.rect.getY();
	}

	@Override
	public boolean contains(Graphics2D g2, double x, double y) {
		return rect.contains(x, y);
	}

	@Override
	public void adjustPosition(double dx, double dy) {
		rect.setRect(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());	
	}

	@Override
	public void draw(Graphics2D g2) {
		Rectangle2D bounds = rect.getBounds2D();
//		g2.scale(this.scale, this.scale);
//		this.scale += 0.1d;
//		g2.transl
		g2.drawImage(image, (int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getMaxX(), (int)bounds.getMaxY(), 0, 0, image.getWidth(null), image.getHeight(null), null);
	}

	@Override
	public void setPosition(double x, double y, Integer width, Integer height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getHeight() {
		return this.rect.getHeight();
	}

	@Override
	public double getWidth() {
		return this.rect.getWidth();
	}	
}


//class DrawingText implements DrawingShape {
//
//	private String text;
//	private Font font;
//	private Color color;
//	private Point2D location;
//	
//	public DrawingText(String text, Font font, Color color, Point2D location) {
//		this.text = text;
//		this.font = font;
//		this.color = color;
//		this.location = location;
//	}
//
//	@Override
//	public boolean contains(Graphics2D g2, double x, double y) {
//		FontRenderContext context = g2.getFontRenderContext();
//		Rectangle2D bounds = font.getStringBounds(text, context);
//		bounds.setRect(location.getX(), location.getY() + bounds.getY(), 
//						bounds.getWidth(), bounds.getHeight());
//		return bounds.contains(x, y);
//	}
//
//	@Override
//	public void adjustPosition(double dx, double dy) {
//		double newX = location.getX() + dx;
//		double newY = location.getY() + dy;
//		location.setLocation(newX, newY);
//	}
//
//	@Override
//	public void draw(Graphics2D g2) {
//		g2.setColor(color);
//		g2.setFont(font);
//		g2.drawString(text, (int)location.getX(), (int)location.getY());
//	}
//	
//	public String getText() {
//		return text;
//	}
//	
//	public void setText(String value) {
//		text = value;
//	}
//
//	@Override
//	public void setPosition(double x, double y) {
//		
//	}
//}

//class DrawingLine implements DrawingShape {
//
//	private Line2D line;
//	private Color color;
//	
//	public DrawingLine(Line2D rect, Color color) {
//		this.line = rect;
//		this.color = color;
//	}
//
//	@Override
//	public boolean contains(Graphics2D g2, double x, double y) {
//
//		final double TOLERANCE = 5.0;
//		
//		Point2D p1 = line.getP1();
//		Point2D p2 = line.getP2();
//		Point2D p3 = new Point2D.Double(x, y);
//		
//		double numerator = (p3.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p3.getY() - p1.getY()) * (p2.getY() - p1.getY());
//		double denominator =  p2.distance(p1) * p2.distance(p1);
//		double u = numerator / denominator;
//		
//		if (u >= 0.0 && u <= 1.0) {
//			Point2D pIntersection = new Point2D.Double(	p1.getX() + u * (p2.getX() - p1.getX()),
//														p1.getY() + u * (p2.getY() - p1.getY()));
//			
//			double distance = pIntersection.distance(p3);
//			
//			return (distance <= TOLERANCE);
//		}
//		
//		return false;
//	}
//
//	@Override
//	public void adjustPosition(double dx, double dy) {
//		line.setLine(line.getX1() + dx, line.getY1() + dy, line.getX2() + dx, line.getY2() + dy);	
//	}
//
//	@Override
//	public void draw(Graphics2D g2) {
//		g2.setColor(color);
//		g2.setStroke(new BasicStroke(5));
//		g2.draw(line);
//		// OR g2.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
//	}
//
//	@Override
//	public void setPosition(double x, double y, Integer width) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public double getX() {
//		return this.line.getX1();
//	}
//
//	@Override
//	public double getY() {
//		return this.line.getY1();
//	}
//
//	@Override
//	public double getHeight() {
//		return 0;
//	}
//
//	@Override
//	public double getWidth() {
//		return 0;
//	}	
//}

