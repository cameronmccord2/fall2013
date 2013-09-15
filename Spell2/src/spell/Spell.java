package spell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import spell.Trie.Node;

public class Spell implements SpellCorrector{
	private Dictionary dictionary;
	
	public Spell(){
		dictionary = new Dictionary();
	}
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		dictionary = new Dictionary();
		Scanner s = null;
		try{
			s = new Scanner(new BufferedReader(new FileReader(dictionaryFileName)));
			s.useDelimiter(Pattern.compile("[^a-zA-Z]+"));
			while(s.hasNext()){
				dictionary.add(s.next());
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			s.close();
		}
	}

	@Override
	public String suggestSimilarWord(String inputWord)throws NoSimilarWordFoundException {
		Node temp = dictionary.find(inputWord);
		if(temp == null || temp.getValue() == 0){
			String d1 = dictionary.checkDistance1(inputWord);
			if(d1 == null){
				String d2 = dictionary.checkDistance2(inputWord);
				if(d2 == null){
					throw new NoSimilarWordFoundException();
				}else
					return d2;
			}else
				return d1;
		}
		else{
			System.out.println(dictionary.find(inputWord).getValue());
			System.out.println("returned inputed word");
			return inputWord;
		}
	}

}
