
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

public class KnownWordProb extends WordProb
{
	public KnownWordProb(Map<String, Map<Integer, Integer>> wordTagFreqs, Map<UniGram, Integer> uniGramFreqs, 
				Map<Integer, String> NumberTags, OOVWordProb OOV, DicLexicon dl, boolean debug)
	{
		super(wordTagFreqs, uniGramFreqs, NumberTags, OOV, dl, debug);
	}
	
	public Map<Integer, Double> tagProbs(String word) 
	{
		/* Find in the structure..
		 */
		if (this.wordTagProbs.containsKey(word))
		{
			return new HashMap<Integer, Double>(this.wordTagProbs.get(word));
		}
		
		/* IF no OOV Handling
		 */
		if (this.OOVWord == null)
		{
			/* JUST CUPU SAYA !!
			 * ini nanti buat BASELINE
			 */
			HashMap<Integer, Double> wp = new HashMap<Integer, Double>();
			int i = 0;
			for (i = 0; i < this.TagCount; i++)
			{
				if (this.NumberTags.get(i).equals("NN")) {
					wp.put(i, 1.0);
				}
			}
			 
			return wp;
		}
		
		/*BEGIN OOV Handling*/

		/* Maybe Decapitalized form
		 */
		if (Character.isUpperCase(word.charAt(0))) 
		{
			String lowWord = word.toLowerCase();
			if (this.wordTagProbs.containsKey(lowWord))
			{
				return new HashMap<Integer, Double>(this.wordTagProbs.get(lowWord));
			}
		}

		/* OOOVVVV !!!! ^____^!! PANIC !
		 */

		// Using Lexicon from KBBI and KATEGLO
		if (DL != null)
		{
			return DL.usingLexicon(word, OOVWord.tagProbs(word));
		}
		else
		{
			// Just From Affix Tree
			return OOVWord.tagProbs(word);
		}
	}
}
