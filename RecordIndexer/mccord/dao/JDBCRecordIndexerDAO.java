package dao;

import java.util.ArrayList;
import java.util.Collection;

import models.FieldValues;
import models.Fields;
import models.Images;
import models.Projects;
import models.Records;
import models.SearchesToDo;
import models.Users;
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

public class JDBCRecordIndexerDAO implements RecordIndexerDAO {

	@Override
	public ValidateUserResult validateUser(ValidateUserParams params)throws InvalidCredentialsException, FailedException {
		if(params == null || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		ValidateUserResult vur = new ValidateUserResult();
		Users u = this.getUserByUsernamePassword(params.getUsername(), params.getPassword());
		vur.setFirstName(u.getFirstName());
		vur.setLastName(u.getLastName());
		vur.setCount(this.getFinishedRecordsForUserId(u.getId()));
		return null;
	}

	private Integer getFinishedRecordsForUserId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	private Users getUserByUsernamePassword(String username, String password) throws InvalidCredentialsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GetProjectsResult> getProjects(ValidateUserParams params)throws FailedException {
		if(params == null || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		try{
			this.getUserByUsernamePassword(params.getUsername(), params.getPassword());// will throw if invalid
		}catch(InvalidCredentialsException e){
			// do nothing, should never happen
			System.out.println("this should never be seen");
		}
		ArrayList<GetProjectsResult> result = this.getAllProjects();
		return result;
	}

	private ArrayList<GetProjectsResult> getAllProjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetSampleImageResult getSampleImage(GetSampleImageParams params)throws FailedException {
		if(params == null || params.getProjectId() == null || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		try{
			this.getUserByUsernamePassword(params.getUsername(), params.getPassword());// will throw if invalid
		}catch(InvalidCredentialsException e){
			// do nothing, should never happen
			System.out.println("this should never be seen");
		}
		GetSampleImageResult result = new GetSampleImageResult();
		result.setUrl(this.getStringPathOfImage(params.getProjectId()));
		// TODO Auto-generated method stub
		return result;
	}

	private String getStringPathOfImage(Integer projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadBatchResult downloadBatch(DownloadBatchParams params)throws FailedException {
		if(params == null || params.getProjectId() == null || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		try{
			this.getUserByUsernamePassword(params.getUsername(), params.getPassword());// will throw if invalid
		}catch(InvalidCredentialsException e){
			// do nothing, should never happen
			System.out.println("this should never be seen");
		}
		DownloadBatchResult result = new DownloadBatchResult();
		Projects p = this.getProjectById(params.getProjectId());
		result.setProjectId(p.getId());
		result.setImageUrl(this.getStringPathOfImage(params.getProjectId()));
		result.setFirstYCoor(p.getFirstYCoor());
		result.setRecordHeight(p.getRecordHeight());
		result.setNumberOfRecords(p.getRecordsPerImage());
		result.setFields(this.getAllFieldsForProjectId(p.getId(), false));
		result.setNumberOfFields(result.getFields().size());
		return result;
	}
	
	private ArrayList<Fields> getAllFieldsForProjectId(int projectId, boolean withAllDetails){
		// TODO Auto-generated method stub
		return null;
	}

	private Projects getProjectById(Integer projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubmitBatchResult submitBatch(SubmitBatchParams params)throws FailedException {
		if(params == null || params.getBatchId() == null || params.getFieldValues() == null || params.getFieldValues().length() < 1 || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		try{
			this.getUserByUsernamePassword(params.getUsername(), params.getPassword());// will throw if invalid
		}catch(InvalidCredentialsException e){
			// do nothing, should never happen
			System.out.println("this should never be seen");
		}
		SubmitBatchResult result = new SubmitBatchResult();
		result.setSuccess(false);
		String[] records = params.getFieldValues().split("/;/");
		for(String r : records){
			if(!this.insertRecord(r, params.getBatchId()))
				return result;
		}
		result.setSuccess(true);
		return result;
	}

	private boolean insertRecord(String record, int batchId) {// returns true if success
		Records r = insertRecordForBatchId(batchId);
		String[] fieldValues = record.split("/,/");
		ArrayList<Fields> fields = this.getFieldsForBatchId(batchId);
		int count = 0;
		for(String fv : fieldValues){
			if(!this.insertFieldValueForRecordIdFieldId(fv, r.getId(), fields.get(count).getId()))
				return false;
			count++;
			if(count == fields.size())
				count = 0;
		}
		return true;
	}

	private boolean insertFieldValueForRecordIdFieldId(String fv, Integer recordId, Integer fieldId) {
		// TODO Auto-generated method stub
		return true;
	}

	private ArrayList<Fields> getFieldsForBatchId(Integer batchId) {
		Images i = this.getBatchById(batchId);
		ArrayList<Fields> fields = this.getAllFieldsForProjectId(i.getProjectId(), false);
		return fields;
	}

	private Images getBatchById(Integer batchId) {
		// TODO Auto-generated method stub
		return null;
	}

	private Records insertRecordForBatchId(int batchId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GetFieldsResult> getFields(GetFieldsParams params)throws FailedException {
		if(params == null || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		try{
			this.getUserByUsernamePassword(params.getUsername(), params.getPassword());// will throw if invalid
		}catch(InvalidCredentialsException e){
			// do nothing, should never happen
			System.out.println("this should never be seen");
		}
		ArrayList<Fields> fields = new ArrayList<Fields>();
		if(params.getProjectId() == null || params.getProjectId() == 0)
			fields.addAll(this.getAllFields());
		else
			fields.addAll(this.getAllFieldsForProjectId(params.getProjectId(), false));
		ArrayList<GetFieldsResult> result = new ArrayList<GetFieldsResult>();
		for(Fields f : fields){
			GetFieldsResult r = new GetFieldsResult();
			result.add(r);
			r.setFieldId(f.getId());
			r.setProjectId(params.getProjectId());
			r.setTitle(f.getTitle());
		}
		return result;
	}

	private ArrayList<Fields> getAllFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SearchResult> search(SearchParams params)throws FailedException {
		if(params == null || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		try{
			this.getUserByUsernamePassword(params.getUsername(), params.getPassword());// will throw if invalid
		}catch(InvalidCredentialsException e){
			// do nothing, should never happen
			System.out.println("this should never be seen");
		}
		ArrayList<SearchResult> result = new ArrayList<SearchResult>();
		String[] fields = params.getFields().split("/,/");
		String[] values = params.getSearchValues().split("/,/");
		ArrayList<SearchesToDo> searches = new ArrayList<SearchesToDo>();
		for(String fieldId : fields){
			for(String value : values){
				SearchesToDo std = new SearchesToDo();
				std.setFieldId(Integer.parseInt(fieldId));
				std.setSearchValue(value);
				searches.add(std);
			}
		}
		for(SearchesToDo s : searches){
			ArrayList<FieldValues> foundFields = this.getAllFieldValuesMatchingValueForFieldId(values[i], Integer.parseInt(fields[i]));
			for(FieldValues f : foundFields){
				SearchResult sr = new SearchResult();
				result.add(sr);
				Records r = this.getRecordById(f.getRecordId());
				Images image = this.getImageById(r.getImageId());
				sr.setFieldId(f.getId());
				sr.setBatchId(r.getImageId());
				sr.setImageUrl(this.getStringPathOfImage(image.getProjectId()));
				sr.setRecordNumber(this.getRowNumberOfRecordIdForProjectId(f.getRecordId(), image.getProjectId()));
			}
		}
		return result;
	}

	private Integer getRowNumberOfRecordIdForProjectId(Integer recordId, Integer projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	private Images getImageById(Integer imageId) {
		// TODO Auto-generated method stub
		return null;
	}

	private Records getRecordById(Integer recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<FieldValues> getAllFieldValuesMatchingValueForFieldId(String string, int parseInt) {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public DownloadFileResult downloadFile(DownloadFileParams params)throws FailedException {
		if(params == null || params.getUsername() == null || params.getUsername().length() < 1 || params.getPassword() == null || params.getPassword().length() < 1)
			throw new FailedException();
		try{
			this.getUserByUsernamePassword(params.getUsername(), params.getPassword());// will throw if invalid
		}catch(InvalidCredentialsException e){
			// do nothing, should never happen
			System.out.println("this should never be seen");
		}
		return null;
	}

}
