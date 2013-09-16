package teamgb.dictionary.lexicon;

public class RatedSense implements Comparable<RatedSense>{
	private CebuanoLexiconSense sense;
	private int score;
	
	public RatedSense(CebuanoLexiconSense e, int i){
		score = i;
		sense = e;
	}

	@Override
	public int compareTo(RatedSense e){
		return score - e.getScore();
	}
	
	public int getScore(){
		return score;
	}
	
	public CebuanoLexiconSense getSense(){
		return sense;
	}

	@Override
	public String toString() {
		return "RatedSense [" + sense + ", " + score + "]";
	}
}
