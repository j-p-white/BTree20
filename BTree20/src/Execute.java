import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Execute
{
	public static void main(String [] args) throws ClassNotFoundException, IOException{
		Scanner scan = new Scanner(System.in);
		ArrayList<String> foundWords;
		BTree t = new BTree();
		JsoupParser parse = new JsoupParser(t);
	     parse.readInFile();
	     
	     System.out.print("enter int findCharecters: " );
	     String val = scan.next();
	     scan.close();
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
	}
}
