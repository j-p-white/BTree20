import java.io.Serializable;

//and a new change
public class BTree<T extends Comparable<T>> implements Serializable {
	private static final long serialVersionUID = 1L;
	Node<T> root;
	
	public void insert(T value){
		if(root.isFull()){
			root.rootSplit();
			insert(value);
		}
		else //call private add 
			insert(root, value);
	}//end public add 
	
	private void insert(Node<T> node,T val){
		Node<T> link = new Node<T>();
		if(node.isLeaf())
		{
			if(node.isFull())
			{
				 return;
			}//end if
			else
			{
				
				//this will end the method
				node.keys.add(val);
			}//end else
		}// end leaf case
		else
		{
				link = findLink(node,val);
				insert(link,val);
				node.split(link);
		}
	}//end private insert
	
	public Node<T> findLink(Node<T> node, T value){
		int count = 0;
		for(T w: node.keys){
			if(compare(w, value)>0){
				count++;
			}//end if
			else{
				break;
			}//end else
		}// end for
			return node.links.get(count);
	}
	public int compare(T obj1,T obj2){
		return obj1.compareTo(obj2);
	}
}//end class
