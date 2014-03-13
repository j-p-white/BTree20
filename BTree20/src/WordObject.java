import java.io.Serializable;

public class WordObject implements Serializable {

private static final long serialVersionUID = 1L;
String word; 
String url; 

public WordObject(){
	
}//end empty constructor

 public WordObject(String letters, String src){
	 word = letters; 
	 url = src;
 }// end constructor
}//end class
