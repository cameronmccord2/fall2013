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
	static public void countThese(File directory, boolean recursive, Pattern filePattern, Pattern subPattern, HashMap<File, List<String>> grepMap, HashMap<File, Integer> lineCountMap){
		if(directory.canRead()){
			if(directory.isDirectory()){
				File[] theFiles = directory.listFiles();
				if(theFiles != null){
					for(int i = 0; i < theFiles.length; i++){
						if(theFiles[i].isDirectory() && recursive || theFiles[i].isFile())
							countThese(theFiles[i], recursive, filePattern, subPattern, grepMap, lineCountMap);
					}
				}
			}else if(directory.isFile()){
				if(filePattern.matcher(directory.getPath()).matches()){// or getName?
					Scanner s = null;
					try{
						s = new Scanner(new BufferedInputStream(new FileInputStream(directory)));
						if(grepMap != null){// do grep
							List<String> theList = new ArrayList<String>();
							while(s.hasNextLine()){
								String theNextLine = s.nextLine();
								if(subPattern.matcher(theNextLine).matches())
									theList.add(theNextLine);
							}
							grepMap.put(directory, theList);
						}else{// do line counter
							int nuOfLines = 0;
							while(s.hasNextLine()){
								s.nextLine();
								nuOfLines++;
							}
							lineCountMap.put(directory, nuOfLines);
						}
					}catch(FileNotFoundException fnfe){
						System.err.println("countThis: fileNotFoundException");
					}finally{
						s.close();
					}
				}
			}else
				System.err.println("CountThese: not directory or file");
		}else
			System.err.println("cannot read directory: " + directory.getPath());
	}
}
