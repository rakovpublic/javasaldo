package com.rakov.saldo.xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rakov.saldo.model.Lemgram;

public class XMLParserSaldom  extends DefaultHandler{
	private HashMap<String,ArrayList<Lemgram>> saldo=new HashMap<String,ArrayList<Lemgram>>();
	private HashMap<String,ArrayList<Lemgram>> result = new HashMap<String,ArrayList<Lemgram>>();
	private List<Lemgram> tLems;
	private Lemgram lem;
	private boolean existFlag=false;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			    switch(qName){
			      case "LexicalEntry":
			    	  existFlag=false;
			    	  tLems.clear();
			        break;
			      case "FormRepresentation":
			    	  existFlag=saldo.containsKey(attributes.getValue("lemgram"));
			    	  if(existFlag)
			    	  {
			    	  tLems=saldo.get(attributes.getValue("lemgram"));
			    	  }
			        break;
			      case "WordForm":
			    	  if(existFlag)
			    	  {
			    		ArrayList<Lemgram> tempList=new ArrayList<Lemgram>();  
			    	  for(int i=0;i<tLems.size();i++)
			    	  {
			    		  Lemgram lem=tLems.get(i);
			    		  lem.setForm(attributes.getValue("writtenForm"));
			    		  lem.setMsd(attributes.getValue("msd"));
			    		  tempList.add(lem);
			    	  }
			    	   if(result.containsKey(tempList.get(0).getLemgram()))
			    	   {
			    		   ArrayList<Lemgram> t=result.get(tempList.get(0).getLemgram());
			    		   t.addAll(tempList);
			    		   result.remove(tempList.get(0).getLemgram());
			    		   result.put(tempList.get(0).getLemgram(), t);
			    	   }
			    	   else{
			    		   result.put(tempList.get(0).getLemgram(), tempList);
			    	   }
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
	 public void endElement(String uri, String localName, String qName) throws SAXException {
			 

			   }

}
