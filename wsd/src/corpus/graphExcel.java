package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class graphExcel {

	/**
	 * @param args
	 */
	class wordStat{
		String krWord;
		int senseSize;
		int instanceSize;
		ArrayList <String> japTrans;
	}
	
	public String dir;
	public int testSize = 4;
	
	graphExcel(String dir){
		this.dir = dir;
	}
	
	void readResult(){
		HashMap <String , ArrayList<Float>> testMap = new HashMap <String , ArrayList<Float>> ();
		String resultFileName;
		String tmpName = dir + "/" + "wsd_window";
		String wordStatCsvFileName = dir + "/" + "finalWordStats.csv";
		String graphExcelFileName =  dir + "/" + "graphResult.csv";
		File readFile;
		BufferedReader reader = null;
		FileWriter fw = null;
		String read;
		String [] splitRead;
		
		for(int i = 1 ; i < testSize + 1 ; i++){
			resultFileName = tmpName + i + ".result";
			readFile = new File(resultFileName);
			try {
				reader = new BufferedReader(new FileReader(readFile));
				
				while ((read = reader.readLine()) != null && !read.startsWith("-")) {
					if(!read.startsWith("/")){
						splitRead = read.split("\t");
						
						if(!testMap.containsKey(splitRead[1]))
							testMap.put(splitRead[1], new ArrayList<Float>());
						
						testMap.get(splitRead[1]).add(Float.parseFloat(splitRead[2].substring(0, splitRead[2].length() -1)));
					}
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		wordStat tmpWord;
		
		try {
			reader = new BufferedReader(new FileReader(new File(wordStatCsvFileName)));
			fw = new FileWriter(new File(graphExcelFileName));
			while ((read = reader.readLine()) != null) {
				splitRead = read.split(",");
				tmpWord = new wordStat();
				tmpWord.krWord = splitRead[0];
				tmpWord.instanceSize = Integer.parseInt(splitRead[1]);
				tmpWord.senseSize = Integer.parseInt(splitRead[2]);
				tmpWord.japTrans = new ArrayList <String>();
				for(int i = 3 ; i < tmpWord.senseSize + 3; i++){
					tmpWord.japTrans.add(splitRead[i]);
				}
				
				fw.write(tmpWord.krWord + ",");
				for(int j = 0; j < testSize ; j ++){
					fw.write(testMap.get(tmpWord.krWord).get(j) + ",");
				}
				fw.write(tmpWord.instanceSize + "," + tmpWord.senseSize + "," + tmpWord.japTrans.get(0).split("=")[1] + ",");
				
				for(int z = 0; z < tmpWord.senseSize ; z++){
					fw.write(tmpWord.japTrans.get(z));
					if(z != tmpWord.senseSize -1)
						fw.write( "," );
				}
				fw.write( "\n" );
			}
			if(fw != null)
				fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		graphExcel ge = new graphExcel("wsd_koma_cv_result_analysis");
		ge.readResult();
	}

}
