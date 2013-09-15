package listem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

// if no substrings matched do i still add the file to the map?


public interface Grep {
	
	/**
	 * Find lines that match a given pattern in files whose names match another
	 * pattern
	 * 
	 * @param directory The base directory to look at files from
	 * @param fileSelectionPattern Pattern for selecting file names
	 * @param substringSelectionPattern Pattern to search for in lines of a file
	 * @param recursive Recursively search through directories
	 * @return A Map containing files that had at least one match found inside them.
	 * Each file is mapped to a list of strings which are the exact strings from
	 * the file where the <code>substringSelectionPattern</code> was found.
	 */
	public Map<File, List<String>> grep(File directory, String fileSelectionPattern, 
			String substringSelectionPattern, boolean recursive);
	
	public class GrepClass extends GLSuper implements Grep {
		Map<File, List<String>> theMap;
		Pattern theSubstringSelectionPattern;
		boolean isRecursive;
		//List<String> theList;
		
		public GrepClass(){
			super();
			theMap = new HashMap<File, List<String>>();
			//theList = new ArrayList<String>();
		}
		@Override
		public Map<File, List<String>> grep(File directory,
				String fileSelectionPattern, String substringSelectionPattern,
				boolean recursive) {
			//Pattern theFilePattern = null;
			isRecursive = recursive;
			try{
				theFileSelectionPattern = Pattern.compile(fileSelectionPattern);
				theSubstringSelectionPattern = Pattern.compile(substringSelectionPattern);
			}catch(PatternSyntaxException pse){
				return null;
			}
			if(directory == null){
				return null;
			} else if(directory.isDirectory()){
				
			}else if(directory.isFile()){
				
			}else{
				return null;
			}
			countThese(directory);
			
			return null;
		}
		
		private Boolean countThese(File directory){
			if(directory.isDirectory()){
				if(directory.canRead()){
					File[] theFiles = directory.listFiles();
					if(theFiles != null){
						for(int i = 0; i < theFiles.length; i++){
							if(theFiles[i].isDirectory()){
								if(isRecursive){
									countThese(theFiles[i]);
								}
							}else if(theFiles[i].isFile()){
								countThese(theFiles[i]);
							}
						}
					}
				}else{
					System.err.println("countThese: cannot read directory");
				}
			} else if(directory.isFile()){
				if(directory.canRead()){
					if(theFileSelectionPattern.matcher(directory.getPath()).matches()){
						Scanner s = null;
						//int matchesThisFile = 0;
						boolean readThis = true;
						List<String> theList = new ArrayList<String>();
						try{
							s = new Scanner(new BufferedInputStream(new FileInputStream(directory)));
						}catch(FileNotFoundException fnfe){
							readThis = false;
							System.err.println("countThis: fileNotFoundException");
						}
						if(readThis){
							while(readThis){
								String theNextLine = null;
								if(s.hasNextLine()){
									theNextLine = s.nextLine();
									if(theSubstringSelectionPattern.matcher(theNextLine).matches()){
										theList.add(theNextLine);
									}
								}else{
									readThis = false;
								}
							}
							theMap.put(directory, theList);
						}
						
						
						s.close();
					}
				}else{
					System.err.println("countThese: cannot read directory");
				}
			}else{
				System.err.println("CountThese: badDirectory");
			}
			
			return true;
		}
		
	}
}
























