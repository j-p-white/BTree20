import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;


public class persistance extends RandomAccessFile{
File storage = new File(path);
	
	public persistance(File file, String myMode) throws FileNotFoundException {
		super(file, myMode);
		// TODO Auto-generated constructor stub
	}

}
