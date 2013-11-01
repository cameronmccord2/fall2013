package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchResult.
 */
public class SearchResult {
	
	/** The batch id. */
	protected Integer batchId;
	
	/** The image url. */
	protected String imageUrl;
	
	/** The record number. */
	protected Integer recordNumber;
	
	/** The field id. */
	protected Integer fieldId;
	
	/*
	 * OUTPUT ::= <SEARCH_RESULT>* 
 
SEARCH_RESULT ::= 
<BATCH_ID>\n 
<IMAGE_URL>\n 
<RECORD_NUM>\n 
<FIELD_ID>\n 
 
BATCH_ID ::= Integer 
IMAGE_URL ::= URL 
RECORD_NUM ::= Integer (>= 1) 
FIELD_ID ::= Integer 

	 */
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(batchId);
		sb.append("\n");
		sb.append(imageUrl);
		sb.append("\n");
		sb.append(recordNumber);
		sb.append("\n");
		sb.append(fieldId);
		sb.append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Gets the batch id.
	 *
	 * @return the batch id
	 */
	public Integer getBatchId() {
		return batchId;
	}
	
	/**
	 * Sets the batch id.
	 *
	 * @param batchId the new batch id
	 */
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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
	 * Gets the record number.
	 *
	 * @return the record number
	 */
	public Integer getRecordNumber() {
		return recordNumber;
	}
	
	/**
	 * Sets the record number.
	 *
	 * @param recordNumber the new record number
	 */
	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
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
}
