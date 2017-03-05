
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

package NLP_ITB.POSTagger.HMM.Model;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
 
public class NGram 
{
	private Map<String, Integer> tagNumbers;
	private Map<Integer, String> numberTags;
	private Map<UniGram, Integer> uniGramFreqs;
	private Map<BiGram, Integer> biGramFreqs;
	private Map<TriGram, Integer> triGramFreqs;
	private Map<QuatoGram, Integer> quatoGramFreqs;
	
	public NGram(Map<String, Integer> tagNumbers, Map<Integer, String> numberTags,
				Map<UniGram, Integer> uniGramFreqs, Map<BiGram, Integer> biGramFreqs,
				Map<TriGram, Integer> triGramFreqs, Map<QuatoGram, Integer> quatoGramFreqs) 
	{
			this.tagNumbers = tagNumbers;
			this.numberTags = numberTags;
			this.uniGramFreqs = uniGramFreqs;
			this.biGramFreqs = biGramFreqs;
			this.triGramFreqs = triGramFreqs;
			this.quatoGramFreqs = quatoGramFreqs;
	}
	
	public Map<String, Integer> getTagNumber()
	{
		return this.tagNumbers;
	}
	
	public Map<Integer, String> getNumberTag()
	{
		return this.numberTags;
	}
	
	public Map<UniGram, Integer> getUniGramFreq()
	{
		return this.uniGramFreqs;
	}
	
	public Map<BiGram, Integer> getBiGramFreq()
	{
		return this.biGramFreqs;
	}
	
	public Map<TriGram, Integer> getTriGramFreq()
	{
		return this.triGramFreqs;
	}
	
	public Map<QuatoGram, Integer> getQuatoGramFreq()
	{
		return this.quatoGramFreqs;
	}
	
	public static NGram readNGrams(BufferedReader reader) throws IOException 
	{
		Map<String, Integer> tagNumbers = new HashMap<String, Integer>();
		Map<Integer, String> numberTags = new HashMap<Integer, String>();
		Map<UniGram, Integer> uniGramFreqs = new HashMap<UniGram, Integer>();
		Map<BiGram, Integer> biGramFreqs = new HashMap<BiGram, Integer>();
		Map<TriGram, Integer> triGramFreqs = new HashMap<TriGram, Integer>();
		Map<QuatoGram, Integer> quatoGramFreqs = new HashMap<QuatoGram, Integer>();
	
		int tagNumber = 0;
		String line = null;
		while ((line = reader.readLine()) != null) 
		{
			String[] lineParts = line.split("\\s+");
	
			int freq = Integer.parseInt(lineParts[lineParts.length - 1]);
	
			if (lineParts.length == 2) 
			{
				tagNumbers.put(lineParts[0], tagNumber);
				numberTags.put(tagNumber, lineParts[0]);
				uniGramFreqs.put(new UniGram(tagNumber), freq);
				++tagNumber;
			}
			else if (lineParts.length == 3)
			{
				biGramFreqs.put(new BiGram(tagNumbers.get(lineParts[0]), tagNumbers.get(lineParts[1])), freq);
			}
			else if (lineParts.length == 4)
			{
				triGramFreqs.put(new TriGram(tagNumbers.get(lineParts[0]), tagNumbers.get(lineParts[1]), tagNumbers.get(lineParts[2])), freq);
			}
			else if (lineParts.length == 5)
			{
				quatoGramFreqs.put(new QuatoGram(tagNumbers.get(lineParts[0]), tagNumbers.get(lineParts[1]), tagNumbers.get(lineParts[2]), tagNumbers.get(lineParts[3])), freq);
			}
		}
	
		return new NGram(tagNumbers, numberTags, uniGramFreqs, biGramFreqs, triGramFreqs, quatoGramFreqs);
	}
}
