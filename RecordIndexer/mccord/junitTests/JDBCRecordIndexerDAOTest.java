package junitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import models.FieldValues;
import models.Fields;
import models.Images;
import models.Projects;
import models.Records;
import models.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.FailedException;
import server.InvalidCredentialsException;
import communicator.DownloadBatchResult;
import communicator.DownloadBatchParams;
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
import dao.JDBCRecordIndexerDAO;

public class JDBCRecordIndexerDAOTest {
	
	private static final String validUsername = "test1";
	private static final String validPassword = "test1";
	private static final String invalidUsername = "invalid1123";
//	private static final String invalidPassword = "invalid1123";
	
	protected JDBCRecordIndexerDAO dao;

	@Before
	public void setUp() throws Exception {
		dao = new JDBCRecordIndexerDAO();
//		e = ExpectedException.none();
	}

	@After
	public void tearDown() throws Exception {
	}
	
//	@Rule
//	  public ExpectedException e = ExpectedException.none();

	// public ValidateUserResult validateUser(ValidateUserParams params)throws InvalidCredentialsException, FailedException {
	// public void resetDatabase(){
	// public ArrayList<GetProjectsResult> getProjects(ValidateUserParams params)throws FailedException {
	// public GetSampleImageResult GetSampleImageResult(GetSampleImageParams params)throws FailedException {
	// public DownloadBatchResult downloadBatch(DownloadBatchParams params)throws FailedException {
	// public SubmitBatchResult submitBatch(SubmitBatchParams params)throws FailedException {
	// public ArrayList<GetFieldsResult> getFields(GetFieldsParams params)throws FailedException {
	// public ArrayList<SearchResult> search(SearchParams params)throws FailedException {
	// public DownloadFileResult downloadFile(DownloadFileParams params)throws FailedException {
	// public Integer putUser(Users u) {
	// public Integer putProject(Projects p) {
	// public Integer putField(Fields f) {
	// public Integer putImage(Images image) {
	// public Integer putRecord(Records r) {
	// public Integer putFieldValue(FieldValues fv) {
	
	@Test
	public void mainTest() throws InvalidCredentialsException, FailedException{
		this.validateUser();
		this.getProjects();
		this.getFields();
		this.getSampleImage();
		this.search();
		this.downloadBatch();
		
		
		this.putProject();
		this.putImage();
		this.putUser();
		this.putRecord();
		this.putField();
		this.putFieldValue();
	}
	
	 public void putUser(){
		 Users u = new Users();
		 u.setEmail("cra");
		 u.setFirstName("firstname");
		 u.setLastName("lastName");
		 u.setPassword("asdfasdfasdf");
		 u.setUserName("qwerqwerqwer");
		 u.setIndexedRecords(0);
		 try {
			assert(dao.putUser(u) > 0);
			assert true;
		} catch (FailedException e) {
			assert false;
		}
	 }
	 
	 public void putProject() {
		 Projects p = new Projects();
		 p.setFields(new ArrayList<Fields>());
		 p.setFirstYCoor(1);
		 p.setImages(new ArrayList<Images>());
		 p.setRecordHeight(1);
		 p.setRecordsPerImage(3);
		 p.setTitle("temp");
		 assert(dao.putProject(p) > 0);
		 assert(dao.putProject(p) > 0);// projects dont have to be unique
		 assert(dao.putProject(p) > 0);
		 assert(dao.putProject(p) > 0);
	 }
	 
	 public void putField() {
		 Fields f = new Fields();
		 f.setHelpHtml("cra");
		 f.setKnownData("cra");
		 f.setPosition(222);
		 f.setProjectId(2);
		 f.setTitle("crap");
		 f.setWidth(2134);
		 f.setXcoor(2);
		 assert(dao.putField(f) > 0);
	 }
	 
	 public void putImage() {
		 Images i = new Images();
		 i.setFile("asdfasdf.png");
		 i.setProjectId(2);
		 i.setRecords(new ArrayList<Records>());
		 assert(dao.putImage(i) > 0);
	 }
	 
	 public void putRecord() {
		 Records r = new Records();
		 r.setFieldValues(new ArrayList<FieldValues>());
		 r.setImageId(1);
		 assert(dao.putRecord(r) > 0);
	 }
	 
	 public void putFieldValue() {
		 FieldValues f = new FieldValues();
		 f.setFieldId(1);
		 f.setRecordId(1);
		 f.setValue("crap");
		 assert(dao.putFieldValue(f) > 0);
	 }
	
