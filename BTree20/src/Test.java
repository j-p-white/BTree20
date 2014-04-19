import java.io.IOException;
import java.util.ArrayList;

public class Test {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//the delete is moving things that should not be
		//testAdd(); //need to check this
		testDelete();
		//testPrefexFind();
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
		testWords.add("vincent");
		
		testWords.add("von"); 
		testWords.add("strudle"); 
		testWords.add("phantom");
		testWords.add("newEarth");
		
		testWords.add("nova");
		testWords.add("nest");
		testWords.add("nerf"); 
		testWords.add("newYork"); 
		testWords.add("newguni");
		
		
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
		ArrayList<String> foundWords = new ArrayList<String>();
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
		testWords.add("vincent");
		
		testWords.add("von"); 
		testWords.add("strudle"); 
		testWords.add("phantom");
		testWords.add("newEarth");
		
		testWords.add("nova");
		testWords.add("nest");
		testWords.add("nerf"); 
		testWords.add("newYork"); 
		testWords.add("newguni");

		for(String k : testWords){
			padding = getPadding(k.length());
			fixedString = k + padding;
			tree.insert(fixedString);
		}
		foundWords = tree.bfs("n");
		for(String s:foundWords){
			System.out.println(s);
			
		}
		System.out.println("end found words");
		
		for(String s: foundWords){
			System.out.println(s);
				tree.delete(s);
		}
	
		
		//tree.delete("onion");
	/*	
		tree.delete("never");
		tree.delete("newYork");
		tree.delete("pink");
		tree.delete("sand");
		tree.delete("nerf");
		tree.delete("nest");
		tree.delete("net");
		tree.delete("newEarth");
		tree.delete("newguni");
		tree.delete("north");
		tree.delete("nova");
		tree.delete("pan");
		tree.delete("phantom");
		tree.delete("vincent");
		tree.delete("von");
		tree.delete("pool");
		tree.delete("math");
		tree.delete("strudle");
		tree.delete("apple");
		tree.delete("tree");
	*/
	
		for(int i =0; i < testWords.size();i++){
		int count =0;
		temp = tree.save.read(i); 
		System.out.println("block number: "+temp.blockNumber);
			for(String s:temp.keys){
				System.out.println(s);
				//System.out.println("wordIndex: "+count);
				//count++;
				
			}
			for(long l : temp.links){
				System.out.println("blocksLinks: "+ l);
				System.out.println("\n");
			}
		}
	}//end method
	
	public static void testPrefexFind() throws IOException, ClassNotFoundException{
		BTree tree = new BTree();	
		Node temp = new Node();
		int i =0;
		JsoupParser parser = new JsoupParser(tree);
		ArrayList<String> foundWords = new ArrayList<String>();
	

		parser.readInFile();
		foundWords = tree.bfs("n");	
	
		for(String s: foundWords){
			try{
				tree.delete(s);
				i++;
			}catch(IndexOutOfBoundsException e){
				System.out.println(foundWords.get(i)); 
				System.out.println("index of I: "+ i);
				System.out.println("out of: "+foundWords.size());
				System.out.println("nodes in tree: "+tree.nodeCount);
				break;
			}
		}
	
		for(int j = 0; j < 3460;j++){
			temp = tree.save.read(j); 
			System.out.println("block number: "+temp.blockNumber);
				for(String s:temp.keys){
					System.out.println(s);
				}
				for(long l : temp.links){
					System.out.println("blocksLinks: "+ l);
					System.out.println("\n");
				}
		}
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