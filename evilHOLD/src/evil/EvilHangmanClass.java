package evil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class EvilHangmanClass implements EvilHangmanGame {
	SortedSet<String> dic;
	Iterator it;
	SortedSet<String> lettersGuessed;
	SortedSet<String> currDic;
	int theWordLength;
	Map<Pattern, TreeSet<String>> matchingWords;
	
	public EvilHangmanClass(){
		dic = new TreeSet<String>();
		it = dic.iterator();
		lettersGuessed = new TreeSet<String>();
		currDic = new TreeSet<String>();
		matchingWords = new HashMap<Pattern, TreeSet<String>>();
	}
	/**
	 * Starts a new game of evil hangman using words from <code>dictionary</code>
	 * with length <code>wordLength</code>
	 * 
	 * @param dictionary Dictionary of words to use for the game
	 * @param wordLength Number of characters in the word to guess
	 */
	@Override
	public void startGame(File dictionary, int wordLength) {
		theWordLength = wordLength;
		Scanner sdic = null;
		try{
			if(!dictionary.canRead()){
				System.err.println("Cannot read the file: " + dictionary.getAbsolutePath());
				return;
			}
			if(wordLength < 2){
				System.err.println("Word length too small: " + wordLength);
				return;
			}
			sdic = new Scanner(new BufferedInputStream(new FileInputStream(dictionary)));
			if(!sdic.hasNext()){
				System.err.println("scanner just created but doesnt have next");
				return;
			}
			
			// read in dictionary file, assume no problems in dictionary file?
			while(sdic.hasNext()){
				String word = sdic.next();
				if(word.length() == wordLength){
					dic.add(word.toLowerCase());
				}
			}
			
			
		}catch(FileNotFoundException fnfe){
			System.err.println("dictionary file not found: " + dictionary.getAbsolutePath());
		}finally{
			sdic.close();
		}
		return;
	}
	
	/**
	 * Make a guess in the current game.
	 * 
	 * @param guess The character being guessed
	 * @return The set of strings that satisfy all the guesses made so far
	 * in the game, including the guess made in this call. The game could claim
	 * that any of these words had been the secret word for the whole game. 
	 * 
	 * @throws GuessAlreadyMadeException If the character <code>guess</code> 
	 * has already been guessed in this game.
	 */
	@Override
	public SortedSet<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		String theGuess = String.valueOf(guess);
		SortedSet<String> newDic = new TreeSet<String>();
		Vector<Vector<String>> theV = new Vector<Vector<String>>();
		Vector<Vector<String>> theV2 = new Vector<Vector<String>>();
		theV.add(new Vector<String>(theV2.elementAt(0)));
		if(lettersGuessed.contains(theGuess)){
			System.err.println("Guess has been guessed: " + guess);
			throw new GuessAlreadyMadeException();
		}
		Vector<Pattern> vecPat = new Vector<Pattern>();
		Vector<TreeSet<String>> theList = new Vector<TreeSet<String>>();
		Vector<TreeSet<String>> theList2 = new Vector<TreeSet<String>>();
		StringBuilder make = new StringBuilder("[");
		make.append(guess);
		make.append("]");
		String useThis = make.toString();
		make = new StringBuilder("[");
		make.append("^");
		make.append(guess);
		make.append("]");
		String dontUseThis = make.toString();
		make = new StringBuilder("");
		StringBuilder template = new StringBuilder("");
		for(int i = 0; i < theWordLength; i++){
			make.append(useThis);
			template.append("u");
		}
		
		make = new StringBuilder("");
		for(int i = 0; i < theWordLength; i++){
			make.append(dontUseThis);
		}
		
		
		int numberToUseToBegin = 0;
		int numberToEndAt = 0;
		String beginString = null;
		String endString = null;
		for(Integer i = 1; i < Integer.MAX_VALUE;){
			if(Integer.toBinaryString(i).length() > theWordLength){
				numberToUseToBegin = i;
				beginString = Integer.toBinaryString(i);
				endString = Integer.toBinaryString(i*2-1);
				numberToEndAt = i *2;
				break;
			}
			i *= 2;
		}
		
		for(int i = numberToUseToBegin; i < numberToEndAt; i++){
			String temp = Integer.toBinaryString(i);
			StringBuilder newSB = new StringBuilder("");
			for(int j = 0; j < temp.length(); j++){
				if(temp.charAt(j) == '0'){
					newSB.append(useThis);
				}else{
					newSB.append(dontUseThis);
				}
			}
			vecPat.add(Pattern.compile(newSB.toString()));
			theList.add(new TreeSet<String>());
		}
		for(int i = 0; i < currDic.size(); i++){
			String currWord = currDic.first();
			currDic.remove(currWord);
			for(int j = 0; j < vecPat.size(); j++){
				if(vecPat.elementAt(j).matcher(currWord).matches()){
					theList.elementAt(j).add(currWord);
					break;
				}
			}
		}
		theList2.add(theList.elementAt(0));				// take biggest list
		for(int i = 1; i < theList.size(); i++){
			if(theList2.elementAt(0).size() < theList.elementAt(i).size()){
				theList2.removeAllElements();
				theList2.add(theList.elementAt(i));
			}else if(theList2.elementAt(0).size() == theList.elementAt(i).size()){
				theList2.add(theList.elementAt(i));
			}
		}
		theList = theList2;
		if(theList.size() == 1){
			currDic = theList.elementAt(0);
			return currDic;
		}else if(theList.size() == 0){
			System.err.println("The list is empty");
			
		}else{
			if(theList.elementAt(theList.size()-1).size() > 0){// take list without the new letter
				currDic = theList.elementAt(theList.size()-1);
				return currDic;
			}else{// take the list with the least last guessed letter, change all lists to vectors or use iterator
				
				// take group with rightmost letter
				
				
				// repeat until chosen a group
			}
		}
		return currDic;
	}

}














































































