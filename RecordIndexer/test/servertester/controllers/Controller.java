package servertester.controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import models.FailedResult;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
	
	private Object sendPost(Object body, String urlPart) throws Exception {
		 
		String url = "http://localhost:39640/" + urlPart;
		URL obj = new URL(url);
//		HttpURLConnection c = new HttpURLConnection();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setDoOutput(true);
//		con.setRequestMethod("POST");
		XStream xstream = new XStream(new DomDriver());
		xstream.toXML(body, con.getOutputStream());
		//add reuqest header
		
		
//		con.addRequestProperty("Content-Type", "application/xml");
//		con.setRequestProperty("Content-Length", Integer.toString(bodyXml.length()));
//		con.getOutputStream().write(bodyXml.getBytes("UTF8"));
 
		// Send post request
//		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
		
		return xstream.fromXML(response.toString());
	}
	
	private void validateUser(){
		ValidateUserParams var = new ValidateUserParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		try {
			getView().setRequest(var.toString());
			String result = "";
			Object response = this.sendPost(var, "validateUser");
			if(response instanceof ValidateUserResult){
				result = ((ValidateUserResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}
			getView().setResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getProjects() {
		ValidateUserParams var = new ValidateUserParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		try {
			getView().setRequest(var.toString());
			String result = "";
			Object response = this.sendPost(var, "getProjects");
			if(response instanceof ValidateUserResult){
				result = ((ArrayList<GetProjectsResult>)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}
			getView().setResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getSampleImage() {
		GetSampleImageParams var = new GetSampleImageParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setProjectId(Integer.parseInt(params[2]));
		try {
			getView().setRequest(var.toString());
			String result = "";
			Object response = this.sendPost(var, "getSampleImage");
			if(response instanceof ValidateUserResult){
				result = ((GetSampleImageResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}
			getView().setResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void downloadBatch() {
		DownloadBatchParams var = new DownloadBatchParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setProjectId(Integer.parseInt(params[2]));
		try {
			getView().setRequest(var.toString());
			String result = "";
			Object response = this.sendPost(var, "downloadBatch");
			if(response instanceof ValidateUserResult){
				result = ((DownloadBatchResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}
			getView().setResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getFields() {
		GetFieldsParams var = new GetFieldsParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setProjectId(Integer.parseInt(params[2]));
		try {
			getView().setRequest(var.toString());
			String result = "";
			Object response = this.sendPost(var, "getFields");
			if(response instanceof ValidateUserResult){
				result = ((ArrayList<GetFieldsResult>)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}
			getView().setResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void submitBatch() {
		SubmitBatchParams var = new SubmitBatchParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setBatchId(Integer.parseInt(params[2]));
		var.setFieldValues(params[3]);
		try {
			getView().setRequest(var.toString());
			String result = "";
			Object response = this.sendPost(var, "submitBatch");
			if(response instanceof ValidateUserResult){
				result = ((SubmitBatchResult)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}
			getView().setResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void search() {
		SearchParams var = new SearchParams();
		String[] params = getView().getParameterValues();
		var.setUsername(params[0]);
		var.setPassword(params[1]);
		var.setFields(params[2]);
		var.setSearchValues(params[3]);
		try {
			getView().setRequest(var.toString());
			String result = "";
			Object response = this.sendPost(var, "search");
			if(response instanceof ValidateUserResult){
				result = ((ArrayList<SearchResult>)response).toString();
			}else if(response instanceof FailedResult){
				result = ((FailedResult)response).toString();
			}
			getView().setResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

