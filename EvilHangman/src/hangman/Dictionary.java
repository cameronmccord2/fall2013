package hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Dictionary {
	public Set<String> lastSet;
	public Integer wordLength;
	
	public Dictionary(File dictionaryFile, int wordLength){
		System.out.println("allocated dictionary, word length: " + wordLength);
		this.wordLength = wordLength;
		this.lastSet = new HashSet<String>();
		Scanner s = null;
		try{
			s = new Scanner(new BufferedReader(new FileReader(dictionaryFile)));
			while(s.hasNext()){
				String newWord = s.next().toLowerCase();
				if(newWord.length() == wordLength){
					this.lastSet.add(newWord);
//					System.out.println(newWord);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			System.out.println("words: " + this.lastSet.size());
			s.close();
		}
	}
	
	public Set<String> makeGuess(char guess){
//		System.out.println("Making guess: " + guess);
		HashMap<String, Set<String>> newMap = new HashMap<String, Set<String>>();
		for(String word : this.lastSet){
			StringBuilder sb = new StringBuilder("");
			for(int j = 0; j < word.length(); j++){
				if(word.charAt(j) == guess)
					sb.append(guess);
				else
					sb.append("-");
			}
			if(!newMap.containsKey(sb.toString()))
				newMap.put(sb.toString(), new HashSet<String>());
			newMap.get(sb.toString()).add(word);
		}
		Set<String> keySet = newMap.keySet();
		int length = 0;
		ArrayList<String> keys = new ArrayList<String>();
		for(String key : keySet){
			if(newMap.get(key).size() > length){
				length = newMap.get(key).size();
				keys = new ArrayList<String>();
				keys.add(key);
			}else if(newMap.get(key).size() == length)
				keys.add(key);
		}
		if(keys.size() == 1){// only found 1 that was longest
//			System.out.println("success 1");
			this.lastSet = newMap.get(keys.get(0));
			return this.lastSet;
		}else{// must do other tests
			StringBuilder blankKey = new StringBuilder("");
			for(int i = 0; i < keys.get(0).length(); i++){
				blankKey.append("-");
			}
			if(keys.contains(blankKey.toString())){
				this.lastSet = newMap.get(blankKey.toString());// has the blank set, return it
				return this.lastSet;
			}
			// count how many of this guessed letter are in each key
			HashMap<String, ArrayList<Integer>> countMap = new HashMap<String, ArrayList<Integer>>();
			for(String key : keys){
				ArrayList<Integer> indexList = new ArrayList<Integer>();
				countMap.put(key, indexList);
				for(int i = 0; i < key.length(); i++){
					if(key.charAt(i) == guess)
						indexList.add(i);
				}
			}
			// find the one(s) that have the least count
			HashMap<String, ArrayList<Integer>> mostCountMap = new HashMap<String, ArrayList<Integer>>();
			int charCount = Integer.MAX_VALUE;
			Set<String> countKeys = countMap.keySet();
			for(String countKey : countKeys){
				if(countMap.get(countKey).size() < charCount){
					mostCountMap = new HashMap<String, ArrayList<Integer>>();
					mostCountMap.put(countKey, countMap.get(countKey));
					charCount = countMap.get(countKey).size();
				}else if(countMap.get(countKey).size() == charCount){
					mostCountMap.put(countKey, countMap.get(countKey));
				}
			}
			if(mostCountMap.keySet().size() == 1){
//				System.out.println("success 2");
				for(String tempKey : mostCountMap.keySet()){// only one has the most occurrances of the guessed character
					this.lastSet = newMap.get(tempKey);
					return this.lastSet;
				}
			}else{
				Set<String> newKeySet = mostCountMap.keySet();
				int endIndex = this.wordLength;
				while(true){
					ArrayList<String> rightMostKeys = this.getRightMostCharacterKeyList(newKeySet, guess, endIndex);
					if(rightMostKeys.size() == 0)
						throw new RuntimeException("error, no chars returned from getRightMostCharacterKeyList");
					if(rightMostKeys.size() == 1){
						this.lastSet = newMap.get(rightMostKeys.get(0));
						return this.lastSet;
					}else{
						newKeySet = new HashSet<String>();
						for(String tempKey : rightMostKeys){
							newKeySet.add(tempKey);
							endIndex = tempKey.substring(0, endIndex).lastIndexOf(guess);
						}
					}
				}
//				ArrayList<String> rightMostCharKeys = new ArrayList<String>();
//				int rightMostIndex = 0;
//				for(String rightMostKey : mostCountMap.keySet()){
//					if(rightMostKey.lastIndexOf(guess) > rightMostIndex){
//						rightMostIndex = rightMostKey.lastIndexOf(guess);
//						rightMostCharKeys = new ArrayList<String>();
//						rightMostCharKeys.add(rightMostKey);
//					}else if(rightMostKey.lastIndexOf(guess) == rightMostIndex){
//						rightMostCharKeys.add(rightMostKey);
//					}
//				}
//				if(rightMostCharKeys.size() == 1){
//					this.lastSet = newMap.get(rightMostCharKeys.get(0));
//					return this.lastSet;
//				}
			}
		}
		System.out.println("got here, shouldnt have");
		return null;
	}
	
	private ArrayList<String> getRightMostCharacterKeyList(Set<String> keySet, char guess, int endIndex){
		ArrayList<String> rightMostCharKeys = new ArrayList<String>();
		int rightMostIndex = 0;
		for(String rightMostKey : keySet){
			if(rightMostKey.substring(0, endIndex).lastIndexOf(guess) > rightMostIndex){
				rightMostIndex = rightMostKey.substring(0, endIndex).lastIndexOf(guess);
				rightMostCharKeys = new ArrayList<String>();
				rightMostCharKeys.add(rightMostKey);
			}else if(rightMostKey.substring(0, endIndex).lastIndexOf(guess) == rightMostIndex){
				rightMostCharKeys.add(rightMostKey);
			}
		}
		System.out.println("keys");
		System.out.println(rightMostCharKeys);
		return rightMostCharKeys;
	}
	
//	private void addValueToKeyForMap(String value, String key, HashMap<String, Set<String>> map){
//		if(!map.containsKey(key)){
//			map.put(key, new TreeSet<String>());
//		}
//		map.get(key).add(value);
//	}
	
//	private void addToMap(HashMap<String, Set<String>> map, String word){
//		StringBuilder sb = new StringBuilder("");
//		for(int i = 0; i < word.length(); i++){
//			sb.append("-");
//		}
//		String key = sb.toString();
//		this.addValueToKeyForMap(word, key, map);
//	}
}
