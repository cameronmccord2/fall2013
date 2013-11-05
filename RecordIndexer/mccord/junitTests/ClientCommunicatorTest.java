package junitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import models.FalseResult;
import models.Fields;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.ClientCommunicator;
import server.FailedException;
import communicator.DownloadBatchParams;
import communicator.DownloadBatchResult;
import communicator.DownloadFileParams;
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
import models.FailedResult;

public class ClientCommunicatorTest {
	
	private static final String validUsername = "test1";
	private static final String validPassword = "test1";
	private static final String invalidUsername = "invalid1123";
	private static final String host = "localhost";
	private static final String port = "39640";
	
	protected ClientCommunicator cc;
	
	@Before
	public void setUp() throws Exception {
		cc = new ClientCommunicator();
	}

	@After
	public void tearDown() throws Exception {
	}

//	public Object validateUser(ValidateUserParams params, String host, String port) throws InvalidCredentialsException, FailedException, Exception;
//	public Object getProjects(ValidateUserParams params, String host, String port) throws FailedException, Exception;
//	public Object getSampleImage(GetSampleImageParams params, String host, String port) throws FailedException, Exception;
//	public Object downloadBatch(DownloadBatchParams params, String host, String port) throws FailedException, Exception;
//	public Object submitBatch(SubmitBatchParams params, String host, String port) throws FailedException, Exception;
//	public Object getFields(GetFieldsParams params, String host, String port) throws FailedException, Exception;
//	public Object search(SearchParams params, String host, String port) throws FailedException, Exception;
//	public Object downloadFile(DownloadFileParams params, String host, String port) throws FailedException, Exception;
	
	@Test
	public void mainTest() throws Exception{
		try{
			this.validateUser();
			this.getProjects();
			this.getFields();
			this.getSampleImage();
			this.search();
			this.downloadBatch();
//			this.downloadFile();
		}catch(Exception e){
			System.out.println("start up a server in order to run these tests");
			System.err.println("start up a server in order to run these tests");
			assert true;// THis is if you didnt start up a server first
		}
	}
	
	@SuppressWarnings("unchecked")
	public void search() throws Exception{
		SearchParams v = new SearchParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setFields("10,11,12,13");
		v.setSearchValues("FOX,H,K,RUSSELL");
		ArrayList<SearchResult> list = null;
		try {
			list = (ArrayList<SearchResult>) cc.search(v, host, port);
			assert true;
		} catch (FailedException e) {
			assert false;
		}
		assert(list.size() == 2);
		SearchResult expected = new SearchResult();
		expected.setBatchId(41);
		expected.setFieldId(10);
		expected.setRecordNumber(1);
		expected.setImageUrl("ourDataStore/images/draft_image0.png");
		assert(expected.toString().equals(list.get(0).toString()));
	}
	
	public void downloadBatch() throws Exception {
		DownloadBatchParams v = new DownloadBatchParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setProjectId(1);
		DownloadBatchResult result = (DownloadBatchResult) cc.downloadBatch(v, host, port);
		DownloadBatchResult expected = new DownloadBatchResult();
		expected.setBatchId(2);
		expected.setProjectId(1);
		expected.setImageUrl("ourDataStore/images/1890_image1.png");
		expected.setFirstYCoor(199);
		expected.setRecordHeight(60);
		expected.setNumberOfRecords(8);
		expected.setNumberOfFields(4);
		ArrayList<Fields> fields = new ArrayList<Fields>();
		Fields f1 = new Fields();
		f1.setId(1);
		f1.setPosition(0);
		f1.setTitle("Last Name");
		f1.setHelpHtml("ourDataStore/fieldhelp/last_name.html");
		f1.setXcoor(60);
		f1.setWidth(300);
		f1.setKnownData("ourDataStore/knowndata/1890_last_names.txt");
		fields.add(f1);
		Fields f2 = new Fields();
		f2.setId(2);
		f2.setPosition(1);
		f2.setTitle("First Name");
		f2.setHelpHtml("ourDataStore/fieldhelp/first_name.html");
		f2.setXcoor(360);
		f2.setWidth(280);
		f2.setKnownData("ourDataStore/knowndata/1890_first_names.txt");
		fields.add(f2);
		Fields f3 = new Fields();
		f3.setId(3);
		f3.setPosition(2);
		f3.setTitle("Gender");
		f3.setHelpHtml("ourDataStore/fieldhelp/gender.html");
		f3.setXcoor(640);
		f3.setWidth(205);
		f3.setKnownData("ourDataStore/knowndata/genders.txt");
		fields.add(f3);
		Fields f4 = new Fields();
		f4.setId(4);
		f4.setPosition(3);
		f4.setTitle("Age");
		f4.setHelpHtml("ourDataStore/fieldhelp/age.html");
		f4.setXcoor(845);
		f4.setWidth(120);
		fields.add(f4);
		expected.setFields(fields);
		assert(result.equals(expected)) : result.toString() + "????????????????" + expected.toString();
		
		if(cc.downloadBatch(v, host, port) instanceof FailedResult)// already have a batch
			assert true;
		else
			assert false;
		
		this.submitBatch();
//		try{
//			cc.downloadBatch(v, host, port);// checkout another one
//			assert true;
//		}catch(FailedException fe){
//			assert false;
//		}
//		expected.setImageUrl("crap");
//		assertFalse(result.equals(expected));
		
		// test throws
		
		v.setUsername(invalidUsername);
		if(cc.downloadBatch(v, host, port) instanceof FailedResult)
			assert true;
		else
			assert false;
	}
	
