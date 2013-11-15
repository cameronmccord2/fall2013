package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import communicator.GetFieldsParams;
import communicator.GetFieldsResult;
import communicator.GetProjectsResult;
import communicator.SearchParams;
import communicator.SearchResult;
import communicator.ValidateUserParams;
import server.ClientCommunicator;
import models.FailedResult;
import models.Projects;
import client.TextPrompt.Show;
import client.login.LoginGUI;
// jframe, jdialog, jpanel, jsplitframe, jcomponent-the thing to extend for custom, jtextfield, jpasswordfield, jlist, jscrollpane, jlabel, jbutton, bufferedImage, image
// events: mousePressed(MouseEvent event), mouseReleased, mouseClicked
//   listeners: mouseListener, keyPressListener, mouseMotionListener, actionListener(button), windowListener, windowStateListener, mouseWheelListener
// layout managers: flowLayout, gridBagLayout, boxLayout
public class SearchGUI extends JFrame implements ListSelectionListener{
	
	private static final long serialVersionUID = 4888473675568560947L;
	private String host;
	private String port;
	private JScrollPane projects;
	private JScrollPane fields;
	private JScrollPane results;
	private JList projectsList;
	private JList fieldsList;
	private JList resultsList;
	private JLabel picLabel;
	private JTextField searchField;
	private ArrayList<Integer> fieldIds;
	private ArrayList<GetProjectsResult> projectsResult;
	private ArrayList<GetFieldsResult> fieldsResult;
	private ArrayList<SearchResult> searchResults;
	private BufferedImage image;
	private ValidateUserParams vup;
	private Container pane;
	private JPanel centerPanel;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchGUI(String host1, String port1, String username, String password){
		super("Search GUI");
		
		fieldIds = new ArrayList<Integer>();
		vup = new ValidateUserParams();
		vup.setPassword(password);
		vup.setUsername(username);
		this.host = host1;
		this.port = port1;
		this.setPreferredSize(new Dimension(1000, 768));
		this.setMinimumSize(new Dimension(1000, 768));
		this.setMaximumSize(new Dimension(1000, 768));
		Dimension listDimension = new Dimension(this.getSize().width, this.getSize().height / 2);
		
		this.setLocationRelativeTo(null);// this sets relative to center of the jframe
		this.setVisible(true);
		
		pane = this.getContentPane();
		
		

		System.out.println("gothere");
		projectsResult = this.getProjects();
		System.out.println("size: " + projectsResult.size());
		DefaultListModel projectListModel = new DefaultListModel();
		for(int i = 0; i < projectsResult.size(); i++){
			projectListModel.addElement(projectsResult.get(i).toString());
		}
		
		class ProjectsListener implements ListSelectionListener {
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("projects listener hit");
		        if (e.getValueIsAdjusting() == false) {
		 
		            if (projectsList.getSelectedIndex() == -1) {
			            //No selection
			            return;
		            } else {
		            	//Selection
		            	fieldsList.setModel(new DefaultListModel());
		            	int projectId = projectsResult.get(projectsList.getSelectedIndex()).getId();
		            	fieldsResult = getFields(projectId);
		            	for(int i = 0; i < fieldsResult.size(); i++){
		        			((DefaultListModel) fieldsList.getModel()).addElement(fieldsResult.get(i).toString());
		        		}
		            	fieldsList.setSelectedIndex(0);
		            }
		        }
		    }
	    }
		
		this.projectsList = new JList(projectListModel);
		this.projectsList.addListSelectionListener(new ProjectsListener());
		System.out.println("got projects list");
		this.projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.projectsList.setVisibleRowCount(20);
		projects = new JScrollPane(this.projectsList);
		projects.setSize(listDimension);
		projects.setAlignmentX(LEFT_ALIGNMENT);
		
		
		
