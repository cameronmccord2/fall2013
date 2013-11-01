package communicator;

import communicator.ValidateUserParams;

// TODO: Auto-generated Javadoc
/**
 * The Class GetSampleImageParams.
 */
public class GetSampleImageParams extends ValidateUserParams {
	
	/** The project id. */
	protected Integer projectId;
	
	public String toString(){
		StringBuilder sb = new StringBuilder("");
		sb.append(username);
		sb.append("\n");
		sb.append(password);
		sb.append("\n");
		sb.append(projectId);
		sb.append("\n");
		
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
}
