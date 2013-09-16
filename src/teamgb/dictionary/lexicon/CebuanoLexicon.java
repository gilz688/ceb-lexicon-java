package teamgb.dictionary.lexicon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CebuanoLexicon {
	private List<CebuanoLexiconEntry> entries;
	
	public CebuanoLexicon() {
		entries = new ArrayList<CebuanoLexiconEntry>();
	}
	
	public boolean addEntry(CebuanoLexiconEntry e){
		return entries.add(e);
	}
	
	public List<CebuanoLexiconEntry> getEntries(){
		return entries;
	}
	
	public void setEntries(Object[] array) {
		entries = new ArrayList<CebuanoLexiconEntry>();
		for(Object o : array)
			entries.add((CebuanoLexiconEntry) o);
	}
	
	public List<String> getIds(){
		return getIds(null);
	}

	public void sort(){
		Collections.sort(entries);
	}
	
	public List<String> getIds(CebuanoLexiconEntry s){
		List<String> ids = new ArrayList<String>();
		for(CebuanoLexiconEntry e: entries){
			if(s== null || !(e.getId() == s.getId()) )
				ids.add(String.valueOf(e.getId()));
		}
		return ids;
	}
	
	public List<String> getLemmas(){
		return getLemmas(null);
	}
	
	public List<String> getLemmas(CebuanoLexiconEntry s){
		List<String> lemmas = new ArrayList<String>();
		for(CebuanoLexiconEntry e: entries){
			if(s==null || !(e.getLemma() == s.getLemma()))
				lemmas.add(e.getLemma());
		}
		return lemmas;
	}

	public void setEntries(List<CebuanoLexiconEntry> readEntries) {
		entries = readEntries;
	}
	
}
