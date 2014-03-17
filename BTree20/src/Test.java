
public class Test {

	public static void main(String[] args) {
		//JsoupTestStringManipulating();
		testAdd();

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
		Node<String> myRoot = new Node<String>();
		Node<String> temp = new Node<String>();
		BTree<String> tree = new BTree<String>();
			
		tree.setRoot(myRoot);
		tree.insert("apple"); 
		
		temp = tree.getRoot();
		
		System.out.println("values of root: "+temp.keys);
		
		
	}
	
}//end class 
