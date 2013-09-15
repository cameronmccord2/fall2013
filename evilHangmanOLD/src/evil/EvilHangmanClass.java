package evil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class EvilHangmanClass implements EvilHangmanGame {
	SortedSet<String> dic;
	Iterator it;
	SortedSet<String> lettersGuessed;
	SortedSet<String> currDic;
	
	public EvilHangmanClass(){
		dic = new TreeSet<String>();
		it = dic.iterator();
		lettersGuessed = new TreeSet<String>();
		currDic = new TreeSet<String>();
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
					dic.add(word);
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
		if(lettersGuessed.contains(theGuess)){
			System.err.println("Guess has been guessed: " + guess);
			throw new GuessAlreadyMadeException();
		}
		
		return currDic;
	}

}
