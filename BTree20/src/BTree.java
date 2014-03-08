
public class BTree<T> {
	NaturalComparator comp = new NaturalComparator();
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
	
	public Node<T> findLink(Node<T> node, T value){
		int count = 0;
		for(WordObject<T> w: node.keys){
			if(comp.compare(w.word, value)>0){
				count++;
			}//end if
			else{
				break;
			}//end else
		}// end for
			return node.links.get(count);
	}
}
