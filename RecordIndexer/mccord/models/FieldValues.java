package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class FieldValues.
 */
public class FieldValues {
	
	/** The id. */
	protected Integer id;
	
	/** The record id. */
	protected Integer recordId;
	
	/** The field id. */
	protected Integer fieldId;
	
	/** The value. */
	protected String value;
	
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
	 * Gets the record id.
	 *
	 * @return the record id
	 */
	public Integer getRecordId() {
		return recordId;
	}
	
	/**
	 * Sets the record id.
	 *
	 * @param recordId the new record id
	 */
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	
	/**
	 * Gets the field id.
	 *
	 * @return the field id
	 */
	public Integer getFieldId() {
		return fieldId;
	}
	
	/**
	 * Sets the field id.
	 *
	 * @param fieldId the new field id
	 */
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * protected Integer id;
protected Integer recordId;
protected Integer fieldId;
protected String value;
	 */
	public static ArrayList<FieldValues> parseResultSet(ResultSet resultSet) {
		ArrayList<FieldValues> responses = new ArrayList<FieldValues>();
    	try{
			while(resultSet.next()){
				FieldValues response = new FieldValues();
				response.setId(resultSet.getInt("id"));
				response.setRecordId(resultSet.getInt("recordId"));
				response.setFieldId(resultSet.getInt("fieldId"));
				response.setValue(resultSet.getString("value"));
				responses.add(response);
			}
    	}catch(SQLException e){
    		throw new RuntimeException(e);
    	}
    	return responses;
	}
}
