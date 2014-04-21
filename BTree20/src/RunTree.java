import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RunTree {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Flarf f = new Flarf(1360,"Btree.dat");
		Save sav = new Save(f);
		Node readRoot = sav.read(0);
		BTree t = new BTree();
		t.root = readRoot;
		Scanner scan = new Scanner(System.in);
		ArrayList<String> foundWords;
	     System.out.print("enter int findCharecters: " );
	     String val = scan.next();
	    while(val != "-999"){
	    	foundWords = t.bfs(val);
	    	System.out.println("found words: "+ foundWords);
	     	for(String s : foundWords){
	     		System.out.println(s);
	     		t.delete(s);
	     	}
	     	foundWords = t.bfs(val);
	     	if(foundWords.size() ==0){
	    	 System.out.println("no values of: "+val+" found");
	     	}
		     System.out.print("enter in next Charecters: " );
		     val = scan.next();
	    }//end while check
	    scan.close();
	}

}