	public void submitBatch() throws Exception{
		SubmitBatchParams v = new SubmitBatchParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setFieldValues("Jones,Fred,13,M;Rogers,Susan,42,F;,,,;,,,;Van Fleet,Bill,23,M");
		v.setBatchId(2);
		SubmitBatchResult result = null;
		
		Object o = cc.submitBatch(v, host, port);
		if(o instanceof SubmitBatchResult){// submit good batch
			assert true;
			result = (SubmitBatchResult) o;
		}else
			assert false : ((FailedResult)o).toString();
		
		SubmitBatchResult expected = new SubmitBatchResult();
		expected.setResult("TRUE");
		assert(result.equals(expected));
		expected.setResult("crap");
		assertFalse(result.equals(expected));
		
		if(cc.submitBatch(v, host, port) instanceof FailedResult)// trying to submit a batch that has already been submitted
			assert true;
		else
			assert false;

		
		v.setBatchId(3);
		if(cc.submitBatch(v, host, port) instanceof FailedResult)// trying to submit a batch that I dont own
			assert true;
		else
			assert false;

		
		v.setUsername(invalidUsername);
		if(cc.submitBatch(v, host, port) instanceof FailedResult)
			assert true;
		else
			assert false;
	}
	
	public void getSampleImage() throws Exception {
		GetSampleImageParams v = new GetSampleImageParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setProjectId(1);
		GetSampleImageResult result = (GetSampleImageResult) cc.getSampleImage(v, host, port);
		GetSampleImageResult expected = new GetSampleImageResult();
		expected.setUrl("ourDataStore/images/1890_image0.png");
		assert(result.equals(expected));
		expected.setUrl("crap");
		assertFalse(result.equals(expected));
		
		// test throws
		v.setUsername(invalidUsername);
		if(cc.getSampleImage(v, host, port) instanceof FailedResult)
			assert true;
		else
			assert false;
	}
	
	public void validateUser() throws Exception {
		ValidateUserParams v = new ValidateUserParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		ValidateUserResult result = (ValidateUserResult) cc.validateUser(v, host, port);
		ValidateUserResult expected = new ValidateUserResult();
		expected.setCount(0);
		expected.setFirstName("Test");
		expected.setLastName("One");
		assert(result.getFirstName().equals(expected.getFirstName()));
		assert(result.getLastName().equals(expected.getLastName()));// because count is bigger than 0, so compare separately
		assert(result.getCount() > 0) : "count really is: " + result.getCount();
//		assert(result.equals(expected));
		expected.setFirstName("crap");
		assertFalse(result.equals(expected));
		
		// test throws
		v.setUsername(invalidUsername);
		if(cc.validateUser(v, host, port) instanceof FalseResult)
			assert true;
		else
			assert false : cc.validateUser(v, host, port).getClass();
	}
//	
//	@Test
//	public void resetDatabase(){
//		assert true;
//	}
//	
	public void getProjects() throws Exception {
		ValidateUserParams v = new ValidateUserParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		@SuppressWarnings("unchecked")
		ArrayList<GetProjectsResult> list = (ArrayList<GetProjectsResult>) cc.getProjects(v, host, port);
		assert(list.size() > 0) : list.size();
		assert(list.get(0) instanceof GetProjectsResult) : list.get(0).getClass();
		GetProjectsResult expected = new GetProjectsResult();
		expected.setId(1);
		expected.setTitle("1890 Census");
		assert(list.get(0).equals(expected)) : list.get(0).toString() + expected.toString();
		expected.setId(0);
		assertFalse(list.get(0).equals(expected));
		
		// test throws
		v.setUsername(invalidUsername);
		if(cc.getProjects(v, host, port) instanceof FailedResult)
			assert true;
		else
			assert false;
	}
	
	@SuppressWarnings("unchecked")
	public void getFields() throws Exception {
		GetFieldsParams v = new GetFieldsParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setProjectId(1);
		ArrayList<GetFieldsResult> list = (ArrayList<GetFieldsResult>) cc.getFields(v, host, port);
		assert(list.size() >= 4);
		assert(list.get(0) instanceof GetFieldsResult);
		GetFieldsResult expected = new GetFieldsResult();
		expected.setFieldId(1);
		expected.setProjectId(1);
		expected.setTitle("Last Name");
		assert(list.get(0).equals(expected)) : list.get(0).toString() + " " + expected.toString();
		v.setProjectId(0);
		assert(((ArrayList<SearchResult>) cc.getFields(v, host, port)).size() >= 13): ((ArrayList<SearchResult>) cc.getFields(v, host, port)).size();
		v.setProjectId(null);
		assert(((ArrayList<SearchResult>) cc.getFields(v, host, port)).size() >= 13);
		
		// test throws
		v.setUsername(null);
		v.setUsername(invalidUsername);
		if(cc.getFields(v, host, port) instanceof FailedResult)
			assert true;
		else
			assert false;
	}
	
//	public void downloadFile() throws Exception{
//		DownloadFileParams v = new DownloadFileParams();
//		GetSampleImageParams p = new GetSampleImageParams();
//		p.setPassword(validPassword);
//		p.setUsername(validUsername);
//		p.setProjectId(1);
//		v.setFilename(((GetSampleImageResult)cc.getSampleImage(p, host, port)).getUrl());
//		System.out.println(v.getFilename() + " " + host + " " + port);
//		cc.downloadFile(v, host, port);
//		assert true;
//	}
}


































