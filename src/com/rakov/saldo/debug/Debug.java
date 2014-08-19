package com.rakov.saldo.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.rakov.saldo.dao.LemgramDAO;
import com.rakov.saldo.daoimpl.LemgramDaoImpl;
import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.service.SaldoService;
import com.rakov.saldo.serviceimpl.SaldoServiceImpl;

public class Debug {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*LemgramDAO lemd= new LemgramDaoImpl();
		List<Lemgram> llem= lemd.getLemgramByGf("kändisgala");
		for(int i=0;i<llem.size();i++){
			System.out.println(llem.get(i).toString());
		}
		System.out.println("End of job");*/
		SaldoService saldServ= new SaldoServiceImpl();//ä
		String [] res=saldServ.split("glasskål", "nn");
		for(int i=0;i<res.length;i++){
			System.out.println(res[i]);
			
		}
		HashMap<Boolean, HashMap<String, ArrayList<Lemgram>>> result = saldServ.isSemanticCompound("glasskål", res, "nn");
		Set<Boolean> res1= result.keySet();
		Iterator<Boolean> iter=res1.iterator();
		while(iter.hasNext())
		{
			boolean val =  iter.next();
			System.out.println(val+"");
			 HashMap<String, ArrayList<Lemgram>> ancestors= result.get(val);
			 Set<String> res2=ancestors.keySet();
			 Iterator<String> iterat=res2.iterator();
			 while(iterat.hasNext()){
				 String val2=iterat.next();
				 System.out.println(val2);
				 ArrayList<Lemgram> ancestorsRes= ancestors.get(val2);
				 for(int i=0;i<ancestorsRes.size();i++)
				 {
					 System.out.println(ancestorsRes.get(i).toString());
				 }
				 
				 
			 }
		}
		System.out.println("End of job");
		

	}

}
