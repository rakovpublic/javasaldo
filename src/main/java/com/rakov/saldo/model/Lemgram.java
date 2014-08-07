package com.rakov.saldo.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Lemgram {
	private String sense;
	private String senseLable;
	public String getSenseLable() {
		return senseLable;
	}

	public void setSenseLable(String senseLable) {
		this.senseLable = senseLable;
	}

	private String mother;
	private String father;
	private String lemgram;
	private String gf;
	private String pos;
	private String writtenForm;
	public String getWrittenForm() {
		return writtenForm;
	}

	public void setWrittenForm(String writtenForm) {
		this.writtenForm = writtenForm;
	}

	private String paradigm;
	private String form;
	private String msd;

	public String getSense() {
		return sense;
	}

	public void setSense(String sense) {
		this.sense = sense;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getLemgram() {
		return lemgram;
	}

	public void setLemgram(String lemgram) {
		this.lemgram = lemgram;
	}

	public String getGf() {
		return gf;
	}

	public void setGf(String gf) {
		this.gf = gf;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getParadigm() {
		return paradigm;
	}

	public void setParadigm(String paradigm) {
		this.paradigm = paradigm;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getMsd() {
		return msd;
	}

	public void setMsd(String msd) {
		this.msd = msd;
	}

	public BasicDBObject toDBObject() {

		BasicDBObject document = new BasicDBObject();

		document.put("sense", sense);
		document.put("mother", mother);
		document.put("father", father);
		document.put("lemgram", lemgram);
		document.put("gf", gf);
		document.put("pos", pos);
		document.put("paradigm", paradigm);
		document.put("form", form);
		document.put("msd", msd);

		return document;
	}

	public static Lemgram fromDBObject(DBObject document) {
		Lemgram b = new Lemgram();

		b.setSense((String) document.get("sense"));
		b.setMother((String) document.get("mother"));
		b.setLemgram((String) document.get("lemgram"));
		b.setGf((String) document.get("gf"));
		b.setPos((String) document.get("pos"));
		b.setParadigm((String) document.get("paradigm"));
		b.setForm((String) document.get("form"));
		b.setMsd((String) document.get("msd"));

		return b;
	}

}
