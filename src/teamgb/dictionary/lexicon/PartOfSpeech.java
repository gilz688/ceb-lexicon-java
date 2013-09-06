package teamgb.dictionary.lexicon;

public enum PartOfSpeech {
	NOUN(0,"n","noun"), VERB(1,"v","verb"), ADJECTIVE(2,"adj","adjective"), ADVERB(3,"adv","adverb");
	
	private int type;
	private String abrev, description;
	
	PartOfSpeech(int type, String abrev, String description){
		this.type = type;
		this.abrev = abrev;
		this.description = description;
	}
	
	public int getType(){
		return type;
	}
	
	public String getAbrev(){
		return abrev;
	}
	
	public String getDescription(){
		return description;
	}
	
	
}
