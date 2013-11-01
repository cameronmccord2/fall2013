package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
		vur.setCount(this.getNumberOfFinishedRecordsForUserId(u.getId()));
		return null;
	}

	private Integer getNumberOfFinishedRecordsForUserId(Integer userId) {
		ArrayList<Images> images = this.getImagesForUserId(userId);
		Integer count = 0;
		for(Images i : images){
			ArrayList<Records> records = this.getRecordsForImageId(i.getId());
			if(records != null)
				count += records.size();
		}
		return count;
	}
	
	@SuppressWarnings("finally")
	private ArrayList<Images> getImagesForUserId(Integer userId) {
		Connection connection = null;
	    ArrayList<Images> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from images WHERE userId = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, userId);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Images.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return result;
	      }
	    }
	}
	
	@Override
	public void resetDatabase(){
		System.out.println("resetting database********************************************************");
		this.dropAllTables();
		this.createAllTables();
	}

	@SuppressWarnings("finally")
	private boolean createAllTables(){
		Connection connection = null;
	    try{
	    	// load the sqlite-JDBC driver using the current class loader
		    Class.forName("org.sqlite.JDBC");
	      // create a database connection
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      Statement statement = connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      statement.executeUpdate("CREATE TABLE users(id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,firstName String NOT NULL,lastName String NOT NULL,email String NOT NULL,userName String UNIQUE NOT NULL,password String NOT NULL,indexedRecords Integer default 0 NOT NULL)");
	      statement.executeUpdate("CREATE TABLE projects(id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,title String NOT NULL,recordsPerImage Integer NOT NULL,firstYCoor Integer NOT NULL,recordHeight Integer NOT NULL)");
	      statement.executeUpdate("CREATE TABLE fields(id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,position Integer NOT NULL,title String NOT NULL,xcoor Integer NOT NULL,width Integer NOT NULL,helpHtml String NULL,knownData String NULL,projectId Integer NOT NULL,FOREIGN KEY(projectId) REFERENCES projects(id))");
	      statement.executeUpdate("CREATE TABLE images(id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,file String NOT NULL,projectId Integer NOT NULL,userId Integer NULL,finished Integer default 0 NOT NULL,FOREIGN KEY(projectId) REFERENCES projects(id),FOREIGN KEY(userId) REFERENCES users(id))");
	      statement.executeUpdate("CREATE TABLE records(id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,imageId Integer NOT NULL,FOREIGN KEY(imageId) REFERENCES images(id))");
	      statement.executeUpdate("CREATE TABLE fieldValues(id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,recordId Integer NOT NULL,fieldId Integer NOT NULL,value String NOT NULL,FOREIGN KEY(fieldId) REFERENCES fields(id),FOREIGN KEY(recordId) REFERENCES records(id))");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
	    	System.out.println("couldnt find db driver");
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  System.out.println("created tables");
	    	  return true;
	      }
	    }
	}
	
	@SuppressWarnings("finally")
	private boolean dropAllTables(){
		Connection connection = null;
	    try{
	    	// load the sqlite-JDBC driver using the current class loader
		    Class.forName("org.sqlite.JDBC");
	      // create a database connection
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      Statement statement = connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      
	      statement.executeUpdate("drop table if exists fieldValues");
	      statement.executeUpdate("drop table if exists records");
	      statement.executeUpdate("drop table if exists images");
	      statement.executeUpdate("drop table if exists fields");
	      statement.executeUpdate("drop table if exists projects");
	      statement.executeUpdate("drop table if exists users");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return true;
	      }
	    }
	}

	@SuppressWarnings("finally")
	private Users getUserByUsernamePassword(String username, String password) throws InvalidCredentialsException {
	    Connection connection = null;
	    ArrayList<Users> users = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from users WHERE username = ? AND password = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setString(1, username);
	      statement.setString(2, password);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      users = Users.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  if(users != null && users.size() > 0)
	    		  return users.get(0);
	    	  throw new InvalidCredentialsException();
	      }
	    }
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

	@SuppressWarnings("finally")
	private ArrayList<GetProjectsResult> getAllProjects() {
		Connection connection = null;
	    ArrayList<GetProjectsResult> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from projects";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = GetProjectsResult.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return result;
	      }
	    }
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
		// TODO Finish this
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
	
	@SuppressWarnings("finally")
	private ArrayList<Fields> getAllFieldsForProjectId(int projectId, boolean withAllDetails){
		Connection connection = null;
	    ArrayList<Fields> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from fields WHERE projectId = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, projectId);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Fields.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return result;
	      }
	    }
	}

	@SuppressWarnings("finally")
	private Projects getProjectById(Integer projectId) {
		Connection connection = null;
	    ArrayList<Projects> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from projects WHERE id = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, projectId);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Projects.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  if(result != null && result.size() == 1)
	    		  return result.get(0);
	    	  return null;
	      }
	    }
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
		result.setResult("FAILED");
		String[] records = params.getFieldValues().split("/;/");
		for(String r : records){
			if(!this.insertRecord(r, params.getBatchId()))
				return result;
		}
		result.setResult("TRUE");
		return result;
	}

	private boolean insertRecord(String record, int batchId) {// returns true if success
		Records r = new Records();
		r.setImageId(batchId);
		int recordId = this.putRecord(r);
		String[] fieldValues = record.split("/,/");
		ArrayList<Fields> fields = this.getFieldsForBatchId(batchId);
		int count = 0;
		for(String fv : fieldValues){
			FieldValues f = new FieldValues();
			f.setRecordId(recordId);
			f.setValue(fv);
			f.setFieldId(fields.get(count).getId());
			if(this.putFieldValue(f) < 1)
				return false;
			count++;
			if(count == fields.size())
				count = 0;
		}
		return true;
	}
	
	private int parseFirstInt(ResultSet generatedKeys, String key) {
		try {
			while(generatedKeys.next())
				return (int) generatedKeys.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			throw new RuntimeException("" + generatedKeys.getLong(1));
		} catch (SQLException e) {
			throw new RuntimeException("asdf" + e);
		}
	}

