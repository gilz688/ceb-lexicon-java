package teamgb.dictionary.lexicon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class LexiconUtils {
	static final String CEB_LEXICON = "CEB-LEXICON";
	static final String ENTRY = "ENTRY";
	static final String ID = "ID";
	static final String LEMMA = "LEMMA";
	static final String SENSES = "SENSES";
	static final String SENSE = "SENSE";
	static final String POS = "POS";
	static final String SUB_LEMMAS = "SUB-LEMMAS";
	static final String SL_E = "SL-E";
	static final String GLOSS = "GLOSS";
	static final String EXAMPLES = "EXAMPLES";
	static final String EXAMPLE_E = "EXAMPLE-E";
	static final String ENG = "ENG";
	static final String CEB = "CEB";

	public static CebuanoLexicon readFromXML(File xmlFile) {
		CebuanoLexicon lex = new CebuanoLexicon();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStreamReader in = new InputStreamReader(new FileInputStream(
					xmlFile), Charset.forName("UTF8"));
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				switch (event.getEventType()) {
				case XMLEvent.START_ELEMENT:
					StartElement startElem = event.asStartElement();
					if (startElem.getName().getLocalPart().equals(CEB_LEXICON)) {
						lex.setEntries(readEntries(eventReader));
					}
					break;
				default:
				}
			}
		} catch (FileNotFoundException e) {

		} catch (XMLStreamException e) {

		}
		return lex;
	}

	private static List<CebuanoLexiconEntry> readEntries(
			XMLEventReader eventReader) {
		List<CebuanoLexiconEntry> entries = new ArrayList<CebuanoLexiconEntry>();
		CebuanoLexiconEntry entry = null;
		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				switch (event.getEventType()) {
				case XMLEvent.START_ELEMENT:
					StartElement startElem = event.asStartElement();
					if (startElem.getName().getLocalPart().equals(ENTRY)) {
						entry = readEntry(eventReader);
						entries.add(entry);
					}
					break;
				case XMLEvent.END_ELEMENT:
					EndElement endElem = event.asEndElement();
					String endLP = endElem.getName().getLocalPart();
					if (endLP.equals(CEB_LEXICON)) {
						return entries;
					}
				default:
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		System.out.println(CEB_LEXICON + " not properly terminated");
		return null;
	}

	private static CebuanoLexiconEntry readEntry(XMLEventReader eventReader) {
		CebuanoLexiconEntry entry = new CebuanoLexiconEntry();
		String data = "";
		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				switch (event.getEventType()) {
				case XMLEvent.START_ELEMENT:
					StartElement startElem = event.asStartElement();
					String startLP = startElem.getName().getLocalPart();
					if (startLP.equals(ID)) {
						event = eventReader.nextEvent();
						data = event.asCharacters().getData();
					} else if (startLP.equals(LEMMA)) {
						event = eventReader.nextEvent();
						data = event.asCharacters().getData();
					} else if (startLP.equals(SENSES)) {
						entry.setSenses(readSenses(eventReader));
					}
					break;
				case XMLEvent.END_ELEMENT:
					EndElement endElem = event.asEndElement();
					String endLP = endElem.getName().getLocalPart();
					if (endLP.equals(ID)) {
						try {
							int id = Integer.parseInt(data);
							entry.setId(id);
						} catch (NumberFormatException e) {

						}
					} else if (endLP.equals(LEMMA)) {
						entry.setLemma(data);
					} else if (endLP.equals(ENTRY)) {
						return entry;
					}
					break;
				default:
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		System.out.println(ENTRY + " not properly terminated");
		return null;
	}

	private static List<CebuanoLexiconSense> readSenses(
			XMLEventReader eventReader) {
		List<CebuanoLexiconSense> senseList = new ArrayList<CebuanoLexiconSense>();
		CebuanoLexiconSense sense = null;
		PartOfSpeech pos = null;
		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				switch (event.getEventType()) {
				case XMLEvent.START_ELEMENT:
					StartElement startElem = event.asStartElement();
					String startLP = startElem.getName().getLocalPart();
					if (startLP.equals(SENSE)) {
						Iterator<Attribute> attributes = startElem
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(POS)) {
								String value = attribute.getValue()
										.toUpperCase();
								pos = PartOfSpeech.valueOf(value);
							}
						}
						sense = readSense(eventReader);
						if (pos != null)
							sense.setPartOfSpeech(pos);
						if (sense != null)
							senseList.add(sense);
					}
					break;
				case XMLEvent.END_ELEMENT:
					EndElement endElem = event.asEndElement();
					String endLP = endElem.getName().getLocalPart();
					if (endLP.equals(SENSES)) {
						return senseList;
					}
					break;
				default:
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		System.out.println(SENSES + " not properly terminated");
		return null;
	}

	private static List<String> readSublemmas(XMLEventReader eventReader) {
		List<String> slList = new ArrayList<String>();
		String data = "";
		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				switch (event.getEventType()) {
				case XMLEvent.START_ELEMENT:
					StartElement startElem = event.asStartElement();
					String startLP = startElem.getName().getLocalPart();
					if (startLP.equals(SL_E)) {
						event = eventReader.nextEvent();
						data = event.asCharacters().getData();
					}
					break;
				case XMLEvent.END_ELEMENT:
					EndElement endElem = event.asEndElement();
					String endLP = endElem.getName().getLocalPart();
					if (endLP.equals(SL_E)) {
						slList.add(data);
					} else if (endLP.equals(SUB_LEMMAS)) {
						return slList;
					}
					break;
				default:
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		System.out.println(SUB_LEMMAS + " not properly terminated");
		return null;
	}

	private static CebuanoLexiconSense readSense(XMLEventReader eventReader) {
		CebuanoLexiconSense sense = new CebuanoLexiconSense();
		List<CebuanoLexiconExample> examples = null;
		String data = "";

		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				switch (event.getEventType()) {
				case XMLEvent.START_ELEMENT:
					StartElement startElem = event.asStartElement();
					String startLP = startElem.getName().getLocalPart();
					if (startLP.equals(GLOSS)) {
						event = eventReader.nextEvent();
						data = event.asCharacters().getData();
					} else if (startLP.equals(SUB_LEMMAS)) {
						List<String> sublemmas = readSublemmas(eventReader);
						if (sublemmas != null)
							sense.setSublemmas(sublemmas);
					} else if (startLP.equals(EXAMPLES)) {
						examples = readExamples(eventReader);
						if (examples != null)
							sense.setExamples(examples);
					}
					break;
				case XMLEvent.END_ELEMENT:
					EndElement endElem = event.asEndElement();
					String endLP = endElem.getName().getLocalPart();
					if (endLP.equals(GLOSS)) {
						sense.setGloss(data);
					} else if (endLP.equals(SENSE)) {
						return sense;
					}
					break;
				default:
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		System.out.println(SENSE + " not properly terminated");
		return null;
	}

	private static List<CebuanoLexiconExample> readExamples(
			XMLEventReader eventReader) {
		List<CebuanoLexiconExample> exampleList = new ArrayList<CebuanoLexiconExample>();
		CebuanoLexiconExample example = null;
		String text = "";
		while (eventReader.hasNext()) {
			try {
				XMLEvent event = eventReader.nextEvent();
				switch (event.getEventType()) {
				case XMLEvent.START_ELEMENT:
					StartElement startElem = event.asStartElement();
					String startLP = startElem.getName().getLocalPart();
					if (startLP.equals(EXAMPLE_E)) {
						example = new CebuanoLexiconExample();
					} else if (startLP.equals(CEB) || startLP.equals(ENG)) {
						event = eventReader.nextEvent();
						text = event.asCharacters().getData();
					}
					break;
				case XMLEvent.END_ELEMENT:
					EndElement endElem = event.asEndElement();
					String endLP = endElem.getName().getLocalPart();
					if (endLP.equals(EXAMPLE_E)) {
						exampleList.add(example);
					} else if (example == null) {

					} else if (endLP.equals(CEB)) {
						example.setCeb_example(text);
					} else if (endLP.equals(ENG)) {
						example.setEng_example(text);
					} else if (endLP.equals(EXAMPLES)) {
						return exampleList;
					}
					break;
				default:
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		System.out.println(EXAMPLES + " not properly terminated");
		return null;
	}

	public static void saveAsXML(CebuanoLexicon lex, File file)
			throws Exception {
		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter
		XMLEventWriter eventWriter = outputFactory
				.createXMLEventWriter(new OutputStreamWriter(
						new FileOutputStream(file), Charset.forName("UTF8")));
		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument("UTF-8",
				"1.0");
		eventWriter.add(startDocument);
		eventWriter.add(end);
		// Create LEXICON-MRD open tag
		StartElement configStartElement = eventFactory.createStartElement("",
				"", CEB_LEXICON);
		eventWriter.add(configStartElement);
		eventWriter.add(end);
		// Write the different entries
		for (CebuanoLexiconEntry entry : lex.getEntries())
			createENTRY(eventWriter, entry);

		eventWriter.add(eventFactory.createEndElement("", "", CEB_LEXICON));
		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
	}

	private static void createNode(XMLEventWriter eventWriter, String name,
			String value) throws XMLStreamException {

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);

		// Create Content
		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);

		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	private static void createENTRY(XMLEventWriter eventWriter,
			CebuanoLexiconEntry entry) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", ENTRY);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(end);

		// Create Content
		eventWriter.add(tab);
		createNode(eventWriter, ID, String.valueOf(entry.getId()));
		eventWriter.add(tab);
		createNode(eventWriter, LEMMA, entry.getLemma());
		createSENSES(eventWriter, entry.getSenses());

		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", ENTRY);
		eventWriter.add(tab);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	private static void createSENSES(XMLEventWriter eventWriter,
			List<CebuanoLexiconSense> list) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t\t");

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", SENSES);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(end);

		// Create Content
		for (CebuanoLexiconSense sense : list)
			createSENSE(eventWriter, sense);

		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", SENSES);
		eventWriter.add(tab);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	private static void createSENSE(XMLEventWriter eventWriter,
			CebuanoLexiconSense sense) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t\t\t");

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", SENSE);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(eventFactory.createAttribute(POS,
				getPartOfSpeechDescription(sense.getPartOfSpeech())));
		eventWriter.add(end);
		// Create Content
		createList(eventWriter, SUB_LEMMAS, SL_E, sense.getSublemmas(), 4);
		eventWriter.add(tab);
		createNode(eventWriter, GLOSS, sense.getGloss());
		createExampleList(eventWriter, sense.getExamples(), 4);

		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", SENSE);
		eventWriter.add(tab);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	private static void createExampleList(XMLEventWriter eventWriter,
			List<CebuanoLexiconExample> list, int level)
			throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");

		String tabStr = "";
		int t = 0;
		while (t < level) {
			tabStr += '\t';
			t++;
		}
		XMLEvent tab = eventFactory.createDTD(tabStr);
		XMLEvent tab_E = eventFactory.createDTD(tabStr + "\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "",
				EXAMPLES);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(end);

		// Create Content
		for (CebuanoLexiconExample ex : list) {
			// Start Element for EXAMPLE_E
			StartElement sElement_E = eventFactory.createStartElement("", "",
					EXAMPLE_E);
			eventWriter.add(tab_E);
			eventWriter.add(sElement_E);
			eventWriter.add(end);

			eventWriter.add(tab_E);
			createNode(eventWriter, CEB, ex.getCeb_example());
			eventWriter.add(tab_E);
			createNode(eventWriter, ENG, ex.getEng_example());

			// Create End node for EXAMPLE_E
			eventWriter.add(tab_E);
			EndElement eElement_E = eventFactory.createEndElement("", "",
					EXAMPLE_E);
			eventWriter.add(eElement_E);
			eventWriter.add(end);
		}

		// Create End node
		eventWriter.add(tab);
		EndElement eElement = eventFactory.createEndElement("", "", EXAMPLES);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	private static void createList(XMLEventWriter eventWriter, String name,
			String elemName, List<String> list, int level)
			throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");

		String tabStr = "";
		int t = 0;
		while (t < level) {
			tabStr += '\t';
			t++;
		}
		XMLEvent tab = eventFactory.createDTD(tabStr);

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		eventWriter.add(end);
		// Create Content
		for (String item : list) {
			eventWriter.add(tab);
			createNode(eventWriter, elemName, item);
		}

		// Create End node
		eventWriter.add(tab);
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}

	public static String getPartOfSpeechAbreviation(PartOfSpeech partOfSpeech) {
		String abreviation = "";
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
		case INTERJECTION:
			abreviation = "intr";
			break;
		case ARTICLE:
			abreviation = "a";
			break;
		case PARTICIPLE:
			abreviation = "p";
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
		case INTERJECTION:
			description = "interjection";
			break;
		case ARTICLE:
			description = "article";
			break;
		case PARTICIPLE:
			description = "participle";
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
			sb.append(generateHtml(s,false));
			sb.append("</li>");
		}
		if (senses.size() > 0)
			sb.append("</ul>");
		return sb.toString();
	}
	
	public static String generateHtml(CebuanoLexiconSense sense, boolean inc) {
		StringBuilder sb = new StringBuilder();
		sb.append("(<font color=\"blue\">");
		sb.append(getPartOfSpeechAbreviation(sense.getPartOfSpeech()));
		sb.append(")</font> ");
		List<String> sublemmas = sense.getSublemmas();
		if(inc){
			sb.append(sense.getLemma());
			if(sublemmas.size() > 0) sb.append(", ");
		}
		Iterator iter = sublemmas.iterator();
		while(iter.hasNext()){
			sb.append(iter.next());
			if(iter.hasNext()) sb.append(", ");
		}
		sb.append(" (");
		sb.append(sense.getGloss());
		sb.append(") ");
		
		iter = sense.getExamples().iterator();
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
}
