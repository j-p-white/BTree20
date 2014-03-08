
public class BTree<T> {
	NaturalComparator<T> comp = new NaturalComparator<T>();
	Node root;
	
	public void insert(T value){
		if(root.isFull()){
			root.rootSplit();
		}
		else //call private add 
			insert(root, value);
	}//end public add 
	
	private void insert(Node node,T val){
		
		if(node.isFull()){
			node.split();
			insert(val);
		}
		
	}//end private insert
	
	public Node find(Node node, T value){
		int count = 0;
		for(WordObject w: node.keys){
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
