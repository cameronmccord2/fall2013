package importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.*;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.DomDriver;

import dao.JDBCRecordIndexerDAO;
import dao.RecordIndexerDAO;
import models.FieldValues;
import models.Fields;
import models.Images;
import models.IndexerData;
import models.Projects;
import models.ProjectsList;
import models.Records;
import models.Users;

/**
 * The Class DataImporter.
 */
public class DataImporter {

	/**
	 * Import archive into the database
	 *
	 * @param path the file path to the zip archive
	 */
	public void importArchive(String path) {
		System.out.println(path);
		String ourDataStore = "ourDataStore";
		// delete previous data
		try {
			deleteRecursive(new File(ourDataStore));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// unzip archive
		this.unZipIt(path, ourDataStore);
		System.out.println("unziped to: " + ourDataStore);
		// xstream
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("indexerdata", IndexerData.class);
		xstream.alias("user", Users.class);
		xstream.aliasField("firstname", Users.class, "firstName");
		xstream.aliasField("lastname", Users.class, "lastName");
		xstream.aliasField("username", Users.class, "userName");
		xstream.aliasField("indexedrecords", Users.class, "indexedRecords");
		xstream.alias("projects", ProjectsList.class);
		xstream.alias("project", Projects.class);
		xstream.aliasField("recordsperimage", Projects.class, "recordsPerImage");
		xstream.aliasField("firstycoord", Projects.class, "firstYCoor");
		xstream.aliasField("recordheight", Projects.class, "recordHeight");
		xstream.alias("indexerdata", IndexerData.class);
		xstream.alias("indexerdata", IndexerData.class);
		xstream.alias("indexerdata", IndexerData.class);
		String xmlFilePath = "failejd";
		try {
			xmlFilePath = this.getPathForXml(ourDataStore);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("xml file path: " + xmlFilePath);
		try {
			 xmlFilePath = this.getPathForXml(ourDataStore);
			System.out.println("xml file path: " + xmlFilePath);
			IndexerData indexerData = (IndexerData)xstream.fromXML(new File(xmlFilePath));
			System.out.println("xtreamed");
			this.importXml(indexerData);
			System.out.println("imported");
		} catch (FileNotFoundException e) {
			System.out.println("couldnt find xml file");
			e.printStackTrace();
		}
	}
	
	private void importXml(IndexerData id) {
		RecordIndexerDAO dao = new JDBCRecordIndexerDAO();
		for(Users u : id.getUsers()){
			dao.putUser(u);
		}
		for(Projects p : id.getProjects()){
			p.setId(dao.putProject(p));
			for(Fields f : p.getFields()){
				f.setProjectId(p.getId());
				f.setId(dao.putField(f));
			}
			for(Images i : p.getImages()){
				i.setProjectId(p.getId());
				i.setId(dao.putImage(i));
				for(Records r : i.getRecords()){
					r.setImageId(i.getId());
					r.setId(dao.putRecord(r));
					int count = 0;
					for(FieldValues fv : r.getFieldValues()){
						fv.setRecordId(r.getId());
						fv.setId(dao.putFieldValue(fv, p.getFields().get(count).getId()));
						count++;
					}
				}
			}
		}
	}

	private String getPathForXml(String ourDataPath) throws FileNotFoundException{
		System.out.println(ourDataPath);
		File f = new File(ourDataPath);
		if(f.isDirectory()){
			for(String p : f.list()){
				if(p.equals("Records.xml")){
					System.out.println("found a file thing: " + p);
					return ourDataPath + File.separator + p;
				}else if(p.equals("Records")){
					System.out.println("found a file thing: " + p);
					return this.getPathForXml(ourDataPath + File.separator + p);
				}
			}
		}else{
			if(f.getPath().equals("Records.xml"))
				System.out.println("found records.xml");
				return ourDataPath + File.separator + "Records.xml";
		}
			
		return "";
	}
	
	private boolean executeCommandLine(String command){
		try{
		Process tr = Runtime.getRuntime().exec( new String[]{ "cat" } );
		Writer wr = new OutputStreamWriter( tr.getOutputStream() );
		wr.write(command);
		wr.flush();
		}catch(IOException ioe){
			return false;
		}
		return true;
	}
	
    /**
     * By default File#delete fails for non-empty directories, it works like "rm". 
     * We need something a little more brutal - this does the equivalent of "rm -r"
     * @param path Root File Path
     * @return true iff the file and all sub files/directories have been removed
     * @throws FileNotFoundException
     */
    public static boolean deleteRecursive(File path) throws FileNotFoundException{
        if (!path.exists()) throw new FileNotFoundException(path.getAbsolutePath());
        boolean ret = true;
        if (path.isDirectory()){
            for (File f : path.listFiles()){
                ret = ret && deleteRecursive(f);
            }
        }
        return ret && path.delete();
    }
	
	private void unZipIt(String zipFile, String OUTPUT_FOLDER){
		byte[] buffer = new byte[1024];
		 
	     try{
	 
	    	//create output directory is not exists
	    	File folder = new File(OUTPUT_FOLDER);
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}
	 
	    	//get the zip file content
	    	System.out.println(zipFile);
	    	ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	 
	    	while(ze!=null){
	 
	    	   String fileName = ze.getName();
	    	   
	           File newFile = new File(OUTPUT_FOLDER + File.separator + fileName);
	           
	           if(ze.isDirectory()){
	    		   //System.out.println("creating directory: " + newFile.getAbsolutePath());
	    	   }else{
	    		   //System.out.println("file unzip : "+ newFile.getAbsoluteFile());
	    			 
		            //create all non exists folders
		            //else you will hit FileNotFoundException for compressed folder
		            new File(newFile.getParent()).mkdirs();
		 
		            FileOutputStream fos = new FileOutputStream(newFile);             
		 
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		            	fos.write(buffer, 0, len);
		            }
		 
		            fos.close();   
	    	   }
	           
	            ze = zis.getNextEntry();
	    	}
	 
	        zis.closeEntry();
	    	zis.close();
	 
	    	System.out.println("Done");
	 
	    }catch(IOException ex){
	       ex.printStackTrace(); 
	    }
	}

}
