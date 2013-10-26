package server;

import java.util.ArrayList;

import communicator.DownloadBatchParams;
import communicator.DownloadBatchResult;
import communicator.DownloadFileParams;
import communicator.DownloadFileResult;
import communicator.GetFieldsParams;
import communicator.GetFieldsResult;
import communicator.GetProjectsResult;
import communicator.GetSampleImageParams;
import communicator.GetSampleImageResult;
import communicator.SearchParams;
import communicator.SearchResult;
import communicator.SubmitBatchParams;
import communicator.SubmitBatchResult;
import communicator.ValidateUserParams;
import communicator.ValidateUserResult;

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
	 */
	public ValidateUserResult validateUser(ValidateUserParams params) throws InvalidCredentialsException, FailedException;
	
	/**
	 * Gets the projects.
	 *
	 * @param params the params
	 * @return the projects
	 * @throws FailedException the failed exception
	 */
	public ArrayList<GetProjectsResult> getProjects(ValidateUserParams params) throws FailedException;
	
	/**
	 * Gets the sample image.
	 *
	 * @param params the params
	 * @return the sample image
	 * @throws FailedException the failed exception
	 */
	public GetSampleImageResult getSampleImage(GetSampleImageParams params) throws FailedException;
	
	/**
	 * Download batch.
	 *
	 * @param params the params
	 * @return the download batch result
	 * @throws FailedException the failed exception
	 */
	public DownloadBatchResult downloadBatch(DownloadBatchParams params) throws FailedException;
	
	/**
	 * Submit batch.
	 *
	 * @param params the params
	 * @return the submit batch result
	 * @throws FailedException the failed exception
	 */
	public SubmitBatchResult submitBatch(SubmitBatchParams params) throws FailedException;
	
	/**
	 * Gets the fields.
	 *
	 * @param params the params
	 * @return the fields
	 * @throws FailedException the failed exception
	 */
	public ArrayList<GetFieldsResult> getFields(GetFieldsParams params) throws FailedException;
	
	/**
	 * Search.
	 *
	 * @param params the params
	 * @return the array list
	 * @throws FailedException the failed exception
	 */
	public ArrayList<SearchResult> search(SearchParams params) throws FailedException;
	
	/**
	 * Download file.
	 *
	 * @param params the params
	 * @return the download file result
	 * @throws FailedException the failed exception
	 */
	public DownloadFileResult downloadFile(DownloadFileParams params) throws FailedException;
}
