
/*=======================================================================*
 * Copyright 2009-2010		                                             *
 * Alfan Farizki Wicaksono			                                     *
 * Institute of Technology Bandung, INDONESIA                            *
 *																	     *
 * This program is free software; you can redistribute it and/or modify  *
 * it under the terms of the GNU General Public License as published by  *
 * the Free Software Foundation; either version 2 of the License, or     *
 * (at your option) any later version.                                   *
 * 																		 *
 * This program is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 * GNU General Public License for more details.                          *
 *                                                                       *
 *=======================================================================*/

package NLP_ITB.POSTagger.HMM.WordProb;
 
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Class For Containing Dictionary Lexicon from Kamus Besar Bahasa Indonesia and Kateglo
 * also Class for containing category table
 */

public class DicLexicon 
{
	//Properti
	private Map<String, List<String>> lexicon;
	private Map<String, List<String>> catTable;
	private Map<Integer, String> numberTags;
	
	//just for testing
	public int jumAccess;
	
	//Constructor
	public DicLexicon(String namaFileKamus, String namaFileTable, Map<Integer, String> numberTags)
	{
		this.catTable = new HashMap<String, List<String>>();
		this.lexicon = new HashMap<String, List<String>>();
		this.loadCatTable(namaFileTable);
		this.loadDicLexicon(namaFileKamus);
		this.numberTags = numberTags;
		this.jumAccess = 0;
	}
	
	public Map<Integer, Double> usingLexicon(String word, Map<Integer, Double> initVector)
	{
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		List<String> temp = new ArrayList<String>();
		
		List<String> poslex = this.getPosLexicon(word);
		if (poslex != null)
		{
			for (int i = 0; i < poslex.size(); i++)
			{
				List<String> sub = this.getSubCat(poslex.get(i));
				for (int j = 0; j < sub.size(); j++)
				{
					temp.add(sub.get(j));
				}
			}
		}
		
		for (Entry<Integer, Double> e: initVector.entrySet()) 
		{
			if (this.isInList(this.numberTags.get(e.getKey()), temp))
			{
				ret.put(e.getKey(), e.getValue());
			}
		}
		
		if (ret.size() == 0) 
			return initVector;
		
		return ret;
	}
	
	public List<String> getPosLexicon(String word)
	{
		List<String> ret = null;
		if (this.lexicon.containsKey(word))
		{
			ret = this.lexicon.get(word);
			this.jumAccess++;
		}
		
		return ret;
	}
	
	public List<String> getSubCat(String dicpos)
	{
		List<String> ret = null;
		if (this.catTable.containsKey(dicpos))
		{
			ret = this.catTable.get(dicpos);
		}
		
		return ret;
	}
	
	private boolean isInList(String pos, List<String> L)
	{
		boolean found = false;
		for (int i = 0; i < L.size(); i++)
		{
			if (L.get(i).equals(pos))
			{
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	private void loadDicLexicon(String namaFileKamus)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(namaFileKamus));		
			String line = null;
		
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
			
				if (line.length() <= 1)
					continue;
				
				String[] lineParts = line.split("\\s+");
				
				if (this.catTable.containsKey(lineParts[1]))
				{
					String lex = new String(lineParts[0]);
					List<String> pos = new ArrayList<String>();
					for (int i = 0; i < lineParts.length; i++)
					{
						if (this.catTable.containsKey(lineParts[i]))
							pos.add(lineParts[i]);
					}
					this.lexicon.put(lex, pos);
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("Failed to read Dictionary Lexicon");
			System.exit(1);
		}
	}
	
	private void loadCatTable(String namaFile)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(namaFile));		
			String line = null;
		
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
			
				if (line.length() <= 1)
					continue;
				
				String[] lineParts = line.split("\\s+");
				List<String> subcat = new ArrayList<String>();
				
				for (int i = 1; i < lineParts.length; i++)
				{
					subcat.add(lineParts[i]);
				}
				
				this.catTable.put(lineParts[0], subcat);
			}
		}
		catch (IOException e)
		{
			System.out.println("Failed to read category table");
			System.exit(1);
		}
	}
	
}
