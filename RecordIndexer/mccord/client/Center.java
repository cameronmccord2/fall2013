package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class Center extends JPanel {

	private static final long serialVersionUID = -7502688847287553078L;
	private ClientGUI cg;
	private DrawingComponent component;
	
	public Center(ClientGUI cg){
		super();
		this.cg = cg;
		this.setBackground(Color.gray);
		this.setLayout(new BorderLayout());
	}

	public void loadImage(String imageUrl) {
		this.removeAll();
		this.component = new DrawingComponent(imageUrl, 0, cg.batch.getFirstYCoor(), cg.batch.getFields().get(1).getWidth(), cg.batch.getRecordHeight());   
		this.component.setVisible(true);
		this.add(component);
		component.addMouseWheelListener(new MouseWheelListener(){ 
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// TODO Auto-generated method stub
				if(e.getWheelRotation() < 0){
					// zoom out
					cg.zoomOut();
				}else{
					//zoom in
					cg.zoomIn();
				}
			}
		});
		this.component.setVisible(true);
	}
	
	public void adjustSquarePosition(int x, int y, Integer width, Integer height) {
		System.out.println("adjust sqare position center");
		this.component.adjustSquarePosition(x, y, width, height);
	}

	public void hideSquare(Integer lastColumn) {
		this.component.hideSquare(lastColumn);
	}
	
	public void showSquare(Integer lastColumn){
		this.component.showSquare(lastColumn);
	}

	public void toggleHighlights() {
		this.component.toggleHighlights();
	}

	public void zoomOut() {
		this.component.zoomOut();
	}

	public void invert() {
		this.component.invert();
	}

	public void zoomIn() {
		this.component.zoomIn();
	}

	public void updateLastColumn(int selectedColumn) {
		this.component.updateLastColumn(selectedColumn);
	}
}
