package com.rakov.saldo.debug;

import java.util.List;

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
		List<Lemgram> llem= lemd.getLemgramByGf("el");
		for(int i=0;i<llem.size();i++){
			System.out.println(llem.get(i).toString());
		}
		System.out.println("End of job");*/
		SaldoService saldServ= new SaldoServiceImpl();
		String [] res=saldServ.split("actionfilm", "nn");
		for(int i=0;i<res.length;i++){
			System.out.println(res[i]);
			
		}
		System.out.println("End of job");
		

	}

}
