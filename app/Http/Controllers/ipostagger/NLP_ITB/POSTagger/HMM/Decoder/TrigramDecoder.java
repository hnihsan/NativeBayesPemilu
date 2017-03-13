
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

public class TrigramDecoder extends Decoder
{
	public TrigramDecoder(Model model, WordProb WH, NGramProb NG, double beamFactor, boolean debug) 
	{
		super(model, WH, NG, beamFactor, debug);
		this.jumTrigram = 0;
		this.jumTrigramOOV = 0;
	}
	
	//data member
	public int jumTrigram;
	public int jumTrigramOOV;
	
	public Sequence backtrack(List<List<MatrixEntryTrigram>> tagMatrix, Model model)
	{
		// Find the most probably final state.
		double highestProb = Double.NEGATIVE_INFINITY;
		MatrixEntryTrigram tail = null;
		MatrixEntryTrigram beforeTail = null;
		
		List<MatrixEntryTrigram> lastColumn = tagMatrix.get(tagMatrix.size() - 1);
		
		// Find the most probable state in the last column.
		for (MatrixEntryTrigram entry: lastColumn) 
		{
			for (Map.Entry<MatrixEntryTrigram, Double> probEntry: entry.probs.entrySet()) 
			{
				if (probEntry.getValue() > highestProb) 
				{
					highestProb = probEntry.getValue();
					tail = entry;
					beforeTail = probEntry.getKey();
				}
			}
		}
		
		List<Integer> tagSequence = new ArrayList<Integer>(tagMatrix.size());
		
		for (int i = 0; i < tagMatrix.size(); ++i) 
		{
			tagSequence.add(tail.tag);			
			
			if (beforeTail != null) 
			{
				MatrixEntryTrigram tmp = tail.bps.get(beforeTail);
				tail = beforeTail;
				beforeTail = tmp;
			}
		}

		Collections.reverse(tagSequence);
		
		return new Sequence(tagSequence, highestProb, model);		
	}
	
	public List<List<MatrixEntryTrigram>> viterbi(List<String> sentence)
	{
		List<List<MatrixEntryTrigram>> tagMatrix = new ArrayList<List<MatrixEntryTrigram>>(sentence.size());

		int startTag = this.model.getTagNumbers().get(sentence.get(0));

		// Prepare initial matrix entries;
		MatrixEntryTrigram firstEntry = new MatrixEntryTrigram(startTag);
		tagMatrix.add(new ArrayList<MatrixEntryTrigram>(1));
		tagMatrix.get(0).add(firstEntry);
	
		tagMatrix.add(new ArrayList<MatrixEntryTrigram>(1));
		tagMatrix.get(1).add(new MatrixEntryTrigram(startTag));
		tagMatrix.get(1).get(0).probs.put(firstEntry, 0.0);
		tagMatrix.get(1).get(0).bps.put(firstEntry, null);
		
		double beam = 0.0;
		
		// Loop through the tokens.
		for (int i = 2; i < sentence.size(); ++i) {
			double columnHighestProb = Double.NEGATIVE_INFINITY;

			tagMatrix.add(new ArrayList<MatrixEntryTrigram>());

			for (Entry<Integer, Double> tagEntry:
					this.WH.tagProbs(sentence.get(i)).entrySet()) {
				MatrixEntryTrigram newEntry = new MatrixEntryTrigram(tagEntry.getKey());
				
				/*
				if (sentence.get(i).equals("Imigrasi"))
				{
					System.out.println("prob :"+tagEntry.getKey()+" "+tagEntry.getValue());
				}*/
				
				// Loop over all possible trigrams
				for (MatrixEntryTrigram t2: tagMatrix.get(i - 1)) { //t2
					double highestProb = Double.NEGATIVE_INFINITY;
					MatrixEntryTrigram highestProbBp = null;

					for (Map.Entry<MatrixEntryTrigram, Double> t1Entry: t2.probs.entrySet()) {  // t1entry

						if (t1Entry.getValue() < beam)
							continue;
						
						TriGram curTriGram = new TriGram(t1Entry.getKey().tag, t2.tag,
							tagEntry.getKey());
						
						this.jumTrigram++;	
						if (!this.NG.isTrigramExist(curTriGram))
							this.jumTrigramOOV++;

						double triGramProb = this.NG.TrigramProb(curTriGram);
						double prob = triGramProb + tagEntry.getValue() + t1Entry.getValue();

						if (prob > highestProb)
						{
							highestProb = prob;
							highestProbBp = t1Entry.getKey();
						}
					}
					
					newEntry.probs.put(t2, highestProb);
					newEntry.bps.put(t2, highestProbBp);

					if (highestProb > columnHighestProb)
						columnHighestProb = highestProb;
				}

				tagMatrix.get(i).add(newEntry);
			}
			
			beam = columnHighestProb - this.beamFactor;
		}
		
		return tagMatrix;
	}
}

