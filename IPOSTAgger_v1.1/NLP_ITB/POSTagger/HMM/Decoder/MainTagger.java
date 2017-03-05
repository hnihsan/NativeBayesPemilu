
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
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
 
public class MainTagger
{	
	//Property
	private Model model;
	private OOVWordProb ovp;
	private DicLexicon dl;
	private WordProb wp;
	private NGramProb np;
	
	private TrigramDecoder td;
	private BigramDecoder bd;
	private BigramSucceedDecoder bsd;
	private TrigramSucceedDecoder tsd;
	
	/*filename of Lexicon file*/
	public String fileLexicon;
	
	/*filename of NGram file*/
	public String fileNGram;
	
	/*maximum affix length for creating affix tree*/
	public int maxAffixLength;
	
	/*Treshold value for prunning affix tree*/
	public int Treshold;
	
	/*minimum word frequency in order for setting the affix tree*/
	public int minWordFreq;
	
	/*0:prefix tree, 1:suffix tree, 2:prefix-suffix tree, 3:BASELINE-NN-without tree*/
	public int modeAffixTree;
	
	/*mode debug*/
	public boolean debug;
	
	/*for Bigram Smoothing, using JELINEC - MERCER*/
	public double LambdaBigram;
	
	/*for beam search decoder*/
	public double beamFactor;
	
	/*Choose the NGram language model = 0:BIGRAM   1:TRIGRAM*/
	public int NGramType;
	
	/*Chooose 2-PHASE HMM type, 0 : not used, 1 : tn-1,tn,tn+1 , 2 : tn-2,tn-1,tn,tn+1*/
	public int TwoPhaseType;
	
	/*Using Lexicon from KBBI or not ?, 0:no 1:yes*/
	public int useLexicon;
	
	//COntstructor
	public MainTagger(String fileLexicon, String fileNGram, int NGramType)
	{
		this.fileLexicon = fileLexicon;
		this.fileNGram   = fileNGram;
		
		//default
		this.maxAffixLength 	= 3;
		this.Treshold 		= 3;
		this.minWordFreq 	= 0;
		//this.Treshold 		= 2000;
		//this.minWordFreq 	= 10;
		this.modeAffixTree	= 0;
		this.debug		= false;
		this.LambdaBigram	= 0.2;
		this.beamFactor		= 500.0;
		this.NGramType		= NGramType;
		this.TwoPhaseType	= 0;
		this.useLexicon		= 0;
		
		this.loadData();
	}
	
	public MainTagger(String fileLexicon, String fileNGram, int NGramType,
			  int maxAffixLength, int Treshold,
			  int minWordFreq, int modeAffixTree,
			  boolean debug, double LambdaBigram,
			  int TwoPhaseType, double beamFactor,
			  int useLexicon)
	{
		this.fileLexicon = fileLexicon;
		this.fileNGram   = fileNGram;
		
		//default
		this.maxAffixLength 	= maxAffixLength;
		this.Treshold 		= Treshold;
		this.minWordFreq 	= minWordFreq;
		this.modeAffixTree	= modeAffixTree;
		this.debug		= debug;
		this.LambdaBigram	= LambdaBigram;
		this.beamFactor		= beamFactor;
		this.NGramType		= NGramType;
		this.TwoPhaseType	= TwoPhaseType;
		this.useLexicon 	= useLexicon;
		
		this.loadData();
	}
	
