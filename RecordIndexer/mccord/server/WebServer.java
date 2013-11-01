package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import models.FailedResult;
import models.FalseResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import communicator.DownloadBatchParams;
import communicator.DownloadFileParams;
import communicator.GetFieldsParams;
import communicator.GetSampleImageParams;
import communicator.SearchParams;
import communicator.SubmitBatchParams;
import communicator.ValidateUserParams;
import dao.JDBCRecordIndexerDAO;

// TODO: Auto-generated Javadoc
/**
 * The Class WebServer.
 */
public class WebServer implements WebServerInterface{
	
//	private static final int SERVER_PORT_NUMBER = 8080;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private HttpServer server;
	private XStream xstream = new XStream(new DomDriver());
	
	private WebServer(){
		return;
	}
	
	private void run(Integer port){
		try{
			server = HttpServer.create(new InetSocketAddress(port), MAX_WAITING_CONNECTIONS);
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
			System.out.println("running on port: " + port);
		}catch(IOException e){
			System.out.println("could not create http server: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	private HttpHandler validateUserHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("validateUserHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			ValidateUserParams params = (ValidateUserParams)xstream.fromXML(exchange.getRequestBody());
			try{
				xstream.toXML(dao.validateUser(params), exchange.getResponseBody());
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			} catch (InvalidCredentialsException e) {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFalseResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler getProjectsHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("getProjectsHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			ValidateUserParams params = (ValidateUserParams)xstream.fromXML(exchange.getRequestBody());
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xstream.toXML(dao.getProjects(params), exchange.getResponseBody());
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler getSampleImageHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("getSampleImageHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			GetSampleImageParams params = (GetSampleImageParams)xstream.fromXML(exchange.getRequestBody());
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xstream.toXML(dao.getSampleImage(params), exchange.getResponseBody());
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler downloadBatchHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("downloadBatchHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			DownloadBatchParams params = (DownloadBatchParams)xstream.fromXML(exchange.getRequestBody());
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xstream.toXML(dao.downloadBatch(params), exchange.getResponseBody());
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler submitBatchHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("submitBatchHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			SubmitBatchParams params = (SubmitBatchParams)xstream.fromXML(exchange.getRequestBody());
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xstream.toXML(dao.submitBatch(params), exchange.getResponseBody());
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler getFieldsHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("getFieldsHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			GetFieldsParams params = (GetFieldsParams)xstream.fromXML(exchange.getRequestBody());
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xstream.toXML(dao.getFields(params), exchange.getResponseBody());
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler searchHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("searchHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			SearchParams params = (SearchParams)xstream.fromXML(exchange.getRequestBody());
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xstream.toXML(dao.search(params), exchange.getResponseBody());
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};

	private HttpHandler downloadFileHandler = new HttpHandler(){
		@Override
		public void handle(HttpExchange exchange)throws IOException{
			System.out.println("downloadFileHandler");
			JDBCRecordIndexerDAO dao = new JDBCRecordIndexerDAO();
			DownloadFileParams params = (DownloadFileParams)xstream.fromXML(exchange.getRequestBody());
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xstream.toXML(dao.downloadFile(params), exchange.getResponseBody());
			}catch(FailedException e){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
				xstream.toXML(getFailedResult(), exchange.getResponseBody());
			}
			exchange.getResponseBody().close();
		}
	};
	
	private FailedResult getFailedResult(){
		FailedResult f = new FailedResult();
		f.setValue("FAILED");
		return f;
	}
	
	private FalseResult getFalseResult(){
		FalseResult f = new FalseResult();
		f.setValue("FALSE");
		return f;
	}



	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		if(args.length != 1)
			throw new RuntimeException("invalid number of args passed web server: " + args.length);
		int port = Integer.parseInt(args[0]);
		WebServer server = new WebServer();
		server.run(port);
	}
}
