package corpus;

import jap.mecabSim;
import jap.ws4jSim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class extractWordCorpus {

	class transPair{
		int idx;
		int tarIdx;
	}
	
	class wdtg{
		String word;
		String tag;
	}
	
	private String krFileName;
	private String tagFileName;
	private String jpFileName;
	private String alignFileName;
	private String targetWord;
	public String csvFileName;
	
	private int transCountThreshold = 6;
	private int meanLimit;
	private int analysisChoice;
	public String senseDir;
	final private String anaToolChoice[] = {"HANNANUM","KOMA"};
	final private String anaToolTargetTag[] = {"nc" , "CM"};
	final private String anaToolContentTag[][] = {{"nc", "nq", "pv", "pa", "ma"},{"CMC", "CMP", "YB", "SB"}};
	final private int windowSize = 4;
	public int eachAllSentSize;
	public int eachSenseSize;
	
	
	extractWordCorpus(String krFileName,String tagFileName,String jpFileName, String alignFileName, String anaChoice){
		this.krFileName = krFileName;
		this.tagFileName = tagFileName;
		this.jpFileName = jpFileName;
		this.alignFileName = alignFileName;
		
		if(anaChoice.equals(anaToolChoice[0]))
			analysisChoice = 0;
		else if (anaChoice.equals(anaToolChoice[1]))
			analysisChoice = 1;
		else{
			System.err.print("AnalysisChoice was not applicable!");
			System.exit(0);
		}
		System.out.println("AnaChoice ==" + analysisChoice);
		
	}
	
	void setTargetWord(String targetWord){
		this.targetWord = targetWord;
	}
	
	void setMeanLimit(int numb){
		meanLimit = numb;
	}
	

	private ArrayList <transPair> getTransPair(String sent , String[] align){
		ArrayList <transPair> result = null;
		int idx;
		if(sent.contains(" " + targetWord + " ")){
			result = new ArrayList <transPair> ();
			String[] sentArray = sent.split(" ");
			for(int i = 0; i < sentArray.length ; i++){
				if(sentArray[i].equals(targetWord) && (idx = alignJapIdx(align,i)) > -1){
					transPair tmp = new transPair();
					tmp.idx = i;
					tmp.tarIdx = idx;
					result.add(tmp);
				}
			}
		}
		return result;
	}
	
	
	private int targetIdx (String sent){
		int idx = -1;
		if(sent.contains(" " + targetWord + " ")){
			String[] sentArray = sent.split(" ");
			for(int i = 0; i < sentArray.length ; i++){
				if(sentArray[i].equals(targetWord)){
					idx = i;
					break;
				}
			}
			return idx;
		}
		return idx;
	}
	
	
	private int alignJapIdx(String[] align, int tarIdx){
		int idx = -1;
		for(int i = 0; i < align.length; i++){
			if(align[i].contains(tarIdx+"-")){
				idx = Integer.parseInt(align[i].split("-")[1]);
				break;
			}
		}
		return idx;
	}
	
	private wdtg[][] extractContentWord(String[] sent, String[] tag , int idx){
		wdtg content[][] = new wdtg [2][windowSize];
		String tagTmp;
		int size = anaToolContentTag[analysisChoice].length;
		int contIdx = 0;
		
		for(int i = idx - 1; i > -1 && -1 < contIdx && contIdx < windowSize; i--){
			
			tagTmp = tag[i];
			for(int j = 0; j < size; j++){
				if(tagTmp.startsWith(anaToolContentTag[analysisChoice][j])){
					content[0][contIdx] = new wdtg();
					content[0][contIdx].tag = tagTmp;
					content[0][contIdx++].word = sent[i];
					break;
				}
			}
		}
		
		contIdx = 0;
		for(int i = idx + 1; i < sent.length && -1 < contIdx && contIdx < windowSize; i++){
			tagTmp = tag[i];
			for(int j = 0; j < size; j++){
				if(tagTmp.startsWith(anaToolContentTag[analysisChoice][j])){
					content[1][contIdx] = new wdtg();
					content[1][contIdx].tag = tagTmp;
					content[1][contIdx++].word = sent[i];
					break;
				}
			}
		}
		
		return content; 
	}
	
	private void showWDTG(wdtg[][] content){
		System.out.println("Left Content Words:");
		for(int i = windowSize -1 ; i > -1; i--){
			if(content[0][i] != null)
				System.out.print(content[0][i].word + "-");
		}
		System.out.println();
		
		System.out.println("Right Content Words:");
		for(int i = 0; i < windowSize; i++){
			if(content[1][i] != null)
				System.out.print(content[1][i].word + "-");
		}
		System.out.println();
	}
	
	private void writeTransFile(ArrayList<ArrayList<Integer>> transLineNumbs, List<ArrayList<String>> wsdTransList){
	    FileWriter targetWordWriter = null ;
	    FileWriter targetTransWordFile = null;
	    BufferedReader krFileReader = null;
	    BufferedReader tagFileReader = null;
	    String krRead, krTag;
	    String [] kr, tag;
	    int line;
	    int size, tmpSize;
	    ArrayList<String> tmp;
	    int whichChoice = 0;
	    wdtg [][] targetContent;
	    
	    size = wsdTransList.size();
	   
	    
	    try {
	    	targetTransWordFile = new FileWriter(new File( senseDir + targetWord+".trans"));
	    	
			for(int i = 0; i < size; i++){
				tmp = wsdTransList.get(i);
				tmpSize = tmp.size();
				
				targetTransWordFile.write((i+1) + "\t");
						
				for(int j = 0; j < tmpSize; j++){
					if( j != tmpSize -1)
						targetTransWordFile.write(tmp.get(j) + "/");
				}
				targetTransWordFile.write("\n");
			}
			
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (targetWordWriter != null || targetTransWordFile != null) {
	            try {
	            	targetTransWordFile.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	    		
		line = 0;
		try {
			krFileReader = new BufferedReader(new FileReader(new File(krFileName)));
			tagFileReader = new BufferedReader(new FileReader(new File(tagFileName)));
			targetWordWriter = new FileWriter(new File(senseDir + targetWord + ".train"));
			
			while ((krRead = krFileReader.readLine()) != null) {
				line++;
				krTag = tagFileReader.readLine();
				
				//write to file.
				kr = krRead.split(" ");
				tag = krTag.split(" ");

				if(kr.length != tag.length)
					continue;
				
				for(int i = 0; i < size; i++){
					if(transLineNumbs.get(i).contains(line)){
						whichChoice = i + 1;
					
						for(int z = 0; z < kr.length ; z++){
							targetWordWriter.write(kr[z] + "\t" + tag[z]  + "\t");
							
							if(targetWord.equals(kr[z])){
//										System.out.println("Line Number :" +line);
								targetContent = extractContentWord(kr, tag, z);
								for(int m = 0; m < 2; m++){
									for(int n = 0; n < windowSize; n++){
										if(targetContent[m][n] != null)
											targetWordWriter.write(targetContent[m][n].word + "\t" + targetContent[m][n].tag  + "\t");
										else
											targetWordWriter.write("-" + "\t" + "-"  + "\t");
									}
								}
								targetWordWriter.write(whichChoice + "\n");
							}
							else{
								for(int m = 0; m < 2; m++){
									for(int n = 0; n < windowSize; n++){
										targetWordWriter.write("-" + "\t" + "-"  + "\t");
									}
								}
								
								targetWordWriter.write("0" + "\n");
							}
						}
						targetWordWriter.write("\n");
					}
					else
						continue;
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
    		if (krFileReader != null || tagFileReader != null || targetWordWriter != null) {
	            try {
	            	krFileReader.close();
	            	tagFileReader.close();
	            	targetWordWriter.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
	
	
	public String process(){
		
	    File krFile = new File(krFileName);
	    File jpFile = new File(jpFileName);
	    File alignFile = new File(alignFileName);
	    File tagFile = new File(tagFileName);
	    
	    
	    BufferedReader krFileReader = null;
	    BufferedReader jpFileReader = null;
	    BufferedReader alignFileReader = null;
	    BufferedReader tagFileReader = null;
	    
	    HashMap <String, ArrayList<Integer>> lineNumb = new  HashMap <String, ArrayList<Integer>>();
	    HashMap<String, Integer> map = new HashMap<String, Integer>();
	    String krRead = "" , jpRead = "" , tagRead = "" , alignRead;
	    String japTrans = "";
	    String[] align , tags , jpSplit;
	    int line = 0;
	    int counter = 0, howmany = 0 ;
	    int japTransCount;
	    String id , japWord;
	    String groupedWords = "";
	    String buf = "";
	    List<ArrayList<String>> wsdTransList;
	    ArrayList<ArrayList<Integer>> transLineNumbs;
	    ArrayList <transPair> transPairs;
	    transPair tmpPair;
	    int eachSentCounter = 0;
	    String eachSentBuff = "";
	    ArrayList<Integer> tmpArrList;
	    
	    try {
	    	krFileReader = new BufferedReader(new FileReader(krFile));
	    	jpFileReader = new BufferedReader(new FileReader(jpFile));
	    	alignFileReader = new BufferedReader(new FileReader(alignFile));
	    	tagFileReader = new BufferedReader(new FileReader(tagFile));
	    	
	        
	        while ((krRead = krFileReader.readLine()) != null) {
	        	line++;
	        	jpRead = jpFileReader.readLine();
	        	alignRead = alignFileReader.readLine();
	        	tagRead = tagFileReader.readLine();
	        	tags = tagRead.split(" ");
	        	align = alignRead.split(" ");
	        	jpSplit = jpRead.split(" ");
	        	
	        	
	        	if((transPairs = getTransPair(krRead , align)) != null){
	        		
	        		for(int i = 0; i < transPairs.size(); i++){
	        			tmpPair = transPairs.get(i);
	        			
		        		try {
	        				if(!tags[tmpPair.idx].startsWith(anaToolTargetTag[analysisChoice]))
	        					continue;
		        				
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
		        		
		        		japTrans = jpSplit[tmpPair.tarIdx];
		        		
		            	if(map.containsKey(japTrans)){
		            		map.put(japTrans, (map.get(japTrans)+1));
		            		
		            		tmpArrList = lineNumb.get(japTrans);
		            		if(!tmpArrList.contains(line)){
		            			tmpArrList.add(line);
		            		}
		            	}else{
		            		map.put(japTrans, 1);
		            		lineNumb.put(japTrans, new ArrayList<Integer>());
		            		lineNumb.get(japTrans).add(line);
		            		}
	        		}
	        	}else
	        		continue;
	        }
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (krFileReader != null) {
	            try {
	            	krFileReader.close();
	            	jpFileReader.close();
	            	alignFileReader.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	    
	    List<Map.Entry<String, Integer>> infoIds =
			    new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
		        return (o2.getValue() - o1.getValue()); 
		    }
		}); 
		
		eachAllSentSize = 0;
		
		
		for (int i = 0; i < infoIds.size(); i++) {
			howmany++;
		    id = infoIds.get(i).toString();
		    japTransCount = Integer.parseInt(id.split("=")[1]);
		    
		    if(howmany > 10 || japTransCount < transCountThreshold){
		    	howmany--; break;
		    }
		    
		    japWord = id.split("=")[0];
		    
		    if(japWord.length() == 1 && !Character.UnicodeBlock.of(japWord.charAt(0)).toString().equals("CJK_UNIFIED_IDEOGRAPHS"))
		    	howmany--;
		    else{
		    	counter += japTransCount;
			    groupedWords += japWord + " ";
			    buf += id + "\t";
			    
			    eachSentCounter = lineNumb.get(japWord).size();
			    eachSentBuff += japWord + "=" + eachSentCounter + "\t";
			    eachAllSentSize += eachSentCounter;
		    }
		}
		System.out.println(howmany);
		
		if(counter < 20 || howmany < 2 ){
			wsdTransList = null;
			buf = null;
			
		}
		else{
			wsdTransList = mecabWs4j4ResList(groupedWords.trim());
			if(wsdTransList != null){
				wsdTransList = sortTransList(wsdTransList , map);
				
				if(csvFileName != null)
					writeCvs(targetWord , wsdTransList, map);
				
				buf = buf.trim() + "\n" + "All:" + eachAllSentSize + "\t" + eachSentBuff.trim() + "\n" + showListStr(wsdTransList);
				eachSenseSize = wsdTransList.size();
			}
			else
				buf = null;
		}
		
		if(wsdTransList != null){
			
			transLineNumbs = getTransLineNumbs(wsdTransList , lineNumb);
			
			writeTransFile(transLineNumbs, wsdTransList);
		}
		
		return buf;
	}
	
	public void writeCvs(String target, List<ArrayList<String>> wsdTransList , HashMap<String, Integer> map){
		File cvsFile = new File(csvFileName);
		FileWriter fw = null;
		int size = wsdTransList.size();
		int tmpSize;
		ArrayList<String> tmp;
		String buf = "" , word;
		int oneSize = 0 , allSize = 0;
		
		try {
			fw = new FileWriter(cvsFile , true);
			fw.write(target + ",");
			
			for(int i = 0; i < size; i++){
				tmp = wsdTransList.get(i);
				tmpSize = tmp.size();
				for(int j = 0; j < tmpSize; j++){
					word = tmp.get(j);
					oneSize = map.get(word);
					allSize += oneSize;
					buf += word + "," + oneSize + ","; 
				}
			}
			buf = buf.substring(0, buf.lastIndexOf(",")) + "\n";
			fw.write(allSize + "," + buf);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	private List<ArrayList<String>> sortTransList(List<ArrayList<String>> wsdTransList, HashMap<String, Integer> map ){
		int transSize[] = getTransSize(wsdTransList , map);
		int tmp;
		int size = wsdTransList.size();
		
		for(int i = 0 ; i < size; i ++){
			for(int j = i +1; j < size ; j++){
				if(transSize[j-1] < transSize[j]){
					tmp = transSize[j-1];
					transSize[j-1] = transSize[j];
					transSize[j] = tmp;
					Collections.swap(wsdTransList,j-1,j);
				}
			}
		}
		
		return wsdTransList;
	}
	
	private int[] getTransSize(List<ArrayList<String>> wsdTransList , HashMap<String, Integer> map){
		int size = wsdTransList.size();
		int tmpSize;
		ArrayList<String> tmp;
		int[] result = new int[size];
		
		for(int i = 0; i < size; i++){
			tmp = wsdTransList.get(i);
			tmpSize = tmp.size();
			result[i] = 0;
			for(int j = 0; j < tmpSize; j++){
//				System.out.print(" [" + tmp.get(j) + "- " + map.get(tmp.get(j)) + "] "); 
				result[i] += map.get(tmp.get(j));
			}
//			System.out.println();
		}
		return result;
	}
	
	private ArrayList<ArrayList<Integer>> getTransLineNumbs(List<ArrayList<String>> wsdTransList , HashMap <String, ArrayList<Integer>> linNumb){
		int size = wsdTransList.size();
		int tmpSize;
		ArrayList<String> tmp;
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> eachMeanTrans = null;
		
		Comparator<Integer> comparator = new Comparator<Integer>(){
			@Override
			public int compare(Integer s1, Integer s2) {
				// TODO Auto-generated method stub
				return s1 - s2;
			}
		};
		
		for(int i = 0; i < size; i++){
			tmp = wsdTransList.get(i);
			tmpSize = tmp.size();
			eachMeanTrans = new ArrayList<Integer> ();
			
			for(int j = 0; j < tmpSize; j++){
				eachMeanTrans.addAll(linNumb.get(tmp.get(j)));
			}
			
			Collections.sort(eachMeanTrans,comparator);
			
			result.add(eachMeanTrans);
//			System.out.println(eachMeanTrans.size());
		}
		
			
		return result;
	}
	
	public String Statistics_old(){
		
	    File krFile = new File(krFileName);
	    File jpFile = new File(jpFileName);
	    File alignFile = new File(alignFileName);
	    File tagFile = new File(tagFileName);
	    
	    BufferedReader krFileReader = null;
	    BufferedReader jpFileReader = null;
	    BufferedReader alignFileReader = null;
	    BufferedReader tagFileReader = null;
	    
	    HashMap <String, ArrayList<Integer>> lineNumb = new  HashMap <String, ArrayList<Integer>>();
	    HashMap<String, Integer> map = new HashMap<String, Integer>();
	    String krRead = "" , jpRead = "" , tagRead = "" , alignRead;
	    String japTrans = "";
	    String[] align , tags, jpSplit;
	    int line = 0;
	    String buf = "";
	    int counter = 0, howmany = 0 ;
	    String id , japWord;
	    String groupedWords = "";
	    String result;
	    int idx_one, tarIdx_one;
	    int cc = 0 , cc2 = 0;
	    
	    try {
	    	krFileReader = new BufferedReader(new FileReader(krFile));
	    	jpFileReader = new BufferedReader(new FileReader(jpFile));
	    	alignFileReader = new BufferedReader(new FileReader(alignFile));
	    	tagFileReader = new BufferedReader(new FileReader(tagFile));
	    	
	        
	        while ((krRead = krFileReader.readLine()) != null) {
	        	line++;
	        	jpRead = jpFileReader.readLine();
	        	alignRead = alignFileReader.readLine();
	        	tagRead = tagFileReader.readLine();
	        	tags = tagRead.split(" ");
	        	align = alignRead.split(" ");
	        	jpSplit = jpRead.split(" ");
	        	
	        	
//	        	if((idx = targetIdx(krRead)) != null){
//	        		tarIdx = alignJapIdx(align,idx);
	        		
	        		if((idx_one = targetIdx(krRead)) > -1 && (tarIdx_one = alignJapIdx(align,idx_one)) > -1){
//	        		cc += idx.size();
//	        		cc2 += tarIdx.size();
	        		
//	        		for(int i = 0; i < idx.size(); i++){
//	        			idx_one = idx.get(i);
//	        			tarIdx_one = idx.get(i);
	        			
		        		try {
		        			if(analysisChoice ==1){
		        				if(!tags[idx_one].startsWith("CM"))
		        					continue;
		        			}else{
		        				if(!tags[idx_one].startsWith("nc"))
		        					continue;
		        			}
		        				
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
		        		System.out.println(tarIdx_one + "==" +  jpSplit.length );
		        		
		        		japTrans = jpSplit[tarIdx_one];
		        		
//		        		if(tarIdx_one < jpSplit.length)
//		        			japTrans = jpSplit[tarIdx_one];
//		        		else
//		        			japTrans = jpSplit[jpSplit.length-1];
		        		System.out.println(jpRead);
		        		System.out.println(japTrans);
	//	        		System.out.println("-----------------------------------------------");
	//	        		System.out.println(krRead);
		        		
		            	if(map.containsKey(japTrans)){
		            		map.put(japTrans, (map.get(japTrans)+1));
		            		lineNumb.get(japTrans).add(line);
		            	}else{
		            		map.put(japTrans, 1);
		            		lineNumb.put(japTrans, new ArrayList<Integer>());
		            		lineNumb.get(japTrans).add(line);
		            		}
//	        		}
	        	}else
	        		continue;
	        }
	        
			List<Map.Entry<String, Integer>> infoIds =
				    new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
			Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
			    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
			        return (o2.getValue() - o1.getValue()); 
			    }
			}); 
			
			
			for (int i = 0; i < infoIds.size(); i++) {
				howmany++;
			    id = infoIds.get(i).toString();
			    if(howmany > 10 || Integer.parseInt(id.split("=")[1]) < transCountThreshold){
			    	howmany--; break;
			    }
			    japWord = id.split("=")[0];
			    
			    //2013.03.11 if Jap TRANS Character is only one-word-JAP Then just skip it.
//			    buf += id + "\t";
//			    groupedWords += id.split("=")[0] + " ";
			    
			    if(japWord.length() == 1 && !Character.UnicodeBlock.of(japWord.charAt(0)).toString().equals("CJK_UNIFIED_IDEOGRAPHS")){
			    	//2013.03.11  if Jap TRANS Character is only one-word-JAP Then just skip it.
//			    	buf = buf.substring(0, buf.length() - id.length() - 1);
//			    	System.out.println(groupedWords + "-----" + groupedWords.length());
//			    	if(groupedWords.length() > 2)
//			    		groupedWords = groupedWords.substring(0 , groupedWords.length() - 3);
//			    	else
//			    		groupedWords = "";
			    	howmany--;
			    }else{
			    	counter += Integer.parseInt(id.split("=")[1]);
				    buf += id + "\t";
				    groupedWords += id.split("=")[0] + " ";
			    }
			}
			System.out.println(howmany);
			System.out.println("[" +groupedWords+ "]");
			if(counter < 20 || howmany < 2 )
				buf = null;
			else{
				result = mecabWs4j4Str(groupedWords.trim());
				if(result != null)
					buf += "\n" + result;
				else
					buf = null;
			}
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (krFileReader != null) {
	            try {
	            	krFileReader.close();
	            	jpFileReader.close();
	            	alignFileReader.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	    System.out.println(buf);
	    System.out.println("All Number is " + cc + "===" + cc2);
	    return buf;
	}
	
	public String statistic(){
		
	    File krFile = new File(krFileName);
	    File jpFile = new File(jpFileName);
	    File alignFile = new File(alignFileName);
	    File tagFile = new File(tagFileName);
	    
	    BufferedReader krFileReader = null;
	    BufferedReader jpFileReader = null;
	    BufferedReader alignFileReader = null;
	    BufferedReader tagFileReader = null;
	    
	    HashMap <String, ArrayList<Integer>> lineNumb = new  HashMap <String, ArrayList<Integer>>();
	    HashMap<String, Integer> map = new HashMap<String, Integer>();
	    String krRead = "" , jpRead = "" , tagRead = "" , alignRead;
	    String japTrans = "";
	    String[] align , tags, jpSplit;
	    int line = 0;
	    String buf = "";
	    int counter = 0, howmany = 0 ;
	    String id , japWord;
	    String groupedWords = "";
	    String result;
	    ArrayList <transPair> transPairs;
	    transPair tmpPair;
	    
	    try {
	    	krFileReader = new BufferedReader(new FileReader(krFile));
	    	jpFileReader = new BufferedReader(new FileReader(jpFile));
	    	alignFileReader = new BufferedReader(new FileReader(alignFile));
	    	tagFileReader = new BufferedReader(new FileReader(tagFile));
	    	
	        
	        while ((krRead = krFileReader.readLine()) != null) {
	        	line++;
	        	jpRead = jpFileReader.readLine();
	        	alignRead = alignFileReader.readLine();
	        	tagRead = tagFileReader.readLine();
	        	tags = tagRead.split(" ");
	        	align = alignRead.split(" ");
	        	jpSplit = jpRead.split(" ");
	        	
	        	
	        	if((transPairs = getTransPair(krRead , align)) != null){
	        		
	        		for(int i = 0; i < transPairs.size(); i++){
	        			tmpPair = transPairs.get(i);
	        			
		        		try {
		        			if(analysisChoice ==1){
		        				if(!tags[tmpPair.idx].startsWith("CM"))
		        					continue;
		        			}else{
		        				if(!tags[tmpPair.idx].startsWith("nc"))
		        					continue;
		        			}
		        				
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
		        		
		        		japTrans = jpSplit[tmpPair.tarIdx];
		        		
		            	if(map.containsKey(japTrans)){
		            		map.put(japTrans, (map.get(japTrans)+1));
		            		lineNumb.get(japTrans).add(line);
		            	}else{
		            		map.put(japTrans, 1);
		            		lineNumb.put(japTrans, new ArrayList<Integer>());
		            		lineNumb.get(japTrans).add(line);
		            		}
	        		}
	        	}else
	        		continue;
	        }
	        
			List<Map.Entry<String, Integer>> infoIds =
				    new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
			Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
			    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
			        return (o2.getValue() - o1.getValue()); 
			    }
			}); 
			
			
			for (int i = 0; i < infoIds.size(); i++) {
				howmany++;
			    id = infoIds.get(i).toString();
			    if(howmany > 10 || Integer.parseInt(id.split("=")[1]) < transCountThreshold){
			    	howmany--; break;
			    }
			    japWord = id.split("=")[0];
			    
			    //2013.03.11 if Jap TRANS Character is only one-word-JAP Then just skip it.
//			    buf += id + "\t";
//			    groupedWords += id.split("=")[0] + " ";
			    
			    if(japWord.length() == 1 && !Character.UnicodeBlock.of(japWord.charAt(0)).toString().equals("CJK_UNIFIED_IDEOGRAPHS")){
			    	//2013.03.11  if Jap TRANS Character is only one-word-JAP Then just skip it.
//			    	buf = buf.substring(0, buf.length() - id.length() - 1);
//			    	System.out.println(groupedWords + "-----" + groupedWords.length());
//			    	if(groupedWords.length() > 2)
//			    		groupedWords = groupedWords.substring(0 , groupedWords.length() - 3);
//			    	else
//			    		groupedWords = "";
			    	howmany--;
			    }else{
			    	counter += Integer.parseInt(id.split("=")[1]);
				    buf += id + "\t";
				    groupedWords += id.split("=")[0] + " ";
			    }
			}
			System.out.println(howmany);
			System.out.println("[" +groupedWords+ "]");
			if(counter < 20 || howmany < 2 )
				buf = null;
			else{
				result = mecabWs4j4Str(groupedWords.trim());
				if(result != null)
					buf += "\n" + result;
				else
					buf = null;
			}
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (krFileReader != null) {
	            try {
	            	krFileReader.close();
	            	jpFileReader.close();
	            	alignFileReader.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	    System.out.println(buf);
	    return buf;
	}
	
	
	private String mecabWs4j4Str(String str){
		
		mecabSim ms = new mecabSim();
		ArrayList <ArrayList <String>> result = ms.similarCheck(str);
		
		ws4jSim ws = new ws4jSim(result);
		result = ws.process();
		
		return ms.returnStr(result , meanLimit);
	}
	
	private  List<ArrayList<String>> mecabWs4j4ResList(String str){
		
		mecabSim ms = new mecabSim();
		ArrayList <ArrayList <String>> result = ms.similarCheck(str);
		
		ws4jSim ws = new ws4jSim(result);
		result = ws.process();
		
		return trimWithLimit(result , meanLimit);
	}
	
	public  List<ArrayList<String>> trimWithLimit(ArrayList <ArrayList <String>> result, int limit){
		int size =  result.size();
		
		if(size < 2)
			return null;
		else if(size < limit)
			return result;
		else{
			return result.subList(0, limit);
		}
		
	}
	
	public String showListStr( List<ArrayList<String>> result){
		ArrayList <String> tmp;
		String str = "";
		int size =  result.size();
		
		for(int i = 0; i < size; i++){
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
	
	ArrayList<String> showHash(HashMap<String, Integer> map){
		System.out.println("*****************************************asdafasf*************************************************************");
		ArrayList<String> transList = new ArrayList<String>();
		List<Map.Entry<String, Integer>> infoIds =
			    new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		
		//  将HASHMAP中的数据排序-------------------------------------------
		//reordering the keywords in Hashtable by its Value.
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
		    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
		        return (o2.getValue() - o1.getValue()); 
//		        return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		}); 
		int counter = 0, howmany = 300;
		for (int i = 0; i < infoIds.size(); i++) {
			howmany--;
		    String id = infoIds.get(i).toString();
//		    if(Integer.parseInt(id.split("=")[1]) > 1)
		    	System.out.println(id);
		    transList.add(id.split("=")[0]);
		    counter += Integer.parseInt(id.split("=")[1]);
		    if(howmany == 0)
		    	break;
		}
		System.out.println("number = " + counter);
		System.out.println("*******************************************aaaaaa***********************************************************");
		return transList;
	}
	
	void showLines(HashMap <String, ArrayList<Integer>>map , String word){
		System.out.println("******************************************************************************************************");
		ArrayList<Integer> ll = map.get(word);
		
		for (int i = 0; i < ll.size(); i++) {
    		System.out.println(ll.get(i));
		}
		System.out.println("number = " + ll.size());
		System.out.println("******************************************************************************************************");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String targetWord = "가계";
		extractWordCorpus ewc = new extractWordCorpus("krJpAlign/final.ko","krJpAlign/final.tg","krJpAlign/final.jp" ,"krJpAlign/aligne.krjp" , "HANNANUM");
		ewc.setTargetWord(targetWord);
//		extractWordCorpus ewc = new extractWordCorpus("testAlign/all.ko","testAlign/all.jp" ,"testAlign/aligned" , targetWord);
//		ewc.process();
	}
}
