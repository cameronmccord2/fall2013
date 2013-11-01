package servertester.controllers;

import java.util.*;

import models.FailedResult;
import models.FalseResult;
import models.Fields;

import communicator.DownloadBatchParams;
import communicator.DownloadBatchResult;
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
import server.ClientCommunicator;
import servertester.views.*;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser(){
		ClientCommunicator cc = new ClientCommunicator();
		ValidateUserParams var = new ValidateUserParams();
		String[] params = getView().getParameterValues();// add checks for no string?
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		getView().setRequest(var.toString());
		String result = "";
		try {
			Object response = cc.validateUser(var, getView().getHost(), getView().getPort());
			if(response instanceof ValidateUserResult){
				result = ((ValidateUserResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}else if(response instanceof FalseResult)
				result = ((FalseResult)response).toString();
			else
				result = "something else" + response.getClass();
			getView().setResponse(result);
		} catch (Exception e) {
			result = "FAILED";
		}finally{
			getView().setResponse(result);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getProjects() {
		ClientCommunicator cc = new ClientCommunicator();
		ValidateUserParams var = new ValidateUserParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		String result = "";
		try {
			getView().setRequest(var.toString());
			Object response = cc.getProjects(var, getView().getHost(), getView().getPort());
			if(response instanceof ArrayList){
				for(GetProjectsResult r : (ArrayList<GetProjectsResult>)response){
					result += r.toString();
				}
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}else
				result = "something else" + response.getClass();
			getView().setResponse(result);
		} catch (Exception e) {
			result = "FAILED";
		}finally{
			getView().setResponse(result);
		}
	}
	
	private void getSampleImage() {
		ClientCommunicator cc = new ClientCommunicator();
		GetSampleImageParams var = new GetSampleImageParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setProjectId(Integer.parseInt(params[2]));
		String result = "";
		try {
			getView().setRequest(var.toString());
			Object response = cc.getSampleImage(var, getView().getHost(), getView().getPort());
			if(response instanceof GetSampleImageResult){
				((GetSampleImageResult)response).setUrl("http://" + getView().getHost() + ":" + getView().getPort() + "/" + ((GetSampleImageResult)response).getUrl());
				result = ((GetSampleImageResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}else
				result = "something else" + response.getClass();
			getView().setResponse(result);
		} catch (Exception e) {
			result = "FAILED";
		}finally{
			getView().setResponse(result);
		}
	}
	
	private void downloadBatch() {
		ClientCommunicator cc = new ClientCommunicator();
		DownloadBatchParams var = new DownloadBatchParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setProjectId(Integer.parseInt(params[2]));
		String result = "";
		try {
			getView().setRequest(var.toString());
			Object response = cc.downloadBatch(var, getView().getHost(), getView().getPort());
			if(response instanceof DownloadBatchResult){
				((DownloadBatchResult)response).setImageUrl("http://" + getView().getHost() + ":" + getView().getPort() + "/" + ((DownloadBatchResult)response).getImageUrl());
				for(Fields f : ((DownloadBatchResult)response).getFields()){
					if(f.getHelpHtml() != null)
						f.setHelpHtml("http://" + getView().getHost() + ":" + getView().getPort() + "/" + f.getHelpHtml());
					if(f.getKnownData() != null)
						f.setKnownData("http://" + getView().getHost() + ":" + getView().getPort() + "/" + f.getKnownData());
				}
				result = ((DownloadBatchResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}else
				result = "something else" + response.getClass();
			getView().setResponse(result);
		} catch (Exception e) {
			result = "FAILED";
		}finally{
			getView().setResponse(result);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getFields() {
		ClientCommunicator cc = new ClientCommunicator();
		GetFieldsParams var = new GetFieldsParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setProjectId(Integer.parseInt(params[2]));
		String result = "";
		try {
			getView().setRequest(var.toString());
			Object response = cc.getFields(var, getView().getHost(), getView().getPort());
			if(response instanceof ArrayList){
				for(GetFieldsResult r : (ArrayList<GetFieldsResult>)response){
					result += r.toString();
				}
//				result = ((ArrayList<GetFieldsResult>)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}else
				result = "something else" + response.getClass();
			getView().setResponse(result);
		} catch (Exception e) {
			result = "FAILED";
		}finally{
			getView().setResponse(result);
		}
	}
	
	private void submitBatch() {
		ClientCommunicator cc = new ClientCommunicator();
		SubmitBatchParams var = new SubmitBatchParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setBatchId(Integer.parseInt(params[2]));
		var.setFieldValues(params[3]);
		String result = "";
		try {
			getView().setRequest(var.toString());
			
			Object response = cc.submitBatch(var, getView().getHost(), getView().getPort());
			if(response instanceof SubmitBatchResult){
				result = ((SubmitBatchResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}else
				result = "something else" + response.getClass();
			getView().setResponse(result);
		} catch (Exception e) {
			result = "FAILED";
		}finally{
			getView().setResponse(result);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void search() {
		ClientCommunicator cc = new ClientCommunicator();
		SearchParams var = new SearchParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setFields(params[2]);
		var.setSearchValues(params[3]);
		String result = "";
		try {
			getView().setRequest(var.toString());
			Object response = cc.search(var, getView().getHost(), getView().getPort());
			if(response instanceof ArrayList){
				for(SearchResult r : (ArrayList<SearchResult>)response){
					result += r.toString();
				}
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}else
				result = "something else" + response.getClass();
			
		} catch (Exception e) {
			result = "FAILED";
		}finally{
			getView().setResponse(result);
		}
	}
}

