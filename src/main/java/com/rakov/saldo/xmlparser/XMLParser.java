package com.rakov.saldo.xmlparser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rakov.saldo.model.Lemgram;

public class XMLParser extends DefaultHandler{
	public HashMap<String, ArrayList<Lemgram>> getSaldo() {
		return saldo;
	}
	private HashMap<String,ArrayList<Lemgram>> saldo=new HashMap<String,ArrayList<Lemgram>>();
	private List<Lemgram> tempLems;
	private Lemgram lem;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			    switch(qName){
			      case "LexicalEntry":
			    	  tempLems= new ArrayList<Lemgram>();
			        break;
			      case "FormRepresentation":
			    	  lem= new Lemgram();
			    	  lem.setLemgram(attributes.getValue("lemgram"));
				    	 lem.setParadigm(attributes.getValue("paradigm"));
				    	 lem.setPos(attributes.getValue("partOfSpeech"));
				    	 lem.setWrittenForm(attributes.getValue("writtenForm"));
				    	 tempLems.add(lem);
			        break;
			      case "Sense":
			    	  for(int i=0;i<tempLems.size();i++)
			    	  {
			    		  Lemgram tlem= tempLems.get(i);
			    		  tlem.setSense(attributes.getValue("id"));
			    		  tempLems.remove(i);
			    		  tempLems.add(i, tlem);
			    	  }
			        break;
			      case "SenseRelation":
				    	 for(int j=0;j<tempLems.size();j++){
				    		 Lemgram flem= tempLems.get(j);
				    		 flem.setFather(attributes.getValue("targets"));
				    		 flem.setSenseLable(attributes.getValue("label"));
				    		 if(saldo.containsKey(flem.getLemgram())){
				    			 ArrayList<Lemgram> tempL = saldo.get(flem.getLemgram());
				    			 tempL.add(flem);
				    			 saldo.remove(flem.getLemgram());
				    			 saldo.put(flem.getLemgram(),tempL);
				    		 }
				    		 else{
				    			 ArrayList<Lemgram> tempL=new ArrayList<Lemgram>();
				    			 tempL.add(flem);
				    			 saldo.put(flem.getLemgram(),tempL);
				    			 
				    		 }
				    		 
				    	    }
					        break;
			
			    }
			    
			  }
	@Override
	 public void endElement(String uri, String localName, String qName) throws SAXException {
			 

			   }



}
