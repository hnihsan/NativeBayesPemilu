
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
 
public class WordFreq
{
	private Map<String, Map<Integer, Integer>> wordTagFreq;
	
	public WordFreq(Map<String, Map<Integer, Integer>> wordTagFreq)
	{
		this.wordTagFreq = wordTagFreq;
	}
	
	public Map<String, Map<Integer, Integer>> getWordTagFreq()
	{
		return this.wordTagFreq;
	}
	
	public static WordFreq readWordTagFreq(BufferedReader reader, Map<String, Integer> tagNumbers) throws IOException 
	{
		Map<String, Map<Integer, Integer>> wordTagFreq = new HashMap<String, Map<Integer, Integer>>();
	
		String line = null;
		while ((line = reader.readLine()) != null) 
		{
			String[] lineParts = line.split("\\s+");
			String word = lineParts[0];
	
			// Make a lexicon entry for this word represented by this line.
			//System.out.println(word+"\n");
			wordTagFreq.put(word, new HashMap<Integer, Integer>());
	
			for (int i = 1; i < lineParts.length; i += 2) 
			{
				int p = Integer.parseInt(lineParts[i + 1]);
				wordTagFreq.get(word).put(tagNumbers.get(lineParts[i]), p);
			}			
		}
	
		return new WordFreq(wordTagFreq);
	}
	
}
