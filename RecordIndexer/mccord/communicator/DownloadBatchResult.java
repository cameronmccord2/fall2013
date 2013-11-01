package communicator;

import java.util.ArrayList;

import models.Fields;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadBatchResult.
 */
public class DownloadBatchResult {
	
	protected Integer batchId;
	
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
	
	public boolean equals(Object o){
		if(o instanceof DownloadBatchResult){
			DownloadBatchResult r = (DownloadBatchResult)o;
			return (r.toString().equals(this.toString()));
//			if(r.getImageUrl().equals(this.getImageUrl()) && r.getProjectId() == this.getProjectId() && r.getFirstYCoor() == this.getFirstYCoor() && r.getRecordHeight() == this.getRecordHeight() && 
//					r.getNumberOfRecords() == this.getNumberOfRecords() && r.getNumberOfFields() == this.getNumberOfFields()){
//				System.out.println("DBR good");
//				for(int i = 0; i < r.getFields().size(); i++){
//					if(!r.getFields().get(i).equals(this.getFields().get(i)))
//						return false;
//				}
//				return true;
//			}
		}
		return false;
	}
	
	/*
	 * OUTPUT ::= <BATCH_ID>\n 
<PROJECT_ID>\n 
<IMAGE_URL>\n 
<FIRST_Y_COORD>\n 
<RECORD_HEIGHT>\n 
<NUM_RECORDS>\n 
<NUM_FIELDS>\n 
<FIELD>+ 
 
FIELD ::= <FIELD_ID>\n 
<FIELD_NUM>\n 
<FIELD_TITLE>\n 
<HELP_URL>\n 
<X_COORD>\n 
<PIXEL_WIDTH>\n 
(<KNOWN_VALUES_URL>\n)? 
 
BATCH_ID ::= Integer 
PROJECT_ID ::= Integer 
FIELD_ID ::= Integer 
IMAGE_URL ::= URL 
FIRST_Y_COORD ::= Integer 
RECORD_HEIGHT ::= Integer 
NUM_RECORDS ::= Integer 
NUM_FIELDS ::= Integer 
FIELD_NUM ::= Integer (>= 1) 
FIELD_TITLE ::= String 
HELP_URL ::= URL 
X_COORD ::= Integer 
PIXEL_WIDTH ::= Integer 
KNOWN_VALUES_URL ::= URL 
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(batchId);
		sb.append("\n");
		sb.append(projectId);
		sb.append("\n");
		sb.append(imageUrl);
		sb.append("\n");
		sb.append(firstYCoor);
		sb.append("\n");
		sb.append(recordHeight);
		sb.append("\n");
		sb.append(numberOfRecords);
		sb.append("\n");
		sb.append(numberOfFields);
		sb.append("\n");
		for(Fields f : fields){
			sb.append(f.getPosition());
			sb.append("\n");
			sb.append(f.getTitle());
			sb.append("\n");
			sb.append(f.getHelpHtml());
			sb.append("\n");
			sb.append(f.getXcoor());
			sb.append("\n");
			sb.append(f.getWidth());
			sb.append("\n");
			if(f.getKnownData() != null){
				sb.append(f.getKnownData());
				sb.append("\n");
			}
			
		}
		
		return sb.toString();
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

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
}
