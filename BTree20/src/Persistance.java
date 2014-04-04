import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.RandomAccessFile;
import java.io.ObjectOutputStream; 
import java.io.ByteArrayOutputStream;
import java.io.Serializable;


public class Persistance implements Serializable {
	private static final long serialVersionUID = 1L;
	public Persistance(){
		
	}// end empty constrctor
	
	public void write(RandomAccessFile raf, Node node) throws IOException{
		ByteArrayOutputStream toBytes = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(toBytes);
		out.writeObject(node);
		raf.write(toBytes.toByteArray(),(int) node.getStartIndex(),69820);
	}// end method
	
	public Node read(RandomAccessFile raf, long startNumber) throws IOException, ClassNotFoundException{
		byte[] array = new byte[69820];
		raf.read(array, (int) startNumber, 69820);
		ByteArrayInputStream b = new ByteArrayInputStream(array);
		ObjectInputStream in = new ObjectInputStream(b);
		return (Node) in.readObject();
	}// end method	
}// end persistance
