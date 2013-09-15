package spell;

import java.util.ArrayList;
import java.util.Collections;

public class Dictionary implements Trie{
	public Node rootNode;
    private int uniqueWords;
    public ArrayList<String> distance1;
    public ArrayList<String> distance2;
    public ArrayList<String> delete1;
    public ArrayList<String> delete2;
    public ArrayList<String> transposition1;
    public ArrayList<String> transposition2;
    public ArrayList<String> alteration1;
    public ArrayList<String> alteration2;
    public ArrayList<String> insertion1;
    public ArrayList<String> insertion2;
    
    @Override
	public String toString(){
    	ArrayList<String> allWords = this.alphabatizeList(rootNode.getAllUniqueWords(""));
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < allWords.size(); i++){
    		sb.append(allWords.get(i));
    		sb.append(" ");
    		sb.append(rootNode.findWord(allWords.get(i)).getValue());
    		sb.append("\n");
    	}
    	return sb.toString();
    }
	
	@Override
	public int hashCode(){
		return rootNode.getAllUniqueWords("").size() * rootNode.getHowManyNodes() * 97;
	}
	
	@Override
	public boolean equals(Object o){
		Dictionary temp = (Dictionary)o;
		return this.rootNode.equals(temp.rootNode);
	}
	
	public Dictionary(){
		rootNode = new Node(-1);
        uniqueWords = 0;
	}

	@Override
	public void add(String word) {
        if(rootNode.addWord(word).getValue() == 1)
            uniqueWords++;
    }

	@Override
	public Node find(String word){
		return rootNode.findWord(word);
	}

	@Override
	public int getWordCount() {
        int temp = rootNode.getAllUniqueWords("").size();
        if(temp != uniqueWords)
            System.out.println("counted previously: " + uniqueWords + ", counted now: " + temp);
		return temp;
	}

	@Override
	public int getNodeCount() {
		return rootNode.getHowManyNodes();
	}
	
	private String doFinalDetermination(ArrayList<String> tempListWithDuplicates, String word){
		ArrayList<String> tempListNoDuplicates = new ArrayList<String>();
		for(int i = 0; i < tempListWithDuplicates.size(); i++){//remove duplicates
			if(!this.isWordInList(tempListNoDuplicates, tempListWithDuplicates.get(i)))
				tempListNoDuplicates.add(tempListWithDuplicates.get(i));
		}
		
		ArrayList<String> tempList = new ArrayList<String>();
		for(int i = 0; i < tempListNoDuplicates.size(); i++){
			Node tempNode = rootNode.findWord(tempListNoDuplicates.get(i));
			if(tempNode != null && tempNode.getValue() != 0)// make sure the words are in the dictionary
				tempList.add(tempListNoDuplicates.get(i));
		}
		if(tempList.size() == 1)
			return tempList.get(0);
		else if(tempList.size() == 0)
			return null;
		else{
			int foundCount = 0;
			ArrayList<String> words = new ArrayList<String>();
			for(int i = 0; i < tempList.size(); i++){// find the one that happens the most in the dictionary
				Node thisOne = rootNode.findWord(tempList.get(i));
				if(thisOne == null)
					continue;
				else{
					if(thisOne.getValue() == foundCount)
						words.add(tempList.get(i));
					else if(thisOne.getValue() > foundCount){
						words = new ArrayList<String>();
						words.add(tempList.get(i));
						foundCount = thisOne.getValue();
					}
				}
			}
			if(words.size() == 1)
				return words.get(0);
			else if(words.size() > 1){// order by alphabetical and return the first one
				return this.alphabatizeList(words).get(0);
			}else
				return null;
		}
	}
	
	private ArrayList<String> alphabatizeList(ArrayList<String> list){
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		return list;
	}
	
	private ArrayList<String> doChecks(String word){
		ArrayList<String> temp = new ArrayList<String>();
		temp.addAll(this.doAlterations(word));
		temp.addAll(this.doDeletion(word));
		temp.addAll(this.doInsertions(word));
		temp.addAll(this.doTranspositions(word));
		return temp;
	}

	public String checkDistance1(String word){
		distance1 = this.doChecks(word);
		System.out.println("words to check: " + distance1.size());
		return this.doFinalDetermination(distance1, word);
	}
	
	public String checkDistance2(String word){
		distance2 = new ArrayList<String>();
		for(int i = 0; i < distance1.size(); i++){
			distance2.addAll(this.doChecks(distance1.get(i)));
		}
		System.out.println("words to check: " + distance2.size());
		return this.doFinalDetermination(distance2, word);
	}

    private ArrayList<String> doDeletion(String word){
//    	System.out.println(word);
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < word.length(); i++){
            list.add(word.substring(0, i) + word.substring(i + 1, word.length()));
//            System.out.println(list.get(list.size() - 1));
        }
        if(list.size() != word.length())
        	throw new RuntimeException("not enough results found for deletion: " + list.size() + ", should have: " + (word.length()));
        return list;
    }
    
    private ArrayList<String> doTranspositions(String word){
        ArrayList<String> list = new ArrayList<String>();
        if(word.length() == 1)
        	list.add(word);
        else{
	        for(int i = 0; i < word.length() - 1; i++){
	        	String temp = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i);
	        	if(!(word.length() <= i + 2))
	        		temp += word.substring(i+2);
	        	list.add(temp);// make sure
	        }
	        if(list.size() != (word.length() - 1))
	        	throw new RuntimeException("not enough results found for transposition: " + list.size() + ", should have: " + (word.length() - 1));
        }
        return list;
    }

    private ArrayList<String> doAlterations(String word){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < word.length(); i++){
            for(int j = 0; j < 26; j++){
            	String temp = word.substring(0, i) + ((char)(j + 'a')) + word.substring(i + 1, word.length());
            	if(!temp.equalsIgnoreCase(word))
            		list.add(temp);
            }
        }
        if(list.size() != (word.length() * 25))
        	throw new RuntimeException("not enough words found for alterations, listSize: " + list.size() + ", should have: " + (word.length() * 25));
        return list;
    }

    private ArrayList<String> doInsertions(String word){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < word.length() + 1; i++){
            for(int j = 0; j < 26; j++){
                list.add(word.substring(0, i) + ((char)(j + 'a')) + word.substring(i, word.length()));
//                System.out.println(list.get(list.size() - 1));
            }
        }
        if(list.size() != ((word.length() + 1) * 26))
        	throw new RuntimeException("not enough words found for insertions, listSize: " + list.size() + ", should have: " + ((word.length() + 1) * 26));
        return list;
    }

    private boolean isWordInList(ArrayList<String> list, String word){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equalsIgnoreCase(word))
                return true;
        }
        return false;
    }
    
    public class Node implements Trie.Node{
    	public Node[] children;
    	private int count;
    	
    	public Node(char letter){
    		this.children = new Node[26];
    		this.count = 0;
    	}
    	
    	public Node(int letter){
    		this.children = new Node[26];
    		this.count = 0;
    	}
    	
    	@Override
    	public boolean equals(Object o){
    		Node temp = (Node)o;
    		for(int i = 0; i < 26; i++){
    			if(this.children[i] != null && temp.children[i] != null){
    				if(!this.children[i].equals(temp.children[i]))
    					return false;
    			}else
    				return false;
    		}
    		return true;
    	}

        public Node findWord(String word){
            if(word.length() == 0)
                throw new RuntimeException("Should never have gotten to adding a word of no length find");
            if(getNodeForLetter(word.charAt(0)) == null)
                return null;
            else if (word.length() == 1) {
                return getNodeForLetter(word.charAt(0));
            } else
                return getNodeForLetter(word.charAt(0)).findWord(word.substring(1));
        }
    	
    	public void makeWord(){
    		this.count++;
    	}
    	
    	public Node addWord(String word){
    		if(word.length() == 0)
    			throw new RuntimeException("Should never have gotten to adding a word of no length add");
    		if(getNodeForLetter(word.charAt(0)) == null){
    			makeNodeForLetter(word.charAt(0));
    		}
    		if(word.length() == 1){
    			getNodeForLetter(word.charAt(0)).makeWord();
                return getNodeForLetter(word.charAt(0));
    		}else
    		    return getNodeForLetter(word.charAt(0)).addWord(word.substring(1));
    	}
    	
    	private void makeNodeForLetter(char letter){
    		int index = letter - 'a';
    		children[index] = new Node(letter);
    	}
    	
    	private Node getNodeForLetter(char letter){
    		int index = letter - 'a';
    		return children[index];
    	}

    	@Override
    	public int getValue() {
    		return count;
    	}

        public ArrayList<String> getAllUniqueWords(String currentWord){
            ArrayList<String> words = new ArrayList<String>();
            for (int i = 0; i < 26; i++){
                if(children[i] != null){
                    if(children[i].count > 0){
                        words.add(currentWord + (i + 'a'));
                        words.addAll(children[i].getAllUniqueWords(currentWord + (i + 'a')));
                    }
                }
            }
            return words;
        }

        public int getHowManyNodes(){
            int howMany = 0;
            for (int i = 0; i < 26; i++){
                if(children[i] != null){
                    howMany++;
                    howMany += children[i].getHowManyNodes();
                }
            }
            return howMany;
        }

    }
}
