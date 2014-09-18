package com.rakov.saldo.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.rakov.saldo.model.SemanticCompoundSupport;
import com.rakov.saldo.service.SaldoService;
import com.rakov.saldo.serviceimpl.SaldoServiceImpl;

public class SaldoTest {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		SaldoService saldServ = new SaldoServiceImpl();
		HashMap<Integer, String[]> res = null;
		int c = 0;
		List<SemanticCompoundSupport> resSC = null;
		try {

			String line = br.readLine();

			while (line != null) {
				if (line.contains("#")) {
					String[] tempStr = line.split(" ");
					res = saldServ.split(tempStr[1], "nn");
					if (res != null) {
						resSC = saldServ.isSemanticCompound(res, "nn",
								tempStr[1]);
					}
					c = 0;
				} else {
					SemanticCompoundSupport result = null;
					if (resSC != null) {
						result = resSC.get(c);
					}
					String input = line.replaceAll("[u'", "");
					input = input.replaceAll("u'", "");
					input = input.replaceAll("'", "");
					String[] inputFlag = input.split("]");
					switch (inputFlag[1]) {
					case "None":
						if (result == null&&res!=null) {
							String []pythonParts=inputFlag[0].split(",");
							String []javaParts=res.get(c);
							boolean r=true;
							for(int i=0;i<javaParts.length;i++){
								if(r){
									r=pythonParts[i].equals(javaParts[i]);
								}
								
							}
							if(r){
								System.out.println("Ok");
							}else{
								System.out.println(result.toString() + line);
							}
						} else {
							System.out.println(result.toString() + line);
						}
						c++;
						break;
					case "True":
						if (result.isFlagIsComp()) {
							String []pythonParts=inputFlag[0].split(",");
							String []javaParts=result.getParts();
							boolean r=true;
							for(int i=0;i<javaParts.length;i++){
								if(r){
									r=pythonParts[i].equals(javaParts[i]);
								}
								
							}
							if(r){
								System.out.println("Ok");
							}else{
								System.out.println(result.toString() + line);
							}
						}else{
							System.out.println(result.toString() + line);
						}
						c++;
						break;
					case "False":
						if (!result.isFlagIsComp()) {
							String []pythonParts=inputFlag[0].split(",");
							String []javaParts=result.getParts();
							boolean r=true;
							for(int i=0;i<javaParts.length;i++){
								if(r){
									r=pythonParts[i].equals(javaParts[i]);
								}
								
							}
							if(r){
								System.out.println("Ok");
							}else{
								System.out.println(result.toString() + line);
							}

						}else{
							System.out.println(result.toString() + line);
						}
						c++;
						break;

					}

				}
				line = br.readLine();

			}

		} finally {
			br.close();
		}
	}

}
