package com.rakov.saldo.xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rakov.saldo.model.Lemgram;

public class XMLParserSaldom extends DefaultHandler {
	private HashMap<String, ArrayList<Lemgram>> saldo = new HashMap<String, ArrayList<Lemgram>>();
	private HashMap<String, ArrayList<Lemgram>> result = new HashMap<String, ArrayList<Lemgram>>();
	private List<Lemgram> tLems = new ArrayList<Lemgram>();

	private ArrayList<Lemgram> tempList;

	private boolean existFlag = false;
	private boolean formRepFlag = false;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		switch (qName) {
		case "LexicalEntry":
			existFlag = false;
			tLems.clear();
			break;
		case "FormRepresentation":
			formRepFlag = true;

			break;
		case "feat":
			switch (attributes.getValue("att")) {
			case ("lemgram"):
				existFlag = saldo.containsKey(attributes.getValue("val"));
				if (existFlag) {
					tLems = saldo.get(attributes.getValue("val"));
					tempList = new ArrayList<Lemgram>();
					tempList.clear();

				}
				break;
			case ("writtenForm"):
				if (!formRepFlag) {
					for (int i = 0; i < tLems.size(); i++) {
						Lemgram lem = new Lemgram(tLems.get(i));
						lem.setForm(attributes.getValue("val"));
						tempList.add(lem);
					}
				}
				break;
			case ("msd"):
				for (int i = 0; i < tempList.size(); i++) {
					Lemgram lem1 = new Lemgram(tempList.get(i));
					if (lem1.getMsd() == null) {
						lem1.setMsd(attributes.getValue("val"));
						tempList.remove(i);
						tempList.add(i, lem1);
					}
				}
				break;

			}
			break;

		}

	}

	public HashMap<String, ArrayList<Lemgram>> getResult() {
		return result;
	}

	public void setSaldo(HashMap<String, ArrayList<Lemgram>> saldo) {
		this.saldo = saldo;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		switch (qName) {
		case "FormRepresentation":
			formRepFlag = false;
			break;
		case "LexicalEntry":
			result.put(tempList.get(0).getLemgram(), tempList);

			break;
		}

	}

}
