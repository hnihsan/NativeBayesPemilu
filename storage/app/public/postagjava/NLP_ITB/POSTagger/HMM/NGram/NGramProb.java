
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
 
package NLP_ITB.POSTagger.HMM.NGram;
 
import NLP_ITB.POSTagger.HMM.Model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LANGUAGE MODEL = NGram !!!
 */
public class NGramProb 
{
	private Map<UniGram, Integer> uniGramFreqs;
	private Map<BiGram, Integer> biGramFreqs;
	private Map<TriGram, Integer> triGramFreqs;
	private Map<QuatoGram, Integer> quatoGramFreqs;
	private Smoother sm;
	
	public NGramProb(Map<UniGram, Integer> uniGramFreqs,
			         Map<BiGram, Integer> biGramFreqs,
			         Map<TriGram, Integer> triGramFreqs,
			         Map<QuatoGram, Integer> quatoGramFreqs,
			         double BigramLambda)
	{
		this.uniGramFreqs = new HashMap<UniGram, Integer>(uniGramFreqs);
		this.biGramFreqs = new HashMap<BiGram, Integer>(biGramFreqs);
		this.triGramFreqs = new HashMap<TriGram, Integer>(triGramFreqs);
		this.quatoGramFreqs = new HashMap<QuatoGram, Integer>(quatoGramFreqs);
		
		this.sm = new Smoother(this.uniGramFreqs, this.biGramFreqs, 
					this.triGramFreqs, this.quatoGramFreqs, BigramLambda);
	}
	
	public double UnigramProb(UniGram u)
	{
		return this.sm.uniGramProb(u);
	}
	
	public double BigramProb(BiGram b)
	{
		return this.sm.biGramProb(b);
	}
	
	public double TrigramProb(TriGram t)
	{
		return this.sm.triGramProb(t);
	}
	
	public boolean isBigramExist(BiGram b)
	{
		return this.biGramFreqs.containsKey(b);
	}
	
	public boolean isTrigramExist(TriGram t)
	{
		return this.triGramFreqs.containsKey(t);
	}
	
	// P(t2|t1,t3) = 2 PHASE HMM, can look succeeding POS Tag
	public double TrigramProbSucceed(TriGram t)
	{
		return this.sm.triGramProbSucceed(t);
	}
	
	// P(t3|t1,t2,t4) = 2 PHASE HMM, can look succeeding POS Tag
	public double QuatogramProbSucceed(QuatoGram t)
	{
		return this.sm.quatoGramProbSucceed(t);
	}
}
