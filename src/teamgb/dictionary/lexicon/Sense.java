package teamgb.dictionary.lexicon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sense {
	private PartOfSpeech pos;
	private String lemma, gloss;
	private List<String> sublemmas;
	List<Example> examples;
	
	public PartOfSpeech getPartOfSpeech() {
		return pos;
	}

	public void setPartOfSpeech(PartOfSpeech pos) {
		this.pos = pos;
	}
	
	public Sense(){
		lemma = new String();
		pos = null;
		this.sublemmas = new ArrayList<String>();
		this.gloss = null;
		this.examples = new ArrayList<Example>();
	}
	
	public void setLemma(String l){
		lemma = l;
	}
	
	public String getLemma(){
		return lemma;
	}
	
	public Sense(PartOfSpeech pos) {
		this.pos = pos;
		this.sublemmas = new ArrayList<String>();
		this.gloss = new String();
		this.examples = new ArrayList<Example>();
	}
	
	public List<String> getSublemmas() {
		return sublemmas;
	}

	@Override
	public String toString() {
		return "<html>" + asHtml(false) + "</html>";
	}
	
	public String asHtml(boolean inc) {
		StringBuilder sb = new StringBuilder();
		sb.append("(<font color=\"blue\">");
		sb.append(pos.getAbrev());
		sb.append(")</font> ");
		if(inc){
			sb.append(lemma);
			if(sublemmas.size() > 0) sb.append(", ");
		}
		Iterator iter = sublemmas.iterator();
		while(iter.hasNext()){
			sb.append(iter.next());
			if(iter.hasNext()) sb.append(", ");
		}
		sb.append(" (");
		sb.append(gloss);
		sb.append(") ");
		
		iter = examples.iterator();
		while(iter.hasNext()){
			sb.append("<i>\"");
			sb.append(iter.next());
			sb.append("</i>\"");
			if(iter.hasNext())
				sb.append("; ");
		}
		sb.append("</i>");
		return sb.toString();
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

	public List<Example> getExamples() {
		return examples;
	}

	public void setExamples(List<Example> examples) {
		this.examples = examples;
	}
	
	public boolean addExample(Example example){
		return examples.add(example);
	}

	public void setExamples(Object[] e) {
		examples = new ArrayList<Example>();
		for(Object object : e){
			Example ex = (Example) object;
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
