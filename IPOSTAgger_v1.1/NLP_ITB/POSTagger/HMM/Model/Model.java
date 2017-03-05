
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
 
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;

public class Model 
{
	private Map<String, Map<Integer, Integer>> wordTagFreqs;
	private Map<String, Integer> tagNumbers;
	private Map<Integer, String> numberTags;
	private Map<UniGram, Integer> uniGramFreqs;
	private Map<BiGram, Integer> biGramFreqs;
	private Map<TriGram, Integer> triGramFreqs;
	private Map<QuatoGram, Integer> quatoGramFreqs;
	
	public Model(BufferedReader wordTagFreqReader, BufferedReader nGramReader) throws IOException
	{
		NGram nGrams = NGram.readNGrams(nGramReader);
		WordFreq wordTagFreqs = WordFreq.readWordTagFreq(wordTagFreqReader, nGrams.getTagNumber());
		
		for (Entry<String, Map<Integer, Integer>> lexiconEntry: wordTagFreqs.getWordTagFreq().entrySet()) 
		{
			wordTagFreqs.getWordTagFreq().put(lexiconEntry.getKey(), Collections.unmodifiableMap(lexiconEntry.getValue()));
		}
		
		this.wordTagFreqs = Collections.unmodifiableMap(wordTagFreqs.getWordTagFreq());
		
		this.tagNumbers = Collections.unmodifiableMap(nGrams.getTagNumber());
		this.numberTags = Collections.unmodifiableMap(nGrams.getNumberTag());
		this.uniGramFreqs = Collections.unmodifiableMap(nGrams.getUniGramFreq());
		this.biGramFreqs = Collections.unmodifiableMap(nGrams.getBiGramFreq());
		this.triGramFreqs = Collections.unmodifiableMap(nGrams.getTriGramFreq());
		this.quatoGramFreqs = Collections.unmodifiableMap(nGrams.getQuatoGramFreq());
	}
	
	public Map<BiGram, Integer> getBiGrams() 
	{
		return this.biGramFreqs;
	}

	public Map<String, Map<Integer, Integer>> getLexicon() 
	{
		return this.wordTagFreqs;
	}
	
	public Map<Integer, String> getNumberTags() 
	{
		return this.numberTags;
	}
	
	public Map<String, Integer> getTagNumbers() 
	{
		return this.tagNumbers;
	}

	public Map<TriGram, Integer> getTriGrams() 
	{
		return this.triGramFreqs;
	}
	
	public Map<QuatoGram, Integer> getQuatoGrams()
	{
		return this.quatoGramFreqs;
	}
	
	public Map<UniGram, Integer> getUniGrams() 
	{
		return this.uniGramFreqs;
	}
}
