package spell;

import java.io.IOException;

import spell.SpellCorrector.NoSimilarWordFoundException;

/**
 * A simple main class for running the spelling corrector
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws NoSimilarWordFoundException, IOException {
		
		String dictionaryFileName = args[0];
		String inputWord = args[1];
		
		/**
		 * Create an instance of your corrector here
		 */
		Spell corrector = new Spell();
		
		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);
		
		System.out.println("Suggestion is: " + suggestion);
//		System.out.println("to String: " + corrector.getDictionary().toString());
		System.out.println("node count: " + corrector.getDictionary().getNodeCount());
		System.out.println("word count: " + corrector.getDictionary().getWordCount());
	}

}
