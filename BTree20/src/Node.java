import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
// this needs to use the trees persist instead of its own. 
public class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<Node> temp = new ArrayList<Node>();
	final int MAXKEYS = 32; 
	final int middle = MAXKEYS/2;
	ArrayList<String> keys = new ArrayList<String>(); 
	ArrayList<Long> links = new ArrayList<Long>();
	long blockNumber;
	boolean visited = false;
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
	
	public boolean isFull(){//here it was =
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
		while(link.keys.size()> middle +1){//here it was not -1
			right.keys.add(0,link.keys.remove(link.keys.size()-1)); //+1	
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("splitKeys: "+right.keys.size());
		if(!link.isLeaf()){
			while(link.links.size()>middle+1){ //+1
				right.links.add(0,link.links.remove(link.links.size()-1));//+1
			}
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("split links: "+right.links.size());
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
		for(int i =0; i < mid -1;i++){
			left.keys.add(keys.remove(0));
		}//end for
		System.out.println("????????????????????????????????????");
		System.out.println("keys in left: "+left.keys.size());
		return left;
	}// end makeNewLeft
	
	private void addLeftLinks(Node left){
		int mid = middle; 
		for(int i =0; i<mid;i++){//here
			left.links.add(links.remove(0));
		}//end for
		System.out.println("????????????????????????????????????");
		System.out.println("links in left: "+left.links.size());
	}//end add left links
	
	private Node makeNewRight(){
		Node right = new Node();
		//get all the keys from the right
		while(keys.size() >1){
			right.keys.add(keys.remove(1));
		}
		System.out.println("????????????????????????????????????");
		System.out.println("keys in right: "+right.keys.size());
		return right;
	}// end makeNewRight
	
	private void addRightLinks(Node right){
			right.links.addAll(links);
			System.out.println("????????????????????????????????????");
			System.out.println("links in right: "+right.links.size());
			links.clear();
	}//end add right links
	
	// this needs to take in the incrament size
	private void hookLinks(long offleft,long offright){
		links.clear();
		links.add(offleft);
		links.add(offright);
	}
	
	
	
	// need to get the link before i remove the key---error
	public void rotateLeft(Node deficient, Node overfull,int count) throws ClassNotFoundException, IOException{
		String parentKey;
		String replaceKey;
		
		// the parent key
		parentKey = keys.remove(count);
		// parent key is placed in deficient right node brining it to minimum
		
		deficient.keys.add(parentKey);
		// get the key from the over full left node
		replaceKey = overfull.keys.remove(0);
		
		//put the new key in the proper spot.
		keys.add(count,replaceKey);
		
		if(!overfull.isLeaf()){
			deficient.links.add(overfull.links.remove(0));
		}
		//write node back to file
		temp.clear();
		temp.add(overfull);
		temp.add(deficient);
	}
	
	public void rotateRight(Node dificent,Node overfull, int count) throws ClassNotFoundException, IOException{
		String parentKey;
		String replaceKey;

		//the parent key
		parentKey = keys.remove(count -1);
		//parent key is placed in deficient left node brining it to minimum
		dificent.keys.add(0,parentKey);
		//get the key from the over full right node
		replaceKey = overfull.keys.remove(overfull.keys.size() -1);
		if(!overfull.isLeaf()){
			dificent.links.add(0,overfull.links.remove(overfull.links.size() -1));
		}
		//put the new key in the proper spot 
		keys.add(count -1,replaceKey);
		
		//write node back to file
		temp.clear();
		temp.add(dificent);
		temp.add(overfull);
	}
	// right is getting value
	public void mergeRight(Node rightLink,Node leftLink, int count) throws ClassNotFoundException, IOException{
		String parentKey;
		
		parentKey = keys.remove(count);
		
		rightLink.keys.add(0,parentKey);
		
		// put left values into right values
		for(String s: leftLink.keys) {
			rightLink.keys.add(0,s);
		}// end for
		//left links go into rights links
		for(int i = leftLink.links.size(); i > 0;i--){
				rightLink.links.add(0,leftLink.links.get(i -1));
		}// end for
		
		links.remove(count);
		temp.clear();
		temp.add(rightLink);
	}// end merge
	//left is getting values
	//need to switch leftLink and rightLink in mergeLeft
	public void mergeLeft(Node leftLink, Node rightLink, int count){
		String parentKey; 
		
		parentKey = keys.remove(count);
		
		leftLink.keys.add(parentKey);
		
		for(String s: rightLink.keys){
			leftLink.keys.add(s);
		}
			leftLink.links.addAll(rightLink.links);
		links.remove(count+1);
		temp.clear();
		temp.add(leftLink);
	}
//this method needs to be improved	
	public boolean minSize(){
		boolean result; 
		if(keys.size() < (MAXKEYS/2) -1){
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
