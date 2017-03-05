
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
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class AbsCorpusReader<TaggedWord>
{
	protected TrainHandler sentenceHandler;
	protected List<TaggedWord> startMarkers;
	protected List<TaggedWord> endMarkers;
	
	public AbsCorpusReader(List<TaggedWord> startMarkers, List<TaggedWord> endMarkers, TrainHandler TH) 
	{
		this.startMarkers = new ArrayList<TaggedWord>(startMarkers);
		this.endMarkers = new ArrayList<TaggedWord>(endMarkers);
		this.sentenceHandler = TH;
	}
	
	abstract public void parse(BufferedReader reader) throws IOException, CorpusReaderException;
}