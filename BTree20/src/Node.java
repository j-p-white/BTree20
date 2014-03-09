import java.util.ArrayList;

public class Node<T extends Comparable<T>> {
	final int MAXKEYS = 31; 
	final int middle = MAXKEYS/2;
	int leftChildStartNumber; 
	int rightChildStartNumber; 
	int myPosition;
	
	ArrayList<T> keys = new ArrayList<T>(); 
	ArrayList<Node<T>> links = new ArrayList<Node<T>>();
	
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
		Node<T> left = new Node<T>(); 
		Node<T> right = new Node<T> ();
		
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
	
	public void split(Node<T> link){
		Node<T> right = new Node<T>();
		T middleVal;
		int count = 0;
		//get right
		while(link.keys.size()> middle+1){
			right.keys.add(link.keys.remove(middle+1));	
		}
		//get the middle value
		middleVal= link.keys.remove(link.keys.size()-1);
		
		//compare the keys to the middle guy
		for(T value: keys){
			if(compare(middleVal,value)>0){
				count++;
			}
		}
		//add keys and links in proper spot
		keys.add(count,middleVal); 
		links.add(count+1,right);
	}//end split
	
	public int compare(T obj1,T obj2){
		return obj1.compareTo(obj2);
	}
}	//end node