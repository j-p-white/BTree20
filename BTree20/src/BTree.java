import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

//and a new change
public class BTree implements Serializable {
	private static final long serialVersionUID = 1L;
	Node root;
	int nodeCount;
	Save save;
	public BTree(){
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
		long nodeLocA=0;
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
				temp = save.read(nodeLocA);
				if(temp.minSize()){
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
	
	public void newRoot() throws ClassNotFoundException, IOException{
		Node temp ;
		temp = save.read(root.links.get(0));
		root.links.clear();
		root = temp;
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
	
	public void printTree(int count) throws IOException{
		Node temp;
		for(int i =0; i < count;i++){
		temp = save.read(i); 
		System.out.println("block number: "+temp.blockNumber);
			for(String s:temp.keys){
				System.out.println(s);
			}
			for(long l : temp.links){
				System.out.println("blocksLinks: "+ l);
				System.out.println("\n");
			}
		}
	}
	
	
	public ArrayList<String> bfs(String Pre) throws IOException
	{
		ArrayList<String> myList = new ArrayList<String>();
		ArrayList<Node> nodeList = new ArrayList<Node>();
		// BFS uses Queue data structure
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);
		root.visited = true;
		while(!queue.isEmpty()) {
			Node node = (Node)queue.remove();
			for(String s : node.keys){
				if(s.indexOf(Pre) != -1){
					myList.add(s);
				}
			}
			Node child=null;
			for(long l: node.links){
				nodeList.add(save.read(l));
			}
			for(Node n : nodeList) {
				n.visited=true;
				queue.add(child);
			}
			nodeList.clear();
		}
		// Clear visited property of nodes
		clearNodes();
		return myList;
	}
	private Node getUnvisitedChildNode(Node node) throws IOException{
		Node toReturn = null;
		for(int i =0; i < node.links.size();i++){
			Node temp = save.read(node.links.get(i));
			if(temp.visited == false){
				toReturn = temp;
				break;
			}
		}
		return toReturn;
	}
	private void clearNodes() throws IOException{
		Node temp;
		for(int i =0; i < nodeCount;i++){
			temp = save.read(i);
			temp.visited = false;
		}
	}
	
}//end class