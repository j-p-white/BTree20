import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
// this needs to use the trees persist instead of its own. 
public class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<Node> temp = new ArrayList<Node>();
	final int MAXKEYS = 3; //31
	final int middle = MAXKEYS/2;
	ArrayList<String> keys = new ArrayList<String>(); 
	ArrayList<Long> links = new ArrayList<Long>();
	long blockNumber;
	BTree tree;
	
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
	
	public void split(Node link, int nodeCount) throws IOException{
		Node right = new Node();
		right.blockNumber = nodeCount;
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
		links.add(count+1,right.blockNumber);
		
		temp.clear();
		temp.add(link);
		temp.add(right);
	}//end split
	
	//look over how i write to splits
	public void rootSplit(int nodeCount) throws IOException{
		int leftCount = nodeCount -1;
		Node myLeft = makeNewLeft();
		Node myRight = makeNewRight();
		myLeft.setBlocknumber(leftCount);
		myRight.setBlocknumber(nodeCount);
		long offLeft = myLeft.blockNumber;
		long offRight = myRight.blockNumber;
		if(!links.isEmpty()){
			addLeftLinks(myLeft); 
			addRightLinks(myRight);
		}
		hookLinks(offLeft,offRight);
		
		temp.clear();
		//send in the new incrament size
		temp.add(myLeft);
		temp.add(myRight);
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
		/*	
			if(right.links.get(0) > right.links.get(1)){
				long temp = right.links.remove(0);
				right.links.add(temp);
			}
		*/
			links.clear();
	}//end add right links
	
	// this needs to take in the incrament size
	private void hookLinks(long offleft,long offright){
		links.add(offleft);
		links.add(offright);
	}
	
	
	
	// need to get the link before i remove the key---error
	public void rotateLeft(Node deficient, Node overfull,int count) throws ClassNotFoundException, IOException{
		String parentKey;
		String replaceKey;
		int apple =overfull.keys.size()-1;
		
		//get the link to the deficient right node
		
		// the parent key
		parentKey = keys.remove(count -1 );
		// parent key is placed in deficient right node brining it to minimum
		deficient.keys.add(0,parentKey);
		// get the key from the over full left node
		replaceKey = overfull.keys.remove(apple);
		
		//put the new key in the proper spot.
		keys.add(count -1,replaceKey);
		
		//write node back to file
		temp.clear();
		temp.add(overfull);
		temp.add(deficient);
	}
	
	public void rotateRight(Node dificent,Node overfull, int count) throws ClassNotFoundException, IOException{
		String parentKey;
		String replaceKey;

		//the parent key
		parentKey = keys.remove(count);
		//parent key is placed in deficient left node brining it to minimum
		dificent.keys.add(parentKey);
		//get the key from the over full right node
		replaceKey = overfull.keys.remove(0);
		//put the new key in the proper spot 
		keys.add(count,replaceKey);
		
		//write node back to file
		temp.clear();
		temp.add(dificent);
		temp.add(overfull);
	}
	
	public void merge(Node rightLink,Node leftLink, int count) throws ClassNotFoundException, IOException{
		String parentKey;
		
		parentKey = keys.remove(count);
		
		rightLink.keys.add(0,parentKey);
		
		// put left values into right values
		for(String s: leftLink.keys) {
			rightLink.keys.add(0,s);
		}// end for
		//left links go into rights links
		for(long Link: leftLink.links){
				rightLink.links.add(0,Link);
		}// end for
		
		links.remove(count);
		temp.clear();
		temp.add(rightLink);
	}// end merge
	

	

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
	public void setBlocknumber(long blockNumber){
		this.blockNumber = blockNumber;
	}
	public long getStartIndex(){
		return blockNumber;
	}
	public ArrayList<Node> getNode(){
		return temp;
	}
}//end node