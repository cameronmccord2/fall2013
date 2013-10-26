package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.rmi.ServerException;
import java.util.ArrayList;

import models.Users;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
import dao.JDBCRecordIndexerDAO;

// TODO: Auto-generated Javadoc
/**
 * The Class WebServer.
 */
public class WebServer implements WebServerInterface{
	
	private static final int SERVER_PORT_NUMBER = 8080;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private HttpServer server;
	private XStream xstream = new XStream(new DomDriver());
	
	private WebServer(){
		return;
	}
	
	private void run(){
		try{
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
		}catch(IOException e){
			System.out.println("could not create http server: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		server.setExecutor(null);
		
		server.createContext("/validateUser", validateUserHandler);
		server.createContext("/getProjects", getProjectsHandler);
		server.createContext("/getSampleImage", getSampleImageHandler);
		server.createContext("/downloadBatch", downloadBatchHandler);
		server.createContext("/submitBatch", submitBatchHandler);
		server.createContext("/getFields", getFieldsHandler);
		server.createContext("/search", searchHandler);
		server.createContext("/downloadFile", downloadFileHandler);
		
		server.start();
	}
	
	private HttpHandler validateUserHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			ValidateUserResult result = null;
			ValidateUserParams params = (ValidateUserParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.validateUser(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			} catch (InvalidCredentialsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler getProjectsHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			ArrayList<GetProjectsResult> result = null;
			ValidateUserParams params = (ValidateUserParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.getProjects(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler getSampleImageHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			GetSampleImageResult result = null;
			GetSampleImageParams params = (GetSampleImageParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.getSampleImage(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler downloadBatchHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			DownloadBatchResult result = null;
			DownloadBatchParams params = (DownloadBatchParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.downloadBatch(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler submitBatchHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			SubmitBatchResult result = null;
			SubmitBatchParams params = (SubmitBatchParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.submitBatch(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler getFieldsHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			ArrayList<GetFieldsResult> result = null;
			GetFieldsParams params = (GetFieldsParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.getFields(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler searchHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			ArrayList<SearchResult> result = null;
			SearchParams params = (SearchParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.search(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler downloadFileHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			DownloadFileResult result = null;
			DownloadFileParams params = (DownloadFileParams)xstream.fromXML(exchange.getRequestBody());
			try{
				result = dao.downloadFile(params);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
				return;
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xstream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
	};



	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		if(args.length != 1)
			throw new RuntimeException("invalid number of args passed web server: " + args.length);
		int port = Integer.parseInt(args[0]);
		
	}
}
