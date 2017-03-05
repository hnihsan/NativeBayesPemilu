
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

public abstract class Decoder
{
	protected Model model;
	protected WordProb WH;
	protected NGramProb NG;
	protected double beamFactor;
	protected boolean debug;
	
	public Decoder(Model model, WordProb WH, NGramProb NG, double beamFactor, boolean debug) 
	{
		this.WH = WH;
		this.model = model;
		this.NG = NG;
		this.beamFactor = Math.log(beamFactor);
		this.debug = debug;
	}
	
	//abstract public Sequence backtrack(List<List<?>> tagMatrix, Model model);
	
	//abstract public List<List<?>> viterbi(List<String> sentence);
}
