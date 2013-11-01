package communicator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Users;

// TODO: Auto-generated Javadoc
/**
 * The Class GetProjectsResult.
 */
public class GetProjectsResult {
	
	/** The title. */
	protected String title;
	
	/** The id. */
	protected Integer id;
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(id);
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

	public static ArrayList<GetProjectsResult> parseResultSet(ResultSet resultSet) {
		ArrayList<GetProjectsResult> responses = new ArrayList<GetProjectsResult>();
    	try{
			while(resultSet.next()){
				GetProjectsResult response = new GetProjectsResult();
				response.setId(resultSet.getInt("id"));
				response.setTitle(resultSet.getString("title"));
				responses.add(response);
			}
    	}catch(SQLException e){
    		throw new RuntimeException(e);
    	}
    	return responses;
	}
}
