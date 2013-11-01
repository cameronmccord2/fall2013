package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.JDBCRecordIndexerDAO;

// TODO: Auto-generated Javadoc
/**
 * The Class Fields.
 */
public class Fields {
	
	/** The id. */
	protected Integer id;			
	
	/** The position. */
	protected Integer position;	
	
	/** The title. */
	protected String title; 		
	
	/** The xcoor. */
	protected Integer xcoor;		
	
	/** The width. */
	protected Integer width; 		
	
	/** The help html. */
	protected String helpHtml; 	
	
	/** The known data. */
	protected String knownData; 	
	
	/** The project id. */
	protected Integer projectId;
	
	public boolean equals(Object o){
		if(o instanceof Fields){
			Fields r = (Fields)o;
			return (r.toString().equals(this.toString()));
//			return (r.getTitle().equals(this.getTitle()) && r.getHelpHtml().equals(this.getHelpHtml()) && r.getKnownData().equals(this.getKnownData()) && r.getId() == this.getId() && r.getPosition() == this.getPosition() && r.getXcoor() == this.getXcoor() && r.getWidth() == this.getWidth() && r.getProjectId() == this.getProjectId());
		}
		return false;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}
	
	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Gets the xcoor.
	 *
	 * @return the xcoor
	 */
	public Integer getXcoor() {
		return xcoor;
	}
	
	/**
	 * Sets the xcoor.
	 *
	 * @param xcoor the new xcoor
	 */
	public void setXcoor(Integer xcoor) {
		this.xcoor = xcoor;
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	/**
	 * Gets the help html.
	 *
	 * @return the help html
	 */
	public String getHelpHtml() {
		return helpHtml;
	}
	
	/**
	 * Sets the help html.
	 *
	 * @param helpHtml the new help html
	 */
	public void setHelpHtml(String helpHtml) {
		this.helpHtml = helpHtml;
	}
	
	/**
	 * Gets the known data.
	 *
	 * @return the known data
	 */
	public String getKnownData() {
		return knownData;
	}
	
	/**
	 * Sets the known data.
	 *
	 * @param knownData the new known data
	 */
	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}
	
	/**
	 * Gets the project id.
	 *
	 * @return the project id
	 */
	public Integer getProjectId() {
		return projectId;
	}
	
	/**
	 * Sets the project id.
	 *
	 * @param projectId the new project id
	 */
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	/*
	 * protected Integer id;			
protected Integer position;	
protected String title; 		
protected Integer xcoor;		
protected Integer width; 		
protected String helpHtml; 	
protected String knownData; 	
protected Integer projectId;
	 */
	public static ArrayList<Fields> parseResultSet(ResultSet resultSet) {
		ArrayList<Fields> responses = new ArrayList<Fields>();
		JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
    	try{
			while(resultSet.next()){
				Fields response = new Fields();
				response.setId(resultSet.getInt("id"));
				response.setPosition(resultSet.getInt("position"));
				response.setTitle(resultSet.getString("title"));
				response.setXcoor(resultSet.getInt("xcoor"));
				response.setWidth(resultSet.getInt("width"));
				response.setHelpHtml(resultSet.getString("helpHtml"));
				if(response.getHelpHtml() != null)
					response.setHelpHtml(dao.getPartialUrlForImageFile(response.getHelpHtml()));
				response.setKnownData(resultSet.getString("knownData"));
				if(response.getKnownData() != null)
					response.setKnownData(dao.getPartialUrlForImageFile(response.getKnownData()));
				response.setProjectId(resultSet.getInt("projectId"));
				responses.add(response);
			}
    	}catch(SQLException e){
    		throw new RuntimeException(e);
    	}
    	return responses;
	}
}
