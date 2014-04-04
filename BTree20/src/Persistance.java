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
		ByteArrayOutputStream b= new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(b);
		out.writeObject(node);
		arraySize = b.toByteArray().length;
		raf.write(b.toByteArray(),node.getStartIndex(),arraySize);
		raf.close();
	}// end method
	
	public Node read(long startNumber) throws IOException, ClassNotFoundException{
		byte[] array = new byte[incrementSize];
		System.out.println("raf file length"+raf.length());
		raf.read(array,(int) startNumber, arraySize);
		ByteArrayInputStream b = new ByteArrayInputStream(array);
		ObjectInputStream in = new ObjectInputStream(b);
		return (Node) in.readObject();
	}// end method	
}// end persistence