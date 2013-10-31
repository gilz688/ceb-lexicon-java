package teamgb.dictionary.lexicon;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="SENSE")
public class CebuanoLexiconSense {
	@Element(name="GLOSS")
	private String gloss;
	
	@Attribute(name="POS")
	private PartOfSpeech partOfSpeech;
	
	@ElementList(name="SUB-LEMMAS",entry="SL-E")
	private ArrayList<String> sublemmas;
	
	@ElementList(name="EXAMPLES")
	private ArrayList<CebuanoLexiconExample> examples;
	
	public PartOfSpeech getPartOfSpeech() {
		return partOfSpeech;
	}

	public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	
	public CebuanoLexiconSense(String gloss, PartOfSpeech partOfSpeech){
		this.gloss = gloss;
		this.partOfSpeech = partOfSpeech;
		this.sublemmas = new ArrayList<String>();
		this.examples = new ArrayList<CebuanoLexiconExample>();
	}
	
	public CebuanoLexiconSense() {
	
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public boolean addExample(CebuanoLexiconExample example){
		return examples.add(example);
	}
	
	public List<CebuanoLexiconExample> getExamples() {
		return examples;
	}

	public void setExamples(ArrayList<CebuanoLexiconExample> examples) {
		this.examples = examples;
	}
	

	public List<String> getSublemmas() {
		return sublemmas;
	}
	
	public void setSublemmas(ArrayList<String> sublemmas) {
		this.sublemmas = sublemmas;
	}
	
	@Override
	public String toString() {
		return gloss;
	}
}
