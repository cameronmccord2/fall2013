package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class EntryView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3430815999308823613L;
	private JList rowList;
	private ArrayList<ArrayList<String>> rows;
	private ArrayList<JTextField> textFields;
	private JScrollPane rowScrollPane;
	private JScrollPane formScrollPane;
	private Integer selectedRow;
	private JTabbedPane entryView;
	private JSplitPane formEntrySplitPane;
	
	public void populateTabs(final ClientGUI cg){
		System.out.println(entryView.getTabCount());
		entryView.removeTabAt(0);
		entryView.removeTabAt(0);
		System.out.println("number of fields: " + cg.batch.getFields().size());
		// data model
		rows = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < cg.batch.getNumberOfRecords(); i++){
			rows.add(new ArrayList<String>());
			rows.get(i).add(new Integer(i + 1).toString());
			System.out.println("title: " + cg.batch.getFields().get(0).getTitle());
			for(int j = 0; j < cg.batch.getNumberOfFields(); j++){
				rows.get(i).add("");
				System.out.println("title: " + cg.batch.getFields().get(j+1).getTitle());
			}
			System.out.println("row generated, size: " + rows.get(i).size());
		}
		System.out.println("number of fields: " + cg.batch.getFields().size());
		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 8694837859047259727L;
			public int getColumnCount() { System.out.println("columns: " + cg.batch.getFields().size()); return cg.batch.getFields().size(); }
	        public int getRowCount() { return cg.batch.getNumberOfRecords();}
	        public Object getValueAt(int row, int col) {return rows.get(row).get(col); }
	        public String getColumnName(int col){ return cg.batch.getFields().get(col).getTitle();}
	        public boolean isCellEditable(int row, int col){ 
	        	return (col != 0);
	        }
			public void setValueAt(Object value, int row, int col){
	        	System.out.println("saving value for row: " + row + ", col: " + col);
	        	rows.get(row).set(col, (String)value);
	        	fireTableCellUpdated(row, col);
//	        	buildFormTab(cg);
	        }
		};
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(){
			private static final long serialVersionUID = -2531370875465975474L;
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component render = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//				render.setBackground(c);
				return this;
			}
			
		};
		final JTable table = new JTable(dataModel);
		table.setDefaultRenderer(String.class, renderer);// im not sure which class to put here
		table.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				cg.changeSelectedCell(table.getSelectedColumn(), table.getSelectedRow());
				if(e.getButton() == 3){// right click functionality
					System.out.println("right click recieved on table");
				}
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		table.addKeyListener(new KeyListener(){
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()){
				case KeyEvent.VK_TAB:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
					System.out.println("got keypress");
					cg.changeSelectedCell(table.getSelectedColumn(), table.getSelectedRow());
					break;
				}
			}
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
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setGridColor(Color.black);
		JScrollPane tableScrollPane = new JScrollPane(table);
		
		entryView.addTab("Table Entry", tableScrollPane);
		entryView.setMnemonicAt(0, KeyEvent.VK_1);
		
		
		rowList = new JList(new DefaultListModel());
		for(int i = 0; i < cg.batch.getNumberOfRecords(); i++){
			((DefaultListModel)(rowList.getModel())).addElement("" + (i + 1));
		}
		
		rowList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
		        if (e.getValueIsAdjusting() == false) {
		            if (rowList.getSelectedIndex() == -1) {
			            //No selection
			            return;
		            } else {
		            	//Selection
		            	selectedRow = rowList.getSelectedIndex();
		            	for(int i = 0; i < textFields.size(); i++){
		            		JTextField field = textFields.get(i);
		            		ArrayList<String> row = rows.get(selectedRow);
		            		String text = row.get(i+1);// skip the rownum column
		            		field.setText(text);// load text from model into newly selected row
		            	}
//							BufferedImage myPicture = ImageIO.read(new URL("http://" + host + ":" + port + "/" + searchResults.get(resultsList.getSelectedIndex()).getImageUrl()));
		            }
		        }
		    }
		});
		rowList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowList.setVisibleRowCount(7);
		rowScrollPane = new JScrollPane(rowList);
		rowScrollPane.setSize(new Dimension(100, 200));
		rowScrollPane.setVisible(true);
		
		this.buildFormTab(cg);
		rowList.setSelectedIndex(0);// load first round of data
		
		formEntrySplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rowScrollPane, formScrollPane);
		formEntrySplitPane.setResizeWeight(0.4d);
		entryView.addTab("Form Entry", formEntrySplitPane);
		entryView.setMnemonicAt(1, KeyEvent.VK_2);
		entryView.addChangeListener(new ChangeListener(){
			@Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    if(pane.getSelectedIndex() == 1){
                    	buildFormTab(cg);
                    }
                }
            }
		});
	}
	
	public void buildFormTab(ClientGUI cg){
		System.out.println("built form tab");
		JPanel formPanel = new JPanel();
//		formPanel.setPreferredSize(new Dimension(200, 200));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		formPanel.setLayout(new GridBagLayout());
		
		this.textFields = new ArrayList<JTextField>();
		for(int i = 1; i < cg.batch.getFields().size(); i++){
			JLabel label = new JLabel(cg.batch.getFields().get(i).getTitle());
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = i-1;
			label.setVisible(true);
			formPanel.add(label, c);
			
			final JTextField textField = new JTextField();
			textField.setText(rows.get(0).get(i));// this could be bad: 0
			textField.setToolTipText(new Integer(i).toString());
			textField.getDocument().addDocumentListener(new DocumentListener(){
				private void saveChanges(){
					System.out.println("field chagned to: " + textField.getText());
					Integer col = Integer.parseInt(textField.getToolTipText());// save text field changes to the data model
					rows.get(selectedRow).set(col, textField.getText());
				}
				@Override public void insertUpdate(DocumentEvent e) { this.saveChanges(); }
				@Override public void removeUpdate(DocumentEvent e) { this.saveChanges(); }
				@Override public void changedUpdate(DocumentEvent e) { this.saveChanges(); }
			});
			c.weightx = 2;
			c.gridx = 1;
			c.gridy = i-1;
			formPanel.add(textField, c);
			textFields.add(textField);
		}
		
		formScrollPane = new JScrollPane(formPanel);
		formScrollPane.setSize(new Dimension(200, 200));
		formScrollPane.setVisible(true);
		if(formEntrySplitPane != null)
			formEntrySplitPane.setRightComponent(formScrollPane);
	}
	
	public EntryView(final ClientGUI cg){
		super();
		
		this.setLayout(new BorderLayout());
		
		JScrollPane tempPane = new JScrollPane();
		tempPane.setBackground(Color.white);
		tempPane.setVisible(true);
		
		JScrollPane tempPane2 = new JScrollPane();
		tempPane2.setBackground(Color.white);
		tempPane2.setVisible(true);
		
		entryView = new JTabbedPane();
		
		entryView.addTab("Table Entry", tempPane2);

		entryView.addTab("Form Entry", tempPane);
		
		entryView.setPreferredSize(new Dimension());
		entryView.setVisible(true);
		
		this.add(entryView);
		System.out.println("build entry view ran");
	}

	public String getFieldValuesForSubmit() {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < this.rows.size(); i++){
			for(int j = 1; j < this.rows.get(i).size(); j++){
				sb.append(this.rows.get(i).get(j));
				if(j != this.rows.get(i).size() - 1)
					sb.append(",");
			}
			if(i != this.rows.size() - 1)
				sb.append(";");
		}
		return sb.toString();
	}

}
