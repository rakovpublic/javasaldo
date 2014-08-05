package com.rakov.saldo.mongodbutils;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoDBUtils {
	private static MongoDBUtils dbUtils=null;
	private Mongo mongo;
	private static DBCollection lemgramCollection;
	
	private MongoDBUtils()
	{
		try {
			mongo = new Mongo();

			DB db = mongo.getDB("lemgramsDB");
			lemgramCollection = db.getCollection("lemgrams");
			if (lemgramCollection == null) {
				lemgramCollection = db.createCollection("lemgrams", null);
				//TODO: add import data from xml
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static DBCollection getDBCollection()
	{
		if(dbUtils==null)
		{
			dbUtils= new MongoDBUtils();
		}
		return lemgramCollection;
	}
	

}
