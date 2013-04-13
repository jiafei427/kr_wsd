package corpus;

import jap.javaTerminalResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

public class japCorpusProcess {

	/**
	 * @param args
	 */
	String corpustPath;
	String mecabTmpFileName;
	
	japCorpusProcess(String corpustPath){
		this.corpustPath = corpustPath;
	}
	void split2WordsTag(){
		runMecab();
		
		File file = new File(mecabTmpFileName);
		BufferedReader reader = null;
		String tempString = null;
	    File wordFile = new File(corpustPath.split("\\.")[0]+"_cl.jp");
	    File tagFile = new File(corpustPath.split("\\.")[0]+"_cl.jptg");
	    
	    FileWriter wordbWriter = null ,tagWriter = null;
	    int count = 0;
	    String tmp = "";
	    String word = "", tag = "";
	    String[] wordTags = null;
		try {
			 reader = new BufferedReader(new FileReader((file)));
			 wordbWriter = new FileWriter(wordFile);
			 tagWriter = new FileWriter(tagFile);
			 
				while ((tempString = reader.readLine()) != null && tempString != "") {
					
					if (tempString.equals("EOS")){
						word = word.trim() +"\n";
						tag  = tag.trim() +"\n";
						
						wordbWriter.write(word);
						tagWriter.write(tag);
						
//						System.out.print(word);
//						System.out.print(tag);
//						
						word = tag = "";
						count++;
            			continue;
            		}
				}
				
				System.out.println(count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (reader != null) {
	            try {
	            	wordbWriter.close();
	            	tagWriter.close();
	                reader.close();
	            } catch (IOException e1) {
	            }
	        }
		
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
//		String path = "newsCorpus/mecab/kj-corpus-1.koma";
		String path = "newsCorpus/raw.jp";
		japCorpusProcess test = new japCorpusProcess(path);
//		test.split2WordsTag();
		test.runMecab();
	}
}
