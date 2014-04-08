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
	int incrementSize =2052;
	long startIndex;
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
		right.setStartIndex(nodeCount * incrementSize);
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
		links.add(count+1,(long) (nodeCount * incrementSize));
		// i will need to send per the incrament size
		tree.per.write(right);
	}//end split
	
	//look over how i write to splits
	public void rootSplit(int nodeCount) throws IOException{
		int leftCount = nodeCount -1;
		long offLeft = (leftCount * incrementSize);
		long offRight = (nodeCount * incrementSize);
		Node myLeft = makeNewLeft();
		Node myRight = makeNewRight();
		if(!links.isEmpty()){
			addLeftLinks(myLeft); 
			addRightLinks(myRight);
		}
		hookLinks(offLeft,offRight);
		
		//send in the new incrament size
		temp.add(myLeft);
		temp.add(myRight);
		//tree.per.write(myLeft);
		//tree.per.write(myRight);
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
	
	// this needs to take in the incrament size
	private void hookLinks(long offleft,long offright){
		links.add(offleft);
		links.add(offright);
	}
	
	public void internalRepair(int count) throws ClassNotFoundException, IOException{
		Node temp,temp2;
		 long nodeLocA = links.get(count);
		temp =  tree.per.read(nodeLocA);
		goRightRepair(temp,count+1);
		for(int i = 0; i < links.size();i++){
			long nodeLocB = links.get(i);
			temp2 = tree.per.read(nodeLocB);
			if(temp2.minSize()){
				repair(i);
				i = 0;
				tree.per.write(temp2);
			}
		}
	}// end internalRepair
	
	//will repair upto the internal node
	public void goRightRepair(Node myNode,int count) throws ClassNotFoundException, IOException{
		if(myNode.isLeaf()){
			return;
		}//end if
		else{
			goRightRepair(tree.per.read(myNode.links.get(count)),count);
			Node temp;
			for(int i = 0; i < links.size();i++){
				long nodeLocA = links.get(i);
				temp = tree.per.read(nodeLocA);
				if(temp.minSize()){
					repair(i);
					i = 0;
					tree.per.write(temp);
					
				}
			}
		}
	}
	
	public void repair(int count) throws ClassNotFoundException, IOException{
		//write node back to file
		Node temp = new Node(); 
		Node temp2 = new Node();
		long nodeLocA = links.get(count -1);
		long nodeLocB = links.get(count +1);
		temp = tree.per.read(nodeLocA);
		temp2 = tree.per.read(nodeLocB);
		if(count != 0 && temp.keys.size() > middle ){
			rotateLeft(count);
			tree.per.write(temp);
		}
		else if(temp2.keys.size() > middle){
			rotateRight(count);
			tree.per.write(temp2);
		}
		else{
			merge(count);
		}
	}// end steal
	// need to get the link before i remove the key---error
	private void rotateLeft(int count) throws ClassNotFoundException, IOException{
		String parentKey;
		String replaceKey;
		Node temp;
		Node temp2;
		long nodeLocA = links.get(count -1);
		long nodeLocB = links.get(count);
		int apple =tree.per.read(nodeLocA).keys.size()-1;
		
		//get the link to the deficient right node
		temp2 = tree.per.read(nodeLocA);
		temp = tree.per.read(nodeLocB);
		
		// the parent key
		parentKey = keys.remove(count -1 );
		// parent key is placed in deficient right node brining it to minimum
		temp.keys.add(0,parentKey);
		// get the key from the over full left node
		replaceKey = temp2.keys.remove(apple);
		
		//put the new key in the proper spot.
		keys.add(count -1,replaceKey);
		
		//write node back to file
		tree.per.write(temp);
		tree.per.write(temp2);
	}
	
	private void rotateRight(int count) throws ClassNotFoundException, IOException{
		String parentKey;
		String replaceKey;
		long nodeLocA = links.get(count); 
		long nodeLocB = links.get(count+1);
		Node temp = tree.per.read(nodeLocA);
		Node temp2 = tree.per.read(nodeLocB);
		//the parent key
		parentKey = keys.remove(count);
		//parent key is placed in deficient left node brining it to minimum
		temp.keys.add(parentKey);
		//get the key from the over full right node
		replaceKey = temp2.keys.remove(0);
		//put the new key in the proper spot 
		keys.add(count,replaceKey);
		
		//write node back to file
		tree.per.write(temp);
		tree.per.write(temp2);
	}
	
	private void merge(int count) throws ClassNotFoundException, IOException{
		String parentKey;
		Node temp,temp2;
		long nodeLocA = links.get(count+1);
		long nodeLocB = links.get(count);
		
		temp = tree.per.read(nodeLocA);
		temp2 = tree.per.read(nodeLocB);
		//get the parentKey
		parentKey = keys.remove(count);
		
		//put parentKey into right link 
		temp.keys.add(0,parentKey);
			// put left values into right values
		for(String s: temp2.keys) {
			temp.keys.add(0,s);
		}// end for
			//left links go into rights links
		for(long Link: temp2.links){
				temp.links.add(0,Link);
		}// end for
		
		links.remove(count);
		
		tree.per.write(temp);
	}// end merge
	
	public String predacessor(int count) throws ClassNotFoundException, IOException{
		long nodeLocA = links.get(count);
		Node temp = tree.per.read(nodeLocA);
		return goRight(temp,count+1);
	}
	
	public String goRight(Node myNode,int count) throws ClassNotFoundException, IOException{
		String toReturn;
		if(myNode.isLeaf()){
		toReturn = myNode.keys.remove(myNode.keys.size()-1);
		}//end if
		else{
			toReturn = goRight(tree.per.read(myNode.links.get(count)),count);
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
	public void setStartIndex(long startIndex){
		this.startIndex = startIndex;
	}
	public long getStartIndex(){
		return startIndex;
	}
	public ArrayList<Node> getNode(){
		return temp;
	}
}//end node