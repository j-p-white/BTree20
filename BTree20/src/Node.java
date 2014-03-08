import java.util.ArrayList;
// let me add something

public class Node {
	final int MAXKEYS = 31; 
	final int middle = MAXKEYS/2;
	int leftChildStartNumber; 
	int rightChildStartNumber; 
	int myPosition;
	
	ArrayList<WordObject> keys = new ArrayList<WordObject>(); 
	ArrayList<Node> links = new ArrayList<Node>();
	
	public Node(){
		
	}
	public boolean isLeaf(){
		if(links.size() == 0)
		{
			return true;
		}
		else
			return false;
	}// end is leaf 
	
	public boolean isFull(){
		if(keys.size() == MAXKEYS)
		{
			return true;
		}
		else
			return false;
	}//end isFull
	
	public void rootSplit(){
		int mid = middle;
		Node left = new Node(); 
		Node right = new Node ();
		
		//get all the left keys
		for(int i =0; i < mid;i++){
			left.keys.add(keys.remove(i));
		}
		//get all the keys from the right
		while(keys.size() >1){
			right.keys.add(keys.remove(1));
		}
		links.add(left);
		links.add(right);
		
	}//end split root
	
	//needs to some how put node into parent
	public WordObject split(){
		Node right = new Node();
		
		//get right
		while(keys.size()> middle+1){
			right.keys.add(keys.remove(middle+1));	
		}
		//hook up the new link
		links.add(right);
		
		//send back the middle node object
		return keys.remove(middle);
	}
	public boolean hasValue(String val)
	{
		for(WordObject w: keys){
			if(w.word.equals(val)){
				return true;
			}//end if
		}//end for
		//else
		return false;
	}//end hasValue
}	//end node