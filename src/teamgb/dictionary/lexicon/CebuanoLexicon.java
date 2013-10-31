package teamgb.dictionary.lexicon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="CEB-LEXICON")
public class CebuanoLexicon {
	
	@ElementList(inline=true)
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

	public void setEntries(List<CebuanoLexiconEntry> entries) {
		this.entries = entries;
	}
	
	public void sort(){
        Collections.sort(entries);
	}
	
}
