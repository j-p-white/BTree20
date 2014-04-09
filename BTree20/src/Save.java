import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;


public class Save {
	Flarf saveFile = new Flarf(1310,"Btree.dat");
	byte[] getBytes;
	final int STRINGLENGTH = 34;
	final int OFFSETBYTELENGTH = 8;
	final int STARTLINKBYTES = 1054;
	public Save() throws FileNotFoundException{
		 
	}
	
	public void saveNode(Node n) throws IOException{
		saveFile.write(packNode(n), n.getStartIndex()); 
	}
	
	
	public Node getNode(long blockNumber) throws IOException{
		getBytes = saveFile.read((int) blockNumber);
		return unpackNode(getBytes);
	}
	
	private byte[] packNode(Node n){
		byte [] nodeBytes = new byte [1320];
		byte [] nodeLinkBytes = new byte [256];
		int count = 0;
		byte [] temp; 
		for(String k:n.keys){
			if(k.length() < 34){
			 String p = getWordPadding(k.length());
				k = p + k;
			}
			temp = k.getBytes();
			
			for(int i = 0; i < temp.length;i++){
				nodeBytes[count] = temp[i];
				count++;
			}//end for
		}//end for
		
		if(n.links.size()< n.MAXKEYS){
			int myDiff = n.MAXKEYS - n.keys.size();
			String badString = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
			for(int i =0; i<myDiff;i++){
				temp = badString.getBytes();
				for(int k =0; k <temp.length;k++){
					nodeBytes[count] = temp[k];
					count++;
				}
			}
		}
		
		 int x = 0;
		for(long l: n.links){
			temp =toByte(l);
			for(int j = 0; j < temp.length; j++){
				nodeLinkBytes[x] = temp[j];
				x++;
			}
		}
		if(n.links.size()< n.MAXKEYS +1){
			int diff = (n.MAXKEYS +1) - n.links.size();
			long badLong = -999;
			for( int i =0 ; i < diff;i++){
				temp = toByte(badLong);
				for( int k =0; k< temp.length;k++){
					nodeLinkBytes[x] = temp[k];
					x++;
				}//end adding bytes
			}// end diffrence
		}// end if not enough links
		
		for(int z = 0; z< nodeLinkBytes.length;z++){
			nodeBytes[count] = nodeLinkBytes[z];
			count++;
		}// end adding links to nodeBytes
		return nodeBytes;
	}// end method
	
	private Node unpackNode(byte[] b){
		Node n = new Node();
		long l;
		byte [] reading = new byte[STRINGLENGTH];
		byte [] linkReading = new byte[OFFSETBYTELENGTH];
		for(int i =0; i < n.MAXKEYS;i++){
			System.arraycopy(b, STRINGLENGTH*i, reading, 0, STRINGLENGTH);
			String s2 = new String(reading);
			if(s2.startsWith(" ")){
				//do nothing
			}
			else{
				s2.trim();
				n.keys.add(s2);
			}
		}//end for
		for(int j =0; j <n.MAXKEYS+1;j++){
			System.arraycopy(b, (OFFSETBYTELENGTH*j)+ STARTLINKBYTES, linkReading, 0, OFFSETBYTELENGTH);
			l = toLong(linkReading);
			if(l == -999){
				
			}
			else{
				n.links.add(l);
			}
		}// end for
		
		return n;
	}// end unpack
	
	private String getWordPadding(int wordLength){
		int diffrence;
		String pad ="x";
		String padding = "";
		diffrence = 34 - wordLength;
		for(int i =0; i < diffrence;i++){
			padding = padding + pad;
		}// end for
		return padding;
	}
	
	private byte[] toByte(long l){
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong(l); 
		return buf.array();
	}
	
	private long toLong(byte[] b){
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.put(b);
		buf.flip();
		return buf.getLong();
	}
	
	
}
