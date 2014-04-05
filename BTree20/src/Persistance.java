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
		raf.seek(node.startIndex);
		// then write the propersize and 
		if(node.keys.size() < node.MAXKEYS){
			fillNode(node);
		}
		ByteArrayOutputStream b= new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(b);
		out.writeObject(node);
		arraySize = b.toByteArray().length; // print out arraySize
		System.out.println("nodeSize"+ arraySize);
		raf.write(b.toByteArray());
	}// end method
	
	public Node read(long startNumber) throws IOException, ClassNotFoundException{
		System.out.println("startNumber: "+startNumber);
		raf.seek(startNumber);
		Node temp;
		byte[] array = new byte[arraySize];
		raf.read(array);
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
	
	// this method may be broken 
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
			if(node.links.get(0) == badLong){
				node.links.remove(0);
				j =0;
			}
		}// end for

	}//end fixNode
	
}// end persistence