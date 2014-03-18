import java.io.IOException;
import java.io.File; 
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.Document;
/*
 * Classname:BTree
 * 
 * Version information: 1
 *
 * Date: 3/10/2014
 * 
 * Copyright notice: none
 */
public class JsoupParser {
	BTree tree = new BTree();
	public void readInFile(){ 
		Scanner scan;
		String url;
		String paddedWord = null;
		File BTreeFile = new File("BTreeURL.txt");
		String[] fileWords;
		try{ 
			scan = new Scanner(BTreeFile);
				while(scan.hasNextLine()){
					url = scan.nextLine();
						//parse the url
					fileWords = JsoupParsing(url);
					String trimmedUrl = urlTrimming(url)[1];
					for(String words:fileWords){
						if(words.length() < 33){
							paddedWord = hookStrings(words,trimmedUrl) + getPadding(hookStrings(words,trimmedUrl).length());
						}// end wordsIf	
						tree.insert(paddedWord);
					}//end for
				}//end scanner while
			scan.close();			
		}//end try
		catch(IOException e){
			e.printStackTrace();
		}
	}//end readInFile
	
	public String hookStrings(String withSpace, String toConnect){
		String pad = " ";
		return withSpace = withSpace+pad+ toConnect;
	}
	
	public String getPadding(int wordLength){
		int diffrence;
		String pad =" ";
		String padding = "";
		diffrence = 33 - wordLength;
		for(int i =0; i < diffrence;i++){
			padding = padding + pad;
		}// end for
		return padding;
	}
	
	public String[] urlTrimming(String url){
		return url.split("[.]+");
	}
	
	public String[] JsoupParsing(String url) throws IOException{
		Document doc;
		String bodyText;
		//get the url
		doc = Jsoup.connect(url).get();
		
		// get the body text
		bodyText = doc.body().text();
		
		bodyText.toLowerCase();
			
	//replace all not text characters 
	String[] myList = bodyText.split("[^a-zA-Z0-9']+");
	return myList;
	}// end JsoupParsing 
}//end class