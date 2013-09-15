package spell;

public class Spell {
	
	
	public static void main(String[] args){
		if(args.length != 2)
			return;
		Dictionary d = new Dictionary(args[0]);
		if(d.isWordInDictionary(args[1])){
			return;
		}else{
			
		}
	}
}
