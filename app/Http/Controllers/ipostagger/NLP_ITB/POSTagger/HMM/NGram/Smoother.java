
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

/*
 * Class for smoothing Trigram and Bigram
 * Trigram = Deleted linier interpolation//P(t3|t2,t1,t4)
 * Bigram  = Jelinek - Mercer
 * QuatoGram = P(t3|t1,t2,t4) ; Smoothing = Lidstone's Law
 */
public class Smoother 
{
	private final Map<UniGram, Integer> UniGramFreq;
	private final Map<BiGram, Integer> BiGramFreq;
	private final Map<TriGram, Integer> TriGramFreq;
	private final Map<QuatoGram, Integer> QuatoGramFreq;
	private int corpusSize;
	private double BigramLambda;
	private double d_l1;
	private double d_l2;
	private double d_l3;
	
	private final Map<TriGram, Double> TriGramCache;
	private final Map<BiGram, Double> BiGramCache;
	
	//Constructor
	public Smoother(Map<UniGram, Integer> UniGramFreqs, 
			Map<BiGram, Integer> BiGramFreqs, Map<TriGram, Integer> TriGramFreqs, 
			Map<QuatoGram, Integer> QuatoGramFreqs, double BigramLambda) 
	{
		this.UniGramFreq = new HashMap<UniGram, Integer>(UniGramFreqs);
		this.BiGramFreq = new HashMap<BiGram, Integer>(BiGramFreqs);
		this.TriGramFreq = new HashMap<TriGram, Integer>(TriGramFreqs);
		this.QuatoGramFreq = new HashMap<QuatoGram, Integer>(QuatoGramFreqs);
		this.TriGramCache = new ConcurrentHashMap<TriGram, Double>();
		this.BiGramCache = new ConcurrentHashMap<BiGram, Double>();
		this.BigramLambda = BigramLambda;
		
		this.calculateCorpusSize();
		this.calculateLambdas();
	}
	
	public double uniGramProb(UniGram uniGram)
	{
		//tak perlu cache : cause just a little, tag set
		// Unigram likelihood P(t1)
		UniGram t1 = new UniGram(uniGram.t1());
		double uniGramProb = Math.log(this.UniGramFreq.get(t1) / (double) this.corpusSize);
		
		return uniGramProb;
	}
	
	public double biGramProb(BiGram biGram)
	{
		// If we have cached the likelihood for this bigram, return it.
		if (this.BiGramCache.containsKey(biGram))
			return this.BiGramCache.get(biGram);
			
		// Unigram likelihood P(t2)
		UniGram t2 = new UniGram(biGram.t2());
		double uniGramProb = this.UniGramFreq.get(t2) / (double) this.corpusSize;
		
		// Bigram likelihood P(t2|t1).
		BiGram t1t2 = new BiGram(biGram.t1(), biGram.t2());
		UniGram t1 = new UniGram(biGram.t1());
		double biGramProb = 0.0;
		if (this.UniGramFreq.containsKey(t1) && this.BiGramFreq.containsKey(t1t2))
			biGramProb = this.BiGramFreq.get(t1t2) / (double) this.UniGramFreq.get(t1);
		
		//JELINEC - MERCER
		double prob = Math.log(this.BigramLambda*uniGramProb + (1-this.BigramLambda)*biGramProb);
		
		//CACHE-in
		this.BiGramCache.put(biGram, prob);
		
		return prob;
	}

	public double triGramProb(TriGram triGram) 
	{
		// If we have cached the likelihood for this trigram, return it.
		if (this.TriGramCache.containsKey(triGram))
			return this.TriGramCache.get(triGram);
		
		// Unigram likelihood P(t3)
		UniGram t3 = new UniGram(triGram.t3());
		double uniGramProb = this.UniGramFreq.get(t3) / (double) this.corpusSize;
		
		// Bigram likelihood P(t3|t2).
		BiGram t2t3 = new BiGram(triGram.t2(), triGram.t3());
		UniGram t2 = new UniGram(triGram.t2());
		double biGramProb = 0.0;
		if (this.UniGramFreq.containsKey(t2) && this.BiGramFreq.containsKey(t2t3))
			biGramProb = this.BiGramFreq.get(t2t3) / (double) this.UniGramFreq.get(t2);
		
		// Trigram likelihood P(t3|t1,t2).
		BiGram t1t2 = new BiGram(triGram.t1(), triGram.t2());
		double triGramProb = 0.0;
		if (this.BiGramFreq.containsKey(t1t2) && this.TriGramFreq.containsKey(triGram))
			triGramProb = this.TriGramFreq.get(triGram) / (double) this.BiGramFreq.get(t1t2);
		
		double prob = Math.log(this.d_l1 * uniGramProb + this.d_l2 * biGramProb +
			this.d_l3 * triGramProb);
		
		this.TriGramCache.put(triGram, prob);
		
		return prob;	
	}
	
