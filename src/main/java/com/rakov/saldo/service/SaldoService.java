package com.rakov.saldo.service;

import java.util.HashMap;
import java.util.List;

import com.rakov.saldo.model.Lemgram;

public interface SaldoService {
	public String[] split(String word, String pos);
	public boolean isSemanticCompound(String word, String segs, String pos); 
	public HashMap<String, List<Lemgram>> getAncestors(String lemgram);
}
