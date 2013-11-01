package communicator;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidateUserParams.
 */
public class ValidateUserParams {
	
	/** The username. */
	protected String username;
	
	/** The password. */
	protected String password;
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(username);
		sb.append("\n");
		sb.append(password);
		sb.append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
