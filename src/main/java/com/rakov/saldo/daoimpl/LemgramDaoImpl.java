package com.rakov.saldo.daoimpl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.rakov.saldo.dao.LemgramDAO;
import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.mongodbutils.MongoDBUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class LemgramDaoImpl implements LemgramDAO {
	private static DBCollection lemgramCollection = MongoDBUtils
			.getDBCollection();

	@Override
	public List<Lemgram> getLemgramByName(String lemgram1) {
		BasicDBObject query = new BasicDBObject("lemgram", lemgram1);
		List<Lemgram> result = new ArrayList<Lemgram>();
		DBCursor temp = lemgramCollection.find(query);
		try {
			while (temp.hasNext()) {
				result.add(Lemgram.fromDBObject(temp.next()));
			}
		} finally {
			temp.close();
		}

		return result;
	}

	@Override
	public List<Lemgram> getLemgramByForm(String form) {
		BasicDBObject query = new BasicDBObject("form", form);
		List<Lemgram> result = new ArrayList<Lemgram>();
		DBCursor temp = lemgramCollection.find(query);
		try {
			while (temp.hasNext()) {
				result.add(Lemgram.fromDBObject(temp.next()));
			}
		} finally {
			temp.close();
		}

		return result;
	}

	@Override
	public List<Lemgram> getLemgramByGf(String gf) {
		BasicDBObject query = new BasicDBObject("gf", gf);
		List<Lemgram> result = new ArrayList<Lemgram>();
		DBCursor temp = lemgramCollection.find(query);
		try {
			while (temp.hasNext()) {
				result.add(Lemgram.fromDBObject(temp.next()));
			}
		} finally {
			temp.close();
		}

		return result;
	}

	@Override
	public List<Lemgram> getSense(String sense) {
		BasicDBObject query = new BasicDBObject("sense", sense);
		List<Lemgram> result = new ArrayList<Lemgram>();
		DBCursor temp = lemgramCollection.find(query);
		try {
			while (temp.hasNext()) {
				result.add(Lemgram.fromDBObject(temp.next()));
			}
		} finally {
			temp.close();
		}

		return result;
	}

}
