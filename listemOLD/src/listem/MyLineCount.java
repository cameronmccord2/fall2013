package listem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MyLineCount implements LineCounter {

	public MyLineCount(){
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
