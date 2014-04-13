import java.io.IOException;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//JsoupTestStringManipulating();
		//testAdd();
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
		Node temp = new Node();
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
		
		for(int i =0; i < testWords.size();i++){
		temp = tree.save.read(i); 
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
	public static void testDelete() throws IOException, ClassNotFoundException{
		BTree tree = new BTree();	
		String padding,fixedString;
		Node temp = new Node();
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
		tree.delete("apple");
		tree.delete("north");
		tree.delete("pink");
		//tree.delete("net");
		tree.delete("onion");

		for(int i =0; i < testWords.size();i++){
		temp = tree.save.read(i); 
		System.out.println("block number: "+temp.blockNumber);
			for(String s:temp.keys){
				System.out.println(s);
			}
			for(long l : temp.links){
				System.out.println("blocksLinks: "+ l);
				System.out.println("\n");
			}
		}
	}//end method
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