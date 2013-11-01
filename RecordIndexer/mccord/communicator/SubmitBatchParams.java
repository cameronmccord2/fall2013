package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class SubmitBatchParams.
 */
public class SubmitBatchParams extends ValidateUserParams {
	
	/** The batch id. */
	protected Integer batchId;
	
	/** The field values. */
	protected String fieldValues;
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(username);
		sb.append("\n");
		sb.append(password);
		sb.append("\n");
		sb.append(batchId);
		sb.append("\n");
		sb.append(fieldValues);
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
	 * Gets the field values.
	 *
	 * @return the field values
	 */
	public String getFieldValues() {
		return fieldValues;
	}
	
	/**
	 * Sets the field values.
	 *
	 * @param fieldValues the new field values
	 */
	public void setFieldValues(String fieldValues) {
		this.fieldValues = fieldValues;
	}
}
