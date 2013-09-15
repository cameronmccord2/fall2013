package spell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Dictionary {
	public Node dictionary;
	public ArrayList<String> delete1;
	public ArrayList<String> delete2;
	public ArrayList<String> transposition1;
	public ArrayList<String> transposition2;
	public ArrayList<String> alteration1;
	public ArrayList<String> alteration2;
	public ArrayList<String> insertion1;
	public ArrayList<String> insertion2;
	
	Dictionary(String filename){
		createDictionary(filename);
	}
	
	private void createDictionary(String filename){
		dictionary = new Node('0', false);
		Scanner s = null;
		try{
			s = new Scanner(new BufferedReader(new FileReader(filename)));
			s.useDelimiter(Pattern.compile("[^a-zA-Z]+"));
			while(s.hasNext()){
				addWord(s.next());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			s.close();
		}
	}
	
	private ArrayList<String> doDeletion(String word){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < word.length(); i++){
			list.add(word.substring(0, i) + word.substring(i + 1, word.length()));
		}
		return list;
	}
	
	private ArrayList<String> doAlterations(String word){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < word.length(); i++){
			for(int j = 0; j < 26; j++){
				list.add(word.substring(0, i) + (j + 'a') + word.substring(i + 1, word.length()));
			}
		}
		return list;
	}
	
	private ArrayList<String> doInsertions(String word){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < word.length(); i++){
			for(int j = 0; j < 26; j++){
				list.add(word.substring(0, i) + (j + 'a') + word.substring(i, word.length()));
			}
		}
		return list;
	}
	
	private ArrayList<String> doTranspositions(String word){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < word.length(); i++){
			for(int j = 0; j < 26; j++){
//				list.add(word.substring(0, i) + (j + 'a') + word.substring(i, word.length()));
			}
		}
		return list;
	}
	
	private boolean isWordInList(ArrayList<String> list, String word){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).equalsIgnoreCase(word))
				return true;
		}
		return false;
	}
	
	public boolean checkDeletion1(String word){
		delete1 = doDeletion(word);
		return isWordInList(delete1, word);
	}
	
	public boolean checkDeletion2(String word){
		delete2 = new ArrayList<String>();
		for(int i = 0; i < delete1.size(); i++){
			delete2.addAll(doDeletion(delete1.get(i)));
		}
		return isWordInList(delete2, word);
	}
	
	public boolean checkTransposition1(String word){
		transposition1 = doTranspositions(word);
		return isWordInList(transposition1, word);
	}
	
	public boolean checkTransposition2(String word){
		
		return false;
	}
	
	public boolean checkAlteration1(String word){
		alteration1 = doAlterations(word);
		return isWordInList(transposition1, word);
	}
	
	public boolean checkAlteration2(String word){
		
		return false;
	}
	
	public boolean checkInsertion1(String word){
		insertion1 = doInsertions(word);
		return isWordInList(insertion1, word);
	}
	
	public boolean checkInsertion2(String word){
		
		return false;
	}
	
	private void addWord(String word){
		dictionary.add(word);
	}
	
	public boolean isWordInDictionary(String word){
		return dictionary.isWordInThisTree(word);
	}
}
