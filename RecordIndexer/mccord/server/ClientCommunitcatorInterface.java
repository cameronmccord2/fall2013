package server;

import communicator.DownloadBatchParams;
import communicator.DownloadFileParams;
import communicator.GetFieldsParams;
import communicator.GetSampleImageParams;
import communicator.SearchParams;
import communicator.SubmitBatchParams;
import communicator.ValidateUserParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface ClientCommunitcatorInterface.
 */
public interface ClientCommunitcatorInterface{
	
	/**
	 * Validate user.
	 *
	 * @param params the params
	 * @return the validate user result
	 * @throws InvalidCredentialsException the invalid credentials exception
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object validateUser(ValidateUserParams params, String host, String port) throws InvalidCredentialsException, FailedException, Exception;
	
	/**
	 * Gets the projects.
	 *
	 * @param params the params
	 * @return the projects
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object getProjects(ValidateUserParams params, String host, String port) throws FailedException, Exception;
	
	/**
	 * Gets the sample image.
	 *
	 * @param params the params
	 * @return the sample image
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object getSampleImage(GetSampleImageParams params, String host, String port) throws FailedException, Exception;
	
	/**
	 * Download batch.
	 *
	 * @param params the params
	 * @return the download batch result
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object downloadBatch(DownloadBatchParams params, String host, String port) throws FailedException, Exception;
	
	/**
	 * Submit batch.
	 *
	 * @param params the params
	 * @return the submit batch result
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object submitBatch(SubmitBatchParams params, String host, String port) throws FailedException, Exception;
	
	/**
	 * Gets the fields.
	 *
	 * @param params the params
	 * @return the fields
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object getFields(GetFieldsParams params, String host, String port) throws FailedException, Exception;
	
	/**
	 * Search.
	 *
	 * @param params the params
	 * @return the array list
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object search(SearchParams params, String host, String port) throws FailedException, Exception;
	
	/**
	 * Download file.
	 *
	 * @param params the params
	 * @return the download file result
	 * @throws FailedException the failed exception
	 * @throws Exception 
	 */
	public Object downloadFile(DownloadFileParams params, String host, String port) throws FailedException, Exception;
}
