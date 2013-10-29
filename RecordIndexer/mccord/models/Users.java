package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Users.
 */
public class Users {
	
	/** The id. */
	protected Integer id;
	
	/** The first name. */
	protected String firstName;
	
	/** The last name. */
	protected String lastName;
	
	/** The email. */
	protected String email;
	
	/** The user name. */
	protected String userName;
	
	/** The password. */
	protected String password;
	
	/** The indexed records. */
	protected Integer indexedRecords;
	
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
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	
	/**
	 * Gets the indexed records.
	 *
	 * @return the indexed records
	 */
	public Integer getIndexedRecords() {
		return indexedRecords;
	}
	
	/**
	 * Sets the indexed records.
	 *
	 * @param indexedRecords the new indexed records
	 */
	public void setIndexedRecords(Integer indexedRecords) {
		this.indexedRecords = indexedRecords;
	}

	/*
	 * protected Integer id;
protected String firstName;
protected String lastName;
protected String email;
protected String userName;
protected String password;
protected Integer indexedRecords;
	 */
	public static ArrayList<Users> parseResultSet(ResultSet resultSet) {
		ArrayList<Users> responses = new ArrayList<Users>();
    	try{
			while(resultSet.next()){
				Users response = new Users();
				response.setId(resultSet.getInt("id"));
				response.setFirstName(resultSet.getString("firstName"));
				response.setLastName(resultSet.getString("lastName"));
				response.setEmail(resultSet.getString("email"));
				response.setUserName(resultSet.getString("username"));
				response.setPassword(resultSet.getString("passwrod"));
				responses.add(response);
			}
    	}catch(SQLException e){
    		throw new RuntimeException(e);
    	}
    	return responses;
	}
}
