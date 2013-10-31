package teamgb.dictionary.lexicon;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ENTRY")
public class CebuanoLexiconEntry implements Comparable<CebuanoLexiconEntry>{
	@Element(name="ID")
	private int id;
	
	@Element(name="LEMMA")
	private String lemma;
	
	@ElementList(name="SENSES")
	private ArrayList<CebuanoLexiconSense> senses; 
	
	public CebuanoLexiconEntry(@Element(name="ID") int id, @Element(name="LEMMA") String lemma) {
		this.id = id;
		this.lemma = lemma;
	}
	
	public CebuanoLexiconEntry(){
		
	}

	public List<CebuanoLexiconSense> getSenses() {
		return senses;
	}

	public void setSenses(ArrayList<CebuanoLexiconSense> senses) {
		this.senses = senses;
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
	
	@Override
	public String toString() {
		return lemma;
	}

	@Override
	public int compareTo(CebuanoLexiconEntry o) {
		return getLemma().compareTo(o.getLemma());
	}
}
