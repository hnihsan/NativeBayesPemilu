
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

package NLP_ITB.POSTagger.HMM.Decoder;

import NLP_ITB.POSTagger.HMM.Model.*;
import NLP_ITB.POSTagger.HMM.WordProb.*;
import NLP_ITB.POSTagger.HMM.NGram.*;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BigramSucceedDecoder extends Decoder
{
	public BigramSucceedDecoder(Model model, WordProb WH, NGramProb NG, double beamFactor, boolean debug) 
	{
		super(model, WH, NG, beamFactor, debug);
	}
	
	public Sequence backtrack(List<List<MatrixEntryBigram>> tagMatrix, Model model)
	{
		double highestProb = Double.NEGATIVE_INFINITY;
		
		List<MatrixEntryBigram> lastColumn = tagMatrix.get(tagMatrix.size() - 1); 
		MatrixEntryBigram tail = null;
		MatrixEntryBigram beforeTail = null;
		
		for (MatrixEntryBigram entry: lastColumn) 
		{
			//System.out.println("---"+entry.tag+"---");
			if (entry.probs > highestProb) 
			{
				tail = entry;
				beforeTail = entry.bps;
			}
		}
		
		//System.out.println("probs : "+tail.probs);
		
		List<Integer> tagSequence = new ArrayList<Integer>(tagMatrix.size());
		
		for (int i = 0; i < tagMatrix.size(); i++) 
		{
			tagSequence.add(tail.tag);			
			//System.out.println("---"+tail.tag+"---");
			if (beforeTail != null) 
			{
				tail = beforeTail;
				beforeTail = tail.bps;
			}
		}

		Collections.reverse(tagSequence);
		
		return new Sequence(tagSequence, highestProb, model);		
	}
	
	public List<List<MatrixEntryBigram>> viterbi(List<String> sentence)
	{
		List<List<MatrixEntryBigram>> tagMatrix = new ArrayList<List<MatrixEntryBigram>>(sentence.size());
		
		int startTag = this.model.getTagNumbers().get(sentence.get(1));
		
		/* PREPARE
		 */
		MatrixEntryBigram firstEntry = new MatrixEntryBigram(startTag);
		tagMatrix.add(new ArrayList<MatrixEntryBigram>(1));
		tagMatrix.get(0).add(new MatrixEntryBigram(startTag));
		tagMatrix.get(0).get(0).probs = 0.0;
		tagMatrix.get(0).get(0).bps = null;
		 
		//MatrixEntryBigram prev = firstEntry;
		
		/*
		 * NOT YET USING BEAM SEARCh => nanti klo dirasa ukuran hidden state udah banyak, more than 30 maybe..
		 */
		
		for (int i = 2; i < sentence.size(); i++) 
		{
			tagMatrix.add(new ArrayList<MatrixEntryBigram>());
			
			int sepIndexCurr = sentence.get(i).lastIndexOf('/');
			int sepIndexSucc = -2;
			if (i < sentence.size()-1)
				sepIndexSucc = sentence.get(i+1).lastIndexOf('/');
				
			if (sepIndexCurr == -1 || sepIndexSucc == -1)
			{
				//continue;
				System.out.println("Error BigramSucceedDecoder: curr/succeed-ing POS tag missing..");
				System.exit(1);
			}

			String word = sentence.get(i).substring(0, sepIndexCurr);
			String Succeedtag = null;
			if (sepIndexSucc != -2)
				Succeedtag = sentence.get(i+1).substring(sepIndexSucc + 1);
			
			if (super.debug) System.out.println("###");
			for (Entry<Integer, Double> tagEntry: this.WH.tagProbs(word).entrySet()) 
			{
				MatrixEntryBigram newEntry = new MatrixEntryBigram(tagEntry.getKey());
				double highestProb = Double.NEGATIVE_INFINITY;
				MatrixEntryBigram maxEntry = null;
				
				/**
				 */
				//System.out.print("=> "+tagEntry.getKey()+"|"+tagEntry.getValue()+" ");
				/*
				if (word.equals("oposisi"))
				{
					System.out.print("=> "+tagEntry.getKey()+"|"+tagEntry.getValue()+" ");
				}
				*/
				
				/*
				 * LOOPING for each element di PREV yang membuat MAX
				 **/
				if (super.debug) System.out.println("@cari max prev:");
				for (MatrixEntryBigram t: tagMatrix.get(i - 2))
				{
					double prob = 0.0f;
					
					if((i < sentence.size()-1) && (this.WH.isOOV(word)))
					{
						int succ = this.model.getTagNumbers().get(Succeedtag);
						TriGram tg = new TriGram(t.tag, tagEntry.getKey(), succ);
						double trigramSuccedProb = this.NG.TrigramProbSucceed(tg);
						prob = trigramSuccedProb + tagEntry.getValue() + t.probs;
						
						//System.out.println("cari kebelakang "+word+" "+t.tag+" "+tagEntry.getKey()+" "+succ+" "+trigramSuccedProb);
					}
					else
					{
						BiGram bg = new BiGram(t.tag, tagEntry.getKey());
						double bigramProb = this.NG.BigramProb(bg);
						prob = bigramProb + tagEntry.getValue() + t.probs;
					}
					
					
					if (prob > highestProb)
					{
						highestProb = prob;
						maxEntry = t;
						newEntry.probs = prob;
					}
					
					//if (super.debug) System.out.println("BigramDecode : "+sentence.get(i)+"\nTag:"+tagEntry.getKey()+"\nnow:"+prob+" prev:"+t.probs+" "+bigramProb+" "+tagEntry.getValue());
				}
				//System.out.println("probsss :"+word+" "+highestProb);
				
				newEntry.bps = maxEntry;
				
				tagMatrix.get(i-1).add(newEntry);
			}
			
			if (super.debug) System.out.println("\n");
			//prev = maxEntry;
			//System.out.println(prev.tag+"@");
			//System.out.print("\n");
		}
		
		return tagMatrix;
	}
}

