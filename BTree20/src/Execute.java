import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;




public class Execute{


	public static void main(String [] args) throws ClassNotFoundException, IOException{
		Scanner scan = new Scanner(System.in);
		ArrayList<String> foundWords;
		BTree t = new BTree();
		JsoupParser parse = new JsoupParser(t);
	     parse.readInFile();
	     
	     System.out.println("enter int findCharecters: " );
	     String val = scan.next();
	     foundWords = t.bfs(val);
	     System.out.println("found words: "+ foundWords);
	     
	     for(String s : foundWords){
	    	 t.delete(s);
	     }
	     
	     
	}
}
