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
		Node temp = new Node();
		if(node.isLeaf())
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
		}// end leaf case
		else
		{	
				for(int i =0;i < node.links.size();i++){
					if(node.links.get(i).isFull()){
						node.split(node.links.get(i));
						i = 0;
					}//end if
				}//end for
			temp = findLink(node,val);
			insert(temp,val);
		}// end else
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
			if(value.compareTo(w)>0){
				count++;
			}//end if
			else{
				break;
			}//end else
		}// end for
			return node.links.get(count);
	}//end find value

	// i am trying to find links in the node i deleted instead of the nodes parent
	public void delete(String value){
	  if(search(value)){
		  delete(root,value);
		  if(root.keys.size() == 0){
		  // reset root
			  newRoot();
		  }
	  }
	  else {
		  System.out.println("this value is not real");
	  }	
	}
	private void delete(Node node, String Val){
		int count = 0;
		int myCount = 0;
		String predecessorVal;
		if(node.keys.contains(Val)){
			if(node.isLeaf()){
				node.keys.remove(Val); 
				return;
			}
			else{
				//find the key value
				for(String s:node.keys){
					if(s.equalsIgnoreCase(s)){
						break;
					}
						count++;
				}// end for
				predecessorVal = node.predacessor(count);
				node.keys.set(count, predecessorVal); 
				node.internalRepair(count);
			}//end else
		}
		else{
			delete(findLink(node,Val),Val);
			for(Node n:node.links){
				if(n.minSize()){
					node.repair(myCount);
				}// end if
					myCount++;
			}//end for
			return;
		}// end else
	}// end delete
	
	public Node getRoot(){
		return root;
	}
	
	public void newRoot(){
		Node temp ;
		temp = root.links.get(0);
		root.links.clear();
		root = temp;
	}
	
}//end class