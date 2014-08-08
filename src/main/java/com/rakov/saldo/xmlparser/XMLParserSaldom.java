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
	private List<Lemgram> tLems;

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
			existFlag = saldo.containsKey(attributes.getValue("lemgram"));
			if (existFlag) {
				tLems = saldo.get(attributes.getValue("lemgram"));
			}
			break;
		case "feat":
			switch (attributes.getValue("att")) {
			case ("lemgram"):
				existFlag = saldo.containsKey(attributes.getValue("val"));
				if (existFlag) {
					tLems = saldo.get(attributes.getValue("val"));
				}
				break;
			case ("writtenForm"):
				if (!formRepFlag) {
					for (int i = 0; i < tLems.size(); i++) {
						Lemgram lem = tLems.get(i);
						lem.setForm(attributes.getValue("val"));

						tempList.add(lem);
					}
				}
				break;
			case ("msd"):
				for (int i = 0; i < tempList.size(); i++) {
					Lemgram lem = tempList.get(i);

					lem.setMsd(attributes.getValue("val"));
					tempList.remove(i);
					tempList.add(i, lem);
				}
				if (result.containsKey(tempList.get(0).getLemgram())) {
					ArrayList<Lemgram> t = result.get(tempList.get(0)
							.getLemgram());
					t.addAll(tempList);
					result.remove(tempList.get(0).getLemgram());
					result.put(tempList.get(0).getLemgram(), t);
				} else {
					result.put(tempList.get(0).getLemgram(), tempList);
				}
				break;

			}
			break;
		case "WordForm":
			if (existFlag) {
				tempList = new ArrayList<Lemgram>();
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
		}

	}

}
