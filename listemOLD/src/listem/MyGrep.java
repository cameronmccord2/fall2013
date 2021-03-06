package listem;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MyGrep implements Grep {

	public MyGrep(){
		super();
	}
	
	@SuppressWarnings("finally")
	@Override
	public Map<File, List<String>> grep(File directory, String fileSelectionPattern, String substringSelectionPattern, boolean recursive) {
		if(directory == null){
			System.out.println("directory is null");
			return null;
		}
		HashMap<File, List<String>> theMap = new HashMap<File, List<String>>();
		try{
			FileManager.countThese(directory, recursive, Pattern.compile(fileSelectionPattern), Pattern.compile(substringSelectionPattern), theMap, null);
		}catch(PatternSyntaxException pse){
			pse.printStackTrace();
			return null;
		}finally{
			return theMap;
		}
	}

}
