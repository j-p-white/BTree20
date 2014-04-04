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
	int incrementSize;
	BTree tree;
	RandomAccessFile raf;
	public Persistance() throws FileNotFoundException{
		raf = new RandomAccessFile("Btree.dat","rw");
		 incrementSize = 2364;
	}// end empty constrctor
	
	public void write(Node node) throws IOException{
		ByteArrayOutputStream b= new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(b);
		out.writeObject(node);
		raf.write(b.toByteArray(),node.getStartIndex(),incrementSize);
	}// end method
	
	public Node read(long startNumber) throws IOException, ClassNotFoundException{
		byte[] array = new byte[69820];
		raf.read(array, (int) startNumber, incrementSize);
		ByteArrayInputStream b = new ByteArrayInputStream(array);
		ObjectInputStream in = new ObjectInputStream(b);
		return (Node) in.readObject();
	}// end method	
}// end persistance
