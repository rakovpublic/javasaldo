package com.rakov.saldo.mongodbutils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.xmlparser.XMLParser;
import com.rakov.saldo.xmlparser.XMLParserSaldom;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class MongoDBUtils {
	private static MongoDBUtils dbUtils = null;
	private Mongo mongo;
	private static DBCollection lemgramCollection;

	private MongoDBUtils() {
		try {
			mongo = new Mongo();

			DB db = mongo.getDB("lemgramsDB");
			lemgramCollection = db.getCollection("lemgrams");
			if (lemgramCollection.count() == 0) {
				lemgramCollection = db.createCollection("lemgrams", null);
				SAXParserFactory parserFactor = SAXParserFactory.newInstance();
				SAXParser parser=null;
				XMLParser handler = new XMLParser();
			    try {
					parser = parserFactor.newSAXParser();
					parser.parse(ClassLoader.getSystemResourceAsStream("src/main/resources/saldo.xml"), handler);
				} catch (ParserConfigurationException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				} catch (SAXException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
			    SAXParserFactory parserFactorSaldom = SAXParserFactory.newInstance();
				SAXParser parserSaldom=null;
				XMLParserSaldom handlerSaldom = new XMLParserSaldom();
				handlerSaldom.setSaldo(handler.getSaldo());
				try {
					parserSaldom =parserFactorSaldom.newSAXParser();
					parserSaldom.parse(ClassLoader.getSystemResourceAsStream("src/main/resources/saldom.xml"), handlerSaldom);
				} catch (ParserConfigurationException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				} catch (SAXException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
				HashMap<String, ArrayList<Lemgram>> result =handlerSaldom.getResult();
				String [] keys= (String[]) result.keySet().toArray();
				for(int i=0;i<keys.length;i++)
				{
					ArrayList<Lemgram> lems =result.get(keys[i]);
					for(int j=0;j<lems.size();j++){
						lemgramCollection.insert(lems.get(j).toDBObject());
					}
				}

			}

		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public static DBCollection getDBCollection() {
		if (dbUtils == null) {
			dbUtils = new MongoDBUtils();
		}
		return lemgramCollection;
	}

}
