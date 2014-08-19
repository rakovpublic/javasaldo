package com.rakov.saldo.dao;

import java.util.ArrayList;
import java.util.List;

import com.rakov.saldo.model.Lemgram;

public interface LemgramDAO {

	public List<Lemgram> getLemgramByName(String lemgram1);

	public List<Lemgram> getLemgramByForm(String form);

	public List<Lemgram> getLemgramByGf(String gf);

	public ArrayList<Lemgram> getSense(String sense);

}
