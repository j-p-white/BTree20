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
	
	//need to make all word and url lengths the same
	public void readInFile(){ //make this line main to test again
		
		Scanner scan;
		WordObject myObj;
		String url;
		String paddedWord = null;
		String paddedUrl = null;
		File BTreeFile = new File("BTreeURL.txt");
		String[] fileWords;
		BTree tree;
		try{ 
			scan = new Scanner(BTreeFile);
				while(scan.hasNextLine()){
					url = scan.nextLine();
						//parse the url
					fileWords = JsoupParsing(url);
					for(String words:fileWords){
						if(words.length() < 33){
							paddedWord = words+getPadding(words.length());
						}// end wordsIf
						String trimmedUrl = urlTrimming(url)[1];
						if(trimmedUrl.length() < 33 ){
							paddedUrl = url+ getPadding(trimmedUrl.length());
						}//end urlIf
						myObj = new WordObject(paddedWord,paddedUrl);
						if(tree.search(myObj)){
						 tree.insert(myObj);
						}//end if
					}//end for
				}//end scanner while
			scan.close();			
		}//end try
		catch(IOException e){
			e.printStackTrace();
		}
	}//end readInFile
	
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