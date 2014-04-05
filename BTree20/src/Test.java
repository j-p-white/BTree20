import java.io.IOException;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//JsoupTestStringManipulating();
		testAdd();
		//testFindPredecessor();
		//testDelete();
		//testPrefexFind();
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
		String padding,fixedString;
		ArrayList<String> testWords = new ArrayList<String>();
		testWords.add("apple");
		testWords.add("sand");
		testWords.add("math");
		testWords.add("tree");
		testWords.add("north");
		testWords.add("onion");
		testWords.add("pan");
		testWords.add("pink");
		testWords.add("pool");
		testWords.add("net");
		testWords.add("never");
		
		for(String k : testWords){
			padding = getPadding(k.length());
			fixedString = k + padding;
			tree.insert(fixedString);
		}
		
		Node temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8,root;
		root = tree.per.read(0);
		temp1 = tree.per.read(root.links.get(0));
		temp2 = tree.per.read(root.links.get(1));
		temp3 = tree.per.read(temp1.links.get(0));
		temp4 = tree.per.read(temp1.links.get(1));
		temp5 = tree.per.read(temp1.links.get(2));
		temp6 = tree.per.read(temp2.links.get(0));
		temp7 = tree.per.read(temp2.links.get(1));
		temp8 = tree.per.read(temp2.links.get(2));
		
	/*
		System.out.println("root: "+root.keys);
		System.out.println("left: "+temp1.keys);
		System.out.println("right: "+temp2.keys);
		System.out.println("left 0: "+temp3.keys);
		System.out.println(" left 1: "+temp4.keys);
		System.out.println(" left 2: "+temp5.keys);
		System.out.println(" right 0: "+temp6.keys);
		System.out.println(" right 1: "+temp7.keys);
		System.out.println(" right 2: "+temp8.keys);
		System.out.println("node count: "+tree.getNodeCount());
	*/
		
		System.out.println("root: "+root.getStartIndex());
		System.out.println("left: "+temp1.getStartIndex());
		System.out.println("right: "+temp2.getStartIndex());
		System.out.println("left 0: "+temp3.getStartIndex());
		System.out.println(" left 1: "+temp4.getStartIndex());
		System.out.println(" left 2: "+temp5.getStartIndex());
		System.out.println(" right 0: "+temp6.getStartIndex());
		System.out.println(" right 1: "+temp7.getStartIndex());
		System.out.println(" right 2: "+temp8.getStartIndex());
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
	/*	
		System.out.println("root: "+tree.getRoot().keys);
		System.out.println("left: "+tree.getRoot().links.get(0).keys);
		System.out.println("right: "+tree.getRoot().links.get(1).keys);
		System.out.println(" left 0: "+tree.getRoot().links.get(0).links.get(0).keys);
		System.out.println(" left 1: "+tree.getRoot().links.get(0).links.get(1).keys);
		//System.out.println(" left 2: "+tree.getRoot().links.get(0).links.get(2).keys);
		System.out.println(" right 0: "+tree.getRoot().links.get(1).links.get(0).keys);
		System.out.println(" right 1: "+tree.getRoot().links.get(1).links.get(1).keys);
		System.out.println(" right 2: "+tree.getRoot().links.get(1).links.get(2).keys);
	*/
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
	private static String getPadding(int wordLength){
		int diffrence;
		String pad =" ";
		String padding = "";
		diffrence = 34 - wordLength;
		for(int i =0; i < diffrence;i++){
			padding = padding + pad;
		}// end for
		return padding;
	}
	
	
}//end class 