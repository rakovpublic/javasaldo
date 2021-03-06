package com.rakov.saldo.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Lemgram {
	private String sense;
	private String senseLable = null;
	private String msd = null;
	private String father;
	private String lemgram;
	private String gf;
	private String pos;
	private String paradigm;
	private String form;

	public Lemgram(Lemgram lem) {
		super();
		this.sense = lem.sense;
		this.senseLable = lem.senseLable;
		this.father = lem.father;
		this.lemgram = lem.lemgram;
		this.gf = lem.gf;
		this.pos = lem.pos;
		this.paradigm = lem.paradigm;
		this.form = lem.form;
		this.msd = lem.msd;
	}

	public Lemgram() {
		super();
		this.sense = null;
		this.senseLable = null;
		this.father = null;
		this.lemgram = null;
		this.gf = null;
		this.pos = null;
		this.paradigm = null;
		this.form = null;
		this.msd = null;
	}

	public String getSenseLable() {
		if (senseLable == null) {
			return null;
		}
		return senseLable;
	}

	public void setSenseLable(String senseLable) {
		this.senseLable = senseLable;
	}

	public String getSense() {
		return sense;
	}

	public void setSense(String sense) {
		this.sense = sense;
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
		if (msd == null) {
			return null;
		}
		return msd;
	}

	public void setMsd(String msd) {
		this.msd = msd;
	}

	public BasicDBObject toDBObject() {

		BasicDBObject document = new BasicDBObject();

		document.put("sense", sense);
		document.put("senselable", senseLable);

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
		b.setFather((String) document.get("father"));
		b.setSenseLable((String) document.get("senselable"));
		b.setLemgram((String) document.get("lemgram"));
		b.setGf((String) document.get("gf"));
		b.setPos((String) document.get("pos"));
		b.setParadigm((String) document.get("paradigm"));
		b.setForm((String) document.get("form"));
		b.setMsd((String) document.get("msd"));

		return b;
	}

	@Override
	public String toString() {
		return "Lemgram [sense=" + sense + ", senseLable=" + senseLable
				+ ", father=" + father + ", lemgram=" + lemgram + ", gf=" + gf
				+ ", pos=" + pos + ", paradigm=" + paradigm + ", form=" + form
				+ ", msd=" + msd + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((father == null) ? 0 : father.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lemgram other = (Lemgram) obj;
		if (father == null) {
			if (other.father != null)
				return false;
		} else if (!father.equals(other.father))
			return false;
		return true;
	}

}
