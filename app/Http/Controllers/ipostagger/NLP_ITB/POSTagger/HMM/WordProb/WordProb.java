
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

import NLP_ITB.POSTagger.HMM.Model.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
 
public abstract class WordProb
{
	protected Map<String, Map<Integer, Double>> wordTagProbs;
	protected OOVWordProb OOVWord;
	protected Map<Integer, String> NumberTags;
	protected int TagCount;
	protected boolean debug;
	protected DicLexicon DL;
	
	public WordProb(boolean debug)
	{
		//for OOV case
		this.debug = debug;
	}
	
	public WordProb(Map<String, Map<Integer, Integer>> wordTagFreqs, Map<UniGram, Integer> uniGramFreqs, 
			Map<Integer, String> NumberTags, OOVWordProb OOV, DicLexicon dl, boolean debug) 
	{
		this.wordTagProbs = new HashMap<String, Map<Integer,Double>>();
		this.debug = debug;
		this.NumberTags = NumberTags;
		this.TagCount = uniGramFreqs.size();	
		this.OOVWord = OOV;
		this.DL = dl;
		
		sumWordTagProbs(wordTagFreqs, uniGramFreqs);
	}
	
	public boolean isOOV(String word)
	{
		boolean oov = false;
		
		if (!this.wordTagProbs.containsKey(word))
		{
			oov = true;
		}
		
		return oov;
	}
	
	protected void sumWordTagProbs(Map<String, Map<Integer, Integer>> wordTagFreqs, Map<UniGram, Integer> uniGramFreqs) 
	{
		for (Entry<String, Map<Integer, Integer>> wordEntry: wordTagFreqs.entrySet()) 
		{
			String word = wordEntry.getKey();
			//System.out.println(word+"\n");
			
			if (!this.wordTagProbs.containsKey(word))
			{
				this.wordTagProbs.put(word, new HashMap<Integer, Double>());
			}

			for (Entry<Integer, Integer> tagEntry: wordEntry.getValue().entrySet()) 
			{
				Integer tag = tagEntry.getKey();
				double freq = (double)tagEntry.getValue();
				double p = 0.0;
				
				/* P(w|t) = f(w,t) / f(t)
				 * 
				 * This bellow operation needs HASH_CODE dari CLASS UniGRAM
				 */
				if (this.debug)
					System.out.println("WordProb : "+word+" "+freq+" "+uniGramFreqs.get(new UniGram(tag))+ "#");

				p = Math.log(freq / (double)uniGramFreqs.get(new UniGram(tag))); 
				this.wordTagProbs.get(word).put(tag, p);
			}
			
		}
	}
	
	abstract public Map<Integer, Double> tagProbs(String word);
	
}
