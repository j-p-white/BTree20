import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
//this class needs to the same type as the BTree some how
//remember after each change to a node you have to re-write back to the file
public class Node implements Serializable{
	private static final long serialVersionUID = 1L;
	final int MAXKEYS = 3; //31
	final int middle = MAXKEYS/2;
	ArrayList<String> keys = new ArrayList<String>(); 
	ArrayList<Long> links = new ArrayList<Long>();
	long incrementSize =2364;
	int startIndex;
	Persistance save;
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
	
	public void split(Node link, int nodeCount,RandomAccessFile raf) throws IOException{
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
		links.add(count+1,nodeCount * incrementSize);
		// i will need to send per the incrament size
		save.write(right);
	}//end split
	
	public void rootSplit(int nodeCount,RandomAccessFile raf) throws IOException{
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
		save.write( myLeft);
		save.write( myRight);
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
		temp =  save.read(nodeLocA);
		goRightRepair(temp,count+1);
		for(int i = 0; i < links.size();i++){
			long nodeLocB = links.get(i);
			temp2 = save.read(nodeLocB);
			if(temp2.minSize()){
				repair(i);
				i = 0;
				save.write(temp2);
			}
		}
	}// end internalRepair
	
	//will repair upto the internal node
	public void goRightRepair(Node myNode,int count) throws ClassNotFoundException, IOException{
		if(myNode.isLeaf()){
			return;
		}//end if
		else{
			goRightRepair(save.read(myNode.links.get(count)),count);
			Node temp;
			for(int i = 0; i < links.size();i++){
				long nodeLocA = links.get(i);
				temp = save.read(nodeLocA);
				if(temp.minSize()){
					repair(i);
					i = 0;
					save.write(temp);
					
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
		temp = save.read(nodeLocA);
		temp2 = save.read(nodeLocB);
		if(count != 0 && temp.keys.size() > middle ){
			rotateLeft(count);
			save.write(temp);
		}
		else if(temp2.keys.size() > middle){
			rotateRight(count);
			save.write(temp2);
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
		int apple =save.read(nodeLocA).keys.size()-1;
		
		//get the link to the deficient right node
		temp2 = save.read(nodeLocA);
		temp = save.read(nodeLocB);
		
		// the parent key
		parentKey = keys.remove(count -1 );
		// parent key is placed in deficient right node brining it to minimum
		temp.keys.add(0,parentKey);
		// get the key from the over full left node
		replaceKey = temp2.keys.remove(apple);
		
		//put the new key in the proper spot.
		keys.add(count -1,replaceKey);
		
		//write node back to file
		save.write(temp);
		save.write(temp2);
	}
	
	private void rotateRight(int count) throws ClassNotFoundException, IOException{
		String parentKey;
		String replaceKey;
		long nodeLocA = links.get(count); 
		long nodeLocB = links.get(count+1);
		Node temp = save.read(nodeLocA);
		Node temp2 = save.read( nodeLocB);
		//the parent key
		parentKey = keys.remove(count);
		//parent key is placed in deficient left node brining it to minimum
		temp.keys.add(parentKey);
		//get the key from the over full right node
		replaceKey = temp2.keys.remove(0);
		//put the new key in the proper spot 
		keys.add(count,replaceKey);
		
		//write node back to file
		save.write(temp);
		save.write(temp2);
	}
	
	private void merge(int count) throws ClassNotFoundException, IOException{
		String parentKey;
		Node temp,temp2;
		long nodeLocA = links.get(count+1);
		long nodeLocB = links.get(count);
		
		temp = save.read(nodeLocA);
		temp2 = save.read(nodeLocB);
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
		
		save.write(temp);
	}// end merge
	
	public String predacessor(int count) throws ClassNotFoundException, IOException{
		long nodeLocA = links.get(count);
		Node temp = save.read(nodeLocA);
		return goRight(temp,count+1);
	}
	
	public String goRight(Node myNode,int count) throws ClassNotFoundException, IOException{
		String toReturn;
		if(myNode.isLeaf()){
		toReturn = myNode.keys.remove(myNode.keys.size()-1);
		}//end if
		else{
			toReturn = goRight(save.read(myNode.links.get(count)),count);
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
	public void setStartIndex(int startIndex){
		this.startIndex = startIndex;
	}
	public int getStartIndex(){
		return startIndex;
	}
	
}//end node