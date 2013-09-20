package listem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileManager {
	static private HashMap<File, List<String>> countThese(File directory, boolean recursive, Pattern filePattern, Pattern subPattern, HashMap<File, List<String>> map){
		if(directory.isDirectory()){
			if(directory.canRead()){
				File[] theFiles = directory.listFiles();
				if(theFiles != null){
					for(int i = 0; i < theFiles.length; i++){
						if(theFiles[i].isDirectory()){
							if(recursive){
								countThese(theFiles[i], recursive, filePattern, subPattern, map);
							}
						}else if(theFiles[i].isFile()){
							countThese(theFiles[i], recursive, filePattern, subPattern, map);
						}
					}
				}
			}else{
				System.err.println("countThese: cannot read directory");
			}
		} else if(directory.isFile()){
			if(directory.canRead()){
				if(filePattern.matcher(directory.getPath()).matches()){// or getName?
					Scanner s = null;
					//int matchesThisFile = 0;
					boolean readThis = true;
					List<String> theList = new ArrayList<String>();
					try{
						s = new Scanner(new BufferedInputStream(new FileInputStream(directory)));
						if(readThis){
							while(readThis){
								String theNextLine = null;
								if(s.hasNextLine()){
									theNextLine = s.nextLine();
									if(subPattern.matcher(theNextLine).matches()){
										theList.add(theNextLine);
									}
								}else{
									readThis = false;
								}
							}
							map.put(directory, theList);
						}
					}catch(FileNotFoundException fnfe){
						readThis = false;
						System.err.println("countThis: fileNotFoundException");
					}finally{
						s.close();
					}
				}
			}else{
				System.err.println("countThese: cannot read directory");
			}
		}else{
			System.err.println("CountThese: badDirectory");
		}
		return map;
	}
}