/*
 * CREATE TABLE fieldValues(
	id 			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	recordId 	Integer 		NOT NULL,
	fieldId 	Integer 		NOT NULL,
	value 		String			NOT NULL,
	FOREIGN KEY(fieldId) 		REFERENCES fields(id),
	FOREIGN KEY(recordId) 		REFERENCES records(id)
);
 */
//	@SuppressWarnings("finally")
//	private int insertFieldValueForRecordIdFieldId(String fv, Integer recordId, Integer fieldId) {
//		Connection connection = null;
//	    int i = 0;
//	    try{
//		  Class.forName("org.sqlite.JDBC");
//	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
//	      String sql = "insert into fieldValues (recordId, fieldId, value) VALUES (?, ?, ?)";
//	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//	      statement.setInt(1, recordId);
//	      statement.setInt(2, fieldId);
//	      statement.setString(3, fv);
//	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
//	      i = statement.executeUpdate();
//	      if(i > 0)
//	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
//	    }catch(SQLException e){
//	      // if the error message is "out of memory", 
//	      // it probably means no database file is found
//	      System.err.println(e.getMessage());
//	    } catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}finally{
//	      try{
//	        if(connection != null)
//	          connection.close();
//	      }catch(SQLException e){
//	        // connection close failed.
//	        System.err.println(e);
//	      }finally{
//	    	  return i;
//	      }
//	    }
//	}

	private ArrayList<Fields> getFieldsForBatchId(Integer batchId) {
		Images i = this.getBatchById(batchId);
		ArrayList<Fields> fields = this.getAllFieldsForProjectId(i.getProjectId(), false);
		return fields;
	}

	@SuppressWarnings("finally")
	private Images getBatchById(Integer batchId) {
		Connection connection = null;
	    ArrayList<Images> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from images WHERE id = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, batchId);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Images.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  if(result != null && result.size() == 1)
	    		  return result.get(0);
	    	  return null;
	      }
	    }
	}

	/*
	 * CREATE TABLE records(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	imageId 	Integer			NOT NULL,
	FOREIGN KEY(imageId)		REFERENCES images(id)
);
	 */
