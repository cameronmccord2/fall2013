package listem;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		
		public GrepClass(){
			super();
		}
		
		@SuppressWarnings("finally")
		@Override
		public Map<File, List<String>> grep(File directory, String fileSelectionPattern, String substringSelectionPattern, boolean recursive) {
			if(directory == null)
				return null;
			HashMap<File, List<String>> theMap = new HashMap<File, List<String>>();
			try{
				FileManager.countThese(directory, recursive, Pattern.compile(fileSelectionPattern), Pattern.compile(substringSelectionPattern), theMap, null);
			}catch(PatternSyntaxException pse){
				return null;
			}finally{
				return theMap;
			}
		}
	}
}
























