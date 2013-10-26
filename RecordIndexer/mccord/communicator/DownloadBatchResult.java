package communicator;

import java.util.ArrayList;

import models.Fields;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadBatchResult.
 */
public class DownloadBatchResult {
	
	/** The project id. */
	protected Integer projectId;
	
	/** The image url. */
	protected String imageUrl;
	
	/** The first y coor. */
	protected Integer firstYCoor;
	
	/** The record height. */
	protected Integer recordHeight;
	
	/** The number of records. */
	protected Integer numberOfRecords;
	
	/** The number of fields. */
	protected Integer numberOfFields;
	
	/** The fields. */
	protected ArrayList<Fields> fields;
	
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
	 * Gets the image url.
	 *
	 * @return the image url
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	
	/**
	 * Sets the image url.
	 *
	 * @param imageUrl the new image url
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	
	/**
	 * Gets the number of records.
	 *
	 * @return the number of records
	 */
	public Integer getNumberOfRecords() {
		return numberOfRecords;
	}
	
	/**
	 * Sets the number of records.
	 *
	 * @param numberOfRecords the new number of records
	 */
	public void setNumberOfRecords(Integer numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	
	/**
	 * Gets the number of fields.
	 *
	 * @return the number of fields
	 */
	public Integer getNumberOfFields() {
		return numberOfFields;
	}
	
	/**
	 * Sets the number of fields.
	 *
	 * @param numberOfFields the new number of fields
	 */
	public void setNumberOfFields(Integer numberOfFields) {
		this.numberOfFields = numberOfFields;
	}
	
	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	public ArrayList<Fields> getFields() {
		return fields;
	}
	
	/**
	 * Sets the fields.
	 *
	 * @param fields the new fields
	 */
	public void setFields(ArrayList<Fields> fields) {
		this.fields = fields;
	}
}
