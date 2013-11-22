package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class EntryView extends JFrame{

	private JList rowList;
	private ArrayList<ArrayList<String>> rows;
	private ArrayList<JTextField> textFields;
	private JScrollPane rowScrollPane;
	private JScrollPane formScrollPane;
	private Integer selectedRow;
	
	public EntryView(final ClientGUI cg){
		super("");
		
		
		// data model
		rows = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < cg.batch.getNumberOfRecords(); i++){
			rows.add(new ArrayList<String>());
			rows.get(i).add(new Integer(i + 1).toString());
			for(int j = 0; j < cg.batch.getNumberOfFields(); j++){
				rows.get(i).add("");
			}
		}
		
		JTabbedPane entryView = new JTabbedPane();
		entryView.setVisible(true);
		
		
		TableModel dataModel = new AbstractTableModel() {
	        public int getColumnCount() { return cg.batch.getNumberOfFields() + 1; }
	        public int getRowCount() { return cg.batch.getNumberOfRecords();}
	        public Object getValueAt(int row, int col) { return rows.get(row).get(col); }
	        public String getColumnName(int col){ return cg.batch.getFields().get(col).getTitle();}
	        public boolean isCellEditable(int row, int col){ 
	        	return (col != 0);
	        }
	        @SuppressWarnings("unused")
			public void setValueAt(String value, int row, int col){
	        	rows.get(row).set(col, value);
	        	fireTableCellUpdated(row, col);
	        }
		};
		JTable table = new JTable(dataModel);
		table.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse button pressed: " + e.getButton());
				if(e.getButton() == 3){// right click functionality
					
				}
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		table.getModel().addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int col = e.getColumn();
				TableModel model = (TableModel)e.getSource();
//				String columnName = model.getColumnName(col);
				String data = (String)model.getValueAt(row, col);
				// do checking here
				System.out.println("data changed for col: " + col + ", row: " + row + ", to: " + data);
			}
		});
		JScrollPane tableScrollPane = new JScrollPane(table);
		
		entryView.addTab("Table Entry", tableScrollPane);
		entryView.setMnemonicAt(0, KeyEvent.VK_1);
		
		
		
		
		
		
		
		
		
		class RowListListener implements ListSelectionListener {
			public void valueChanged(ListSelectionEvent e) {
		        if (e.getValueIsAdjusting() == false) {
		 
		            if (rowList.getSelectedIndex() == -1) {
			            //No selection
			            return;
		            } else {
		            	//Selection
		            	for(int i = 0; i < textFields.size(); i++){
		            		textFields.get(i).setText(rows.get(selectedRow).get(i));// load text from model into newly selected row
		            	}
//							BufferedImage myPicture = ImageIO.read(new URL("http://" + host + ":" + port + "/" + searchResults.get(resultsList.getSelectedIndex()).getImageUrl()));
							
		            }
		        }
		    }
	    }
		
		rowList = new JList(new DefaultListModel());
		for(int i = 0; i < cg.batch.getNumberOfRecords(); i++){
			((DefaultListModel)(rowList.getModel())).addElement("" + (i + 1));
		}
		
		rowList.addListSelectionListener(new RowListListener());
		rowList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowList.setVisibleRowCount(7);
		rowScrollPane = new JScrollPane(rowList);
		rowScrollPane.setSize(new Dimension(100, 200));
		rowScrollPane.setVisible(true);
		
		
		
		JPanel formPanel = new JPanel();
		formPanel.setPreferredSize(new Dimension(200, 200));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		formPanel.setLayout(new GridBagLayout());
		
		
		for(int i = 0; i < cg.batch.getFields().size(); i++){
			JLabel label = new JLabel(cg.batch.getFields().get(i).getTitle());
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = i;
			label.setVisible(true);
			formPanel.add(label, c);
			
			final JTextField textField = new JTextField();
			textField.setToolTipText(new Integer(i).toString());
			textField.getDocument().addDocumentListener(new DocumentListener(){
				@Override public void insertUpdate(DocumentEvent e) {}
				@Override public void removeUpdate(DocumentEvent e) {}
				@Override
				public void changedUpdate(DocumentEvent e) {
					Integer col = Integer.parseInt(textField.getToolTipText());// save text field changes to the data model
					rows.get(selectedRow).set(col, textField.getText());
				}
			});
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = i;
			formPanel.add(textField, c);
			textFields.add(textField);
		}
		
		formScrollPane = new JScrollPane(formPanel);
		formScrollPane.setSize(new Dimension(200, 200));
		formScrollPane.setVisible(true);
		rowList.setSelectedIndex(0);// load first round of data
		
		JSplitPane formEntry = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rowScrollPane, formScrollPane);
		formEntry.setResizeWeight(0.4d);
		entryView.addTab("Form Entry", formEntry);
		entryView.setMnemonicAt(1, KeyEvent.VK_2);
	}

}
