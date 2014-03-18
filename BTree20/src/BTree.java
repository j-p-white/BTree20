import java.io.Serializable;

//and a new change
public class BTree implements Serializable {
	private static final long serialVersionUID = 1L;
	Node root;
	
	public BTree(){
		root = new Node();
	}// end BTree
	
	public void insert(String value){
		if(root.isFull()){
			root.rootSplit();
		}
			insert(root, value);	
	}//end public add 
	
	private void insert(Node node,String val){
		int count= 0;
		Node link = new Node();
		if(node.isLeaf())
		{
			if(node.isFull())
			{
				 return;
			}//end if
			else
			{
				for(String letter: node.keys){
					if(val.compareTo(letter)>0){
						count++;
					}
					else{
						break;
					}
				}//end for
				node.keys.add(count,val);
			}//end else
		}// end leaf case
		else
		{
				link = findLink(node,val);
				insert(link,val);
				//node.split(link); not sure what this is about
		}
	}//end private insert
	
	public boolean search(String val){
		boolean result = true;
		if(root.keys.contains(val)){
			result = true;
		}
		else{
			result = search(root, val);
		}
		return result;
	}//end method
	
	private boolean search(Node node, String value){
		boolean found = true;
		if(node.isLeaf()){
			found = false;
		}
		else{
		 Node temp;
		 temp = findLink(node, value);
		 if(temp.keys.contains(value)){
			 found = true;
		 }
		 else{
			 search(temp, value);
		 }
		}//end link else 
		return found;
	}
	public Node findLink(Node node, String value){
		int count = 0;
		for(String w: node.keys){
			if(w.compareTo(value)>0){
				count++;
			}//end if
			else{
				break;
			}//end else
		}// end for
			return node.links.get(count);
	}//end find value
	public Node getRoot(){
		return root;
	}
	
}//end class