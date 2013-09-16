package teamgb.dictionary.lexicon;

import java.util.ArrayList;
import java.util.List;

public class CebuanoLexiconEntry implements Comparable<CebuanoLexiconEntry>{
	private int id;
	private String lemma;
	private List<CebuanoLexiconSense> senses;

	public CebuanoLexiconEntry(){
		id = 0;
		lemma = null;
		senses = new ArrayList<CebuanoLexiconSense>();
	}
	
	public CebuanoLexiconEntry(int id, String lemma) {
		super();
		this.id = id;
		this.lemma = lemma;
		this.senses = new ArrayList<CebuanoLexiconSense>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public List<CebuanoLexiconSense> getSenses() {
		return senses;
	}
	
	public boolean addSense(CebuanoLexiconSense sense){
		return senses.add(sense);
	}

	public void setSenses(List<CebuanoLexiconSense> list) {
		this.senses = list;
	}

	@Override
	public String toString() {
		return lemma;
	}

	public void setSenses(Object[] array) {
		senses = new ArrayList<CebuanoLexiconSense>();
		for(Object o : array)
			addSense((CebuanoLexiconSense) o);
	}
	
	public List<String> getSublemmas(){
		List<String> sls = new ArrayList<String>();
		for(CebuanoLexiconSense s : this.getSenses()){
			sls.addAll(s.getSublemmas());
		}
		return sls;
	}

	@Override
	public int compareTo(CebuanoLexiconEntry o) {
		return getLemma().compareTo(o.getLemma());
	}
}
