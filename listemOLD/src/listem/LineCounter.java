package listem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
		
		public LineCounterClass(){
			super();
		}
		
		@SuppressWarnings("finally")
		@Override
		public Map<File, Integer> countLines(File directory, String fileSelectionPattern, boolean recursive) {
			if(directory == null)
				return null;
			HashMap<File, Integer> theMap = new HashMap<File, Integer>();
			try{
				FileManager.countThese(directory, recursive, Pattern.compile(fileSelectionPattern), null, null, theMap);
			}catch(PatternSyntaxException pse){
				return null;
			}finally{
				return theMap;
			}
		}
	}
}































