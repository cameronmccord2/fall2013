package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidateUserResult.
 */
public class ValidateUserResult {
	
	/** The first name. */
	protected String firstName;
	
	/** The last name. */
	protected String lastName;
	
	/** The count. */
	protected Integer count;
	
	/*
	 * OUTPUT ::= TRUE\n 
<USER_FIRST_NAME>\n 
<USER_LAST_NAME>\n 
<NUM_RECORDS>\n 
 
USER_FIRST_NAME ::= String 
USER_LAST_NAME ::= String 
NUM_RECORDS ::= Integer 

	 */
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(firstName);
		sb.append("\n");
		sb.append(lastName);
		sb.append("\n");
		sb.append(count);
		sb.append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	
	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
}
