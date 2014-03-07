import java.util.Comparator;

public class BTree<T extends Comparator<T>> {

	NaturalComparator compare = new NaturalComparator();
	Node<T> root;
	
	public void insert(T value){
		if(root.isFull()){
			root.rootSplit();
		}
		else //call private add 
			insert(root, value);
	}//end public add 
	
	private void insert(Node<T> node,T val){
		
		if(node.isFull()){
			node.split();
			insert(val);
		}
		
	}//end private insert
	
	public boolean search(Node<T> node, T value){
		for(T myVal: node.keys){
			m
		}
	}
}
