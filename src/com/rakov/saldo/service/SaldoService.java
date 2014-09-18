package com.rakov.saldo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.model.SemanticCompoundSupport;

public interface SaldoService {
	/*
	 * This method returns null if cann't split this word or
	 *  HashMap<Integer, String[]> where int is number of split and string [] is split
	 */
	public HashMap<Integer, String[]> split(String word, String pos);

	/*This method returns null if cann't find full word in dictionary or boolean which shoes the quality of splits
	 * */
	public List<SemanticCompoundSupport> isSemanticCompound(
			HashMap<Integer, String[]> sRes, String pos, String word);
}
