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
		BufferedReader br = new BufferedReader(new FileReader(
				"/home/rakovskyi/workfile.txt"));
		SaldoService saldServ = new SaldoServiceImpl();
		HashMap<Integer, String[]> res = null;
		int c = 0;
		int errors=0;
		int rights=0;
		List<SemanticCompoundSupport> resSC = null;
		try {

			String line = br.readLine();
			String word = null;
			while (line != null) {
				if (line.contains("#1")) {
					String[] tempStr = line.split("@");
					word = tempStr[1];
					c = 0;
				} else {
					if (line.contains("#2")) {
						String[] tempStr = line.split("@");
						if (word != null)
							res = saldServ
									.split(word, tempStr[1].toLowerCase());
						if (res != null) {
							resSC = saldServ.isSemanticCompound(res,
									tempStr[1].toLowerCase(), word);
						}
					} else {
						SemanticCompoundSupport result = null;
						if (resSC != null) {
							try {
								result = resSC.get(c);
							} catch (IndexOutOfBoundsException e) {
								System.out.println(resSC.toString() + c + line);

							}
						}
						String input = line.replaceAll("\\[u\\'", "");
						input = input.replaceAll("u\\'", "");
						input = input.replaceAll("\\'", "");
						input = input.replaceAll(" ", "");
						String[] inputFlag = input.split("\\]");
						String tempStr = null;
						tempStr = inputFlag[1];
						
						switch (tempStr) {
						case "None":
							if (result == null && res != null) {
								String[] pythonParts = inputFlag[0].split(",");
								String[] javaParts = res.get(c);
								boolean r = true;
								for (int i = 0; i < javaParts.length; i++) {
									if (r) {
										r = pythonParts[i].equals(javaParts[i]);
									}

								}
								if (r) {
									System.out.println("Ok");
									rights++;
								} else {
									System.out.println(word+line);
									errors++;
								}
							} else {
								System.out.println(word +" "+result.toString() + line);
								errors++;
							}
							c++;
							break;
						case "True":
							boolean result2=false;
							try{
								 result2= result.isFlagIsComp();
							}
							catch(NullPointerException e){
								System.out.println("none"+line);
							}
							if (result2) {
									String[] pythonParts = inputFlag[0]
											.split(",");
									String[] javaParts = result.getParts();
									boolean r = true;
									for (int i = 0; i < javaParts.length; i++) {
										if (r) {
											r = pythonParts[i]
													.equals(javaParts[i]);
										}

									}
									if (r) {
										System.out.println("Ok");
										rights++;
									} else {
										System.out.println(word +" "+result.toString()
												+ line);
										errors++;
									}
								} else {
									System.out
											.println("Error "+word+ line);
									errors++;
								}
							
							c++;
							break;
						case "False":
							boolean result3=true;
							try{
								 result3= result.isFlagIsComp();
							}
							catch(NullPointerException e){
								System.out.println("none "+line);
							}
							if (!result3) {
								String[] pythonParts = inputFlag[0].split(",");
								String[] javaParts = result.getParts();
								boolean r = true;
								for (int i = 0; i < javaParts.length; i++) {
									if (r) {
										r = pythonParts[i].equals(javaParts[i]);
									}

								}
								if (r) {
									System.out.println("Ok");
									rights++;
								} else {
									System.out
											.println(word +" "+result.toString() + line);
									errors++;
								}

							} else {
								System.out.println("Error "+word+ line);
								errors++;
							}
							c++;
							break;

						}

					}
				}
				line = br.readLine();

			}

		} finally {
			System.out.println("Right results:" +rights+"Errors:"+errors);
			br.close();
		}
	}

}
