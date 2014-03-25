import java.io.Serializable;
import java.util.ArrayList;
//this class needs to the same type as the BTree some how
public class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	final int MAXKEYS = 3; //31
	final int middle = MAXKEYS/2;
	ArrayList<String> keys = new ArrayList<String>(); 
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
		else{
			return false;
		}
	}//end isFull
	
	public void split(Node link){
		Node right = new Node();
		String middleVal;
		int count = 0;
		//get right
		while(link.keys.size()> middle+1){
			right.keys.add(link.keys.remove(middle+1));	
		}
		//get the middle value
		middleVal= link.keys.remove(link.keys.size()-1);
		
		//compare the keys to the middle guy
		for(String value: keys){
			if(middleVal.compareTo(value)>0){
				count++;
			}
		}
		//add keys and links in proper spot
		keys.add(count,middleVal); 
		links.add(count+1,right);
	}//end split
	
	public void rootSplit(){
		Node myLeft = makeNewLeft();
		Node myRight = makeNewRight();
		if(!links.isEmpty()){
			addLeftLinks(myLeft); 
			addRightLinks(myRight);
		}
		hookLinks(myLeft,myRight);
	}//end split root
	
	private Node makeNewLeft(){
		int mid = middle;
		Node left = new Node(); 
		//get all the left keys
		for(int i =0; i < mid;i++){
			left.keys.add(keys.remove(i));
		}//end for
		return left;
	}// end makeNewLeft
	
	private void addLeftLinks(Node left){
		int mid = middle+1; 
		for(int i =0; i<mid;i++){
			left.links.add(links.remove(0));
		}//end for
	}//end add left links
	
	private Node makeNewRight(){
		Node right = new Node();
		//get all the keys from the right
		while(keys.size() >1){
			right.keys.add(keys.remove(1));
		}
		return right;
	}// end makeNewRight
	
	private void addRightLinks(Node right){
			right.links.addAll(links);
			links.clear();
	}//end add right links
	
	private void hookLinks(Node left,Node right){
		links.add(left);
		links.add(right);
	}
	
	public void repair(int count){
		if(links.get(count -1).keys.size() > middle && count > 0){
			rotateLeft(count);
		}
		else if(links.get(count+1).keys.size() > middle){
			rotateRight(count);
		}
		else{
			merge(count);
		}
	}// end steal
	// need to get the link before i remove the key---error
	private void rotateLeft(int count){
		String parentKey;
		String replaceKey;
		// the parent key
		parentKey = keys.remove(count);
		// parent key is placed in deficient right node brining it to minimum
		links.get(count +1).keys.add(0,parentKey);
		// get the key from the over full left node
		replaceKey = links.get(count -1).keys.remove(keys.size()-1);
		//put the new key in the proper spot.
		keys.add(count,replaceKey);	
	}
	
	private void rotateRight(int count){
		String parentKey;
		String replaceKey;
		//the parent key
		parentKey = keys.remove(count);
		//parent key is placed in deficient left node brining it to minimum
		links.get(count-1).keys.add(parentKey);
		//get the key from the over full right node
		replaceKey = links.get(count +1).keys.remove(0);
		//put the new key in the proper spot 
		keys.add(count,replaceKey);
	}
	
	private void merge(int count){
		String parentKey;
		//get the parentKey
		parentKey = keys.remove(count);
		//put parentKey into right link 
		links.get(count+1).keys.add(0,keys.remove(count));
		// put the left links into the right link
			// values 1st
		for(String s: links.get(count -1).keys) {
			links.get(count +1).keys.add(0,s);
		}// end for
			//left links go into rights links
		for(Node Link: links.get(count -1).links){
				links.get(count+1).links.add(0,Link);
		}// end for
	}// end merge
	
	public String  predacessor (int count){
		Node temp = links.get(count);
		return goRight(temp,count+1);
	}
	
	public String goRight(Node myNode,int count){
		String toReturn;
		if(myNode.isLeaf()){
		toReturn = myNode.keys.get(myNode.keys.size()-1);
		}//end if
		else{
			toReturn = goRight(myNode.links.get(count),count);
		}
		return toReturn;
	}
	public boolean minSize(){
		boolean result; 
		if(keys.size() < MAXKEYS/2){
			result = true;
		}
		else{ 
			result = false;
		}
		return result;
	}// end minSize
}//end node