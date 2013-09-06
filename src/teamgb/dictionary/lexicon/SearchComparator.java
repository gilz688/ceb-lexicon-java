package teamgb.dictionary.lexicon;

import java.util.Comparator;


public class SearchComparator implements Comparator<RatedSense> {
	
	@Override
	public int compare(RatedSense senseA, RatedSense senseB) {
		return senseA.getScore() - senseB.getScore();
	}

}
