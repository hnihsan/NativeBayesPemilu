
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
 
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
public class CorpusReaderSatu extends AbsCorpusReader<TaggedWord>
{
	public CorpusReaderSatu(List<TaggedWord> startMarkers, List<TaggedWord> endMarkers, TrainHandler TH) 
	{
		super(startMarkers, endMarkers, TH);
	}
		
	public void parse(BufferedReader reader) throws IOException, CorpusReaderException 
	{
		String line = null;
		//int j = 0;
		
		while ((line = reader.readLine()) != null) 
		{
			line = line.trim();

			//System.out.print(j+" ");
			//j++;
			
			if (line.length() == 0)
			{
				continue;
			}
			
			List<TaggedWord> sentence = new ArrayList<TaggedWord>(this.startMarkers);
			
			String[] lineParts = line.split("\\s+");
			for (int i = 0; i < lineParts.length; ++i) 
			{
				String wordTag = lineParts[i];
			
				int sepIndex = wordTag.lastIndexOf('/');
				
				if (sepIndex == -1)
				{
					//continue;
					throw new CorpusReaderException("Tag is missing in '" + wordTag + "'", CorpusReaderException.CorpusReadError.MISSING_TAG);
				}

				String word = wordTag.substring(0, sepIndex);
				String tag = wordTag.substring(sepIndex + 1);

				if (word.length() == 0)
				{
					throw new CorpusReaderException("Zero-length word in '" + wordTag + "'", CorpusReaderException.CorpusReadError.ZERO_LENGTH_WORD);
				}
				
				//Always decapitalize FIRST, Walaupun ada beberapa kata awal yang memang
				//huruf awalnya besar, tapi lebih banyak yang tidak.
				if (i == 0) 
				{
					word = Utility.replaceCharAt(word, 0, Character.toLowerCase(word.charAt(0)));
				}
				
				sentence.add(new TaggedWord(word, tag));				
			}
			
			sentence.addAll(this.endMarkers);

			// jangan dimodify !!
			// make immutable;
			sentence = Collections.unmodifiableList(sentence);

			// Call handlers.
			this.sentenceHandler.handleSentence(sentence);
		}
	}
}
