package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class makeCrossValidationResult {

	/**
	 * @param args
	 */
	private String dir;
	private String dirFileName;
	
	makeCrossValidationResult(){
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		this.dir = path.substring(0 , path.lastIndexOf("/") + 1);
		this.dirFileName = dir + "dirFile.txt";
		System.out.println(dir);
	}
	
	makeCrossValidationResult(String dir){
		this.dir = dir + "CV/";
		this.dirFileName = this.dir + "dirFile.txt";
	}
	
	public void getResult(){
		BufferedReader reader = null;
		File file=new File(dir);
		File[] allFiles = file.listFiles();
		int size = allFiles.length;
		String fileName;
		String tarExtension = "result";
		int fileCounter = 0 ;
		float score_all = 0, score_right = 0;
		String read;
		String[] readSplit;
		HashMap <String,String> numbWordMap = new HashMap <String, String>();
		HashMap <String,String> wordNumbMap = new HashMap <String, String>();
		String targetWord;
		float correct_each[] , all_each[]; 
		String wordNumb;
		HashMap <String, Float> result = new HashMap<String, Float>();
		String word;
		float accuracy;
		int mapSize;

		try {
			reader = new BufferedReader(new FileReader(dirFileName));
			while ((read = reader.readLine()) != null) {
				readSplit = read.split("\t");
				numbWordMap.put(readSplit[0], readSplit[1]);
				wordNumbMap.put(readSplit[1], readSplit[0]);
			}
			if(reader != null)
				reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mapSize = numbWordMap.size() + 1;
		correct_each = new float[mapSize];
		all_each    = new float[mapSize];
		for(int z = 1; z < mapSize; z++){
			correct_each[z] = 0;
			all_each[z] = 0;
		}
		
		
		
		try {
			for(int i = 0; i < size; i++){
				if(allFiles[i].isFile() && (fileName = allFiles[i].getName()).endsWith(tarExtension)){
					fileCounter++;
					
					reader = new BufferedReader(new FileReader(allFiles[i]));
					wordNumb = fileName.split("_")[0];
					targetWord = numbWordMap.get(wordNumb);
					
					while ((read = reader.readLine()) != null) {
						readSplit = read.split("\t");
						
						if(readSplit[0].equals(targetWord)){
							score_all++;
							all_each[Integer.parseInt(wordNumb)]++;
							if(readSplit[readSplit.length - 1].equals(readSplit[readSplit.length - 2])){
								score_right++;
								correct_each[Integer.parseInt(wordNumb)]++;
							}
						}
					}
				}
			}
			if(reader!=null)
				reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 1 ; i < mapSize ; i++){
			word = numbWordMap.get(i + "");
			if(all_each[i] != 0)
				accuracy = (correct_each[i]/all_each[i])*100;
			else
				accuracy = 0;
			result.put( word , accuracy );
//			System.out.println(i + "\t" + word + "\t" + accuracy +"%" );
		}
		
		ArrayList<Map.Entry<String,Float>> infoIds = new ArrayList<Map.Entry<String,Float>>(result.entrySet());  
		
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Float>>() {  
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());  
			}  
		});
		
		for (Entry<String,Float> e : infoIds) {
			System.out.println(wordNumbMap.get(e.getKey()) + "\t" + e.getKey() + "\t" + e.getValue() + "%");
		}
		
		
		System.out.println("-------------------------------------------------------------");
		System.out.println("HashMap Size: " + (mapSize -1) );
		System.out.println("Result File Size:" + fileCounter);
		System.out.println("All TargetTagged Words: " + score_all);
		System.out.println("Correctly TargetTagged Words: " + score_right);
		System.out.println("Classifier's Accuracy: " + (score_right/score_all)*100 +"%");
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		makeCrossValidationResult mcvr = new makeCrossValidationResult("senseTagCorpora/");
		makeCrossValidationResult mcvr = new makeCrossValidationResult();
		mcvr.getResult();
//		System.out.println(new File("").getAbsolutePath());  
	}

}
