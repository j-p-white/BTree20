import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

//and a new change
public class BTree implements Serializable {
	private static final long serialVersionUID = 1L;
	Node root;
	int nodeCount;
	Save save;
	public BTree() throws IOException{
		root = new Node();
		nodeCount = 0;
		save = new Save();
	}// end BTree
	
	public void insert(String value) throws IOException, ClassNotFoundException{
		if(root.keys.size() >0){
			root = save.read(0);
		}
			
		if(root.isFull()){
			nodeCount = nodeCount +2;
			root.rootSplit(nodeCount);
			for(Node n:root.getNode()){
				save.write(n);
			}
			save.write(root);
			root = save.read(0);
		}
			insert(root, value);	
			
			if(root.isFull()){
				nodeCount = nodeCount +2;
				root.rootSplit(nodeCount);
				for(Node n:root.getNode()){
					save.write(n);
				}
				save.write(root);
				root = save.read(0);
			}
			save.write(root);
		
	}//end public add 
	
	private void insert(Node node,String val) throws IOException, ClassNotFoundException{
		int count= 0;
		Node temp = new Node();
		Node looking = new Node();
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
			//write the node 
			save.write(node);
		}// end leaf case
		else
		{	
				for(int i =0;i < node.links.size();i++){
					long nodeLocA = node.links.get(i);
					looking = save.read(nodeLocA);
					if(looking.isFull()){
						nodeCount++;
						node.split(looking, nodeCount);
						for(Node n: node.getNode()){
							save.write(n);
						}
						save.write(node);
						i = 0;
					}//end if
				}//end for
			temp = findLink(node,val);
			insert(temp,val);
			
			for(int i =0;i < node.links.size();i++){
				long nodeLocA = node.links.get(i);
				looking = save.read(nodeLocA);
				if(looking.isFull()){
					nodeCount++;
					node.split(looking, nodeCount);
					for(Node n: node.getNode()){
						save.write(n);
					}
					save.write(node);
					i = 0;
				}//end if
			}//end for
		}// end else
	}//end private insert
	
	public boolean search(String val) throws ClassNotFoundException, IOException{
		boolean result = true;
		if(root.keys.contains(val)){
			result = true;
		}
		else{
			result = search(root, val);
		}
		return result;
	}//end method
	
	private boolean search(Node node, String value) throws ClassNotFoundException, IOException{
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
	public Node findLink(Node node, String value) throws ClassNotFoundException, IOException{
		int count = 0;
		Node temp = new Node();
		for(String w: node.keys){
			if(value.compareTo(w)>0){
				count++;
			}//end if
			else{
				break;
			}//end else
		}// end for
		long nodeLocA = node.links.get(count);
			temp = save.read(nodeLocA);
			return temp;
	}//end find value

	public void delete(String value) throws ClassNotFoundException, IOException{
		root = save.read(0);
	  if(search(value)){
		  String p = getWordPadding(value.length());
		  value = value + p;
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
	private void delete(Node node, String Val) throws ClassNotFoundException, IOException{
		int count = 0;
		Node badLink = new Node();
		String predecessorVal;
		if(node.keys.contains(Val)){ // need to fix this with find
			if(node.isLeaf()){
				node.keys.remove(Val); 
				save.write(node);
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
			for(int i =0; i < node.links.size();i++){
				long nodeLocA = node.links.get(i);
				badLink = save.read(nodeLocA);
				if(badLink.minSize()){
					repair(i,node,badLink);
					i = 0;
				}// end if
			}//end for
			return;
		}// end else
	}// end delete
	
	public Node getRoot(){
		return root;
	}
	
	public void newRoot() throws ClassNotFoundException, IOException{
		Node temp ;
		long nodeLocA = root.links.get(0);
		temp = save.read(nodeLocA);
		root.links.clear();
		root = temp;
	}
	
	public ArrayList<String> findPrefix(String Pre) throws ClassNotFoundException, IOException{
		return findPrefix(root,Pre);
	}
	
	private ArrayList<String> findPrefix(Node node, String Pre) throws ClassNotFoundException, IOException{
		ArrayList<String> valueList = new ArrayList<String>();
		Node temp = new Node();
		Node temp2 = new Node();
		for(int i = 0; i < node.keys.size();i++){
			if(node.keys.get(i).startsWith(Pre)||node.keys.get(i).compareTo(Pre)>0){
				long nodeLocA = node.links.get(i);
				long nodeLocB = node.links.get(i+1);
				temp = save.read(nodeLocA);
				temp2 = save.read(nodeLocB);
				if(!node.isLeaf()){
					checkLists(valueList,findPrefix(temp,Pre));
					//if(node.links.get(i +1)!= null){
					// valueList.addAll(findPrefix(node.links.get(i+1),Pre));
					//}
				}
				if(!node.isLeaf() && node.links.get(i +1)!= null){
					checkLists(valueList,findPrefix(temp2,Pre));
				}
				if(node.keys.get(i).startsWith(Pre)){
					if(!valueList.contains(node.keys.get(i))){
						valueList.add(node.keys.get(i));
					}
				}// end three if's
				
			}// end big if 
			else if(i == node.keys.size()-1){
				if(!node.isLeaf() && node.links.get(i+1)!=null){ // Potently issue
					valueList.addAll(findPrefix(temp2,Pre));
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
	
	private String getWordPadding(int wordLength){
		int diffrence;
		String pad =" ";
		String padding = "";
		diffrence = 34 - wordLength;
		for(int i =0; i < diffrence;i++){
			padding = padding + pad;
		}// end for
		return padding;
	}	
	
	//having a inifnate loop here
	public void repair(int count,Node n,Node badLink) throws ClassNotFoundException, IOException{
		Node neighbor = new Node();
		if( count !=0 && n.links.get(count -1) !=null){
				neighbor = save.read(n.links.size() -1);
				if( neighbor.keys.size() > neighbor.middle){
					n.rotateLeft(badLink,neighbor,count);
				}
		}
		 if(count+1 != n.links.size()){
				neighbor = save.read(n.links.size() +1);
				if( neighbor.keys.size() > neighbor.middle){
					n.rotateRight(badLink,neighbor,count);
				}
		}
		
		 if(count+1 == n.links.size()){
			    neighbor = save.read(n.links.get(count -1));
			    n.merge(badLink, neighbor, count -1);
		 }
		 else{
			 neighbor = save.read(n.links.get(count +1));
			 n.merge(neighbor, badLink, count);
		 }
			    
		
		for(Node t: n.getNode()){
				save.write(t);
		}
		save.write(n);
	}
}//end class