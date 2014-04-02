import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;

//and a new change
public class BTree implements Serializable {
	private static final long serialVersionUID = 1L;
	Node root;
	int nodeCount;
	RandomAccessFile raf;
	
	public BTree(){
		root = new Node();
		root.setStartIndex(0);
		nodeCount = 1;
		try {
			raf = new RandomAccessFile("Btree.dat","rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end BTree
	
	public void insert(String value) throws IOException{
		if(root.isFull()){
			nodeCount = nodeCount +2;
			root.rootSplit(nodeCount,raf);
		}
			insert(root, value);	
	}//end public add 
	
	private void insert(Node node,String val) throws IOException{
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
						nodeCount++;
						node.split(node.links.get(i), nodeCount,raf);
						i = 0;
					}//end if
				}//end for
			temp = findLink(node,val);
			insert(temp,val);
			
			for(int i =0;i < node.links.size();i++){
				if(node.links.get(i).isFull()){
					nodeCount++;
					node.split(node.links.get(i),nodeCount,raf);
					i = 0;
				}//end if
			}//end for
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
		//	for(Node n:node.links){
			for(int i =0; i < node.links.size();i++){
				if(node.links.get(i).minSize()){
					node.repair(i);
					i = 0;
				}// end if
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
	
	public ArrayList<String> findPrefix(String Pre){
		return findPrefix(root,Pre);
	}
	
	private ArrayList<String> findPrefix(Node node, String Pre){
		ArrayList<String> valueList = new ArrayList<String>();
		for(int i = 0; i < node.keys.size();i++){
			if(node.keys.get(i).startsWith(Pre)||node.keys.get(i).compareTo(Pre)>0){
				if(!node.isLeaf()){
					checkLists(valueList,findPrefix(node.links.get(i),Pre));
					//if(node.links.get(i +1)!= null){
					// valueList.addAll(findPrefix(node.links.get(i+1),Pre));
					//}
				}
				if(!node.isLeaf() && node.links.get(i +1)!= null){
					checkLists(valueList,findPrefix(node.links.get(i+1),Pre));
				}
				if(node.keys.get(i).startsWith(Pre)){
					if(!valueList.contains(node.keys.get(i))){
						valueList.add(node.keys.get(i));
					}
				}// end three if's
				
			}// end big if 
			else if(i == node.keys.size()-1){
				if(!node.isLeaf() && node.links.get(i+1)!=null){ // Potently issue
					valueList.addAll(findPrefix(node.links.get(i+1),Pre));
				}//end one last check if 
			}// end end-of-rope else
		}// end for
		return valueList;
	}// end findPrefix
	
	private void checkLists(ArrayList<String>valueList,ArrayList<String> tempList){
		for(String s:tempList){
			if(valueList.contains(s)){
				//dont add it 
			}
			else{
				valueList.add(s);
			}
		}// end for
	}//end checkList
	
	public int getNodeCount(){
		return nodeCount;
	}//end nodeCount
	
}//end class