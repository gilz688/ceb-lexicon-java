package teamgb.dictionary.lexicon;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class LexiconUtils {
	public static List<String> getLemmas(CebuanoLexicon lexicon) {
		List<String> lemmas = new ArrayList<String>();
		for (CebuanoLexiconEntry e : lexicon.getEntries()) {
			lemmas.add(e.getLemma());
		}
		return lemmas;
	}

	public static List<String> getIds(CebuanoLexicon lexicon) {
		List<String> ids = new ArrayList<String>();
		for (CebuanoLexiconEntry e : lexicon.getEntries()) {
			ids.add(String.valueOf(e.getId()));
		}
		return ids;
	}

	public static List<String> getIds(CebuanoLexicon lex, CebuanoLexiconEntry s) {
		List<String> ids = new ArrayList<String>();
		for (CebuanoLexiconEntry e : lex.getEntries()) {
			if (s == null || !(e.getId() == s.getId()))
				ids.add(String.valueOf(e.getId()));
		}
		return ids;
	}

	public static List<String> getLemmas(CebuanoLexicon lex,
			CebuanoLexiconEntry s) {
		List<String> lemmas = new ArrayList<String>();
		for (CebuanoLexiconEntry e : lex.getEntries()) {
			if (s == null || !(e.getLemma() == s.getLemma()))
				lemmas.add(e.getLemma());
		}
		return lemmas;
	}

	public static String getPartOfSpeechAbreviation(PartOfSpeech partOfSpeech) {
		String abreviation = "";
		if (partOfSpeech == null)
			return "n/a";
		switch (partOfSpeech) {
		case NOUN:
			abreviation = "n";
			break;
		case VERB:
			abreviation = "v";
			break;
		case ADJECTIVE:
			abreviation = "adj";
			break;
		case ADVERB:
			abreviation = "adv";
			break;
		case PREPOSITION:
			abreviation = "prep";
			break;
		case CONJUNCTION:
			abreviation = "conj";
			break;
		default:
		}
		return abreviation;
	}

	public static String getPartOfSpeechDescription(PartOfSpeech partOfSpeech) {
		String description = "";
		switch (partOfSpeech) {
		case NOUN:
			description = "noun";
			break;
		case VERB:
			description = "verb";
			break;
		case ADJECTIVE:
			description = "adjective";
			break;
		case ADVERB:
			description = "adverb";
			break;
		case PREPOSITION:
			description = "preposition";
			break;
		case CONJUNCTION:
			description = "conjunction";
			break;
		default:
		}
		return description;
	}

	public static String generateHtml(CebuanoLexiconEntry entry) {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: <b>");
		sb.append(entry.getId());
		sb.append("<br></b>LEMMA: <b>");
		sb.append(entry.getLemma());
		sb.append("</b><br>SENSES:");
		List<CebuanoLexiconSense> senses = entry.getSenses();
		if (senses.size() > 0)
			sb.append("<ul>");
		for (CebuanoLexiconSense s : senses) {
			sb.append("<li>");
			sb.append(generateHtml(s, entry.getLemma(), false));
			sb.append("</li>");
		}
		if (senses.size() > 0)
			sb.append("</ul>");
		return sb.toString();
	}

	private static String generateHtml(CebuanoLexiconSense sense, String lemma,
			boolean inc) {
		StringBuilder sb = new StringBuilder();
		sb.append("(<font color=\"blue\">");
		sb.append(getPartOfSpeechAbreviation(sense.getPartOfSpeech()));
		sb.append(")</font> ");
		List<String> sublemmas = sense.getSublemmas();
		if (inc) {
			sb.append(lemma);
			if (sublemmas.size() > 0)
				sb.append(", ");
		}
		Iterator iter = sublemmas.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			if (iter.hasNext())
				sb.append(", ");
		}
		sb.append(" (");
		sb.append(sense.getGloss());
		sb.append(") ");

		iter = sense.getExamples().iterator();
		while (iter.hasNext()) {
			sb.append("<i>\"");
			sb.append(iter.next());
			sb.append("</i>\"");
			if (iter.hasNext())
				sb.append("; ");
		}
		sb.append("</i>");
		return sb.toString();
	}

	public static String generateHtml(CebuanoLexiconSense sense) {
		StringBuilder sb = new StringBuilder();
		sb.append("(<font color=\"blue\">");
		sb.append(getPartOfSpeechAbreviation(sense.getPartOfSpeech()));
		sb.append(")</font> ");
		List<String> sublemmas = sense.getSublemmas();

		Iterator iter = sublemmas.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			if (iter.hasNext())
				sb.append(", ");
		}
		sb.append(" (");
		sb.append(sense.getGloss());
		sb.append(") ");

		iter = sense.getExamples().iterator();
		while (iter.hasNext()) {
			sb.append("<i>\"");
			sb.append(iter.next());
			sb.append("</i>\"");
			if (iter.hasNext())
				sb.append("; ");
		}
		sb.append("</i>");
		return sb.toString();
	}

	public static void saveAsXML(CebuanoLexicon lex, File cFile) {
		Serializer serializer = new Persister();
		try {
			serializer.write(lex, cFile);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
				    e.getMessage(),
				    "Error writing xml file",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

	public static CebuanoLexicon readFromXML(File cFile) {
		System.out.println(cFile.getAbsolutePath());
		Serializer serializer = new Persister();
		CebuanoLexicon lex = null;
		try {
			lex = serializer.read(CebuanoLexicon.class,cFile);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
				    e.getMessage(),
				    "Error reading xml file",
				    JOptionPane.ERROR_MESSAGE);
		}
		System.out.println(lex);
		return lex;
	}
}
