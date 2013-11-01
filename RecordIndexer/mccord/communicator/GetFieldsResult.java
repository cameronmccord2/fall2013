package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class GetFieldsResult.
 */
public class GetFieldsResult {
	
	/** The title. */
	protected String title;
	
	/** The project id. */
	protected Integer projectId;
	
	/** The field id. */
	protected Integer fieldId;
	
	/*
	 * OUTPUT ::= <FIELD_INFO>+ 
 
FIELD_INFO ::= <PROJECT_ID>\n 
<FIELD_ID>\n 
<FIELD_TITLE>\n 
 
PROJECT_ID ::= Integer 
FIELD_ID ::= Integer 
FIELD_TITLE ::= String 

	 */
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(projectId);
		sb.append("\n");
		sb.append(fieldId);
		sb.append("\n");
		sb.append(title);
		sb.append("\n");
		
		return sb.toString();
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