	private void loadData()
	{
		//load data
		try 
		{
			this.model = new Model(new BufferedReader(new FileReader(this.fileLexicon)), 
						new BufferedReader(new FileReader(this.fileNGram)));
			
			this.dl = null;						
			if (this.useLexicon != 0) {						
				this.dl = new DicLexicon("./resource/inlex.txt", "./resource/cattable.txt", model.getNumberTags());
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Training file doesn't exist !");
			e.printStackTrace();
			System.exit(1);
		}
		
		//for OOV
		this.ovp = null;
		if (this.modeAffixTree != 3) {  		
		this.ovp = new OOVWordProb(this.model.getLexicon(), this.model.getUniGrams(), 
					   this.maxAffixLength, this.Treshold, this.minWordFreq, 
					   this.modeAffixTree, this.debug);
		}
		
		//for words in corpus
		this.wp = new KnownWordProb(model.getLexicon(), model.getUniGrams(), model.getNumberTags(), this.ovp, this.dl, this.debug);

		
		//language model, transition probability
		this.np = new NGramProb(model.getUniGrams(), model.getBiGrams(), 
					model.getTriGrams(), model.getQuatoGrams(), this.LambdaBigram);
		
		if (this.NGramType == 1) {
			this.td = new TrigramDecoder(this.model, this.wp, this.np, this.beamFactor, this.debug);
		} else {
			this.bd = new BigramDecoder(this.model, this.wp, this.np, this.beamFactor, this.debug);
		}
		
		if (this.TwoPhaseType == 1) {
			this.bsd = new BigramSucceedDecoder(this.model, this.wp, this.np, this.beamFactor, this.debug);
		} else if (this.TwoPhaseType == 2) {
			this.tsd = new TrigramSucceedDecoder(this.model, this.wp, this.np, this.beamFactor, this.debug);
		}
	}
	
	/**
	 * This method is used for tagging the input string
	 **/
	public ArrayList<String> taggingStr(String str)
	{
		ArrayList<String> temp = new ArrayList<String>();			
		ArrayList<String> ret = new ArrayList<String>();
		byte[] input = str.getBytes();
		int n = 0;
		
		while (n < input.length) 
		{
			String line = "";
			// very risky...this part of code....
			while ((n < input.length) && ((char)input[n] != '\n')) 
			{
				line = line + String.valueOf((char)input[n]);
				n++;
			}
			
			String tokens[] = line.split("\\s+");
			List<String> tokenList = new ArrayList<String>(Arrays.asList(tokens));
			
			//assumption : there is no sentence..just consist of 1 word
			if ((tokens.length == 1)) {
				n++;
				continue;
			}
			
			tokenList.add(0, "<STARTTAG>");
			tokenList.add(0, "<STARTTAG>");
			tokenList.add("<ENDTAG>");
			
			List<String> tags = null;
			if (this.NGramType == 1) 
			{
				tags = this.td.backtrack(this.td.viterbi(tokenList), this.model).sequence();
			} else 
			{
				tags = this.bd.backtrack(this.bd.viterbi(tokenList), this.model).sequence();
			}
			
			
			//dari 2 - ke tag.size() - 1, karena awalnya diisi dengan <STARTTAG> dua kali
			 
			int i;
			int j = 2;
			List<String> temp2 = new ArrayList<String>();
			for (i = 1; i < tags.size()-1; i++)
			{
				if (!tags.get(i).equals("<STARTTAG>") && !tags.get(i).equals("<ENDTAG>"))
				{
					temp.add(tokenList.get(j)+"/"+tags.get(i));
					temp2.add(tokenList.get(j)+"/"+tags.get(i));
					j++;
				}
			}
			
			//Check if using 2 phase
			if (this.TwoPhaseType >= 1)
			{
				ArrayList<String> p = this.tagging2Phase(temp2);
				for (int k = 0; k < p.size(); k++) {
					ret.add(p.get(k));
				}
			}
			
			n++;
		}
		
		if (this.TwoPhaseType == 0)
			ret = temp;
		
		return ret;
	}
	
	/**
	 * This method is used for tagging the content of the file
	 **/
	public ArrayList<String> taggingFile(String tagFile) 
	{	
		try 
		{
			BufferedReader reader =  new BufferedReader(new FileReader(tagFile));
			String line = null;
			ArrayList<String> temp = new ArrayList<String>();			
			ArrayList<String> ret = new ArrayList<String>();			
			
			while ((line = reader.readLine()) != null) 
			{
				String tokens[] = line.split("\\s+");
				List<String> tokenList = new ArrayList<String>(Arrays.asList(tokens));
				
				//assumption : there is no sentence..just consist of 1 word
				if ((tokens.length == 1))
					continue;
				
				tokenList.add(0, "<STARTTAG>");
				tokenList.add(0, "<STARTTAG>");
				tokenList.add("<ENDTAG>");
				
				List<String> tags = null;
				if (this.NGramType == 1) 
				{
					tags = this.td.backtrack(this.td.viterbi(tokenList), this.model).sequence();
				} else 
				{
					tags = this.bd.backtrack(this.bd.viterbi(tokenList), this.model).sequence();
				}
				
				
				//dari 2 - ke tag.size() - 1, karena awalnya diisi dengan <STARTTAG> dua kali
				 
				int i;
				int j = 2;
				List<String> temp2 = new ArrayList<String>();
				for (i = 1; i < tags.size()-1; i++)
				{
					if (!tags.get(i).equals("<STARTTAG>") && !tags.get(i).equals("<ENDTAG>"))
					{
						temp.add(tokenList.get(j)+"/"+tags.get(i));
						temp2.add(tokenList.get(j)+"/"+tags.get(i));
						j++;
					}
				}
				
				//Check if using 2 phase
				if (this.TwoPhaseType >= 1)
				{
					ArrayList<String> p = this.tagging2Phase(temp2);
					for (int k = 0; k < p.size(); k++) {
						ret.add(p.get(k));
					}
				}
			}
			
			// MEncoba menampilkan pohon AFFIX
			// System.out.println("\n pohon : \n");
			// ovp.getCARPrefixTree().viewAffixTree();
			// ovp.getUPSuffixTree().viewAffixTree();
			
			if (this.TwoPhaseType == 0)
				ret = temp;
				
			return ret;
		} 
		catch (IOException e) 
		{
			return null;
		}

	}
	
	/**
	 * this method is used for tagging tagged file
	 * usually for testing...
	 **/
	public ArrayList<String> taggingTaggedFile(String tagFile) 
	{	
		try 
		{
			BufferedReader reader =  new BufferedReader(new FileReader(tagFile));
			String line = null;
			ArrayList<String> temp = new ArrayList<String>();			
			ArrayList<String> ret = new ArrayList<String>();
			
			while ((line = reader.readLine()) != null) 
			{
				String tokens[] = line.split("\\s+");
				
				//assumption : there is no sentence..just consist of 1 word
				if ((tokens.length == 1))
					continue;
				
				//separate the POS tag
				for (int j = 0; j < tokens.length; j++)
				{
					int sepIndex = tokens[j].lastIndexOf('/');
					tokens[j] = tokens[j].substring(0, sepIndex);
				}
				
				List<String> tokenList = new ArrayList<String>(Arrays.asList(tokens));
				
				tokenList.add(0, "<STARTTAG>");
				tokenList.add(0, "<STARTTAG>");
				tokenList.add("<ENDTAG>");
				
				List<String> tags = null;
				if (this.NGramType == 1) 
				{
					tags = this.td.backtrack(this.td.viterbi(tokenList), this.model).sequence();
				} else 
				{
					tags = this.bd.backtrack(this.bd.viterbi(tokenList), this.model).sequence();
				}
				
				
				//dari 2 - ke tag.size() - 1, karena awalnya diisi dengan <STARTTAG> dua kali
				 
				int i;
				int j = 2;
				List<String> temp2 = new ArrayList<String>();
				for (i = 1; i < tags.size()-1; i++)
				{
					if (!tags.get(i).equals("<STARTTAG>") && !tags.get(i).equals("<ENDTAG>"))
					{
						//System.out.print(tokenList.get(j)+"/"+tags.get(i)+" ");
						temp.add(tokenList.get(j)+"/"+tags.get(i));
						temp2.add(tokenList.get(j)+"/"+tags.get(i));
						j++;
					}
				}
				//System.out.println("");
				
				//Check if using 2 phase
				if (this.TwoPhaseType >= 1)
				{
					ArrayList<String> p = this.tagging2Phase(temp2);
					for (int k = 0; k < p.size(); k++) {
						ret.add(p.get(k));
					}
				}
			}
			
			if (this.td != null) {
				//System.out.println("Jumlah TRIGRAM :"+this.td.jumTrigram);
				//System.out.println("Jumlah TRIGRAM OOV:"+this.td.jumTrigramOOV);
			} else {
				//System.out.println("Jumlah BIGRAM :"+this.bd.jumBigram);
				//System.out.println("Jumlah BIGRAM OOV:"+this.bd.jumBigramOOV);
			}
			
			// MEncoba menampilkan pohon AFFIX
			// System.out.println("\n pohon : \n");
			// ovp.getLOWPrefixTree().viewAffixTree();
			// ovp.getLOWSuffixTree().viewAffixTree();
			//System.out.println("jum access lexicon : "+this.dl.jumAccess);
			
			if (this.TwoPhaseType == 0)
				ret = temp;
				
			return ret;
		} 
		catch (IOException e) 
		{
			return null;
		}

	}
	
	/**
	 * FOR DEBUGGER
	 **/
	
	public boolean isOOV(String word)
	{
		return this.wp.isOOV(word);
	}
	
	/////for 2 PHASE HMM
	private ArrayList<String> tagging2Phase(List<String> input)
	{
		List<String> tokenList = input;
		ArrayList<String> ret = new ArrayList<String>();
				
		tokenList.add(0, "<STARTTAG>");
		tokenList.add(0, "<STARTTAG>");
		tokenList.add("<ENDTAG>/<ENDTAG>");
		
		List<String> tags = null;
		if (this.TwoPhaseType == 1) 
		{
			tags = this.bsd.backtrack(this.bsd.viterbi(tokenList), this.model).sequence();
		} else if (this.TwoPhaseType == 2)
		{
			tags = this.tsd.backtrack(this.tsd.viterbi(tokenList), this.model).sequence();
		}
		
		int i;
		int j = 2;
		for (i = 1; i < tags.size()-1; i++)
		{
			if (!tags.get(i).equals("<STARTTAG>") && !tags.get(i).equals("<ENDTAG>"))
			{
				int sepIndex = tokenList.get(j).lastIndexOf('/');
				String token = tokenList.get(j).substring(0, sepIndex);
				ret.add(token+"/"+tags.get(i));
				j++;
			}
		}
		
		return ret;
	}
}

