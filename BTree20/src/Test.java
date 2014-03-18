
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
		BTree tree = new BTree();	
		tree.insert("apple"); 
		tree.insert("sand");
		tree.insert("math");
		tree.insert("tree");
		System.out.println("root: "+tree.getRoot().keys);
		System.out.println("leftlink: "+tree.getRoot().links.get(0).keys);
		System.out.println("rightlink: "+tree.getRoot().links.get(1).keys);
	}
	
}//end class 
