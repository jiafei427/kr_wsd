package jap;

import java.util.ArrayList;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class ws4jSim {

	/**
	 * This Program Uses ws4j to Grouping A list of Words 
	 * that already grouped by the Mecab that uses their Hatsuon.
	 */
	
	private static ILexicalDatabase db = new NictWordNet();
	private ArrayList <ArrayList <String>> wordList;
	
	public ws4jSim( ArrayList <ArrayList <String>> wordList){
		this.wordList = wordList;
	}
	
	public ArrayList <ArrayList <String>> process(){
		ArrayList <ArrayList <String>> result = new ArrayList <ArrayList <String>>();
		ArrayList <String> grp1, grp2;
		ArrayList <Integer> skipList = new ArrayList <Integer>(); 
		
		for(int i = 0; i < wordList.size(); i ++){
			grp1 = wordList.get(i);
			
			if(!skipList.contains(i)){
				for(int j = i +1; j < wordList.size(); j ++){
					if(skipList.contains(j))
						continue;
					grp2 = wordList.get(j);
					if(compareGrp(grp1, grp2)){
						grp1.addAll(grp2);
						skipList.add(j);
					}
				}
				result.add(grp1);
			}
		}
		return result;
	}
	
	private Boolean compareGrp(ArrayList <String> grp1, ArrayList <String> grp2){
		String word1, word2;
		for(int i = 0; i < grp1.size(); i ++){
			word1 = grp1.get(i);
			for(int j = 0; j < grp2.size(); j++){
				word2 = grp2.get(j);
//				System.out.println(word1 + "----" + word2);
//				if(calSimByLeacockChodorow(word1, word2) > 0)
//					return true;
				if(calSimByWuPalmer(word1, word2) > 0.6) //maybe change it to 0.67
					return true;
			}
		}
		return false;
	}
	
	private static double calSimByLeacockChodorow(String word1, String word2){
		double score;
		WS4JConfiguration.getInstance().setMFS(true);
		RelatednessCalculator rc = new Lin(db);
		score = rc.calcRelatednessOfWords(word1,word2);
		return score;
	}
	
	private static double calSimByWuPalmer(String word1, String word2){
		double score;
		WS4JConfiguration.getInstance().setMFS(true);
		RelatednessCalculator rc = new WuPalmer(db);
		score = rc.calcRelatednessOfWords(word1,word2);
		return score;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test = "倍 船 腹 舟 お腹 おなか 小舟 梨汗 ばむ なし";
		
		mecabSim ms = new mecabSim();
		ArrayList <ArrayList <String>> result = ms.similarCheck(test);
		
		//ws4j여기에 LOGIC을 추가해서 리스트에 붙어 있는 단어랑 새로운 리스트중의 헤드워드랑 SIMILARITY비교해서 원래거 보다 높으면 그쪽으로 붙게 하는것도 좋은것 같은데, 동일한 의미중의 JAP-Trans에서 이상한 단어가 나오지 않기를 바라며 이만 하겠음..
		ws4jSim ws = new ws4jSim(result);
		result = ws.process();
		
		ms.showResult(result);
		System.out.println("**********************************************************");
		System.out.println(ms.returnStr(result));
		
	}
}
