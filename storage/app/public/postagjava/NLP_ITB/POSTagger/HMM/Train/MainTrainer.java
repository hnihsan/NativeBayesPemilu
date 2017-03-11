
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
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MainTrainer 
{
	private static void writeNGrams(Map<String, Integer> uniGrams, 
									Map<String, Integer> biGrams, 
									Map<String, Integer> triGrams,
									Map<String, Integer> quatoGrams,
									BufferedWriter writer) throws IOException 
	{
			
		for (Entry<String, Integer> entry: uniGrams.entrySet())
		{
			writer.write(entry.getKey() + " " + entry.getValue() + "\n");
		}
		
		for (Entry<String, Integer> entry: biGrams.entrySet())
		{
			writer.write(entry.getKey() + " " + entry.getValue() + "\n");
		}

		for (Entry<String, Integer> entry: triGrams.entrySet())
		{
			writer.write(entry.getKey() + " " + entry.getValue() + "\n");
		}
		
		for (Entry<String, Integer> entry: quatoGrams.entrySet())
		{
			writer.write(entry.getKey() + " " + entry.getValue() + "\n");
		}

		writer.flush();
	}
	
	private static void writeLexicon(Map<String, Map<String, Integer>> lexicon,
									 BufferedWriter writer) throws IOException 
	{
	
		for (Entry<String, Map<String, Integer>> wordEntry: lexicon.entrySet()) 
		{
			String word = wordEntry.getKey();
			
			writer.write(word);
			
			for (Entry<String, Integer> tagEntry: lexicon.get(word).entrySet()) 
			{
				writer.write(" ");
				writer.write(tagEntry.getKey());
				writer.write(" ");
				writer.write(tagEntry.getValue().toString());
			}
			
			writer.newLine();
		}
		
		writer.flush();
	}
		
	public static void Train(String corpus) {
		
		List<TaggedWord> startMarkers = new ArrayList<TaggedWord>();
		startMarkers.add(new TaggedWord("<STARTTAG>", "<STARTTAG>"));
		startMarkers.add(new TaggedWord("<STARTTAG>", "<STARTTAG>"));
		List<TaggedWord> endMarkers = new ArrayList<TaggedWord>();
		endMarkers.add(new TaggedWord("<ENDTAG>", "<ENDTAG>"));
		
		TrainHandler trainHandler = new TrainHandler();
		AbsCorpusReader<TaggedWord> corpusReader = new CorpusReaderSatu(startMarkers, endMarkers, trainHandler);

		try 
		{
			corpusReader.parse(new BufferedReader(new FileReader(corpus)));
		} 
		catch (IOException e) 
		{
			System.out.println("Could not read corpus!");
			e.printStackTrace();
			System.exit(1);
		} 
		catch (CorpusReaderException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}

		try 
		{
			writeLexicon(trainHandler.getLexicon(), new BufferedWriter(new FileWriter("./resource/Lexicon.trn")));
			writeNGrams(trainHandler.getUnigram(), trainHandler.getBigram(),
				    trainHandler.getTrigram(), trainHandler.getQuatogram(),
				    new BufferedWriter(new FileWriter("./resource/Ngram.trn")));
		}
		catch (IOException e) 
		{
			// Error IO..
			System.out.println("System Can not write training data!");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
