package spell;

public class Node implements Trie {
	public Node[] nodes;
	public int letter;
	public int count;
	
	public Node(int letter, boolean isWord){
		this.letter = letter;
		this.count = 0;
		if(isWord)
			this.count++;
		this.nodes = new Node[26];
	}
	
	public Node(String letter, boolean isWord){
		this.letter = letter.charAt(0) - 'a';
		this.count = 0;
		if(isWord)
			this.count++;
		this.nodes = new Node[26];
	}
	
	public Node(char letter, boolean isWord){
		this.letter = letter - 'a';
		this.count = 0;
		if(isWord)
			this.count++;
		this.nodes = new Node[26];
	}
	
	public Node getOrMakeNodeForLetter(char letter, boolean isWord){
		int index = letter - 'a';
		if(this.nodes[index] == null){
			this.nodes[index] = new Node(letter, isWord);
		}else{
			if(isWord)
				this.nodes[index].makeWord();
		}
		return this.nodes[index];
	}
	
	
	
	
	
	@Override
	public Node find(String word){
		return isWordInThisTree(word);
	}
	
	private int makeWord(){
		this.count++;
		return count;
	}

	@Override
	public void add(String word) {
		Node n = getOrMakeNodeForLetter(word.charAt(0), false);
		if(word.length() == 1)
			n.makeWord();
		else
			n.addWord(word.substring(1));
		
	}

	@Override
	public int getNodeCount() {
		return getHowManyNodes(dictionary);
	}

	private int getHowManyNodes(Node node) {
		
	}

	@Override
	public int getWordCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
