package spell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
    	ArrayList<String> allWords = this.alphabatizeList(rootNode.getAllUniqueWords(new StringBuilder("")));
    	StringBuilder sb = new StringBuilder();
//    	System.out.println(allWords.toString());
    	for(int i = 0; i < allWords.size(); i++){
//    		System.out.println("Word: " + allWords.get(i) + " " + allWords.size());
    		sb.append(allWords.get(i));
    		sb.append(" ");
    		sb.append(rootNode.findWord(allWords.get(i)).getValue());
    		if(i < allWords.size() - 1)
    			sb.append("\n");
    	}
//    	System.out.println("here");
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
		rootNode = new Node(-1);
        uniqueWords = 0;
	}

	@Override
	public void add(String word) {
        if(rootNode.addWord(word.toLowerCase()).getValue() == 1)
            uniqueWords++;
    }

	@Override
	public Node find(String word){
		return rootNode.findWord(word.toLowerCase());
	}

	@Override
	public int getWordCount() {
        int temp = rootNode.getAllUniqueWords(new StringBuilder("")).size();
        if(temp != uniqueWords)
            System.out.println("counted previously: " + uniqueWords + ", counted now: " + temp);
		return temp;
	}

	@Override
	public int getNodeCount() {
		return rootNode.getHowManyNodes() + 1;
	}
	
	private String doFinalDetermination(ArrayList<String> tempListWithDuplicates, String word){
		Date tempDate = new Date();
//		System.out.println("checkpoint1: " + (( (new Date()).getTime() - tempDate.getTime())));
//		ArrayList<String> tempListNoDuplicates = new ArrayList<String>();
//		for(int i = 0; i < tempListWithDuplicates.size(); i++){//remove duplicates
//			if(!this.isWordInList(tempListNoDuplicates, tempListWithDuplicates.get(i)))
//				tempListNoDuplicates.add(tempListWithDuplicates.get(i));
//		}
//		System.out.println("checkpoint2: " + (( (new Date()).getTime() - tempDate.getTime())));
		ArrayList<String> tempList = new ArrayList<String>();
		for(int i = 0; i < tempListWithDuplicates.size(); i++){
			Node tempNode = rootNode.findWord(tempListWithDuplicates.get(i));
			if(tempNode != null && tempNode.getValue() > 0)// make sure the words are in the dictionary
				if(!this.isWordInList(tempList, tempListWithDuplicates.get(i)))
					tempList.add(tempListWithDuplicates.get(i));
		}
//		System.out.println("checkpoint3: " + (( (new Date()).getTime() - tempDate.getTime())));
		if(tempList.size() == 1)
			return tempList.get(0);
		else if(tempList.size() == 0)
			return null;
		else{
//			System.out.println("checkpoint4: " + (( (new Date()).getTime() - tempDate.getTime())));
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
//			System.out.println("checkpoint5: " + (( (new Date()).getTime() - tempDate.getTime())));
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
//		System.out.println(word);
		ArrayList<String> temp = new ArrayList<String>();
		temp.addAll(this.doAlterations(word));
		temp.addAll(this.doDeletion(word));
		temp.addAll(this.doInsertions(word));
		temp.addAll(this.doTranspositions(word));
		return temp;
	}

	public String checkDistance1(String word){
//		System.out.println("check distance1, length: " + word.length());
		distance1 = new ArrayList<String>();
		distance1.addAll(this.doChecks(word));
//		System.out.println("word length: " + word.length());
		System.out.println("words to check: " + distance1.size());
		return this.doFinalDetermination(distance1, word);
	}
	
	public String checkDistance2(String word){
		distance2 = new ArrayList<String>();
//		System.out.println("check distance 2");
		for(int i = 0; i < distance1.size(); i++){
//			System.out.println("check distance2, length: " + distance1.get(i).length());
//			System.out.println("check distance 2: " + distance1.get(i).length());
			distance2.addAll(this.doChecks(distance1.get(i)));
//			System.out.println("word length: " + distance1.get(i).length());
		}
		System.out.println("words to check: " + distance2.size());
		return this.doFinalDetermination(distance2, word);
	}

    private ArrayList<String> doDeletion(String word){
//    	System.out.println("do deletion:" + word);
    	if(word.length() == 1)
    		return new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < word.length(); i++){
        	sb = new StringBuilder();
        	sb.append(word.substring(0, i));
        	sb.append(word.substring(i + 1, word.length()));
        	String temp = sb.toString();
//        	if(temp.length() < 2)
//        		System.out.println("word has been reduced too much:"+ temp.length() + " " + temp + " " + word);
        	if(temp.length() > 0)
        		list.add(temp);
        	else
        		System.out.println("found word of 0 length");
//            System.out.println(list.get(list.size() - 1));
        }
        if(list.size() != word.length())
        	throw new RuntimeException("not enough results found for deletion: " + list.size() + ", should have: " + (word.length()));
        return list;
    }
    
    private ArrayList<String> doTranspositions(String word){
//    	System.out.println("transposition: " + word);
        ArrayList<String> list = new ArrayList<String>();
        if(word.length() == 1)
        	list.add(word);
        else{
        	StringBuilder sb = new StringBuilder();
	        for(int i = 0; i < word.length() - 1; i++){
	        	sb = new StringBuilder();
	        	sb.append(word.substring(0, i));
	        	sb.append(word.charAt(i + 1));
	        	sb.append(word.charAt(i));
//	        	String temp = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i);
	      
	        	if(!(word.length() <= i + 2))
	        		sb.append(word.substring(i+2));
	        	list.add(sb.toString());// make sure
	        }
	        if(list.size() != (word.length() - 1))
	        	throw new RuntimeException("not enough results found for transposition: " + list.size() + ", should have: " + (word.length() - 1) + ", word: " + word);
        }
//        if(word.equals("ezya"))
//        	System.out.println("here: " + list.toString());
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
//            	String temp = word.substring(0, i) + ((char)(j + 'a')) + word.substring(i + 1, word.length());
            	String temp = sb.toString();
            	if(!temp.equalsIgnoreCase(word) && word.length() != 0)
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
            	sb.append(word.substring(i, word.length()));
//                list.add(word.substring(0, i) + ((char)(j + 'a')) + word.substring(i, word.length()));
            	list.add(sb.toString());
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
    		children[index] = new Node(letter);
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
//                	System.out.println("child is not null: " + ((char)(i+'a')));
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
