package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import corpus.extractWordCorpus.transPair;
import corpus.extractWordCorpus.wdtg;

public class makeTestCorpora {

	/**
	 * @param args
	 */
	final private String anaToolChoice[] = {"HANNANUM","KOMA"};
	final private String anaToolTargetTag[] = {"nc" , "CM"};
	private String krFileName;
	private String tagFileName;
	private String targetWord;
	private int analysisChoice;
	public String dir;
	private int limit = 30;
	
	makeTestCorpora(String krFileName,String tagFileName,String dir, String targetWord, String anaChoice){
		this.krFileName = krFileName;
		this.tagFileName = tagFileName;
		this.targetWord = targetWord;
		this.dir = dir;
		
		if(anaChoice.equals(anaToolChoice[0]))
			analysisChoice = 0;
		else if (anaChoice.equals(anaToolChoice[1]))
			analysisChoice = 1;
		else{
			System.err.print("AnalysisChoice was not applicable!");
			System.exit(0);
		}
	}
	
	private void writeTransFile( ){
	    FileWriter targetWordWriter = null ;
	    BufferedReader krFileReader = null;
	    BufferedReader tagFileReader = null;
	    String krRead, krTag;
	    String [] kr, tag;
	    int idx;
	    int counter = 0;
	   
		try {
			krFileReader = new BufferedReader(new FileReader(new File(krFileName)));
			tagFileReader = new BufferedReader(new FileReader(new File(tagFileName)));
			targetWordWriter = new FileWriter(new File(dir + "/" + targetWord + ".test"));
			
			while ((krRead = krFileReader.readLine()) != null) {
				krTag = tagFileReader.readLine();
				
				//write to file.
				kr = krRead.split(" ");
				tag = krTag.split(" ");
				
				if(kr.length != tag.length && krRead.contains(" " + targetWord + " ") && 
						(idx = tarIdx(krRead , kr)) != -1 && tag[idx].startsWith(anaToolTargetTag[analysisChoice])){
					targetWordWriter.write(krRead + "\n");
					counter++;
				}
				if(counter == limit)
					break;
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
    		if (krFileReader != null || tagFileReader != null || targetWordWriter != null) {
	            try {
	            	krFileReader.close();
	            	tagFileReader.close();
	            	targetWordWriter.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
	
	private int tarIdx(String sent , String[] sentArray){
		int idx = -1;
		if(sent.contains(" " + targetWord + " ")){
			for(int i = 0; i < sentArray.length ; i++){
				if(sentArray[i].equals(targetWord)){
					idx = i;
					return idx;
				}
			}
		}
		return idx;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
