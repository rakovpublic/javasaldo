package com.rakov.saldo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.rakov.saldo.model.Lemgram;



public interface SaldoService {
	public String[] split(String word, String pos);
	HashMap<Boolean, HashMap<String, ArrayList<Lemgram>>> isSemanticCompound(String word, String[] segs, String pos);
}
