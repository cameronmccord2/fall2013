package importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.*;

import com.thoughtworks.xstream.XStream;

import models.IndexerData;

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
		String ourDataStore = "";
		// delete previous data
		try {
			deleteRecursive(new File(ourDataStore));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// unzip archive
		this.unZipIt(path, ourDataStore);
		// xstream
		XStream xstream = new XStream();
		try {
			IndexerData id = (IndexerData)xstream.fromXML(this.getPathForXml(path));
		} catch (FileNotFoundException e) {
			System.out.println("couldnt find xml file");
			e.printStackTrace();
		}
	}
	
	private String getPathForXml(String basePath) throws FileNotFoundException{
		File f = new File(basePath);
		if(f.isDirectory()){
			for(String p : f.list()){
				
			}
		}else
			throw new FileNotFoundException();
		return "";
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
	    	ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	 
	    	while(ze!=null){
	 
	    	   String fileName = ze.getName();
	           File newFile = new File(OUTPUT_FOLDER + File.separator + fileName);
	 
	           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
	 
	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParent()).mkdirs();
	 
	            FileOutputStream fos = new FileOutputStream(newFile);             
	 
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	       		fos.write(buffer, 0, len);
	            }
	 
	            fos.close();   
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
