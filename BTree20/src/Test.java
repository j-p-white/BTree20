import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
public class Test {

	public static void main(String[] args) {
		//JsoupTestStringManipulating();
		//testAdd();
		//testSave();
		//testFindPredecessor();
		testDelete();
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
	
	public static void testAdd(){
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
		System.out.println(" left-left: "+tree.getRoot().links.get(0).links.get(0).keys);
		System.out.println(" left-right: "+tree.getRoot().links.get(0).links.get(1).keys);
		System.out.println(" right-left: "+tree.getRoot().links.get(1).links.get(0).keys);
		System.out.println(" right-left: "+tree.getRoot().links.get(1).links.get(1).keys);

	}
	public static void testFindPredecessor(){
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
	public static void testSave(){
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
	public static void testDelete(){
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
		tree.delete("net");
		tree.delete("math");
		
		System.out.println("root: "+tree.getRoot().keys);
		System.out.println("left: "+tree.getRoot().links.get(0).keys);
		System.out.println("mid: "+tree.getRoot().links.get(1).keys);
		System.out.println("right: "+tree.getRoot().links.get(2).keys);
	}
}//end class 