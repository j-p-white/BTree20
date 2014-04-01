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
	
	public Node read(RandomAccessFile raf, long startNumber) throws IOException{
		raf.seek(startNumber);
		Node myTemp = new Node();
		
		for(int j =0; j < myTemp.MAXKEYS;j++){
			char [] temp = new char[33];
				for(int i=0;i < temp.length;i++){
					temp[i]=raf.readChar();			
				}
				String s = new String(temp);
				myTemp.keys.add(s);
		}// end for
		
		for(int j = 0; j < myTemp.MAXKEYS +1;j++){
			 long l = raf.readLong();
			 myTemp.links.get(j).setStartIndex(l);
		}// end for
		return myTemp;
	}// end method	
}
