package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import communicator.DownloadBatchParams;
import communicator.DownloadFileParams;
import communicator.GetFieldsParams;
import communicator.GetSampleImageParams;
import communicator.SearchParams;
import communicator.SubmitBatchParams;
import communicator.ValidateUserParams;

public class ClientCommunicator implements ClientCommunitcatorInterface{

	private Object sendPost(Object body, String urlPart, String host, String port) throws Exception {
		 
		String url = "http://" + host + ":" + port + "/" + urlPart;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setDoOutput(true);
		XStream xstream = new XStream(new DomDriver());
		xstream.toXML(body, con.getOutputStream());

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.flush();
		wr.close();// sends request
 
		System.out.println("Sending request to URL : " + url);
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		return xstream.fromXML(in);
	}
	
	@Override
	public Object validateUser(ValidateUserParams params, String host, String port) throws Exception {
		return this.sendPost(params, "validateUser", host, port);
	}

	@Override
	public Object getProjects(ValidateUserParams params, String host, String port)throws Exception {
		return this.sendPost(params, "getProjects", host, port);
	}

	@Override
	public Object getSampleImage(GetSampleImageParams params, String host, String port)throws Exception {
		return this.sendPost(params, "getSampleImage", host, port);
	}

	@Override
	public Object downloadBatch(DownloadBatchParams params, String host, String port)throws Exception {
		return this.sendPost(params, "downloadBatch", host, port);
	}

	@Override
	public Object submitBatch(SubmitBatchParams params, String host, String port)throws Exception {
		return this.sendPost(params, "submitBatch", host, port);
	}

	@Override
	public Object getFields(GetFieldsParams params, String host, String port)throws Exception {
		return this.sendPost(params, "getFields", host, port);
	}

	@Override
	public Object search(SearchParams params, String host, String port)throws Exception {
		return this.sendPost(params, "search", host, port);
	}

	@Override
	public Object downloadFile(DownloadFileParams params, String host, String port)throws Exception {
		return this.sendPost(params, "downloadFile", host, port);
	}
}
