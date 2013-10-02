package hangman;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
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
//		for(int i = 0; i < 100000; i++){
//			System.out.println("teast");
////			Random ran = new Random();
//			int tooMany = 0;
//			int howManyGuessed = 0;
//			while(guesses > 0){
////				int x =  (int)Math.floor(Math.random()*26);
//				try {
//					howManyGuessed++;
//					if(howManyGuessed == 26)
//						howManyGuessed = 0;
//					if(eh.makeGuess(((char)('a' + howManyGuessed))).size() == 1)
//						break;
//				} catch (GuessAlreadyMadeException e) {
//					tooMany++;
//					guesses++;
//				}
//				guesses--;
//				if(tooMany > 1000){
//					System.out.println("too many guesses already made");
//					break;
//				}
////				System.out.println("here");
//			}
////			if(i % 100 == 0)
//				System.out.println("done: " + i);
//			guesses = Integer.parseInt(args[2]);
//		}
//		boolean playGame = true;
		//
//		ArrayList<String> list = new ArrayList<String>();
//		list.add("c");
//		list.add("r");
//		list.add("e");
//		list.add("g");
//		list.add("j");
//		list.add("b");
//		list.add("t");
//		list.add("u");
//		list.add("w");
//		list.add("d");
//		list.add("f");
//		list.add("n");
//		list.add("l");
//		list.add("h");
//		list.add("x");
//		list.add("i");
//		list.add("o");
//		for(int i = 0; i < list.size(); i++){
//			try {
//				eh.makeGuess(list.get(i).charAt(0));
//			} catch (GuessAlreadyMadeException e) {
//				// TODO Auto-generated catch block
////				e.printStackTrace();
//			}
//		}
//		System.out.println(eh.dictionary.lastSet.toString());
		//
		Console c = System.console();
		char lastGuess = 'a';
		while(guesses > 0){
			System.out.println("You have " + guesses + " left.");
			System.out.println("Used letters: " + eh.getGuessedCharacters());
			System.out.println("Word: " + eh.getWordSoFar());
			System.out.print("Make Guess: ");
			String lastLine = c.readLine();
			if(lastLine.length() != 1)
				System.out.println("you must enter a char of length 1");
			else{
				lastGuess = lastLine.charAt(0);
				if(lastGuess - 'a' > -1 && lastGuess - 'a' < 26){
					try {
						guesses--;
//						System.out.println(eh.makeGuess(lastGuess));
						if(eh.makeGuess(lastGuess).size() == 1 && eh.isFinalWord()){
							System.out.println("You guessed the word: " + eh.getWordSoFar());
							break;
						}
						
					} catch (GuessAlreadyMadeException e) {
						System.out.println("you already guessed " + lastGuess);
						guesses++;
					}
				}else
					System.out.println("you must chose a member of the alphabet");
			}
			
		}
		for(int i = 0; i < eh.getWordSoFar().length(); i++){
			if(eh.getWordSoFar().charAt(i) == '-'){
				System.out.println("haha, you werent able to guess the word: " + eh.getAWord());
				break;
			}
		}
		System.out.println("done");
	}
	
	private boolean isFinalWord(){
		for(int i = 0; i < this.getWordSoFar().length(); i++){
			if(this.getWordSoFar().charAt(i) == '-'){
				return false;
			}
		}
		return true;
	}
	private String getWordSoFar() {
		if(this.dictionary.lastSet == null)
			System.out.println("null");
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
	
	private String getAWord(){
		for(String word : this.dictionary.lastSet){
			return word;
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
