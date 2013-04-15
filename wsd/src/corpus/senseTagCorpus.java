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
	public int windowSize = 4;
	String koFile, kotgFile, jpwdFile, alignFlie, anaChoice;
	
	/**
	 * @param args
	 */
	senseTagCorpus(String koFile, String kotgFile, String jpwdFile, String alignFlie, String anaChoice){
		this.koFile = koFile;
		this.kotgFile = kotgFile;
		this.jpwdFile = jpwdFile;
		this.alignFlie = alignFlie;
		this.anaChoice = anaChoice;
	}
	
	public void process(){
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
	        	
	        	ewc.windowSize = windowSize;
	        	ewc.senseDir = senseDir + "/";
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
		
		senseTagCorpus st = new senseTagCorpus("newsCorpus/newKoma/news.krwd","newsCorpus/newKoma/news.krtg","newsCorpus/newKoma/news.jpwd" ,"newsCorpus/newKoma/news.krjp" , "KOMA");
		makeCrossValidationFiles test; 
		
		for(int i = 1 ; i < 5 ; i++){
			st.senseDir = "senseTagCorpora_komaWindow" + i;
			st.process();
			test = new makeCrossValidationFiles(st.senseDir, "train", 5);
			test.process();
		}
		
		
		
//		st.senseDir = "senseTagCorpora_Hannanum/";
//		st.process("newsCorpus/newHannanum/rawNews.krwd","newsCorpus/newHannanum/rawNews.krtg","newsCorpus/newHannanum/rawNews.jpwd" ,"newsCorpus/newHannanum/rawNews.krjp" , "HANNANUM");

	}

}
