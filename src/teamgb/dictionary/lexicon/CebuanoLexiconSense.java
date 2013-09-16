package teamgb.dictionary.lexicon;
import java.util.ArrayList;
import java.util.List;

public class CebuanoLexiconSense {
	private PartOfSpeech pos;
	private String lemma, gloss;
	private List<String> sublemmas;
	List<CebuanoLexiconExample> examples;
	
	public PartOfSpeech getPartOfSpeech() {
		return pos;
	}

	public void setPartOfSpeech(PartOfSpeech pos) {
		this.pos = pos;
	}
	
	public CebuanoLexiconSense(){
		lemma = new String();
		pos = null;
		this.sublemmas = new ArrayList<String>();
		this.gloss = null;
		this.examples = new ArrayList<CebuanoLexiconExample>();
	}
	
	public void setLemma(String l){
		lemma = l;
	}
	
	public String getLemma(){
		return lemma;
	}
	
	public CebuanoLexiconSense(PartOfSpeech pos) {
		this.pos = pos;
		this.sublemmas = new ArrayList<String>();
		this.gloss = new String();
		this.examples = new ArrayList<CebuanoLexiconExample>();
	}
	
	public List<String> getSublemmas() {
		return sublemmas;
	}

	@Override
	public String toString() {
		return "<html>" + LexiconUtils.generateHtml(this,false) + "</html>";
	}
	
	public void setSublemmas(List<String> sublemmas) {
		this.sublemmas = sublemmas;
	}
	
	public boolean addSublemma(String sublemma){
		return sublemmas.add(sublemma);
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public List<CebuanoLexiconExample> getExamples() {
		return examples;
	}

	public void setExamples(List<CebuanoLexiconExample> examples) {
		this.examples = examples;
	}
	
	public boolean addExample(CebuanoLexiconExample example){
		return examples.add(example);
	}

	public void setExamples(Object[] e) {
		examples = new ArrayList<CebuanoLexiconExample>();
		for(Object object : e){
			CebuanoLexiconExample ex = (CebuanoLexiconExample) object;
			if(ex!=null)
				examples.add(ex);
		}
			
	}

	public void setSublemmas(Object[] array) {
		sublemmas = new ArrayList<String>();
		for(Object o : array)
			sublemmas.add((String) o);
	}
}
