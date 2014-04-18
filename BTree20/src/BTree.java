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
		try{
		save = new Save();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}// end BTree
	// some how more links are being made then should be. 
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
			temp = save.read(node.links.get(count));
		save.write(node);
			return temp;
	}//end find value

	public void delete(String value) throws ClassNotFoundException, IOException{
		root = save.read(0);
	  
		  if(value.length() < 34){
			  String p = getWordPadding(value.length());
			  value = value + p;
		  }
		  delete(root,value);
		  if(root.keys.size() == 0 && root.links.size() ==0){
			  System.out.println("deadTree");
		  }
		  else if(root.keys.size() == 0){
				Node temp ;
				temp = save.read(root.links.get(0));
				temp.blockNumber = 0;
				save.write(temp);
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
					if(s.equalsIgnoreCase(Val)){
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
	//double check neigbors
	//having a inifnate loop here
	public void repair(int count,Node n,Node badLink) throws ClassNotFoundException, IOException{
		Node neighborL = new Node();
		Node neighborR = new Node();
		
		if( count !=0 && n.links.get(count -1) !=null){
				neighborL = save.read(n.links.get(count -1));
		}
		if(count+1 < n.links.size() && count+1 != n.links.size()){
				neighborR = save.read(n.links.get(count+1));
		}
		
		 if( neighborL.keys.size() > (neighborL.MAXKEYS/2)-1){
			n.rotateRight(badLink,neighborL,count);
		 }
		
		 else if( neighborR.keys.size() > (neighborR.MAXKEYS/2)-1){
				n.rotateLeft(badLink,neighborR,count);	
		}
		
		else if(count == 0){
			 n.mergeRight(neighborR, badLink, count);
			    
		 }
		 else{
			 n.mergeLeft(neighborL,badLink, count -1);
		 }
		    
		
		for(Node t: n.getNode()){
				save.write(t);
		}
		save.write(n);
	}
	
	public String predacessor(int count, Node n) throws ClassNotFoundException, IOException{
		Node temp = save.read(n.links.get(count));
		return goRight(temp);
	}
	
	public String goRight(Node myNode) throws ClassNotFoundException, IOException{
		String toReturn;
		Node temp;
		if(myNode.isLeaf()){
		toReturn = myNode.keys.remove(myNode.keys.size()-1);
		save.write(myNode);
		}//end if
		else{
			temp =save.read(myNode.links.get(myNode.links.size() -1));
			toReturn = goRight(temp);
		}
		return toReturn;
	}
	
	
	public void internalRepair(int count,Node node) throws ClassNotFoundException, IOException{
		Node temp,temp2;
		temp = save.read(node.links.get(count));
		goRightRepair(temp);// why is parent in there
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
	public void goRightRepair(Node myNode) throws ClassNotFoundException, IOException{
		Node temp2;
		if(myNode.isLeaf()){
			return;
		}//end if
		else{
			temp2 =save.read(myNode.links.get(myNode.links.size() -1));
			goRightRepair(temp2);
			Node temp;
			for(int i = 0; i < myNode.links.size();i++){
				temp = save.read(myNode.links.get(i));
				if(temp.minSize()){
					repair(i,myNode,temp);
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
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(root);
		while(!queue.isEmpty()) {
			Node node = (Node)queue.remove();
			for(String s : node.keys){
				String[] v = s.split("\\s+");
				if(v[0].indexOf(Pre) != -1){
					myList.add(s);
				}
			}
			for(long l: node.links){
				queue.add(save.read(l));
			}
			//nodeList.clear();
		}
		// Clear visited property of nodes
		return myList;
	}
}//end class