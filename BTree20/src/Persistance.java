import java.io.IOException;
import java.io.RandomAccessFile;


public class Persistance {
	
	public Persistance(){
		
	}// end empty constrctor
	
	public void write(RandomAccessFile raf, Node node) throws IOException{
		raf.seek(node.startIndex);
		StringBuffer sb;
		for(String key:node.keys){
			sb = new StringBuffer(key);
			sb.setLength(33);
			raf.writeChars(sb.toString());
		}//end for
		
		for(Node n:node.links){
			raf.writeLong(n.getStartIndex());
		}
	}// end method
}
