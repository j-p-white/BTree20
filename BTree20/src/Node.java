import java.util.ArrayList;
import java.util.Comparator;


public class Node <T extends Comparator<T>> {
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
		Node<T> right = new Node<T>();
		
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
	public T split(){
		Node<T> right = new Node<T>();
		
		//get right
		while(keys.size()> middle+1){
			right.keys.add(keys.remove(middle+1));	
		}
		//hook up the new link
		links.add(right);
		
		//send back the middle node object
		return keys.remove(middle);
	}
}	


