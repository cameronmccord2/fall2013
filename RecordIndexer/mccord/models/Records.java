package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Records.
 */
public class Records {
	
	/** The id. */
	protected Integer id;
	
	/** The image id. */
	protected Integer imageId;
	
	protected ArrayList<FieldValues> fieldValues;
	
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
	 * Gets the image id.
	 *
	 * @return the image id
	 */
	public Integer getImageId() {
		return imageId;
	}
	
	/**
	 * Sets the image id.
	 *
	 * @param imageId the new image id
	 */
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public static ArrayList<Records> parseResultSet(ResultSet resultSet) {
		ArrayList<Records> responses = new ArrayList<Records>();
    	try{
			while(resultSet.next()){
				Records response = new Records();
				response.setId(resultSet.getInt("id"));
				response.setImageId(resultSet.getInt("imageId"));
				responses.add(response);
			}
    	}catch(SQLException e){
    		throw new RuntimeException(e);
    	}
    	return responses;
	}

	public ArrayList<FieldValues> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(ArrayList<FieldValues> fieldValues) {
		this.fieldValues = fieldValues;
	}
}
