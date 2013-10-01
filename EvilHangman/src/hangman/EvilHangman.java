package hangman;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class EvilHangman implements EvilHangmanGame {
	private Dictionary dictionary;
	private Set<String> guessedCharacters;
	
	public EvilHangman(){
		
	}

	@Override
	public void startGame(File dictionary, int wordLength) {
		this.dictionary = new Dictionary(dictionary, wordLength);
		this.guessedCharacters = new TreeSet<String>();
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		if(this.guessedCharacters.contains("" + guess))
			throw new GuessAlreadyMadeException();
		this.guessedCharacters.add("" + guess);
		return dictionary.makeGuess(guess);
	}

	public static void main(String[] args) {
		if(args.length > 3)
			return;
		EvilHangman eh = new EvilHangman();
		int guesses = Integer.parseInt(args[2]);
		eh.startGame(new File(args[0]), Integer.parseInt(args[1]));
//		for(int i = 0; i < 26; i++){
//			try{
//				System.out.println(((char)(i + 'a')));
//				Set<String> set = eh.makeGuess(((char)(i + 'a')));
//				System.out.println(set.size());
//				if(set.size() == 1){
//					System.out.println(set.toString());
//					break;
//				}
//				
//			}catch(GuessAlreadyMadeException e){
//				System.out.println("exception");
//				e.printStackTrace();
//			}
//		}
		boolean playGame = true;
		Console c = System.console();
		char lastGuess = 'a';
		while(guesses > 0){
			System.out.println("You have " + guesses + " left.");
			System.out.println("Used letters: " + eh.getGuessedCharacters());
			System.out.println("Word: " + eh.getWordSoFar());
			System.out.print("Make Guess: ");
			try {
				lastGuess = c.readLine().charAt(0);
				if(eh.makeGuess(lastGuess).size() == 1)
					System.out.println("You guessed the word: " + eh.getWordSoFar());
			} catch (GuessAlreadyMadeException e) {
				System.out.println("you already guessed " + lastGuess);
			}
			guesses--;
		}
		for(int i = 0; i < eh.getWordSoFar().length(); i++){
			if(eh.getWordSoFar().charAt(i) == '-'){
				System.out.println("haha, you werent able to guess the word");
				break;
			}
		}
		System.out.println("done");
	}

	private String getWordSoFar() {
		for(String word : this.dictionary.lastSet){
			StringBuilder sb = new StringBuilder("");
			for(int i = 0; i < word.length(); i++){
				if(this.guessedCharacters.contains("" + word.charAt(i)))
					sb.append(word.charAt(i));
				else
					sb.append("-");
			}
			return sb.toString();
		}
		return null;
	}

	private String getGuessedCharacters() {
		StringBuilder sb = new StringBuilder("");
		for(String character : this.guessedCharacters){
			sb.append(character);
		}
		return sb.toString();
	}

}
