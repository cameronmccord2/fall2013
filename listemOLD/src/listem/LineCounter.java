package listem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public interface LineCounter {
	
	/**
	 * Count the number of lines in files whose names match a given pattern.
	 * 
	 * @param directory The base directory to look at files from
	 * @param fileSelectionPattern Pattern for selecting file names
	 * @param recursive Recursively search through directories
	 * @return A Map containing files whose lines were counted. Each file is mapped
	 * to an integer which is the number of lines counted in the file.
	 */
	public Map<File, Integer> countLines(File directory, String fileSelectionPattern, 
			boolean recursive);
	
	public class LineCounterClass extends GLSuper implements LineCounter{
		Map<File, Integer> theMap;
		boolean isRecursive;
		
		public LineCounterClass(){
			super();
			theMap = new HashMap<File, Integer>();
		}
		@Override
		public Map<File, Integer> countLines(File directory,
				String fileSelectionPattern, boolean recursive) {
			isRecursive = recursive;
			try{
			theFileSelectionPattern = Pattern.compile(fileSelectionPattern);	
			}catch(PatternSyntaxException p){
				return null;
			}
			if(directory.isDirectory()){
				
			}else if(directory.isFile()){
				
			}else{
				return null;
			}
			
			countThese(directory);
			return theMap;
		}
		
		private Boolean countThese(File directory)
		{
			if(directory != null)
			{
				//System.out.println(directory.getAbsolutePath());
				if(directory.isDirectory()){
					if(directory.canRead()){
						File[] theFiles = directory.listFiles();
						if(theFiles != null){
							System.out.println(theFiles.length);
							for(int i = 0; i < theFiles.length; i++)
							{
								if(theFiles[i].isDirectory()){
									if(isRecursive){
										countThese(theFiles[i]);
									}
								} else if(theFiles[i].isFile()){
									countThese(theFiles[i]);
								} else {
									System.err.println("countThese: theFiles[i] gave not a file or directory");
								}
							}
						}
					}else{
						System.err.println("countThese: cannot read directory");
					}
				} else if(directory.isFile()){
					if(directory.canRead()){
						//Matcher matcher = theFileSelectionPattern.matcher(directory.getName());
						if(theFileSelectionPattern.matcher(directory.getName()).matches()){
							if(directory.canRead()){
								Boolean readThis = true;
								int nuOfLines = 0;
								Scanner s = null;
								try{
									s = new Scanner(new BufferedInputStream(new FileInputStream(directory)));
								}catch(FileNotFoundException fnfe){
									System.err.println("countThese: FileNotFouExc. should never reach here");
									readThis = false;
								}
								if(readThis){
									while(readThis){
										if(s.hasNextLine()){
											s.nextLine();
											nuOfLines++;
										}else{
											readThis = false;
										}
									}
									theMap.put(directory, nuOfLines);
								}
								s.close();
							}else{
								System.err.println("countThese: cannot read directory");
							}
						}
					}
					
				
				}
			}
			
			return true;
		}
		
	}
	
}































