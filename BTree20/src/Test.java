import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//JsoupTestStringManipulating();
		//testAdd();
		//testFindPredecessor();
		testDelete();
		//testPrefexFind();
		//testSave3();	
		//testLongSave();
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
			for(String s:temp.keys){
				System.out.println(s);
			}
		}
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
		tree.delete("north");
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
	public static void testSave3() throws IOException{
		Flarf f = new Flarf(126,"flarf.dat");
		byte [] temp; // loads ary2 and ary6
		byte[]ary2 = new byte[126]; // ends out the node
		byte[]ary3; // reads in the node
		byte[]ary5 = new byte [34]; // gets stings back
		byte[]ary6 = new byte [8]; // will gets longs
		long l1,l2,l3;// longs for array
		long l4; // gets the long back
		l1 = 34; 
		l2 = 68;
		l3 = 102;
		ArrayList<String> ary = new ArrayList<String>();
		ArrayList<Long> ary4 = new ArrayList<Long>();
		ary.add("apple"); 
		ary.add("sand"); 
		ary.add("math");
		ary4.add(l1);
		ary4.add(l2); 
		ary4.add(l3);
		int count =0;
		
		for(int i =0; i < ary.size();i++){
			String s2 = ary.get(i);
			String p = getPadding(s2.length());
			s2 = s2 + p;
			temp = s2.getBytes();
				for(int j =0; j <temp.length;j++){
					ary2[count] = temp[j]; 
					count++;
				}
		}
		for(int i =0; i < ary4.size();i++){
				temp = toByte(ary4.get(i));
				for(int  j =0; j <temp.length;j++){
					ary2[count] = temp[j];
					count++;
				}
		}
		
		
		System.out.println("ary2 length: "+ ary2.length);
		f.write(ary2, 0);
		System.out.println("all writen");
				
		ary3 = f.read(0);
		System.out.println("filled ary length: "+ary3.length);
	
		System.arraycopy(ary3, 0, ary5, 0, 34);
		String s2 = new String(ary5);
		System.out.println(s2);
				
		System.arraycopy(ary3, 34, ary5, 0, 34);
		String s3 = new String(ary5);
		System.out.println(s3);
					
		System.arraycopy(ary3, 68, ary5, 0, 34);
		String s4 = new String(ary5);
		System.out.println(s4);
		// get longs
		System.arraycopy(ary3, 102, ary6, 0, 8);
		l4 = toLong(ary6);
		System.out.println(l4);
		
		System.arraycopy(ary3, 110, ary6, 0, 8);
		l4 = toLong(ary6);
		System.out.println(l4);
		
		System.arraycopy(ary3, 118, ary6, 0, 8);
		l4 = toLong(ary6);
		System.out.println(l4);
	}	
	
	public static void testLongSave(){
		long test = 89078;
		long check;
		byte [] get; 
		get = toByte(test);
		check = toLong(get);
		
		System.out.println("my value: "+ check);
	}
	
	public static byte[] toByte(long l){
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong(l); 
		return buf.array();
	}
	
	public static long toLong(byte[] b){
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.put(b);
		buf.flip();
		return buf.getLong();
	}
	
	
	
}//end class 