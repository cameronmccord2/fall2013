package dao;

import java.util.ArrayList;

import server.FailedException;
import server.InvalidCredentialsException;
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
import models.FieldValues;
import models.Fields;
import models.Images;
import models.Projects;
import models.Records;
import models.Users;

/**
 * The Interface RecordIndexerDAO.
 */
public interface RecordIndexerDAO {
	
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

	void resetDatabase();

	public Integer putUser(Users u);

	public Integer putProject(Projects p);

	public Integer putField(Fields f);

	public Integer putImage(Images i);

	public Integer putRecord(Records r);

	public Integer putFieldValue(FieldValues fv, Integer id);
	
	
	
	
	
	
	

//	/**
//	 * Gets the all projects.
//	 *
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return all projects
//	 */
//	public ArrayList<Projects> getAllProjects(boolean withAllDetails);
//	
//	/**
//	 * Gets the project by id.
//	 *
//	 * @param id of the requested project
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return the project by id
//	 */
//	public Projects getProjectById(int id, boolean withAllDetails);
//	
//	/**
//	 * Gets the all fields for project id.
//	 *
//	 * @param projectId the project id of the requested fields
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return the fields for this project id
//	 */
//	public ArrayList<Fields> getAllFieldsForProjectId(int projectId, boolean withAllDetails);
//	
//	/**
//	 * Gets the field by id.
//	 *
//	 * @param id the id of the requested field
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return the field requested by id
//	 */
//	public Fields getFieldById(int id, boolean withAllDetails);
//	
//	/**
//	 * Gets the all images.
//	 *
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return all images
//	 */
//	public ArrayList<Images> getAllImages(boolean withAllDetails);
//	
//	/**
//	 * Gets the all images for project id.
//	 *
//	 * @param projectId the project id of the requested images
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return the all images for project id
//	 */
//	public ArrayList<Images> getAllImagesForProjectId(int projectId, boolean withAllDetails);
//	
//	/**
//	 * Gets the image by id.
//	 *
//	 * @param id the image id
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return the image by id
//	 */
//	public Images getImageById(int id, boolean withAllDetails);
//	
//	/**
//	 * Gets the all records.
//	 *
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return all records
//	 */
//	public ArrayList<Records> getAllRecords(boolean withAllDetails);
//	
//	/**
//	 * Gets the all records for image id.
//	 *
//	 * @param imageId the image id of the requested records
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return all records for image id
//	 */
//	public ArrayList<Records> getAllRecordsForImageId(int imageId, boolean withAllDetails);
//	
//	/**
//	 * Gets the record by id.
//	 *
//	 * @param id the record id
//	 * @param withAllDetails is a flag indicating give all the sub data too
//	 * @return the record by id
//	 */
//	public Records getRecordById(int id, boolean withAllDetails);
//	
//	/**
//	 * Gets the all field values.
//	 *
//	 * @return all field values
//	 */
//	public ArrayList<FieldValues> getAllFieldValues();
//	
//	/**
//	 * Gets all field values for record id.
//	 *
//	 * @param recordId the record id for the requested field values
//	 * @return the all field values for record id
//	 */
//	public ArrayList<FieldValues> getAllFieldValuesForRecordId(int recordId);
//	
//	/**
//	 * Gets the all field values for field id, used to get all possible values for each field
//	 *
//	 * @param fieldId the field id of the requested values
//	 * @return all field values for field id
//	 */
//	public ArrayList<FieldValues> getAllFieldValuesForFieldId(int fieldId);
//	
//	/**
//	 * Gets the field values by id.
//	 *
//	 * @param id the requested field values id
//	 * @return the field values by id
//	 */
//	public FieldValues getFieldValuesById(int id);
//	
//	/**
//	 * Login user.
//	 *
//	 * @param username the username
//	 * @param password the password
//	 * @return the user object for the requested user, throws 401 if not matching username and password
//	 */
//	public Users loginUser(String username, String password);
//	
//	public Fields insertField(Fields field);
//	
//	public int deleteFieldById(int fieldId);
//	
//	public FieldValues insertFieldValue(FieldValues fieldValue);
//	
//	public int deleteFieldValueById(int fieldValueId);
//	
//	public Images insertImage(Images image);
//	
//	public int deleteImageById(int imageId);
//	
//	public Projects insertProject(Projects project);
//	
//	public int deleteProjectById(int projectId);
//	
//	public Records insertRecord(Records record);
//	
//	public int deleteRecordById(int recordId);
//	
//	public Users insertUser(Users user);
//	
//	public int deleteUser(int userId);
}





























