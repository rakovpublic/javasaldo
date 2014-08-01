package com.rakov.saldo.dao;

import java.util.List;

import com.rakov.saldo.model.Lemgram;

public interface LemgramDAO {

	public List<Lemgram> getLemgramByName(String lemgram1);
	public List<Lemgram>  getLemgramByForm(String form);
	public List<Lemgram>  getLemgramByGf(String gf);
	
	public List<Lemgram>  getSense(String sense);
	
}
