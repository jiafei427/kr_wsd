package wsd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.hannanum.WorkflowFactory;

public class krSeg {
	private String inputFileName, tmpFileName;
	private File tmpFile;
	
	krSeg(String inputFileName){
		this.inputFileName = inputFileName;
		init();
	}

	private void init() {
		tmpFileName = "seg.tmp";
		tmpFile = new File(tmpFileName);
		if(tmpFile.exists())
			tmpFile.delete();
	}
	
	private int countPlus(String str){
		if(str.contains("+"))
			return str.split("\\+").length - 1;
		else
			return 0;
	}
	
	private int countSlash(String str){

		if(str.contains("/"))
			return str.split("/").length - 1;
		else
			return 0;
	}
	
	private void writeTmpFile(String word, String tag){
		String []wordArr, tagArr;
		File tmpFile = new File("seg.tmp");
		FileWriter fw = null;
		try {
			fw =new FileWriter(tmpFile, true);
			wordArr = word.split(" ");
			tagArr = tag.split(" ");
			for(int i = 0; i < wordArr.length ; i++){
				fw.write(wordArr[i] + "\t" + tagArr[i] + "\t" + "0" + "\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fw != null)
				try {
					fw.write("\n");
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private String removeInfoParse(String tempString){
	    String tmp = "";
	    String word = "";
	    String tag = "";
	    String[] wordTags = null;
	    String[] infos = tempString.split("\n");
		
	    for(int i = 0; i < infos.length; i++){
	    	if(infos[i].length() > 1 && infos[i].charAt(0) == '\t'){
				tmp = infos[i].substring(1);
				if(countPlus(tmp) < countSlash(tmp)){
					wordTags = tmp.split("\\+");
					for(int j = 0; j < wordTags.length ; j ++){
						word += wordTags[j].split("/")[0] + " ";
						tag += wordTags[j].split("/")[1] + " ";
					}
				}else{
					word += tmp.split("/")[0] + " ";
					tag += tmp.split("/")[1] + " ";
				}
			}
	    }
	    writeTmpFile(word, tag);
		return word.trim() +"\n";
	}

	public void krMorphAnalysis() {
		String tempString = null;
		BufferedReader reader = null;
		File inFile = new File(inputFileName);
//		this.segFileName = "seg.ko";
//	    File outFile = new File(segFileName);
//	    FileWriter outWriter = null ;
	    Workflow workflow1 = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_HMM_POS_TAGGER); 
	    
		try {
			 reader = new BufferedReader(new FileReader((inFile)));
//			 outWriter = new FileWriter(outFile);
			 workflow1.activateWorkflow(false);
			 
				while ((tempString = reader.readLine()) != null && tempString != "") {
					workflow1.analyze(tempString);
					removeInfoParse(workflow1.getResultOfDocument());
//					outWriter.write(removeInfoParse(workflow1.getResultOfDocument()));
//					outWriter.flush();
				}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}if (reader != null) {
            try {
//            	outWriter.close();
                reader.close();
            } catch (IOException e1) {
            }
        }
		workflow1.close();
	}
}
