package com.rakov.saldo.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.rakov.saldo.dao.LemgramDAO;
import com.rakov.saldo.daoimpl.LemgramDaoImpl;
import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.model.SemanticCompoundSupport;
import com.rakov.saldo.service.SaldoService;
import com.rakov.saldo.serviceimpl.SaldoServiceImpl;

public class Debug {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//kändisgala glasskål
		/*LemgramDAO lemd= new LemgramDaoImpl();
		List<Lemgram> llem= lemd.getSense("glas..1");
		for(int i=0;i<llem.size();i++){
			System.out.println(llem.get(i).toString());
		}*/
		System.out.println("End of job");
		SaldoService saldServ= new SaldoServiceImpl();//ä
		HashMap<Integer,String[]> res= new HashMap<Integer,String[]>();
		res=saldServ.split("glasskål", "nn");
		/*if(res!=null){
		for(int i=0;i<res.size();i++){
			String[] tres=res.get(i);
			for(int j=0;j<tres.length;j++){
				System.out.println(tres[j]);	
			}
			
			
		}}*/
	    List<SemanticCompoundSupport>resSC= saldServ.isSemanticCompound(res, "nn", "glasskål");
	    for(int i=0; i<resSC.size();i++)
	    {
	    	System.out.println(resSC.get(i).isFlagIsComp());
	    	 String[] parts= resSC.get(i).getParts();
	    	 for(int j=0; j<parts.length;j++)
	    	 {
	    		 System.out.println(parts[j]);
	    	 }
	    	 
	    }
		System.out.println("End of job");
		

	}

}
