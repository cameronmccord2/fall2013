package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Images.
 */
public class Images {
	
	/** The id. */
	protected Integer id;
	
	/** The file. */
	protected String file;
	
	/** The project id. */
	protected Integer projectId; 	
	
	/** The user id. */
	protected Integer userId; 		
	
	/** The finished. */
	protected Integer finished;
	
	protected ArrayList<Records> records;
	
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
	 * Gets the file.
	 *
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
	
	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(String file) {
		this.file = file;
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
	
	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the finished.
	 *
	 * @return the finished
	 */
	public Integer getFinished() {
		return finished;
	}
	
	/**
	 * Sets the finished.
	 *
	 * @param finished the new finished
	 */
	public void setFinished(Integer finished) {
		this.finished = finished;
	}

	/*
	 * protected Integer id;
protected String file;
protected Integer projectId; 	
protected Integer userId; 		
protected Integer finished;
	 */
	public static ArrayList<Images> parseResultSet(ResultSet resultSet) {
		ArrayList<Images> responses = new ArrayList<Images>();
    	try{
			while(resultSet.next()){
				Images response = new Images();
				response.setId(resultSet.getInt("id"));
				response.setFile(resultSet.getString("file"));
				response.setProjectId(resultSet.getInt("projectId"));
				response.setUserId(resultSet.getInt("userId"));
				response.setFinished(resultSet.getInt("finished"));
				responses.add(response);
			}
    	}catch(SQLException e){
    		throw new RuntimeException(e);
    	}
    	return responses;
	}

	public ArrayList<Records> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<Records> records) {
		this.records = records;
	}
}
