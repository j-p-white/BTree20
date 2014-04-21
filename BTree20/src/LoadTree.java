import java.io.IOException;

public class LoadTree
{
	public static void main(String []args) throws ClassNotFoundException, IOException{
		BTree t = new BTree();
		JsoupParser parse = new JsoupParser(t);
	    parse.readInFile();
	    RunTree.main(args);
	}
	
}
