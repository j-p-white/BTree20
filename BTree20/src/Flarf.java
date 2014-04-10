import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
// thanks john

public class Flarf extends RandomAccessFile implements Serializable {
	private static final long serialVersionUID = 1L;
	private int blockSize;
	
	public Flarf(int blockSize,String file) throws FileNotFoundException{
		super(file,"rw");
		this.blockSize =blockSize;
	}
	
	public byte[] read(int blockNumber) throws IOException{
		seek(blockNumber * blockSize);
		byte b[] = new byte[blockSize];
		read(b);
		return b;
	}
	public void write(byte []b, int blockNumber) throws IOException{
		seek(blockNumber * blockSize);
		write(b);
		return;
	}
}
