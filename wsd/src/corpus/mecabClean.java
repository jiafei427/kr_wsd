package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class mecabClean {

	String corpustPath;
	mecabClean(String corpustPath){
		this.corpustPath = corpustPath;
	}
	
	
	void mecab2SegJap(){
		File file = new File(corpustPath);
		BufferedReader reader = null;
		String tempString = null;
	    File outFile = new File(corpustPath.split("\\.")[0]+".jpwd");
	    File tagFile = new File(corpustPath.split("\\.")[0]+".jptg");
	    
	    FileWriter outWriter = null ;
	    FileWriter tagWriter = null ;
	    StringBuffer sb = new StringBuffer();
	    StringBuffer sb2 = new StringBuffer();
	    int count = 0;
	    String tmp , tag;
		try {
			 reader = new BufferedReader(new FileReader((file)));
			 outWriter = new FileWriter(outFile);
			 tagWriter = new FileWriter(tagFile);
			 
				while ((tempString = reader.readLine()) != null && tempString != "") {
					if (tempString.equals("EOS")){
						tmp = sb.toString().trim() + "\n";
						tag = sb2.toString().trim() + "\n";
						
						outWriter.write(tmp);
						tagWriter.write(tag);
						
						sb.setLength(0);
						sb2.setLength(0);
						count++;
            			continue;
            		}
            		else{
            			sb.append(tempString.split("\t")[0]+" ");
            			sb2.append(tempString.split("\t")[1].split(",")[0]+" ");
            		}
				}
				
				System.out.println(count);
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
//		String path = "jp.txt";
//		String path = "newsCorpus/kj_koma.jp";
		String path = "newsCorpus/newHannanum/rawNews.mecab";
		mecabClean test = new mecabClean(path);
		test.mecab2SegJap();
	}

}
