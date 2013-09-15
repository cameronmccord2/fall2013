package evil;

import java.io.File;
import java.util.SortedSet;
import java.io.FileNotFoundException;

import evil.EvilHangmanGame.GuessAlreadyMadeException;

public class EvilHG {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		File dictionary = null;
		SortedSet<String> theList = null;
		EvilHangmanClass game = new EvilHangmanClass();
		int wordLength = 0, guesses = 0;
		if(args.length != 3){
			return;
		}
		try{
			dictionary = new File(args[0]);
			if(!dictionary.canRead()){
				System.err.println("Cannot read the file: " + args[0]);
				return;
			}
			wordLength = Integer.parseInt(args[1]);
			if(wordLength < 2){
				System.err.println("word length is: " + wordLength);
				return;
			}
			guesses = Integer.parseInt(args[2]);
			if(guesses < 1){
				System.err.println("guesses is: " + guesses);
				return;
			}
			
		}catch(NullPointerException npe){
			
		}catch(ArrayIndexOutOfBoundsException aioobe){
			System.err.println("Array out of bounds exception");
		}
		game.startGame(dictionary, wordLength);
		
		//game text here
		System.out.println("Welcome to Evil Hangman\nTotal Guesses: " + guesses + "\nWord Length: " + wordLength);
		while(guesses > 0){
			
			String theGuessS;
			System.out.println("Which letter will you guess? ");
			theGuessS = System.console().readLine();
			if(theGuessS.length() == 1){
				if(Character.isAlphabetic(theGuessS.charAt(0))){
					try{
						theList = game.makeGuess(theGuessS.toLowerCase().charAt(0));
						if(theList.size() == 1){
							System.out.println("You Win!\nYour Word: " + theList.toString());
						}else if(guesses != 0){
							guesses--;
							
							// add here, print current word data
							
							
							
							
						}else if(guesses == 0){
							System.out.println("You Loose! Game Over!\nThe word was: " + theList.first());
						}
					}catch(GuessAlreadyMadeException gexc){
						System.out.println("You have already guessed: " + theGuessS);
					}
				}else{
					System.out.println("Sorry, " + theGuessS + " is not one alphabetic character");
				}
			}else{
				
			}
		}
		
	}

}

/*
Usage: java [your main class name] dictionary wordLength guesses
dictionary is the path to a text file with whitespace separated words (no numbers,
punctuation, etc.)
wordLength is an integer ≥ 2.
guesses is an integer ≥ 1.*/