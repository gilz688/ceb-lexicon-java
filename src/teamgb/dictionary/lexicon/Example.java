package teamgb.dictionary.lexicon;

public class Example {
	private String eng_example;
	private String ceb_example;
	
	public String getEng_example() {
		return eng_example;
	}
	
	public void setEng_example(String eng_example) {
		this.eng_example = eng_example;
	}
	
	public String getCeb_example() {
		return ceb_example;
	}
	
	public void setCeb_example(String ceb_example) {
		this.ceb_example = ceb_example;
	}
	
	@Override
	public String toString(){
		return ceb_example +" ("+eng_example+")";
	}
}
