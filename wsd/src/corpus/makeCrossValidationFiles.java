package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class makeCrossValidationFiles {

	/**
	 * @param args
	 */
	String dir, tarExtension;
	int howMany;
	
	makeCrossValidationFiles(String dir, String tarExtension, int howMany){
		this.dir = dir;
		this.tarExtension = tarExtension;
		this.howMany = howMany;
	}
	
	public void process(){
		File file=new File(dir);
		File[] allFiles = file.listFiles();
		int size = allFiles.length;
		int sentCounter;
		int fileCounter = 0;;
		String cvDir = dir + "/CV";
		
		if(!new File(cvDir).isDirectory())
			new File(cvDir).mkdir();
		
		
		String dirFileName =  cvDir + "/" + "dirFile.txt";
		BufferedReader reader = null;
		FileWriter writer_one = null;
		FileWriter writer_five = null;
		String file_oneName , file_fiveName;
		String fileName;
		FileWriter fw = null;
		
		String read, buf = "";
		ArrayList<String> arr;
		int start_one, end_one;
		int sentSize;
		
		try {
			fw = new FileWriter(new File(dirFileName));
			
			for(int i = 0; i < size; i++){
				if(allFiles[i].isFile() && (fileName = allFiles[i].getName()).endsWith(tarExtension)){
					
					fileCounter++;
					arr = new ArrayList<String>();
					start_one = end_one = 0;
					sentCounter = 0;
					reader = new BufferedReader(new FileReader(allFiles[i]));
//					System.out.println(fileName);
					fileName = fileName.substring(0 , fileName.length() - (tarExtension.length()+1));
//					System.out.println(fileName);
					fw.write(fileCounter + "\t" + fileName + "\n");
					
					while ((read = reader.readLine()) != null) {
						
						if(read.length() > 2){
							buf += read + "\n";
						}else{
							sentCounter++;
							arr.add(buf);
							buf = "";
						}
					}
					
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e1) {
						}
					}
					
					sentSize = arr.size();
					Collections.shuffle(arr);
					
					for(int j = 0; j < howMany ; j++ ){
						if(j == howMany -1)
							end_one = sentSize;
						else
							end_one += sentSize / howMany;
						
//						file_oneName  = cvDir + "/" + fileName + "_" + (j+1) + ".tess";
//						file_fiveName = cvDir + "/" + fileName + "_" + (j+1) + ".train";
						file_oneName  = cvDir + "/" + fileCounter + "_" + (j+1) + ".tess";
						file_fiveName = cvDir + "/" + fileCounter + "_" + (j+1) + ".train";
						writer_one = new FileWriter(new File(file_oneName));
						writer_five = new FileWriter(new File(file_fiveName));
						
						System.out.println("start: " + start_one + "  end: " + end_one);
						for(int z = 0 ; z < sentSize; z++){
							if( z >= start_one && z < end_one){
								writer_one.write(arr.get(z) + "\n");
							}else
								writer_five.write(arr.get(z) + "\n");
						}
						start_one = end_one;
						writer_one.flush();
						writer_five.flush();
					}
					
					if (writer_one != null || writer_five != null) {
						try {
							writer_one.close();
							writer_five.close();
						} catch (IOException e1) {
						}
					}
				}
			}
			if(fw!=null)
				fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		makeCrossValidationFiles test = new makeCrossValidationFiles("senseTagCorpora_test", "train", 5);
		makeCrossValidationFiles test = new makeCrossValidationFiles("senseTagCorpora_komaWindow1", "train", 5);
		test.process();
	}

}
