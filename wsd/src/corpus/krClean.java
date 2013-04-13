package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.hannanum.WorkflowFactory;


//corpus.dep.head-n100


public class krClean {

	String corpustPath;
	File inputFile;
	
	krClean(String corpustPath){
		this.corpustPath = corpustPath;
	}
	int countPlus(String str){
		if(str.contains("+"))
			return str.split("\\+").length - 1;
		else
			return 0;
	}
	int countSlash(String str){
		if(str.contains("/"))
			return str.split("/").length - 1;
		else
			return 0;
	}
	
	public void Hannanum(){
		 jhanWriteFile();
		 hannanumCorpus();
	}
	
	private void jhanWriteFile() {
		String tempString = null;
		BufferedReader reader = null;
		File inFile = new File(corpustPath);
		
		inputFile = new File(corpustPath.split("\\.")[0]+".JhanTmp");
	    FileWriter outWriter = null ;
	    Workflow workflow1 = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_POS_SIMPLE_09); 
	    
		try {
			 reader = new BufferedReader(new FileReader((inFile)));
			 outWriter = new FileWriter(inputFile);
			 workflow1.activateWorkflow(false);
			 
				while ((tempString = reader.readLine()) != null && tempString != "") {
					workflow1.analyze(tempString);
					outWriter.write(workflow1.getResultOfDocument());
					outWriter.flush();
				}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}if (reader != null) {
            try {
            	outWriter.close();
                reader.close();
            } catch (IOException e1) {
            }
        }
		workflow1.close();
	}
	
	void hannanumCorpus(){
		BufferedReader reader = null;
		String tempString = null;
		
		inputFile = new File(corpustPath.split("\\.")[0]+".HannanumTmp");
	    File wordFile = new File(corpustPath.split("\\.")[0]+"_word.ko");
	    File tagFile = new File(corpustPath.split("\\.")[0]+"_tag.ko");
	    
	    FileWriter wordbWriter = null ,tagWriter = null;
	    int count = 0;
	    String tmp = "";
	    String word = "", tag = "";
	    String[] wordTags = null;
		try {
			 reader = new BufferedReader(new FileReader((inputFile)));
			 wordbWriter = new FileWriter(wordFile);
			 tagWriter = new FileWriter(tagFile);
			 
				while ((tempString = reader.readLine()) != null && tempString != "") {
//					System.out.println(tempString);
					if (tempString.equals("EOS")){
						word = word.trim() +"\n";
						tag  = tag.trim() +"\n";
						
						wordbWriter.write(word);
						tagWriter.write(tag);
						
//						System.out.print(word);
//						System.out.print(tag);

						word = tag = "";
						count++;
            			continue;
            		}
					else if(tempString.length() > 1 && tempString.charAt(0) == '\t'){
//						tmp = tempString;
//						tmp += tempString.substring(1).replaceAll("\\+", " ")+" ";
						tmp = tempString.substring(1);
//						System.out.println(tmp);
						if(countPlus(tmp) < countSlash(tmp)){
//							System.out.println(countPlus(tmp));
//							System.out.println(countSlash(tmp));
							wordTags = tmp.split("\\+");
							for(int i = 0; i < wordTags.length ; i ++){
								word += wordTags[i].split("/")[0] + " ";
								tag  += wordTags[i].split("/")[1] + " ";
							}
						}else{
							word += tmp.split("/")[0] + " ";
							tag  += tmp.split("/")[1] + " ";
						}
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
	void koma2WordTag(){
		File file = new File(corpustPath);
		BufferedReader reader = null;
		String tempString = null;
	    File wordFile = new File(corpustPath.split("\\.")[0]+"_word.ko");
	    File tagFile = new File(corpustPath.split("\\.")[0]+"_tag.ko");
	    
	    FileWriter wordbWriter = null ,tagWriter = null;
	    int count = 0;
	    String tmp = "", wordTag;
	    String word = "", tag = "";
	    String[] wordTags = null;
		try {
			 reader = new BufferedReader(new FileReader((file)));
			 wordbWriter = new FileWriter(wordFile);
			 tagWriter = new FileWriter(tagFile);
			 
				while ((tempString = reader.readLine()) != null) {
					if (tempString.equals("")){
						word = word.trim() +"\n";
						tag  = tag.trim() +"\n";
						
						wordbWriter.write(word);
						tagWriter.write(tag);
//						
//						System.out.print(word);
//						System.out.print(tag);
						
						word = tag = "";
						count++;
            			continue;
            		}
					else if(tempString.length() > 1 && tempString.charAt(0) == ' '){
//						tmp = tempString;
//						tmp += tempString.substring(1).replaceAll("\\+", " ")+" ";
						tmp = tempString.substring(tempString.indexOf(":") + 1);
						wordTags = tmp.split(" \\+ ");
						for(int i = 0; i < wordTags.length ; i ++){
							wordTag = wordTags[i].trim();
							
//							System.out.println(wordTag);
							
							word += wordTag.substring(0, wordTag.lastIndexOf("(")) + " ";
							
							tag  += wordTag.substring(wordTag.lastIndexOf("(") + 1 , wordTag.length() -1) + " ";
						}
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String path = "moses_kr/tour_info.ko";
//		String path = "koma.clean";
		String path = "newsCorpus/newHannanum/rawNews.ko";
		krClean test = new krClean(path);
		test.hannanumCorpus();
//		test.koma2WordTag();
	}
	
}
