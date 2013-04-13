package jap;

import java.util.List;
import java.util.Set;

import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.jawjaw.db.SenseDAO;
import edu.cmu.lti.jawjaw.db.SynlinkDAO;
import edu.cmu.lti.jawjaw.db.SynsetDAO;
import edu.cmu.lti.jawjaw.db.SynsetDefDAO;
import edu.cmu.lti.jawjaw.db.WordDAO;
import edu.cmu.lti.jawjaw.pobj.Lang;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Sense;
import edu.cmu.lti.jawjaw.pobj.Synlink;
import edu.cmu.lti.jawjaw.pobj.Synset;
import edu.cmu.lti.jawjaw.pobj.SynsetDef;
import edu.cmu.lti.jawjaw.pobj.Word;

public class jawTest {

	private static void demo1( String word, POS pos ) {
		// Access the Japanese WordNet DB and process the raw data
		List<Word> words = WordDAO.findWordsByLemmaAndPos(word, pos);
		List<Sense> senses = SenseDAO.findSensesByWordid( words.get(0).getWordid() );
		String synsetId = senses.get(0).getSynset();
		Synset synset = SynsetDAO.findSynsetBySynset( synsetId );
		SynsetDef synsetDef = SynsetDefDAO.findSynsetDefBySynsetAndLang(synsetId, Lang.eng);
		List<Synlink> synlinks = SynlinkDAO.findSynlinksBySynset( synsetId );
		// Showing the result
		System.out.println( words.get(0) );
		System.out.println( senses.get(0) );
		System.out.println( synset );
		System.out.println( synsetDef );
		System.out.println( synlinks.get(0) );
	}
		private static void demo2( String word, POS pos ) {
			// Accessing Japanese WordNet from the façade class called JAWJAW
			Set<String> hypernyms = JAWJAW.findHypernyms(word, pos);
			Set<String> hyponyms = JAWJAW.findHyponyms(word, pos);
			Set<String> consequents = JAWJAW.findEntailments(word, pos);
			Set<String> translations = JAWJAW.findTranslations(word, pos);
			Set<String> definitions = JAWJAW.findDefinitions(word, pos);
			// Showing results. (note: polysemies are mixed up here)
			System.out.println( "hypernyms of "+word+" : \t"+ hypernyms );
			System.out.println( "hyponyms of "+word+" : \t"+ hyponyms );
			System.out.println( word+" entails : \t\t"+ consequents );
			System.out.println( "translations of "+word+" : \t"+ translations );
			System.out.println( "definitions of "+word+" : \t"+ definitions );		
		}

	public static void main(String[] args) {
		// Showing a demo for "自然言語処理"(noun) which means NLP
//		jawTest.run( "自然言語処理", POS.n ); 
		jawTest.demo1( "船", POS.n ); 
		System.out.println("------------------------------------------------------");
		jawTest.demo2( "買収", POS.v );
	}
}
