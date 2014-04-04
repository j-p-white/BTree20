import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
public class Test {

	public static void main(String[] args) throws IOException {
		//JsoupTestStringManipulating();
		//testAdd();
		//testSave();
		//testFindPredecessor();
		//testDelete();
		//testPrefexFind();
		testPersist();
	}
	
	public static void JsoupTestStringManipulating(){
		JsoupParser pars = new JsoupParser();
		String word = "Spike Spegal"; 
		String URL = "http://en.wikipedia.org/wiki/Zombie";
		
		 for(String s: pars.urlTrimming(URL)){
			 System.out.println("words: "+s);
		 }
	
		System.out.println(pars.getPadding(word.length()));
	}
	
	public static void testAdd() throws IOException, ClassNotFoundException{
		BTree tree = new BTree();	
		tree.insert("apple"); 
		tree.insert("sand");
		tree.insert("math");
		tree.insert("tree");
		tree.insert("north");
		tree.insert("onion");
		tree.insert("pan");
		tree.insert("pink");
		tree.insert("pool"); 
		tree.insert("net");
		tree.insert("never");
		System.out.println("root: "+tree.getRoot().keys);
		System.out.println("left: "+tree.getRoot().links.get(0).keys);
		System.out.println("right: "+tree.getRoot().links.get(1).keys);
		System.out.println(" left 0: "+tree.getRoot().links.get(0).links.get(0).keys);
		System.out.println(" left 1: "+tree.getRoot().links.get(0).links.get(1).keys);
		System.out.println(" left 2: "+tree.getRoot().links.get(0).links.get(2).keys);
		System.out.println(" right 0: "+tree.getRoot().links.get(1).links.get(0).keys);
		System.out.println(" right 1: "+tree.getRoot().links.get(1).links.get(1).keys);
		System.out.println(" right 2: "+tree.getRoot().links.get(1).links.get(2).keys);
		System.out.println("node count: "+tree.getNodeCount());

	}
	public static void testFindPredecessor() throws IOException, ClassNotFoundException{
		BTree tree = new BTree();
		tree.insert("apple"); 
		tree.insert("sand");
		tree.insert("math");
		tree.insert("tree");
		tree.insert("north");
		tree.insert("onion");
		tree.insert("pan");
		tree.insert("pink");
		tree.insert("pool"); 
		System.out.println("get predicessor: "+tree.getRoot().predacessor(0));
	}	
	public static void testSave() throws IOException, ClassNotFoundException{
		BTree tree = new BTree();	
		tree.insert("apple"); 
		tree.insert("sand");
		tree.insert("math");
		tree.insert("tree");
		tree.insert("north");
		tree.insert("onion");
		tree.insert("pan");
		tree.insert("pink");
		tree.insert("pool"); 
		File myFile = new File("C:\\BTree.ser");
		try{
			FileOutputStream fout = new FileOutputStream(myFile);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(tree);
			oos.close();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}// end save test
	public static void testDelete() throws IOException, ClassNotFoundException{
		BTree tree = new BTree();
		tree.insert("apple"); 
		tree.insert("sand");
		tree.insert("math");
		tree.insert("tree");
		tree.insert("north");
		tree.insert("onion");
		tree.insert("pan");
		tree.insert("pink");
		tree.insert("pool"); 
		tree.insert("net");
		tree.insert("never");
		tree.delete("apple");
		
		System.out.println("root: "+tree.getRoot().keys);
		System.out.println("left: "+tree.getRoot().links.get(0).keys);
		System.out.println("right: "+tree.getRoot().links.get(1).keys);
		System.out.println(" left 0: "+tree.getRoot().links.get(0).links.get(0).keys);
		System.out.println(" left 1: "+tree.getRoot().links.get(0).links.get(1).keys);
		//System.out.println(" left 2: "+tree.getRoot().links.get(0).links.get(2).keys);
		System.out.println(" right 0: "+tree.getRoot().links.get(1).links.get(0).keys);
		System.out.println(" right 1: "+tree.getRoot().links.get(1).links.get(1).keys);
		System.out.println(" right 2: "+tree.getRoot().links.get(1).links.get(2).keys);
	}
	
	public static void testPrefexFind() throws IOException, ClassNotFoundException{
		BTree tree = new BTree();	
		tree.insert("apple"); 
		tree.insert("sand");
		tree.insert("math");
		tree.insert("tree");
		tree.insert("north");
		tree.insert("onion");
		tree.insert("pan");
		tree.insert("pink");
		tree.insert("pool"); 
		tree.insert("net");
		tree.insert("never");
		
		System.out.println(tree.findPrefix("s"));
	}//end prefex test
	
	public static void testPersist() throws IOException{
		Persistance per = new Persistance();
		Node myNode = new Node();
		Node temp;
		
		myNode.keys.add("apples");
		myNode.keys.add("bananas");
		myNode.keys.add("alromegabetadeltaseimpafiwhooohooo");
		temp = new Node();
		temp.keys.add("zing");
		myNode.links.add(0,temp);
		
		temp = new Node();
		temp.keys.add("zoom");
		myNode.links.add(1,temp);
		
		temp = new Node();
		temp.keys.add("whzooz");
		myNode.links.add(2,temp);
		
		
		
		
		BTree tree = new BTree();
		
		per.write(tree.getFile(), myNode);
	/*	tree.insert("apple"); 
		tree.insert("sand");
		tree.insert("math");
		tree.insert("tree");
		tree.insert("north");
		tree.insert("onion            ");
		tree.insert("pan");
		tree.insert("pink");
		tree.insert("pool"); 
		tree.insert("net");
		tree.insert("never");
	*/
		//	System.out.println("root: "+tree.getRoot().getStartIndex());
		/*	System.out.println("left: "+tree.getRoot().links.get(0).getStartIndex());
			System.out.println("right: "+tree.getRoot().links.get(1).getStartIndex());
			System.out.println(" left 0: "+tree.getRoot().links.get(0).links.get(0).getStartIndex());
			System.out.println(" left 1: "+tree.getRoot().links.get(0).links.get(1).getStartIndex());
			System.out.println(" left 2: "+tree.getRoot().links.get(0).links.get(2).getStartIndex());
			System.out.println(" right 0: "+tree.getRoot().links.get(1).links.get(0).getStartIndex());
			System.out.println(" right 1: "+tree.getRoot().links.get(1).links.get(1).getStartIndex());
			System.out.println(" right 2: "+tree.getRoot().links.get(1).links.get(2).getStartIndex());
			
		*/
		/*
			per.write(tree.getFile(), tree.getRoot());
			myNode = per.read(tree.getFile(),0);
			
			for(String key:myNode.keys){
				System.out.println(key.toString());
			}
			for(int i =0; i <myNode.links.size();i++){
				temp = new Node();
				temp = per.read(tree.getFile(), myNode.links.get(i).getStartIndex());
				for(String key:temp.keys){
					System.out.println(key);
				}
			}
		*/
	}
	
}//end class 