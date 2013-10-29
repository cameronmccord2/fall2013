package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Projects.
 */
public class Projects {
	
	/** The id. */
	protected Integer id;
	
	/** The title. */
	protected String title;
	
	/** The records per image. */
	protected Integer recordsPerImage;
	
	/** The first y coor. */
	protected Integer firstYCoor;
	
	/** The record height. */
	protected Integer recordHeight;
	
	protected ArrayList<Fields> fields;
	protected ArrayList<Images> images;
	
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
	 * Gets the records per image.
	 *
	 * @return the records per image
	 */
	public Integer getRecordsPerImage() {
		return recordsPerImage;
	}
	
	/**
	 * Sets the records per image.
	 *
	 * @param recordsPerImage the new records per image
	 */
	public void setRecordsPerImage(Integer recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}
	
	/**
	 * Gets the first y coor.
	 *
	 * @return the first y coor
	 */
	public Integer getFirstYCoor() {
		return firstYCoor;
	}
	
	/**
	 * Sets the first y coor.
	 *
	 * @param firstYCoor the new first y coor
	 */
	public void setFirstYCoor(Integer firstYCoor) {
		this.firstYCoor = firstYCoor;
	}
	
	/**
	 * Gets the record height.
	 *
	 * @return the record height
	 */
	public Integer getRecordHeight() {
		return recordHeight;
	}
	
	/**
	 * Sets the record height.
	 *
	 * @param recordHeight the new record height
	 */
	public void setRecordHeight(Integer recordHeight) {
		this.recordHeight = recordHeight;
	}
/*
 * protected Integer id;
protected String title;
protected Integer recordsPerImage;
protected Integer firstYCoor;
protected Integer recordHeight;
 */
	public static ArrayList<Projects> parseResultSet(ResultSet resultSet) {
		ArrayList<Projects> responses = new ArrayList<Projects>();
    	try{
			while(resultSet.next()){
				Projects response = new Projects();
				response.setId(resultSet.getInt("id"));
				response.setFirstYCoor(resultSet.getInt("firstYCoor"));
				response.setTitle(resultSet.getString("title"));
				response.setRecordHeight(resultSet.getInt("recordHeight"));
				response.setRecordsPerImage(resultSet.getInt("recordsPerImage"));
				responses.add(response);
			}
    	}catch(SQLException e){
    		throw new RuntimeException(e);
    	}
    	return responses;
	}

	public ArrayList<Fields> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Fields> fields) {
		this.fields = fields;
	}

	public ArrayList<Images> getImages() {
		return images;
	}

	public void setImages(ArrayList<Images> images) {
		this.images = images;
	}
}
