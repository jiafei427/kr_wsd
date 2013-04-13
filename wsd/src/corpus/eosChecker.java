package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class eosChecker {
	String corpustPath;
	eosChecker(String corpustPath){
		this.corpustPath = corpustPath;
	}
	
	
	void readCorpus(){
		File file = new File(corpustPath);
		BufferedReader reader = null;
		String tempString = null;
	    int count = 0 , eosCounter = 0;
	    String tmp;
		try {
			 reader = new BufferedReader(new FileReader((file)));
			 
				while ((tempString = reader.readLine()) != null && tempString != "") {
					if (tempString.startsWith("EOS")){
						tmp = tempString.substring(3).trim();
						eosCounter = Integer.parseInt(tmp);
						count++;
						System.out.print(count + "--" + eosCounter);
						if(count == eosCounter)
							System.out.println();
						else
							System.out.println("<---------------Problem");
            		}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	            }
	        }
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "asdf.txt";
		eosChecker test = new eosChecker(path);
		test.readCorpus();
	}
}
