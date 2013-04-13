package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class lastClean {
	private String newKr, newZh, krTar, zhTar, tagFileName ,tarTagFileName;
	lastClean(String kr, String tagFileName, String zh){
		this.krTar = kr;
		this.zhTar = zh;
		this.tagFileName = tagFileName;
//		this.tarTagFileName = "final.tg";
//		this.newKr = "final.ko";
//		this.newZh = "final.jp";
		this.tarTagFileName = "final_koma.tg";
		this.newKr = "final_koma.ko";
		this.newZh = "final_koma.jp";
		System.out.println(newKr);
		System.out.println(newZh);
	}
	public void process(){
	    File krTarFile = new File(newKr);
	    File tarTagFile = new File(tarTagFileName);
	    File zhTarFile = new File(newZh);
	    File krFile = new File(krTar);
	    File tagFile = new File(tagFileName);
	    File zhFile = new File(zhTar);
	    
	    BufferedReader krReader = null;
	    BufferedReader zhReader = null;
	    BufferedReader tagReader = null;
	    FileWriter krWriter = null ;
	    FileWriter zhWriter = null ;
	    FileWriter tagWriter = null ;
	    String krRead = "", zhRead = "", tagRead;
	    
	    try {
	    	krReader = new BufferedReader(new FileReader(krFile));
	    	zhReader = new BufferedReader(new FileReader(zhFile));
	    	tagReader = new BufferedReader(new FileReader(tagFile));
	    	krWriter = new FileWriter(krTarFile);
	    	zhWriter = new FileWriter(zhTarFile);
	    	tagWriter = new FileWriter(tarTagFile);
	        
	        
	        while ((krRead = krReader.readLine()) != null) {
	        	krRead = krRead.trim();
	        	zhRead = zhReader.readLine().trim();
	        	tagRead = tagReader.readLine();

//	        	System.out.println("[" + krRead + "]");
	        	
	        	if(!krRead.contains("=") && krRead.length() > 1 && zhRead.length() > 1){
		        	krWriter.write(krRead + "\n");
		        	zhWriter.write(zhRead + "\n");
		        	tagWriter.write(tagRead  + "\n");
	        	}
	        	
	        }
//	        System.out.println("Wrote ["+ lineNumber +"] Line Sentences Frome ["+allLines + "] Lines");
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (krReader != null) {
	            try {
	            	krReader.close();
	            	zhReader.close();
	            	tagReader.close();
	            	krWriter.close();
	            	tagWriter.close();
	            	zhWriter.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		lastClean cc = new lastClean("kr_seg_word.txt" ,"kr_seg_tag.txt", "jp_mecab.txt");
		lastClean cc = new lastClean("koma_word.ko" ,"koma_tag.ko", "jp_mecab.txt");
		cc.process();
	}
}
