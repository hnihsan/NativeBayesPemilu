
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
 
public class Sequence
{	
		
	private List<Integer> sequence;
	private double logProb;
	private Map<Integer, String> numberTags;
	
	public Sequence(List<Integer> sequence, double logProb, Model model) 
	{
		this.sequence = sequence;
		this.logProb = logProb;
		this.numberTags = model.getNumberTags();
	}

	public List<String> sequence() 
	{
		List<String> tagSequence = new ArrayList<String>(this.sequence.size());

		for (Integer tagNumber: this.sequence) 
		{
			tagSequence.add(this.numberTags.get(tagNumber));
			//System.out.println(this.numberTags.get(tagNumber)+" ");
		}
		
		return tagSequence;
	}

	public double logProb() 
	{
		return this.logProb;
	}
}