package jap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class mecabSim {

	private int leftMeanCount;
	
	class japWord{
		String word;
		String hatsuon;
	}
	/**
	 * This Program Uses Mecab to mark the Japanese word list that splitted by a space
	 */
	public ArrayList <ArrayList <String>> similarCheck(String transWords){
//		if(!writeTransWordsFile(transWords))
//				System.out.println("Error While Writing TransMecab File;");
		
		ArrayList <ArrayList <String>> result = new ArrayList <ArrayList <String>>();
		ArrayList <String> eachWord;
		ArrayList <japWord> wordList = mecabProcess(transWords);
		ArrayList <Integer> skipList = new ArrayList <Integer>();
		
		japWord word1, word2 , gotWord;
		ArrayList <japWord> newGrp;
		
		
		for(int i = 0; i < wordList.size() ; i++){
			word1 = wordList.get(i);
			if(!skipList.contains(i)){
				
				eachWord = new ArrayList <String>();
				eachWord.add(word1.word);
				newGrp = new ArrayList <japWord>();
				newGrp.add(word1);
				
				for(int z = 0; z < newGrp.size(); z++){
					gotWord = newGrp.get(z);
//					System.out.println("[" +gotWord.word + "]");
					
					for(int j = i +1; j < wordList.size() ; j ++){
						if(skipList.contains(j))
							continue;
						word2 = wordList.get(j);
//						System.out.print(gotWord.word + "--" + word2.word);
						if(hatsuonCheck(gotWord , word2)){
//							System.out.println("-- same");
							eachWord.add(word2.word);
							newGrp.add(word2);
							skipList.add(j);
						}
//						else
//							System.out.println();
					}
				}
				result.add(eachWord);
			}
		}
		
//		showResult(result);
		
		return result;
	}
	public void showResult(ArrayList <ArrayList <String>> result){
		System.out.println("-------------------------------------------------");
		ArrayList <String> tmp;
		for(int i = 0; i < result.size(); i++){
			tmp = result.get(i);
			for(int j = 0; j < tmp.size(); j++){
				System.out.print(tmp.get(j) + "--->");
			}
			System.out.println();
		}
	}
	
	public String returnStr(ArrayList <ArrayList <String>> result){
		ArrayList <String> tmp;
		String str = "";
		for(int i = 0; i < result.size(); i++){
			tmp = result.get(i);
			for(int j = 0; j < tmp.size(); j++){
				if(j < tmp.size() -1)
					str += tmp.get(j) + "--->";
				else
					str += tmp.get(j);
			}
			str += "\n";
		}
		return str.trim();
	}
	
	public String returnStr(ArrayList <ArrayList <String>> result, int limit){
		ArrayList <String> tmp;
		String str = "";
		int size =  result.size();
		System.out.println(limit);
		if(size < 2)
			return null;
		
		for(int i = 0; i < size && i < limit; i++){
			tmp = result.get(i);
			for(int j = 0; j < tmp.size(); j++){
				if(j < tmp.size() -1)
					str += tmp.get(j) + "--->";
				else
					str += tmp.get(j);
			}
			str += "\n";
		}
		return str.trim();
	}
	
	private Boolean hatsuonCheck(japWord word1, japWord word2){
		if(allHanjaCheck(word1.word) && allHanjaCheck(word2.word) && word1.word.length() == word2.word.length())
			return false;
//		else if(word1.hatsuon.equals(word2.hatsuon) || word1.hatsuon.contains(word2.hatsuon) || word2.hatsuon.contains(word1.hatsuon))
//			return true;
		
		if(word1.hatsuon.equals(word2.hatsuon) || checkTwoWord(word1.word, word2.word))
			return true;
		else 
			return false;
	}
	
	private Boolean checkTwoWord(String word1, String word2){
//		System.out.println(word1 + "---" + word2);
//		if(word1.length() > 1)
//			System.out.println(word1.substring(1));
//		if(word2.length() > 1)
//			System.out.println(word2.substring(1));
		Boolean result = false;
		if(word1.length() < 2 && word2.length() < 2)
			return false;
		
		if(word1.substring(0 , word1.length() -1).equals(word2) || word1.equals(word2.substring(0, word2.length() -1)))
			result = true;
		else if((word1.length() > 1 && word1.substring(1).equals(word2)) || (word2.length()>1 && word1.equals(word2.substring(1)) ))
			result = true;
		else 
			result = false;
		
		return result;
	}
	
	private Boolean allHanjaCheck(String word){
		Boolean result = true;
		char [] wordArray = word.toCharArray();
		
		for(char words : wordArray)
			if(!Character.UnicodeBlock.of(words).toString().equals("CJK_UNIFIED_IDEOGRAPHS")){
				result = false; break;
			}
//		System.out.println(word + "====" + result);
		return result;
	}

	
//	2013.03.05
//	Wrote my own template file that mecab will give result that I want.
//	Located on C:\Program Files (x86)\MeCab\etc named mecabrc
//	In the Previous Version I made a file that all the words written in that file and Let the mecab to do the Hatsuon Tagging Process.
//	But when I tag the word like "たいしょ", mecab split it to "たい" & "しょ" so there were split problem.
//	And I just changed my mind to write every word to the file once, and do the mecab once.
//	Eventhough it may take a bit longer time than previous approach, It may be more accurate. 
	private ArrayList <japWord> mecabProcess(String transWords) {
		ArrayList<japWord> wordList = new ArrayList<japWord>();
		String japWord, japHatsuon;
		String tempString = null;
		japWord tmpJap;
		javaTerminalResult jtr;
		LineNumberReader reader = null;
		String [] tmpStr = transWords.split(" ");
		
		System.out.println("[" + transWords + "]");
		
		try {
			for(int i = 0; i < tmpStr.length; i ++){
				writeTransWordsFile(tmpStr[i]);
				jtr = new javaTerminalResult("mecab -O CK transWords.mecab");
				reader = jtr.getTerminalOutput();
				tempString = reader.readLine();
//				System.out.println("[" + tempString + "]");
				
				japWord    = tmpStr[i];
				
				// If there's Mecab error, then tag the Hatsuon of the Japword to "*"
				if( tempString != null)
					japHatsuon = tempString.split("\t")[1];
				else
					japHatsuon = "*";
				
				tmpJap = new japWord();
				tmpJap.word = japWord;
				tmpJap.hatsuon = japHatsuon;
				
				wordList.add(tmpJap);
				
//				System.out.println(japWord + "===" + japHatsuon);
				
			}
		
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}if(reader != null)
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return wordList;
	}
	
	private Boolean writeTransWordsFile(String words){
		File japWordFile = new File("transWords.mecab");
		FileWriter fw = null;
		try {
			fw = new FileWriter(japWordFile);
			fw.write(words);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			if(fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
		}
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String test = "醤油 しょうゆ しょう油 倍 ばい 売り場 売場 祈り お祈り";
		
//		String test = "家計 家系 倍 船 腹 舟 小舟 ボート ばい 梨 醤油 しょうゆ しょう油 売り場 売場 祈り お祈り お腹 おなか 行っ";
		
//		String test = "家庭 仮定";
		
//		String test = "倍 船 腹 舟 お腹 おなか 小舟 梨汗 ばむ なし";
		
		String test = "国家 国 諸国 韓国 国々 代表 ショートトラック 格付け 各国";
		
		mecabSim ms = new mecabSim();
		ArrayList <ArrayList <String>> result = ms.similarCheck(test);
		
		//ws4j여기에 LOGIC을 추가해서 리스트에 붙어 있는 단어랑 새로운 리스트중의 헤드워드랑 SIMILARITY비교해서 원래거 보다 높으면 그쪽으로 붙게 하는것도 좋은것 같은데, 동일한 의미중의 JAP-Trans에서 이상한 단어가 나오지 않기를 바라며 이만 하겠음..
//		ws4jSim ws = new ws4jSim(result);
//		result = ws.process();
		
		ms.showResult(result);
//		System.out.println("**********************************************************");
//		System.out.println(ms.returnStr(result));
		
	}

}
