package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class senseTagCorpus {

	/**
	 * @param args
	 */
	
//	private String inFileName = "YoujinTest.txt";
	private String inFileName = "Youjin.txt";
	private String senseDir;
	
	/**
	 * @param args
	 */
	
	
	public void process(String koFile, String kotgFile, String jpwdFile, String alignFlie, String anaChoice){
	    File inFile = new File(inFileName);
	    
	    BufferedReader inFileReader = null;
	    int youjinMeanCount;
	    
	    String read = "";
		String targetWord;
		extractWordCorpus ewc = new extractWordCorpus(koFile,  kotgFile,  jpwdFile,  alignFlie , anaChoice);
	    try {
	    	inFileReader = new BufferedReader(new FileReader(inFile));
	        
	        while ((read = inFileReader.readLine()) != null) {
	        	targetWord = read.split("\t")[0].split(" ")[0];
	        	youjinMeanCount = Integer.parseInt(read.substring(read.indexOf("(") + 1, read.indexOf(")"))) + 1;
	        	System.out.println(targetWord);
	        	
	        	ewc.windowSize = 2;
	        	ewc.senseDir = senseDir ;
	        	ewc.setTargetWord(targetWord);
	        	ewc.setMeanLimit(youjinMeanCount);
	        	
	        	ewc.process();
	        
	        }
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (inFileReader != null) {
	            try {
	            	inFileReader.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		senseTagCorpus st = new senseTagCorpus();
		
		st.senseDir = "senseTagCorpora_komaWindow2/";
		st.process("newsCorpus/newKoma/news.krwd","newsCorpus/newKoma/news.krtg","newsCorpus/newKoma/news.jpwd" ,"newsCorpus/newKoma/news.krjp" , "KOMA");
		
		
//		st.senseDir = "senseTagCorpora_Hannanum/";
//		st.process("newsCorpus/newHannanum/rawNews.krwd","newsCorpus/newHannanum/rawNews.krtg","newsCorpus/newHannanum/rawNews.jpwd" ,"newsCorpus/newHannanum/rawNews.krjp" , "HANNANUM");

	}

}
