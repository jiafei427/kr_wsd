package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cleanCorpus {

	/**
	 * @param args
	 */
	private String newKor, newJap, korTar, japTar;
	cleanCorpus(String kor, String jap){
		this.korTar = kor;
		this.japTar = jap;
		this.newKor = kor.split("\\.")[0] + "_cl.ko";
		this.newJap = jap.split("\\.")[0] + "_cl.jp";
	}
	public void process(){
	    File korTarFile = new File(newKor);
	    File japTarFile = new File(newJap);
	    File korFile = new File(korTar);
	    File japFile = new File(japTar);
	    
	    BufferedReader krReader = null;
	    BufferedReader jpReader = null;
	    FileWriter krWriter = null ;
	    FileWriter jpWriter = null ;
	    String krRead = "", jpRead = "", previous = "";
	    
	    try {
	    	krReader = new BufferedReader(new FileReader(korFile));
	    	jpReader = new BufferedReader(new FileReader(japFile));
	    	krWriter = new FileWriter(korTarFile);
	    	jpWriter = new FileWriter(japTarFile);
	        
	        
	        while ((krRead = krReader.readLine()) != null) {
	        	krRead = krRead.trim();
	        	jpRead = jpReader.readLine().trim();

//	        	System.out.println("[" + krRead + "]");
	        	if(countWords(krRead) < 2 || Character.isDigit(krRead.charAt(0)))
	        		continue;
	        	if(checkTwoString(previous, krRead))
	        		continue;
	        	
	        	krRead = krClean(krRead);
	        	jpRead = jpClean(jpRead);
	        	
	        	if(countWords(krRead) > 2){
		        	krWriter.write(krRead + "\n");
		        	jpWriter.write(jpRead + "\n");
	        	}
	        	
	        	previous = krRead;
	        }
//	        System.out.println("Wrote ["+ lineNumber +"] Line Sentences Frome ["+allLines + "] Lines");
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("[" + krRead + "]");
	    } finally {
	        if (krReader != null) {
	            try {
	            	krReader.close();
	            	jpReader.close();
	            	krWriter.close();
	            	jpWriter.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
	
	public String krClean(String kr){	//Clean the KOR sentence
		if(kr.contains(". "))
			return removeBrackets(removeParen(kr.split("\\. ")[0]));
		else
			return removeBrackets(removeParen(kr));
	}
	public String jpClean(String jp){	//Clean the JAP sentence
		return removeBrackets(removeParen(jp.replaceAll("-", ""))).replaceAll(" ", ""); 
	}
	public boolean checkTwoString(String prev, String present){	//Check present sentence & Previous one is equal or not.
		if(prev.equals(present))
			return true;
		else if(prev.startsWith(present.substring(0, present.length()*3/4)))
			return true;
		else
			return false;
	}
	public String removeParenthesis(String string) {	// didn't use this func.
//		Pattern parenthesis = Pattern.compile("\\(.*\\)");
		Pattern parenthesis = Pattern.compile("\\(.+?\\)");
//		Pattern parenthesis = Pattern.compile("\\([^\\)].+?\\)");
		if (string == null || string.length() == 0) {
			return string;
		}
//		Pattern parenthesis = Pattern.compile("\\([^()].*\\)");
		Matcher m = parenthesis.matcher(string);
		String tmp = m.replaceAll("");
//		System.out.println(tmp);
//		if(tmp.contains("("))
//			return removeParenthesis(tmp);
//		else
			return tmp.trim();
	}
	public String removeParen(String str){	//Remove the Parenthesis in the sentence
		String reStr = "";
		if((str.indexOf("(")) > -1 && checkParenthesis(str)){
			int length = str.length();
			int idxTrue = 0;
			for(int i = 0; i < length ; i ++){
				if(str.charAt(i) == '('){
					idxTrue++;
				}
				if(idxTrue == 0)
					reStr += str.charAt(i);
				else if (str.charAt(i) == ')'){
					idxTrue--;
				}
			}
//			System.out.println(reStr);
			return reStr.trim();
		}else
			return str.trim();
	}
	public boolean checkParenthesis(String str){	// Check whether parenthesis in the sentence is equal or not
		int len = str.length();
		int checker = 0;
		for (int i = 0; i < len ; i++){
			if(str.charAt(i) == '(')
				checker++;
			else if(str.charAt(i) == ')')
				checker--;
		}
		if(checker == 0)
			return true;
		else
			return false;
	}
	public String removeBrackets(String string) {	//remove all the things in the Brackets
		Pattern brackets = Pattern.compile("\\[.+?\\]");
		if (string == null || string.length() == 0) {
			return string;
		}
		Matcher m = brackets.matcher(string);
		return m.replaceAll("").trim();
	}
	public int countWords(String sent){
		sent = sent.replaceAll("  ", " ");
		return sent.length() - sent.replaceAll(" ", "").length() + 1;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		cleanCorpus cc = new cleanCorpus("raw.ko" , "raw.jp");
		cc.process();
//		String shit = "はり-だし(張(り)出(し))舞台(ぶたい).((3행 반으로 쓴 데서)) 이혼장.";
//		System.out.println(cc.removeParen(shit));
//		System.out.println(shit.split("\\. ")[0]);
	}
}
