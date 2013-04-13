package jap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import corpus.japCorpusProcess;

public class mecabTest {

	/**
	 * @param args
	 */
	
	String corpustPath;
	String mecabTmpFileName;
	
	mecabTest(String corpustPath){
		this.corpustPath = corpustPath;
	}
	
	void runMecab(){
		mecabWordSeg();
		mecabTag();
	}
	
	void mecabWordSeg(){
		String tempString = null;
		mecabTmpFileName = corpustPath.split("\\.")[0]+"_mecab.jpwd";
	    File outFile = new File(mecabTmpFileName);
	    
	    FileWriter outWriter = null ;
	    javaTerminalResult jtr = null ;
	    LineNumberReader reader = null ;
	    int i = 0;
	    System.out.println("******************************");
	    System.out.println(corpustPath);
		try {
			 outWriter = new FileWriter(outFile);
			 jtr = new javaTerminalResult("mecab -O CKWD " + corpustPath);
			 reader = jtr.getTerminalOutput();
			 
			 while ((tempString = reader.readLine()) != null)
				 	i++;
					outWriter.write(tempString.trim() + "\n");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (reader != null) {
	            try {
	            	outWriter.close();
	                reader.close();
	            } catch (IOException e1) {
	            }
	        }
	        System.out.println(i);
	}
	
	void mecabTag(){
		String tempString = null;
		mecabTmpFileName = corpustPath.split("\\.")[0]+"_mecab.jptg";
	    File outFile = new File(mecabTmpFileName);
	    
	    FileWriter outWriter = null ;
	    javaTerminalResult jtr = null ;
	    LineNumberReader reader = null ;
	    
		try {
			 outWriter = new FileWriter(outFile);
			 jtr = new javaTerminalResult("mecab -O CKTG " + corpustPath);
			 reader =  jtr.getTerminalOutput();
			 
			 while ((tempString = reader.readLine()) != null)
						outWriter.write(tempString.trim() + "\n");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (reader != null) {
	            try {
	            	outWriter.close();
	                reader.close();
	            } catch (IOException e1) {
	            }
	        }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tempString;
//		javaTerminalResult jtr = new javaTerminalResult("mecab a.txt");
//		javaTerminalResult jtr = new javaTerminalResult("mecab -O CK2 a.txt");
//		javaTerminalResult jtr = new javaTerminalResult("mecab -O CKWD a.txt");
		javaTerminalResult jtr = new javaTerminalResult("mecab -O CKTG a.txt");
		
		//Wrote my own template file that mecab will give result that I want.
		//Located on C:\Program Files (x86)\MeCab\etc named mecabrc
		
		
		LineNumberReader reader = jtr.getTerminalOutput();
		try {
			while ((tempString = reader.readLine()) != null)
				System.out.println(tempString.trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}if(reader != null)
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		String path = "newsCorpus/mecab/kj-corpus-1.jp";
		mecabTest test = new mecabTest(path);
		test.runMecab();
	}
}
