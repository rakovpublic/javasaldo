package com.rakov.saldo.service;



public interface SaldoService {
	public String[] split(String word, String pos);
	boolean isSemanticCompound(String word, String[] segs, String pos);
}
