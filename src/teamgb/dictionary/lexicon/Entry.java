package teamgb.dictionary.lexicon;

import java.util.ArrayList;
import java.util.List;

public class Entry implements Comparable<Entry>{
	private int id;
	private String lemma;
	private List<Sense> senses;

	public Entry(){
		id = 0;
		lemma = null;
		senses = new ArrayList<Sense>();
	}
	
	public Entry(int id, String lemma) {
		super();
		this.id = id;
		this.lemma = lemma;
		this.senses = new ArrayList<Sense>();
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

	public List<Sense> getSenses() {
		return senses;
	}
	
	public boolean addSense(Sense sense){
		return senses.add(sense);
	}

	public void setSenses(List<Sense> list) {
		this.senses = list;
	}

	public String asHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: <b>");
		sb.append(id);
		sb.append("<br></b>LEMMA: <b>");
		sb.append(lemma);
		sb.append("</b><br>SENSES:");
		if(senses.size() > 0)
			sb.append("<ul>");
		for(Sense s: senses){
			sb.append("<li>");
			sb.append(s.asHtml(false));
			sb.append("</li>");
		}
		if(senses.size() > 0)
			sb.append("</ul>");
		return sb.toString();
	}

	@Override
	public String toString() {
		return lemma;
	}

	public void setSenses(Object[] array) {
		senses = new ArrayList<Sense>();
		for(Object o : array)
			addSense((Sense) o);
	}
	
	public List<String> getSublemmas(){
		List<String> sls = new ArrayList<String>();
		for(Sense s : this.getSenses()){
			sls.addAll(s.getSublemmas());
		}
		return sls;
	}

	@Override
	public int compareTo(Entry o) {
		return getLemma().compareTo(o.getLemma());
	}
}