	public void search(){
		SearchParams v = new SearchParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setFields("10,11,12,13");
		v.setSearchValues("FOX,H,K,RUSSELL");
		ArrayList<SearchResult> list = null;
		try {
			list = dao.search(v);
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
	
	public void downloadBatch() throws FailedException {
		DownloadBatchParams v = new DownloadBatchParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setProjectId(1);
		DownloadBatchResult result = dao.downloadBatch(v);
		DownloadBatchResult expected = new DownloadBatchResult();
		expected.setBatchId(1);
		expected.setProjectId(1);
		expected.setImageUrl("ourDataStore/images/1890_image0.png");
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
		assert(result.equals(expected)) : result.toString() + "?????????????????" + expected.toString();
		try{
			dao.downloadBatch(v);// already have a batch
			assert false;
		}catch(FailedException fe){
			assert true;
		}
		this.submitBatch();
//		try{
//			dao.downloadBatch(v);// get another one
//			assert true;
//		}catch(FailedException fe){
//			assert false;
//		}
		expected.setImageUrl("crap");
		assertFalse(result.equals(expected));// test comparison
		
		// test throws
		
		v.setUsername(invalidUsername);
		try{
			dao.downloadBatch(v);
			assert false;
		}catch(FailedException fe){
			assert true;
		}
	}
	
	public void submitBatch(){
		SubmitBatchParams v = new SubmitBatchParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setFieldValues("Jones,Fred,13,M;Rogers,Susan,42,F;,,,;,,,;Van Fleet,Bill,23,M");
		v.setBatchId(1);
		SubmitBatchResult result = null;
		try {
			System.out.println("got here");
			result = dao.submitBatch(v);
			assert true;
		} catch (FailedException e) {
			assert false : e.getLocalizedMessage();
		}
		SubmitBatchResult expected = new SubmitBatchResult();
		expected.setResult("TRUE");
		assert(result.equals(expected));
		expected.setResult("crap");
		assertFalse(result.equals(expected));
		
		try{
			dao.submitBatch(v);// trying to submit a batch that has already been submitted
			assert false;
		}catch(FailedException fe){
			assert true;
		}
		
		v.setBatchId(2);
		try{
			dao.submitBatch(v);// trying to submit a batch that I dont own
			assert false;
		}catch(FailedException fe){
			assert true;
		}
		
		v.setUsername(invalidUsername);
		try{
			dao.submitBatch(v);
			assert false;
		}catch(FailedException fe){
			assert true;
		}
	}
	
	public void getSampleImage() throws FailedException {
		GetSampleImageParams v = new GetSampleImageParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setProjectId(1);
		GetSampleImageResult result = dao.getSampleImage(v);
		GetSampleImageResult expected = new GetSampleImageResult();
		expected.setUrl("ourDataStore/images/1890_image0.png");
		assert(result.equals(expected)) : result.getUrl();
		expected.setUrl("crap");
		assertFalse(result.equals(expected));
		
		// test throws
		v.setUsername(invalidUsername);
		try{
			dao.getSampleImage(v);
			assert false;
		}catch(FailedException fe){
			assert true;
		}
	}
	
	public void validateUser() throws InvalidCredentialsException, FailedException {
		ValidateUserParams v = new ValidateUserParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		ValidateUserResult result = dao.validateUser(v);
		ValidateUserResult expected = new ValidateUserResult();
		expected.setCount(0);
		expected.setFirstName("Test");
		expected.setLastName("One");
		assert(result.getFirstName().equals(expected.getFirstName()));
		assert(result.getLastName().equals(expected.getLastName()));
//		assert(result.equals(expected));
		expected.setFirstName("crap");
		assertFalse(result.equals(expected));
		
		// test throws
		v.setUsername(invalidUsername);
		try{
			dao.validateUser(v);
			assert false;
		}catch(InvalidCredentialsException fe){
			assert true;
		}
	}
	
	@Test
	public void resetDatabase(){
		assert true;
	}
	
	public void getProjects() throws FailedException {
		ValidateUserParams v = new ValidateUserParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		ArrayList<GetProjectsResult> list = dao.getProjects(v);
		assert(list.size() >= 3);
		assert(list.get(0) instanceof GetProjectsResult);
		GetProjectsResult expected = new GetProjectsResult();
		expected.setId(1);
		expected.setTitle("1890 Census");
		assert(list.get(0).equals(expected));
		expected.setId(0);
		assertFalse(list.get(0).equals(expected));
		
		// test throws
		v.setUsername(null);
		try{
			dao.getProjects(v);
			assert false;
		}catch(FailedException fe){
			assert true;
		}
	}
	
	public void getFields() throws FailedException {
		GetFieldsParams v = new GetFieldsParams();
		v.setUsername(validUsername);
		v.setPassword(validPassword);
		v.setProjectId(1);
		ArrayList<GetFieldsResult> list = dao.getFields(v);
		assert(list.size() >= 4);
		assert(list.get(0) instanceof GetFieldsResult);
		GetFieldsResult expected = new GetFieldsResult();
		expected.setFieldId(1);
		expected.setProjectId(1);
		expected.setTitle("Last Name");
		assert(list.get(0).equals(expected));
		v.setProjectId(0);
		assert(dao.getFields(v).size() >= 13);
		v.setProjectId(null);
		assert(dao.getFields(v).size() >= 13);
		
		// test throws
		v.setUsername(null);
		try{
			dao.getFields(v);
			assert false;
		}catch(FailedException fe){
			assert true;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	// helper methods


}
