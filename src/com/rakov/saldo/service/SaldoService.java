package com.rakov.saldo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.model.SemanticCompoundSupport;



public interface SaldoService {
	public HashMap<Integer, String[]> split(String word, String pos);
	/*check if it right and rewrite if not*/
	List<SemanticCompoundSupport> isSemanticCompound( HashMap<Integer, String[]> sRes, String pos, String word);
}
