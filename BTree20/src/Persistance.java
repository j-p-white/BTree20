import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.RandomAccessFile;
import java.io.ObjectOutputStream; 
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Persistance implements Serializable {
	private static final long serialVersionUID = 1L;
	int incrementSize = 2364;
	RandomAccessFile raf;
	int arraySize;
	
	public Persistance() throws FileNotFoundException{
		raf= new RandomAccessFile("Btree.dat","rw");
	}// end empty constructor
	
	public void write(Node node) throws IOException{
		//this method needs to fill the node properly to the proper size 
		// then write the propersize and 
		if(node.keys.size() < node.MAXKEYS){
			fillNode(node);
		}
		ByteArrayOutputStream b= new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(b);
		out.writeObject(node);
		arraySize = b.toByteArray().length;
		raf.write(b.toByteArray(),node.getStartIndex(),arraySize);
		raf.close();
	}// end method
	
	public Node read(long startNumber) throws IOException, ClassNotFoundException{
		Node temp;
		byte[] array = new byte[incrementSize];
		System.out.println("raf file length"+raf.length());
		raf.read(array,(int) startNumber, arraySize);
		ByteArrayInputStream b = new ByteArrayInputStream(array);
		ObjectInputStream in = new ObjectInputStream(b);
		temp = (Node) in.readObject(); 
		fixNode(temp); 
		return temp;
	}// end method	
	
	private void fillNode(Node node){
		String star = "**********************************"; 
		long badLong = -1;
		
		for(int i = node.keys.size(); i < node.MAXKEYS;i++){
			node.keys.add(i,star);
		}
		for(int j = node.links.size(); j < node.MAXKEYS+1;j++){
			node.links.add(badLong);
		}
	}//end fillNode
	
	private void fixNode(Node node){
		String star = "**********************************"; 
		long badLong = -1;
		
		for(int i =0; i < node.keys.size();i++){
			if(node.keys.get(i).equals(star)){
				node.keys.remove(i);
				i =0;
			}
		}// end for
		
		for(int j =0; j < node.links.size();j++){
			if(node.links.get(j) == badLong){
				node.links.remove(j);
				j =0;
			}
		}// end for
	}//end fixNode
	
}// end persistence