package spell;

public class Words implements Trie {
	private WordNode rootNode;

	@Override
	public void add(String word) {
		rootNode.addWord(word);
	}
	
	@Override
	public Node find(String word) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWordCount() {
		return getNumberOfNodesFromHere(rootNode);
	}

	private int getNumberOfNodesFromHere(WordNode node) {
		if(node == null)
			return 0;
		int count = 0;
		for(int i = 0; i < 26; i++){
			
		}
		return count;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
