package wsd;

public class krWSD {

	/**
	 * @param args
	 */
	private String dir = "trans/";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputFileName = "sent.ko";
		krSeg kiss = new krSeg(inputFileName);
		kiss.krMorphAnalysis();  
	}
}
