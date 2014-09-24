package com.rakov.saldo.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.rakov.saldo.model.TestSupport;

public class InputParse {

	public static void main(String[] args)  {
		try{
		BufferedReader br = new BufferedReader(new FileReader("/home/rakovskyi/Combined_English.txt"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("/home/rakovskyi/input.txt"));
		String line = br.readLine();
		TestSupport sup= null;
		while(line!=null){
		    if(line.contains(" ")&&line!=""){
			String [] parts=line.split(" ");
			if(parts.length>0)
			{
			switch (parts[0]){
			case "#01":
				sup=new TestSupport();
				if(parts[1].contains("~"))
				{
				sup.setWord(parts[1].replace("~",""));
				}
				break;
			case "#32":
				if(sup.getWord()!=null&&parts.length>1&&sup.getWord()!=null){
				
				sup.setPos(parts[1].replace(".", ""));
				bw.write(sup.getWord()+"\n");
				bw.write(sup.getPos()+"\n");
				}
				break;
				
			}
			}
		    }
			line = br.readLine();
		}
		br.close();
		bw.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		
	}

}
