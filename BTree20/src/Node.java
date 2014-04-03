import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
//this class needs to the same type as the BTree some how
public class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	final int MAXKEYS = 3; //31
	final int middle = MAXKEYS/2;
	ArrayList<String> keys = new ArrayList<String>(); 
	ArrayList<Node> links = new ArrayList<Node>();
	long startIndex;
	long incrementSize =1280;
	Persistance save = new Persistance();
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
	
	public void split(Node link, int nodeCount,RandomAccessFile raf) throws IOException{
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
		links.add(count+1,right);
		save.write(raf, right);
	}//end split
	
	public void rootSplit(int nodeCount,RandomAccessFile raf) throws IOException{
		int leftCount = nodeCount -1;
		Node myLeft = makeNewLeft();
		myLeft.setStartIndex(leftCount * incrementSize);
		Node myRight = makeNewRight();
		myRight.setStartIndex(nodeCount * incrementSize);
		if(!links.isEmpty()){
			addLeftLinks(myLeft); 
			addRightLinks(myRight);
		}
		hookLinks(myLeft,myRight);
		save.write(raf, myLeft);
		save.write(raf, myRight);
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
	
	public void internalRepair(int count){
		Node temp;
		temp = links.get(count);
		goRightRepair(temp,count+1);
		for(int i = 0; i < links.size();i++){
			if(links.get(i).minSize()){
				repair(i);
				i = 0;
			}
		}
	}// end internalRepair
	
	//will repair upto the internal node
	public void goRightRepair(Node myNode,int count){
		if(myNode.isLeaf()){
			return;
		}//end if
		else{
			goRightRepair(myNode.links.get(count),count);
			for(int i = 0; i < links.size();i++){
				if(links.get(i).minSize()){
					repair(i);
					i = 0;
				}
			}
		}
	}
	
	public void repair(int count){
		if(count != 0 && links.get(count -1).keys.size() > middle ){
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
		Node temp;
		
		int apple =links.get(count -1).keys.size()-1;
		
		//get the link to the deficient right node
		temp =links.get(count);
		
		// the parent key
		parentKey = keys.remove(count -1 );

		// parent key is placed in deficient right node brining it to minimum
		temp.keys.add(0,parentKey);
		
		// get the key from the over full left node
		replaceKey = links.get(count -1).keys.remove(apple);
		
		//put the new key in the proper spot.
		keys.add(count -1,replaceKey);	
	}
	
	private void rotateRight(int count){
		String parentKey;
		String replaceKey;
		Node temp = links.get(count);
		//the parent key
		parentKey = keys.remove(count);
		//parent key is placed in deficient left node brining it to minimum
		temp.keys.add(parentKey);
		//get the key from the over full right node
		replaceKey = links.get(count +1).keys.remove(0);
		//put the new key in the proper spot 
		keys.add(count,replaceKey);
	}
	
	private void merge(int count){
		String parentKey;
		Node temp;
		
		temp = links.get(count+1);
		
		//get the parentKey
		parentKey = keys.remove(count);
		
		//put parentKey into right link 
		temp.keys.add(0,parentKey);
			// put left values into right values
		for(String s: links.get(count).keys) {
			temp.keys.add(0,s);
		}// end for
			//left links go into rights links
		for(Node Link: links.get(count).links){
				temp.links.add(0,Link);
		}// end for
		
		links.remove(count);
	}// end merge
	
	public String predacessor(int count){
		Node temp = links.get(count);
		return goRight(temp,count+1);
	}
	
	public String goRight(Node myNode,int count){
		String toReturn;
		if(myNode.isLeaf()){
		toReturn = myNode.keys.remove(myNode.keys.size()-1);
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
	
	public void setStartIndex(long startIndex){
		this.startIndex = startIndex;
	}
	
	public long getStartIndex(){
		return startIndex;
	}
}//end node