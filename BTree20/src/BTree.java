import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

//and a new change
public class BTree implements Serializable {
	private static final long serialVersionUID = 1L;
	Node root;
	int nodeCount;
	Save save;
	public BTree(){
		root = new Node();
		nodeCount = 0;
		try{
		save = new Save();
		}
		catch(IOException e){
			e.printStackTrace();
		}
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
		String[] s = val.split("\\s+");
		if(node.isLeaf())
		{
			for(String word: node.keys){
				if(s[0].compareTo(word)>0){
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
			temp = findLink(node,s[0]);
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
			String[] s = w.split("\\s+");
			if(value.compareTo(s[0])>0){
				count++;
			}//end if
			else{
				break;
			}//end else
		}// end for
		long nodeLocA = node.links.get(count);
		save.write(node);
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
				predecessorVal = predacessor(count,node);
				node.keys.set(count, predecessorVal); 
				internalRepair(count,node);
				save.write(node);
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
		temp = save.read(root.links.get(0));
		root.links.clear();
		root = temp;
		save.write(root);
	}
	
	public ArrayList<String> findPrefix(String Pre) throws ClassNotFoundException, IOException{
		root = save.read(0);
		return findPrefix(root,Pre);
	}
	
	private ArrayList<String> findPrefix(Node node, String pre) throws ClassNotFoundException, IOException{
		ArrayList<String> valueList = new ArrayList<String>();
		Node temp = new Node();
		Node temp2 = new Node();
		for(int i = 0; i < node.keys.size();i++){
			String[] s = node.keys.get(i).split("\\s+");
			if(s[0].indexOf(pre) != -1||s[0].compareTo(pre)>0){
				if(node.links.size()!= 0){
					if(node.links.get(i) != null){
						temp = save.read(node.links.get(i));
					}
					if(node.links.get(i+1) != null){
						temp2 = save.read(node.links.get(i+1));
					}
				}
				if(!node.isLeaf()){
					checkLists(valueList,findPrefix(temp,pre));
					//if(node.links.get(i +1)!= null){
					// valueList.addAll(findPrefix(node.links.get(i+1),Pre));
					//}
				}
				if(!node.isLeaf() && node.links.get(i +1)!= null){
					checkLists(valueList,findPrefix(temp2,pre));
				}
				if(s[0].indexOf(pre)!= -1){
					if(!valueList.contains(s[0])){
						valueList.add(node.keys.get(i));
					}
				}// end three if's
				
			}// end big if 
			else if(i == node.keys.size()-1){
				if(!node.isLeaf() && node.links.get(i+1)!=null){ // Potently issue
					valueList.addAll(findPrefix(temp2,pre));
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
		Node neighborL = new Node();
		Node neighborR = new Node();
		
		if( count !=0 && n.links.get(count -1) !=null){
				neighborL = save.read(n.links.get(count -1));
		}
		 if(count+1 < n.links.size() && count+1 != n.links.size()){
				neighborR = save.read(n.links.get(count+1));
		 }
		 
		
		if( neighborL.keys.size() > neighborL.middle){
				n.rotateLeft(badLink,neighborL,count);	
		}

		else if( neighborR.keys.size() > neighbor.middle){
				n.rotateRight(badLink,neighborR,count);
		}
		
		else if(count+1 == n.links.size()){
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
	
	
	
	public String predacessor(int count, Node n) throws ClassNotFoundException, IOException{
		Node temp = save.read(n.links.get(count));
		return goRight(temp,count+1);
	}
	
	public String goRight(Node myNode,int count) throws ClassNotFoundException, IOException{
		String toReturn;
		if(myNode.isLeaf()){
		toReturn = myNode.keys.remove(myNode.keys.size()-1);
		save.write(myNode);
		}//end if
		else{
			toReturn = goRight(save.read(myNode.links.get(count)),count);
		}
		return toReturn;
	}
	
	
	public void internalRepair(int count,Node node) throws ClassNotFoundException, IOException{
		Node temp,temp2;
		temp = save.read(node.links.get(count));
		goRightRepair(node,count+1,temp);
		for(int i = 0; i < node.links.size();i++){
			temp2 = save.read(node.links.get(i));
			if(temp2.minSize()){
				repair(i,node,temp2);
				i = 0;
				save.write(temp2);
			}
		}
	}// end internalRepair
	
	//will repair upto the internal node
	public void goRightRepair(Node myNode,int count,Node parentNode) throws ClassNotFoundException, IOException{
		if(myNode.isLeaf()){
			return;
		}//end if
		else{
			goRightRepair(save.read(myNode.links.get(count)),count,parentNode);
			Node temp;
			for(int i = 0; i < parentNode.links.size();i++){
				temp = save.read(parentNode.links.get(i));
				if(temp.minSize()){
					repair(i,parentNode,temp);
					i = 0;
					save.write(temp);
				}
			}
		}
	}
	
	
	
	
}//end class