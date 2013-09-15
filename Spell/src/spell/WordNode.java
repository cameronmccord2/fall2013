package spell;

import spell.Trie.Node;

public class WordNode implements Trie.Node {
	public int count;
	public int letter;
	public WordNode[] nodes;
	
	
	public WordNode(char letter, boolean isWord){
		this.letter = letter - 'a';
		this.count = 0;
		if(isWord)
			this.count++;
		this.nodes = new WordNode[26];
	}

	@Override
	public int getValue() {
		return count;
	}
	
	private int makeWord(){
		count++;
		return count;
	}
	
	public void addWord(String word) {
		WordNode n = getOrMakeNodeForLetter(word.charAt(0), false);
		if(word.length() == 1)
			n.makeWord();
		else
			n.addWord(word.substring(1));
	}
	
	public WordNode getOrMakeNodeForLetter(char letter, boolean isWord){
		int index = letter - 'a';
		if(this.nodes[index] == null){
			this.nodes[index] = new WordNode(letter, isWord);
		}else{
			if(isWord)
				this.nodes[index].makeWord();
		}
		return this.nodes[index];
	}
	
	public WordNode getNodeForLetter(char letter){
		return this.nodes[letter - 'a'];
	}
	
	public WordNode isWordInThisTree(String word){
		if(word.length() == 0)
			return null;
		char c = word.charAt(0);
		if(getNodeForLetter(c) != null){
			if(word.length() == 1){
				if(getNodeForLetter(c).count > 0)
					return getNodeForLetter(c);
				else
					return null;
			}else
				return getNodeForLetter(c).isWordInThisTree(word.substring(1));
		}else
			return null;
	}

}
