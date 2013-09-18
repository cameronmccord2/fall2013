package spell;

import java.util.ArrayList;
import java.util.Collections;

// TODO always cast chars correctly ((char)(number))

public class Dictionary implements Trie{
	public Node rootNode;
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
    	ArrayList<String> allWords = this.alphabatizeList(rootNode.getAllUniqueWords(new StringBuilder("")));
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < allWords.size(); i++){
    		sb.append(allWords.get(i));
    		sb.append(" ");
    		sb.append(rootNode.findWord(allWords.get(i)).getValue());
    		if(i < allWords.size() - 1)
    			sb.append("\n");
    	}
    	return sb.toString();
    }
	
	@Override
	public int hashCode(){
		return rootNode.getAllUniqueWords(new StringBuilder("")).size() * rootNode.getHowManyNodes() * 97;
	}
	
	@Override
	public boolean equals(Object o){
		if(o != null && o instanceof Dictionary){
			Dictionary temp = (Dictionary)o;
			return this.rootNode.equals(temp.rootNode);
		}
		return false;
	}
	
	public Dictionary(){
		rootNode = new Node();
	}

	@Override
	public void add(String word) {
		rootNode.addWord(word.toLowerCase()).getValue();
    }

	@Override
	public Node find(String word){
		return rootNode.findWord(word.toLowerCase());
	}

	@Override
	public int getWordCount() {
        return rootNode.getAllUniqueWords(new StringBuilder("")).size();
	}

	@Override
	public int getNodeCount() {
		return rootNode.getHowManyNodes() + 1;
	}
	
	private String doFinalDetermination(ArrayList<String> tempListWithDuplicates, String word){
		ArrayList<String> tempList = new ArrayList<String>();
		for(int i = 0; i < tempListWithDuplicates.size(); i++){
			Node tempNode = rootNode.findWord(tempListWithDuplicates.get(i));
			if(tempNode != null && tempNode.getValue() > 0)// make sure the words are in the dictionary
				if(!this.isWordInList(tempList, tempListWithDuplicates.get(i)))// Make sure you arent adding duplicates
					tempList.add(tempListWithDuplicates.get(i));
		}
		if(tempList.size() == 1)// found our one answer
			return tempList.get(0);
		else if(tempList.size() == 0)// do next round
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
		distance1 = new ArrayList<String>();
		distance1.addAll(this.doChecks(word));
		return this.doFinalDetermination(distance1, word);
	}
	
	public String checkDistance2(String word){
		distance2 = new ArrayList<String>();
		for(int i = 0; i < distance1.size(); i++){
			distance2.addAll(this.doChecks(distance1.get(i)));
		}
		return this.doFinalDetermination(distance2, word);
	}

    private ArrayList<String> doDeletion(String word){
    	if(word.length() == 1)// you cant get valid strings from deleting the only character, will fail final check if not delt with here
    		return new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < word.length(); i++){
        	sb = new StringBuilder();
        	sb.append(word.substring(0, i));
        	sb.append(word.substring(i + 1, word.length()));
        	String temp = sb.toString();
        	list.add(temp);
        }
        if(list.size() != word.length())
        	throw new RuntimeException("not enough results found for deletion: " + list.size() + ", should have: " + (word.length()));
        return list;
    }
    
    private ArrayList<String> doTranspositions(String word){// only one with edge case test
        ArrayList<String> list = new ArrayList<String>();
        if(word.length() == 1)// cant transpose a word without two letters, will fail final test if not checked here
        	list.add(word);
        else{
        	StringBuilder sb = new StringBuilder();
	        for(int i = 0; i < word.length() - 1; i++){
	        	sb = new StringBuilder();
	        	sb.append(word.substring(0, i));
	        	sb.append(word.charAt(i + 1));
	        	sb.append(word.charAt(i));
	        	if(!(word.length() <= i + 2))// if the index for the rest of the letters isnt past the end of the string, append them. Edge case
	        		sb.append(word.substring(i+2));
	        	list.add(sb.toString());
	        }
	        if(list.size() != (word.length() - 1))
	        	throw new RuntimeException("not enough results found for transposition: " + list.size() + ", should have: " + (word.length() - 1) + ", word: " + word);
        }
        return list;
    }

    private ArrayList<String> doAlterations(String word){
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < word.length(); i++){
            for(int j = 0; j < 26; j++){
            	sb = new StringBuilder();
            	sb.append(word.substring(0, i));
            	sb.append(((char)(j + 'a')));
            	sb.append(word.substring(i + 1, word.length()));
            	String temp = sb.toString();
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
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < word.length() + 1; i++){
            for(int j = 0; j < 26; j++){
            	sb = new StringBuilder();
            	sb.append(word.substring(0, i));
            	sb.append(((char)(j + 'a')));
            	sb.append(word.substring(i));
            	list.add(sb.toString());
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
    	
    	public Node(){
    		this.children = new Node[26];
    		this.count = 0;
    	}
    	
    	@Override
    	public boolean equals(Object o){
    		if(o == null || !(o instanceof Node))
    			return false;
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
            if(word.length() == 0){
                if(this.getValue() > 0)
                	return this;
                else
                	return null;
            }
            if(getNodeForLetter(word.charAt(0)) == null)
                return null;
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
    		children[index] = new Node();
    	}
    	
    	private Node getNodeForLetter(char letter){
    		
    		int index = letter - 'a';
    		try{
    		return children[index];
    		}catch(Exception e){
    			System.out.println("letter: " + letter);
    		}
    		return null;
    	}

    	@Override
    	public int getValue() {
    		return count;
    	}

        public ArrayList<String> getAllUniqueWords(StringBuilder currentWord){
            ArrayList<String> words = new ArrayList<String>();
            for (int i = 0; i < 26; i++){
                if(children[i] != null){
                	StringBuilder sb = new StringBuilder(currentWord);
                	sb.append(((char)(i + 'a')));
                    if(children[i].count > 0){
                        words.add(sb.toString());
                    }
                    words.addAll(children[i].getAllUniqueWords(sb));
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