		class FieldsListener implements ListSelectionListener {
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("fieldslistener hit");
		        if (e.getValueIsAdjusting() == false) {
		 
		            if (fieldsList.getSelectedIndex() == -1) {
			            //No selection
			            return;
		            } else {
		            	//Selection
//		            	fieldsList.getsele
//		            	System.out.println("selected index: " + fieldsList.getSelectedIndex());
//		            	fieldIds.add(fieldsResult.get(fieldsList.getSelectedIndex()).getFieldId());
		            }
		        }
		    }
	    }
		
		fieldsList = new JList(new DefaultListModel());
		fieldsList.addListSelectionListener(new FieldsListener());
		fieldsResult = new ArrayList<GetFieldsResult>();
		if(projectsResult.size() > 0)
			fieldsResult = this.getFields(projectsResult.get(0).getId());
		for(int i = 0; i < fieldsResult.size(); i++){
			((DefaultListModel)fieldsList.getModel()).addElement(fieldsResult.get(i).toString());
		}
		fieldsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		fieldsList.setVisibleRowCount(20);
//		fieldsList.setSize(listDimension);
		fields = new JScrollPane(fieldsList);
		fields.setSize(listDimension);
		fields.setVisible(true);
		
		
		
		class ResultsListener implements ListSelectionListener {
			public void valueChanged(ListSelectionEvent e) {
		        if (e.getValueIsAdjusting() == false) {
		 
		            if (resultsList.getSelectedIndex() == -1) {
			            //No selection
			            return;
		            } else {
		            	//Selection
		            	
						try {
							System.out.println("getting image");
							BufferedImage myPicture = ImageIO.read(new URL("http://" + host + ":" + port + "/" + searchResults.get(resultsList.getSelectedIndex()).getImageUrl()));
							
							System.out.println("image: " + myPicture.getHeight() + ", " + myPicture.getWidth());
							
			            	
			            	
			            	int type = myPicture.getType() == 0? BufferedImage.TYPE_INT_ARGB : myPicture.getType();
			            	BufferedImage resizedImage = new BufferedImage(500, 500, type);
			            	Graphics2D g = resizedImage.createGraphics();
			            	g.drawImage(myPicture, 0, 0, 500, 500, null);
			            	g.dispose();
			            	picLabel = new JLabel(new ImageIcon(resizedImage));
							picLabel.setPreferredSize(new Dimension(500, 500));
			            	picLabel.setVisible(true);
			            	
			            	
			            	centerPanel.setVisible(false);
			            	pane.remove(centerPanel);
			            	centerPanel = new JPanel();
//			            	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
			            	centerPanel.setPreferredSize(new Dimension(500, 500));
			            	centerPanel.setVisible(true);
			            	centerPanel.add(picLabel);
			            	pane.add(centerPanel, BorderLayout.CENTER);
			            	System.out.println("added it to pane");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		            }
		        }
		    }
	    }
		
		resultsList = new JList(new DefaultListModel());
		resultsList.addListSelectionListener(new ResultsListener());
		searchResults = new ArrayList<SearchResult>();
		resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsList.setVisibleRowCount(20);
		results = new JScrollPane(resultsList);
		results.setSize(listDimension);
		results.setVisible(true);
		
		searchField = new JTextField();
		TextPrompt usernameTextPrompt = new TextPrompt("Search", searchField);
		usernameTextPrompt.changeAlpha(.75f);
		usernameTextPrompt.setShow(Show.FOCUS_LOST);
		searchField.setSize(new Dimension(this.getSize().width / 2, 20));
		searchField.setText("FOX");
		
		JButton searchButton = new JButton("Search");
		searchButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse click search");
				if(searchField.getText().length() > 0){
					String fieldIdsString = "";
					int[] fieldIdsArray = fieldsList.getSelectedIndices();
					if(!(fieldIdsArray.length > 0))
						return;
					for(int i = 0; i < fieldIdsArray.length; i++){
						System.out.println("index: " + fieldIdsArray[i] + ", fieldsResultSize: " + fieldsResult.size());
						fieldIdsString += fieldsResult.get(fieldIdsArray[i]).getFieldId();
						if(i != fieldIdsArray.length - 1)
							fieldIdsString += ",";
					}
					searchResults = search(fieldIdsString, searchField.getText());
					System.out.println(searchResults.size() + " " + fieldIdsString + " " + searchField.getText());
					resultsList.setModel(new DefaultListModel());
					for(int i = 0; i < searchResults.size(); i++){
						((DefaultListModel) resultsList.getModel()).addElement(searchResults.get(i).getImageUrl());
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
		});
		
		
//		image = new BufferedImage();
		
		JPanel leftPane = new JPanel();
		JPanel rightPane = new JPanel();
//		leftPane.setAlignmentX(LEFT_ALIGNMENT);
//		rightPane.setAlignmentX(LEFT_ALIGNMENT);
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.PAGE_AXIS));
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.PAGE_AXIS));
		leftPane.setSize(new Dimension(this.getSize().width / 2, this.getSize().height));
		rightPane.setSize(new Dimension(this.getSize().width / 2, this.getSize().height));
		leftPane.add(projects);
		rightPane.add(fields);
		rightPane.add(results);
		// image in center
		leftPane.setVisible(true);
		rightPane.setVisible(true);
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		northPanel.add(searchField);
		northPanel.add(searchButton);
		northPanel.setSize(new Dimension(this.getSize().width, 20));
		
		centerPanel = new JPanel();
		centerPanel.setSize(new Dimension(500, 500));
		centerPanel.setBackground(new Color(123, 123, 123));
		centerPanel.setVisible(true);
		
		pane.add(northPanel, BorderLayout.PAGE_START);
		pane.add(leftPane, BorderLayout.LINE_START);
		pane.add(centerPanel, BorderLayout.CENTER);
		pane.add(rightPane, BorderLayout.LINE_END);
		pane.setVisible(true);
		

		this.projectsList.setSelectedIndex(2);
	}
	
	@SuppressWarnings({ "unchecked", "finally" })
	private ArrayList<SearchResult> search(String fields, String values) {
		ClientCommunicator cc = new ClientCommunicator();
		SearchParams var = new SearchParams();
		ArrayList<SearchResult> result = new ArrayList<SearchResult>();
		var.setUsername(this.vup.getUsername());
		var.setPassword(this.vup.getPassword());
		var.setFields(fields);
		var.setSearchValues(values);
//		String result = "";
		try {
			Object response = cc.search(var, this.host, this.port);
			if(response instanceof ArrayList){
				result = (ArrayList<SearchResult>)response;
			}else if(response instanceof FailedResult){
				System.out.println("failedResult");
			}else
				System.out.println("somehting sel");
		} catch (Exception e) {
			System.out.println("exception");
		}finally{
			return result;
		}
	}
	
	@SuppressWarnings({ "unchecked", "finally" })
	private ArrayList<GetFieldsResult> getFields(Integer projectId) {
		ClientCommunicator cc = new ClientCommunicator();
		GetFieldsParams var = new GetFieldsParams();
		ArrayList<GetFieldsResult> result = new ArrayList<GetFieldsResult>();
//		String[] params = getView().getParameterValues();
		var.setUsername(this.vup.getUsername());
		var.setPassword(this.vup.getPassword());
		var.setProjectId(projectId);
//		if(params[2].length() > 0)
//			var.setProjectId(Integer.parseInt(params[2]));
//		else
//			var.setProjectId(0);
//		String result = "";
		try {
//			getView().setRequest(var.toString());
			Object response = cc.getFields(var, this.host, this.port);
			if(response instanceof ArrayList){
				result = (ArrayList<GetFieldsResult>)response;
			}else if(response instanceof FailedResult){
				System.out.println("failedResult");
			}else
				System.out.println("somethine else");
		} catch (Exception e) {
			System.out.println("exception");
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
	@SuppressWarnings({ "finally", "unchecked" })
	private ArrayList<GetProjectsResult> getProjects() {
		ArrayList<GetProjectsResult> result = null;
		ClientCommunicator cc = new ClientCommunicator();
		try {
			Object response = cc.getProjects(this.vup, this.host, this.port);
			if(response instanceof ArrayList){
				System.out.println("is array list");
				result = (ArrayList<GetProjectsResult>)response;
			}else if(response instanceof FailedResult){
				System.out.println("failedResult");
//				result = ((FailedResult)response).toString();
			}else
				System.out.println("something selse");
//			else
//				result = "something else" + response.getClass();
//			getView().setResponse(result);
		} catch (Exception e) {
			System.out.println("exception");
			e.printStackTrace();
//			result = "FAILED";
		}finally{
//			getView().setResponse(result);
			System.out.println("finally");
			return result;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		// TODO Auto-generated method stub
		
	}
	
}
