package jap;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class SimilarityCalculationDemo {
	
	private static ILexicalDatabase db = new NictWordNet();
	private static RelatednessCalculator[] rcs = {
			new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db), 
			new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
			};
	
	private static void run( String word1, String word2 ) {
		WS4JConfiguration.getInstance().setMFS(true);
		for ( RelatednessCalculator rc : rcs ) {
			double s = rc.calcRelatednessOfWords(word1, word2);
			System.out.println( rc.getClass().getName()+"\t"+s );
		}
	}
	private static double calSimByLin(String word1, String word2){
		double score;
		WS4JConfiguration.getInstance().setMFS(true);
		RelatednessCalculator rc = new Lin(db);
		score = rc.calcRelatednessOfWords(word1,word2);
		return score;
	}
	public static void main(String[] args) {
//		long t0 = System.currentTimeMillis();
////		run( "act","moderate" );
//		run( "船","舟" );
		long t1 = System.currentTimeMillis();
//		System.out.println( "Done in "+(t1-t0)+" msec." );
		System.out.println("船--------------------舟");
		run( "船","舟" );
		System.out.println("------------------------------------------------------------");
		System.out.println("船---------------------------------ボート");
		run( "船","ボート" );
		System.out.println("------------------------------------------------------------");
		System.out.println("小舟-----------------------------舟" );
		run( "小舟","舟" );
		System.out.println("------------------------------------------------------------");

		System.out.println("船----------------------------------腹");
		run( "船","腹" );
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("英会話-------------------------------会話");
		run( "英会話","会話" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("型-------------------------------形");
		run("型","形" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("実兄-------------------------------兄");
		run( "実兄","兄" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("兄貴-------------------------------兄");
		run("兄貴","兄" );
		System.out.println("------------------------------------------------------------");


		System.out.println("最も-------------------------------一番");
		run( "最も","一番" );
		System.out.println("------------------------------------------------------------");
		System.out.println("最も-------------------------------いちばん");
		run( "最も","いちばん" );
		System.out.println("------------------------------------------------------------");
		System.out.println("最も-------------------------------最大");
		run( "最も","最大" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("試合-------------------------------競技");
		run("試合","競技" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("ボール-------------------------------球");
		run("ボール","球" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("大げさ-------------------------------誇張");
		run( "大げさ","誇張" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("オーバー-------------------------------誇張");
		run("オーバー","誇張");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("教員-------------------------------教師");
		run("教員","教師" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("先生-------------------------------教師");
		run( "先生","教師" );
		System.out.println("------------------------------------------------------------");
		
		System.out.println("国家-------------------------------国");
		run("国家","国" );
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("家庭-------------------------------家族");
		run( "家庭","家族");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("年間-------------------------------日間");
		run( "年間","日間");
		System.out.println("------------------------------------------------------------");
		
		System.out.println( "金持ち-------------------------------金持");
		run( "金持ち","金持");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("祈り-------------------------------祈祷");
		run( "祈り","祈祷");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("お腹-------------------------------舟");
		run( "お腹","舟");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("おなか-------------------------------舟");
		run( "おなか","舟");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("家計--->家系");
		run("家計" , "家系");
		System.out.println("------------------------------------------------------------");
		
		
//		System.out.println("家庭--->仮定");
//		run("家庭" , "仮定");
//		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("試合= 	景気");
		run("試合" , "景気");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("感謝--->監査");
		run("感謝" , "監査");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("ありがたい--->ありがとう");
		run("ありがたい" , "ありがとう");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("星--->城");
		run("星" , "城");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("成人--->聖人");
		run("成人" , "聖人");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("音-------------声");
		run("音" , "声");
		System.out.println("------------------------------------------------------------");
		

		System.out.println("風-------------望み");
		run("風" , "望み");
		System.out.println("------------------------------------------------------------");
		
		System.out.println("夜--------------今夜");
		run("夜" , "今夜");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("保守--------------------->補修");
		run("保守" , "補修");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("否定--------------------->否認");
		run("否定" , "否認");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("足------------------------>橋-");//0.60
		run("足" , "橋");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("演技------------------->延期");//0.67
		run("演技" , "延期");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("指名------------------------------->地名");
		run("指名" , "地名");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("所長------------------------------>少将");//0.64
		run("長" , "少将");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("力説------------------------------>逆説");//0.71
		run("力説" , "逆説");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("誘導------------------------------>柔道");
		run("誘導" , "柔道");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println("演技-------------------------->延期");
		run("演技" , "延期");
		System.out.println("------------------------------------------------------------");
		
		
		System.out.println(Character.UnicodeBlock.of('誰'));
		System.out.println(Character.UnicodeBlock.of('の'));
		System.out.println(Character.UnicodeBlock.of('「'));
		System.out.println(Character.UnicodeBlock.of('아'));
	}
}