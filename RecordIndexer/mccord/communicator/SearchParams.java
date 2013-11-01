package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchParams.
 */
public class SearchParams extends ValidateUserParams {
	
	/** The fields. */
	protected String fields;
	
	/** The search values. */
	protected String searchValues;
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(username);
		sb.append("\n");
		sb.append(password);
		sb.append("\n");
		sb.append(fields);
		sb.append("\n");
		sb.append(searchValues);
		sb.append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	public String getFields() {
		return fields;
	}
	
	/**
	 * Sets the fields.
	 *
	 * @param fields the new fields
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}
	
	/**
	 * Gets the search values.
	 *
	 * @return the search values
	 */
	public String getSearchValues() {
		return searchValues;
	}
	
	/**
	 * Sets the search values.
	 *
	 * @param searchValues the new search values
	 */
	public void setSearchValues(String searchValues) {
		this.searchValues = searchValues;
	}
}
