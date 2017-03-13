
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

package NLP_ITB.POSTagger.HMM.Train;
 
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TrainHandler 
{
	private Map<String, Map<String, Integer>> lexicon;
	private Map<String, Integer> unigrams;
	private Map<String, Integer> bigrams;
	private Map<String, Integer> trigrams;
	private Map<String, Integer> quatograms;
	
	/*
	 * Constructor
	 */
	public TrainHandler() 
	{
		this.lexicon = new HashMap<String, Map<String,Integer>>();
		this.unigrams = new HashMap<String, Integer>();
		this.bigrams = new HashMap<String, Integer>();
		this.trigrams = new HashMap<String, Integer>();
		this.quatograms = new HashMap<String, Integer>();
	}
	
	public Map<String, Integer> getBigram() 
	{
		return this.bigrams;
	}
	
	public Map<String, Map<String, Integer>> getLexicon() 
	{
		return this.lexicon;
	}
	
	public Map<String, Integer> getQuatogram()
	{
		return this.quatograms;
	}
	
	public Map<String, Integer> getTrigram() 
	{
		return this.trigrams;
	}

	public Map<String, Integer> getUnigram() 
	{
		return this.unigrams;
	}
	
	public void handleSentence(List<TaggedWord> sentence) 
	{
		for (int i = 0; i < sentence.size(); ++i) 
		{
			addLexiconEntry(sentence.get(i));
			addUniGram(sentence, i);
			if (i > 0)
			{
				addBiGram(sentence, i);
			}
			if (i > 1) 
			{
				addTriGram(sentence, i);
				if (i < sentence.size()-1)
				{
					addQuatoGram(sentence, i);
				}
			}
		}
	}
	
	private void addLexiconEntry(TaggedWord w) 
	{
		String word = w.getWord();
		String tag = w.getTag();
		
		if (!this.lexicon.containsKey(word))
		{
			this.lexicon.put(word, new HashMap<String, Integer>());
		}
		
		if (!this.lexicon.get(word).containsKey(tag))
		{
			this.lexicon.get(word).put(tag, 1);
		}
		else
		{
			this.lexicon.get(word).put(tag, this.lexicon.get(word).get(tag) + 1);			
		}
	}
	
	private void addUniGram(List<TaggedWord> sentence, int index) 
	{
		String unigram = sentence.get(index).getTag();
		
		if (!this.unigrams.containsKey(unigram))
		{
			this.unigrams.put(unigram, 1);
		}
		else
		{
			this.unigrams.put(unigram, this.unigrams.get(unigram) + 1);
		}
	}
	
	private void addBiGram(List<TaggedWord> sentence, int index) 
	{
		String bigram = sentence.get(index - 1).getTag() + " " + sentence.get(index).getTag();
		
		if (!this.bigrams.containsKey(bigram))
		{
			this.bigrams.put(bigram, 1);
		}
		else
		{
			this.bigrams.put(bigram, this.bigrams.get(bigram) + 1);
		}
	}

	private void addTriGram(List<TaggedWord> sentence, int index) 
	{
		String trigram = sentence.get(index - 2).getTag() + " " + sentence.get(index - 1).getTag() + " " + sentence.get(index).getTag();
		
		if (!this.trigrams.containsKey(trigram))
		{
			this.trigrams.put(trigram, 1);
		}
		else
		{
			this.trigrams.put(trigram, this.trigrams.get(trigram) + 1);
		}
	}
	
	// for 2-PASS HMM	
	private void addQuatoGram(List<TaggedWord> sentence, int index)
	{
		String quatogram = sentence.get(index-2).getTag() + " " + sentence.get(index-1).getTag() + " " + 
				   sentence.get(index).getTag() + " " + sentence.get(index+1).getTag();
		
		if (!this.quatograms.containsKey(quatogram))
		{
			this.quatograms.put(quatogram, 1);
		}
		else
		{
			this.quatograms.put(quatogram, this.quatograms.get(quatogram) + 1);
		}
	}
}
