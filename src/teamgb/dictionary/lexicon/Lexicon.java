package teamgb.dictionary.lexicon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lexicon {
	private List<Entry> entries;
	
	public Lexicon() {
		entries = new ArrayList<Entry>();
	}
	
	public boolean addEntry(Entry e){
		return entries.add(e);
	}
	
	public List<Entry> getEntries(){
		return entries;
	}
	
	public void setEntries(Object[] array) {
		entries = new ArrayList<Entry>();
		for(Object o : array)
			entries.add((Entry) o);
	}
	
	public List<String> getIds(){
		return getIds(null);
	}

	public void sort(){
		Collections.sort(entries);
	}
	
	public List<String> getIds(Entry s){
		List<String> ids = new ArrayList<String>();
		for(Entry e: entries){
			if(s== null || !(e.getId() == s.getId()) )
				ids.add(String.valueOf(e.getId()));
		}
		return ids;
	}
	
	public List<String> getLemmas(){
		return getLemmas(null);
	}
	
	public List<String> getLemmas(Entry s){
		List<String> lemmas = new ArrayList<String>();
		for(Entry e: entries){
			if(s==null || !(e.getLemma() == s.getLemma()))
				lemmas.add(e.getLemma());
		}
		return lemmas;
	}

	public void setEntries(List<Entry> readEntries) {
		entries = readEntries;
	}
	
}