//	@SuppressWarnings("finally")
//	private int insertRecordForBatchId(int batchId) {
//		Connection connection = null;
//	    int i = 0;
//	    try{
//		  Class.forName("org.sqlite.JDBC");
//	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
//	      String sql = "insert into records (imageId) VALUES (?)";
//	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//	      statement.setInt(1, batchId);
//	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
//	      i = statement.executeUpdate();
//	      if(i > 0)
//	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
//	    }catch(SQLException e){
//	      // if the error message is "out of memory", 
//	      // it probably means no database file is found
//	      System.err.println(e.getMessage());
//	    } catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}finally{
//	      try{
//	        if(connection != null)
//	          connection.close();
//	      }catch(SQLException e){
//	        // connection close failed.
//	        System.err.println(e);
//	      }finally{
//	    	  return i;
//	      }
//	    }
//	}

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

	@SuppressWarnings("finally")
	private ArrayList<Fields> getAllFields() {
		Connection connection = null;
	    ArrayList<Fields> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from fields";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Fields.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return result;
	      }
	    }
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
			ArrayList<FieldValues> foundFields = this.getAllFieldValuesMatchingValueForFieldId(s.getSearchValue(), s.getFieldId());
			for(FieldValues f : foundFields){
				SearchResult sr = new SearchResult();
				result.add(sr);
				Records r = this.getRecordById(f.getRecordId());
				Images image = this.getImageById(r.getImageId());
				sr.setFieldId(f.getId());
				sr.setBatchId(r.getImageId());
				sr.setImageUrl(this.getStringPathOfImage(image.getProjectId()));
				sr.setRecordNumber(this.getRowNumberOfRecordIdForProjectId(f.getRecordId(), image.getId()));
			}
		}
		return result;
	}

	private Integer getRowNumberOfRecordIdForProjectId(Integer recordId, Integer imageId) {
		ArrayList<Records> result = this.getRecordsForImageId(imageId);
		for(int i = 0; i < result.size(); i++){
			if(result.get(i).getId() == recordId)
				return i + 1;
		}
		throw new RuntimeException("shouldnt get here, couldnt find record id for row num calculation");
	}

	@SuppressWarnings("finally")
	private ArrayList<Records> getRecordsForImageId(Integer imageId) {
		Connection connection = null;
	    ArrayList<Records> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from records WHERE imageId = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, imageId);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Records.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return result;
	      }
	    }
	}

	@SuppressWarnings("finally")
	private Images getImageById(Integer imageId) {
		Connection connection = null;
	    ArrayList<Images> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from images WHERE id = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, imageId);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Images.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  if(result != null && result.size() == 1)
	    		  return result.get(0);
	    	  return null;
	      }
	    }
	}

	@SuppressWarnings("finally")
	private Records getRecordById(Integer recordId) {
		Connection connection = null;
	    ArrayList<Records> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from records WHERE id = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, recordId);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = Records.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  if(result != null && result.size() == 1)
	    		  return result.get(0);
	    	  return null;
	      }
	    }
	}

	/*
	 * CREATE TABLE fieldValues(
	id 			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	recordId 	Integer 		NOT NULL,
	fieldId 	Integer 		NOT NULL,
	value 		String			NOT NULL,
	FOREIGN KEY(fieldId) 		REFERENCES fields(id),
	FOREIGN KEY(recordId) 		REFERENCES records(id)
);
	 */
	@SuppressWarnings("finally")
	private ArrayList<FieldValues> getAllFieldValuesMatchingValueForFieldId(String value, int fieldId) {
		Connection connection = null;
	    ArrayList<FieldValues> result = null;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "select * from fieldValues WHERE fieldId = ? AND value = ?";
	      PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setInt(1, fieldId);
	      statement.setString(2, value);
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      ResultSet rs = statement.executeQuery();
	      result = FieldValues.parseResultSet(rs);
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return result;
	      }
	    }
	}

	@Override
	public DownloadFileResult downloadFile(DownloadFileParams params)throws FailedException {
		if(params == null || params.getFilename() == null || params.getFilename().length() < 1)
			throw new FailedException();
		// TODO finish this
		return null;
	}

	/*
	 * CREATE TABLE users(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	firstName	String 			NOT NULL,
	lastName	String			NOT NULL,
	email 		String			NOT NULL,
	userName	String			UNIQUE NOT NULL,
	password	String 			NOT NULL,
	indexedRecords Integer	default 0	NOT NULL
);
	 */
	@SuppressWarnings("finally")
	@Override
	public Integer putUser(Users u) {
		Connection connection = null;
	    int i = 0;
//	    System.out.println(u.getFirstName() + ", " +  u.getLastName() + ", " +  u.getEmail() + ", " +  u.getUserName() + ", " +  u.getPassword());
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "insert into users (firstName, lastName, email, username, password) VALUES (?, ?, ?, ? ,?)";
	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	      statement.setString(1, u.getFirstName());
	      statement.setString(2, u.getLastName());
	      statement.setString(3, u.getEmail());
	      statement.setString(4, u.getUserName());
	      statement.setString(5, u.getPassword());
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      i = statement.executeUpdate();
	      if(i > 0)
	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println("sqlexception" + e);
	    } catch (ClassNotFoundException e) {
	    	System.err.println("class not found");
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println("failed to close connection" + e.getLocalizedMessage());
	      }finally{
	    	  return i;
	      }
	    }
	}

	
	/*
	 * CREATE TABLE projects(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	title 		String 			NOT NULL,
	recordsPerImage Integer 	NOT NULL,
	firstYCoor 	Integer			NOT NULL,
	recordHeight 	Integer		NOT NULL
);
	 */
	@SuppressWarnings("finally")
	@Override
	public Integer putProject(Projects p) {
		Connection connection = null;
	    int i = 0;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "insert into projects (title, recordsPerImage, firstYCoor, recordHeight) VALUES (?, ?, ?, ?)";
	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	      statement.setString(1, p.getTitle());
	      statement.setInt(2, p.getRecordsPerImage());
	      statement.setInt(3, p.getFirstYCoor());
	      statement.setInt(4, p.getRecordHeight());
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      i = statement.executeUpdate();
	      if(i > 0)
	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return i;
	      }
	    }
	}

	/*
	 * CREATE TABLE fields(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	position	Integer 		NOT NULL,
	title 		String 			NOT NULL,
	xcoor		Integer 		NOT NULL,
	width 		Integer 		NOT NULL,
	helpHtml 	String 			NULL,
	knownData 	String 			NULL,
	projectId 	Integer 		NOT NULL,
	FOREIGN KEY(projectId) 		REFERENCES projects(id)
);
	 */
	@SuppressWarnings("finally")
	@Override
	public Integer putField(Fields f) {
		Connection connection = null;
	    int i = 0;
//	    System.out.println(f.getPosition() + ", " + f.getTitle() + ", " + f.getXcoor() + ", " + f.getWidth() + ", " + f.getHelpHtml() + ", " + f.getKnownData() + ", " + f.getProjectId());
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "insert into fields (position, title, xcoor, width, helpHtml, knownData, projectId) VALUES (?, ?, ?, ?, ?, ?, ?)";
	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	      statement.setInt(1, f.getPosition());
	      statement.setString(2, f.getTitle());
	      statement.setInt(3, f.getXcoor());
	      statement.setInt(4, f.getWidth());
	      statement.setString(5, f.getHelpHtml());
	      statement.setString(6, f.getKnownData());
	      statement.setInt(7, f.getProjectId());
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      i = statement.executeUpdate();
	      if(i > 0)
	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
	    	System.out.println("class nto found");
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return i;
	      }
	    }
	}

	
	/*
	 * CREATE TABLE images(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	file 		String 			NOT NULL,
	projectId 	Integer 		NOT NULL,
	userId 		Integer 		NULL,
	finished 	Integer default 0 NOT NULL,
	FOREIGN KEY(projectId) 		REFERENCES projects(id),
	FOREIGN KEY(userId) 		REFERENCES users(id)
);
	 */
	@SuppressWarnings("finally")
	@Override
	public Integer putImage(Images image) {
		Connection connection = null;
	    int i = 0;
//	    System.out.println(image.getFile() + ", " + image.getProjectId());
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "insert into images (file, projectId) VALUES (?, ?)";
	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	      statement.setString(1, image.getFile());
	      statement.setInt(2, image.getProjectId());
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      i = statement.executeUpdate();
	      if(i > 0)
	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return i;
	      }
	    }
	}
	
	/*
	 * CREATE TABLE records(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	imageId 	Integer			NOT NULL,
	FOREIGN KEY(imageId)		REFERENCES images(id)
);
	 */

	@SuppressWarnings("finally")
	@Override
	public Integer putRecord(Records r) {
		Connection connection = null;
	    int i = 0;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "insert into records (imageId) VALUES (?)";
	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	      statement.setInt(1, r.getImageId());
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      i = statement.executeUpdate();
	      if(i > 0)
	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return i;
	      }
	    }
	}

	/*
	 * CREATE TABLE fieldValues(
	id 			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	recordId 	Integer 		NOT NULL,
	fieldId 	Integer 		NOT NULL,
	value 		String			NOT NULL,
	FOREIGN KEY(fieldId) 		REFERENCES fields(id),
	FOREIGN KEY(recordId) 		REFERENCES records(id)
);
	 */
	@SuppressWarnings("finally")
	@Override
	public Integer putFieldValue(FieldValues fv) {
		Connection connection = null;
//		System.out.println(fv.getRecordId() + ", " + fv.getFieldId() + ", " + fv.getValue());
	    int i = 0;
	    try{
		  Class.forName("org.sqlite.JDBC");
	      connection = DriverManager.getConnection("jdbc:sqlite:dbStuff" + File.separator + "indexer_server.sqlite");
	      String sql = "insert into fieldValues (recordId, fieldId, value) VALUES (?, ?, ?)";
	      PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	      statement.setInt(1, fv.getRecordId());
	      statement.setInt(2, fv.getFieldId());
	      statement.setString(3, fv.getValue());
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      i = statement.executeUpdate();
	      if(i > 0)
	    	  i = this.parseFirstInt(statement.getGeneratedKeys(), "id");
	    }catch(SQLException e){
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
	      try{
	        if(connection != null)
	          connection.close();
	      }catch(SQLException e){
	        // connection close failed.
	        System.err.println(e);
	      }finally{
	    	  return i;
	      }
	    }
	}

}

