	public double triGramProbSucceed(TriGram triGram)
	{
		//Count sum of t1,tx,t3
		int B = 0;
		int N = 0;
		int X = 0;
		
		//Check Cache, if exist..then return it
		if (this.TriGramCache.containsKey(triGram))
			return this.TriGramCache.get(triGram);
		
		for (Entry<UniGram, Integer> entry: this.UniGramFreq.entrySet())
		{
			TriGram t1t2t3 = new TriGram(triGram.t1(), entry.getKey().t1(), triGram.t3()); 
							   
			if (this.TriGramFreq.containsKey(t1t2t3))
			{
				B += 1;
				N += this.TriGramFreq.get(t1t2t3);
			}
		}
		
		// Lidstone's law
		// X + 1c / N + Bc
		// X = sum(t1t2t3t4)
		// 0 <= c <= 1
		if (this.TriGramFreq.containsKey(triGram))
		{
			X = this.TriGramFreq.get(triGram);
		}
		
		double prob = 0.00000001f;
		if (N != 0) {
			prob = ((double)X + 0.5) / (double)(N + (0.5 * B));
		}
		
		this.TriGramCache.put(triGram, Math.log(prob));		
		
		return Math.log(prob);
	}
	
	//P(t3|t2,t1,t4)
	//No need cache, because the frequency is very rare...
	public double quatoGramProbSucceed(QuatoGram quatoGram)
	{
		//Count sum of t1,t2,tx,t4
		int B = 0;
		int N = 0;
		int X = 0;
		
		for (Entry<UniGram, Integer> entry: this.UniGramFreq.entrySet())
		{
			QuatoGram t1t2t3t4 = new QuatoGram(quatoGram.t1(), quatoGram.t2(), 
							   entry.getKey().t1(), quatoGram.t4());
							   
			if (this.QuatoGramFreq.containsKey(t1t2t3t4))
			{
				B += 1;
				N += this.QuatoGramFreq.get(t1t2t3t4);
			}
		}
		
		// Lidstone's law
		// X + 1c / N + Bc
		// X = sum(t1t2t3t4)
		// 0 <= c <= 1
		if (this.QuatoGramFreq.containsKey(quatoGram))
		{
			X = this.QuatoGramFreq.get(quatoGram);
		}
		
		double prob = 0.00000001f;
		if (N != 0) {
			prob = ((double)X + BigramLambda) / (double)(N + (BigramLambda * B));
		}
		
		return Math.log(prob);
	}
	
	private void calculateCorpusSize() 
	{
		for (Entry<UniGram, Integer> entry: this.UniGramFreq.entrySet())
			this.corpusSize += entry.getValue();
	}
	
	private void calculateLambdas() 
	{
		int l1f = 0;
		int l2f = 0;
		int l3f = 0;
		
		for (Entry<TriGram, Integer> triGramEntry: this.TriGramFreq.entrySet()) 
		{
			TriGram t1t2t3 = triGramEntry.getKey();
			
			BiGram t1t2 = new BiGram(t1t2t3.t1(), t1t2t3.t2());
			double l3p = 0.0;
			if (this.BiGramFreq.containsKey(t1t2))
				l3p = (triGramEntry.getValue() - 1) / (double) (this.BiGramFreq.get(t1t2) - 1);
		
			BiGram t2t3 = new BiGram(t1t2t3.t2(), t1t2t3.t3());
			UniGram t2 = new UniGram(t1t2t3.t2());
			double l2p = 0.0;
			if (this.UniGramFreq.containsKey(t2) && this.BiGramFreq.containsKey(t2t3))
				l2p = (this.BiGramFreq.get(t2t3) - 1) / (double) (this.UniGramFreq.get(t2) - 1);
			
			UniGram t3 = new UniGram(t1t2t3.t3());
			double l1p = 0.0;
			if (this.UniGramFreq.containsKey(t3))
				l1p = (this.UniGramFreq.get(t3) - 1) / (double) (this.corpusSize - 1);
			
			if (l1p > l2p && l1p > l3p)
			{
				l1f += triGramEntry.getValue();
			}
			else if (l2p > l1p && l2p > l3p)
			{
				l2f += triGramEntry.getValue();
			}
			else
			{
				l3f += triGramEntry.getValue();
			}
		}
		
		double totalTriGrams = l1f + l2f + l3f;
		
		this.d_l1 = l1f / totalTriGrams;
		this.d_l2 = l2f / totalTriGrams;
		this.d_l3 = l3f / totalTriGrams;
	}
}

