package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class statis {

	private String inFileName = "YouJin.txt";
	private String outFileName = "Statistic.txt";
	private String testFileName = "Stat_Test.txt";
	private String wordFileName = "YoujinClean.txt";
	private String csvFileName = "word_statics.csv";
	/**
	 * @param args
	 */
	public void process(String koFile, String kotgFile, String jpwdFile, String alignFlie, String anaChoice){
	    File inFile = new File(inFileName);
	    File outFile = new File(outFileName);
	    File wordFile = new File(wordFileName);
	    
	    BufferedReader inFileReader = null;
	    FileWriter fw = null , fw2 = null;
	    int youjinMeanCount;
	    int eachAllSentSize = 0 , wordCounter = 0;
	    
	    String read = "";
	    String buf;
		String targetWord;
		File csvFile;
		extractWordCorpus ewc = new extractWordCorpus(koFile,  kotgFile,  jpwdFile,  alignFlie , anaChoice);
//		extractWordCorpus ewc = new extractWordCorpus("komaAlign/final_koma.ko","komaAlign/final_koma.tg","komaAlign/final_koma.jp" ,"komaAlign/koma.aligned");
//		extractWordCorpus ewc = new extractWordCorpus("newsCorpus/news.ko","newsCorpus/news.tg","newsCorpus/news.jp" ,"newsCorpus/news.aligned");
		
	    try {
	    	inFileReader = new BufferedReader(new FileReader(inFile));
	    	fw = new FileWriter(outFile);
	    	fw2 = new FileWriter(wordFile);
	    	ewc.csvFileName = csvFileName;
	    	csvFile = new File(csvFileName);
	    	if(csvFile.exists())
	    		csvFile.delete();
	        
	        while ((read = inFileReader.readLine()) != null) {
	        	targetWord = read.split("\t")[0].split(" ")[0];
	        	youjinMeanCount = Integer.parseInt(read.substring(read.indexOf("(") + 1, read.indexOf(")"))) + 1;
//	        	System.out.println(read.substring(read.indexOf("(") + 1, read.indexOf(")")));
	
	        	System.out.println(targetWord);
	        	ewc.setTargetWord(targetWord);
	        	ewc.setMeanLimit(youjinMeanCount);
	        	
	        	buf = ewc.process();
	        	if(buf != null){
	        		fw.write("**************************************************************************************\n");
	        		fw.write(read + "\n");
	        		fw.write(buf.trim() + "\n");
	        		eachAllSentSize += ewc.eachAllSentSize;
	        		wordCounter++;
	        		
	        		fw2.write(targetWord + "(" + ewc.eachSenseSize + ")" + "\n");
	        	}
	        }
	        
	        fw.write("WordSize:" + wordCounter + "\tSentSize:" + eachAllSentSize + "\n");
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (inFileReader != null) {
	            try {
	            	inFileReader.close();
	            	fw.close();
	            	fw2.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
	public void testOneWord(String targetWord){
	    File outFile = new File(testFileName);
	    
	    FileWriter fw = null;
	    
	    String read = "";
	    String buf;
//		extractWordCorpus ewc = new extractWordCorpus("newsCorpus/newHannanum/rawNews.krwd","newsCorpus/newHannanum/rawNews.krtg","newsCorpus/newHannanum/rawNews.jpwd" ,"newsCorpus/newHannanum/rawNews.krjp" , "HANNANUM");
		extractWordCorpus ewc = new extractWordCorpus("newsCorpus/newKoma/news.krwd","newsCorpus/newKoma/news.krtg","newsCorpus/newKoma/news.jpwd" ,"newsCorpus/newKoma/news.krjp" , "KOMA");
		
		try {
	    	fw = new FileWriter(outFile);
        	System.out.println(targetWord);
        	ewc.setTargetWord(targetWord);
        	ewc.setMeanLimit(5);
        	
//        	ewc.pro();
        	ewc.Statistics_old();
        	
        	buf = ewc.statistic();
        	if(buf != null){
        		fw.write("**************************************************************************************\n");
        		fw.write(read + "\n");
        		fw.write(buf.trim() + "\n");
        	}
	        	
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (fw != null) {
	            try {
	            	fw.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		statis st = new statis();
		st.outFileName = "Statis_newsKoma.txt";
		st.wordFileName = "YoujinClean_Koma.txt";
//		st.csvFileName = "word_statics_Koma.csv";
		st.process("newsCorpus/newKoma/news.krwd","newsCorpus/newKoma/news.krtg","newsCorpus/newKoma/news.jpwd" ,"newsCorpus/newKoma/news.krjp" , "KOMA");

		st = new statis();
		st.outFileName = "Statis_newsHannanum.txt";
		st.wordFileName = "YoujinClean_Hannanum.txt";
//		st.csvFileName = "word_statics_Hannanum.csv";
		st.process("newsCorpus/newHannanum/rawNews.krwd","newsCorpus/newHannanum/rawNews.krtg","newsCorpus/newHannanum/rawNews.jpwd" ,"newsCorpus/newHannanum/rawNews.krjp" , "HANNANUM");

		//		st.testOneWord("배");
	}
	
}
