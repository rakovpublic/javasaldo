package com.rakov.saldo.xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rakov.saldo.model.Lemgram;

public class XMLParser extends DefaultHandler {
	public HashMap<String, ArrayList<Lemgram>> getSaldo() {
		return saldo;
	}

	private HashMap<String, ArrayList<Lemgram>> saldo = new HashMap<String, ArrayList<Lemgram>>();
	private List<Lemgram> tempLems;
	private List<Lemgram> finalLems;

	private Lemgram lem;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		switch (qName) {
		case "LexicalEntry":
			tempLems = new ArrayList<Lemgram>();
			tempLems.clear();
			break;
		case "FormRepresentation":
			lem = new Lemgram();
			break;
		case "Sense":
			finalLems=new ArrayList<Lemgram>();
			for (int i = 0; i < tempLems.size(); i++) {
				Lemgram tlem = new Lemgram(tempLems.get(i));
				tlem.setSense(attributes.getValue("id"));
				tempLems.remove(i);
				tempLems.add(i, tlem);
			}
			break;
		
		case "SenseRelation": 
			  for (int k = 0; k < tempLems.size(); k++) {
				  Lemgram ttlem = new Lemgram(tempLems.get(k));
				 
					  ttlem.setFather(attributes.getValue("targets"));
					  finalLems.add(ttlem);
				  
				}
			  break;
		case "feat":
			switch (attributes.getValue("att")) {
			case "lemgram":
				lem.setLemgram(attributes.getValue("val"));
				break;
			case "paradigm":
				lem.setParadigm(attributes.getValue("val"));
				break;
			case "partOfSpeech":
				lem.setPos(attributes.getValue("val"));
				break;
			case "writtenForm":
				lem.setGf(attributes.getValue("val"));
				break;
			
			case "label":
				for (int j = 0; j < finalLems.size(); j++) {
					Lemgram flem = new Lemgram(finalLems.get(j));
					if(flem.getSenseLable()==null){
					flem.setSenseLable(attributes.getValue("val"));
					if (saldo.containsKey(flem.getLemgram())) {
						ArrayList<Lemgram> tempL = saldo.get(flem.getLemgram());
						tempL.add(flem);
						saldo.remove(flem.getLemgram());
						saldo.put(flem.getLemgram(), tempL);
					} else {
						ArrayList<Lemgram> tempL = new ArrayList<Lemgram>();
						tempL.add(flem);
						saldo.put(flem.getLemgram(), tempL);

					}
					}

				}
				break;

			}
			break;

		
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		switch (qName) {
		case "FormRepresentation":
			tempLems.add(lem);
		}

	}

}
