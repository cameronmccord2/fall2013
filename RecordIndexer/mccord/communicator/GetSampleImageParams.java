package communicator;

import communicator.ValidateUserParams;

// TODO: Auto-generated Javadoc
/**
 * The Class GetSampleImageParams.
 */
public class GetSampleImageParams extends ValidateUserParams {
	
	/** The project id. */
	protected Integer projectId;

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
