package teamgb.dictionary.lexicon;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="EXAMPLE-E")
public class CebuanoLexiconExample {
	@Element(name="CEB")
	private String example;
	@Element(name="ENG")
	private String englishTranslation;
	
	public CebuanoLexiconExample(@Element(name="CEB") String example, @Element(name="ENG") String englishTranslation){
		this.example = example;
		this.englishTranslation = englishTranslation;
	}
	
	public CebuanoLexiconExample() {
	}

	public String getExample(){
		return example;
	}
	
	public String getEnglishTranslation() {
		return englishTranslation;
	}
	
	public void setExample(String example){
		this.example = example;
	}
	
	public void setEnglishTranslation(String englishTranslation){
		this.englishTranslation = englishTranslation;
	}	
	
	@Override
	public String toString(){
		return example +" ("+englishTranslation+")";
	}
}
