
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
import java.util.regex.Pattern;

public class OOVWordProb extends WordProb
{
	//contstructor
	//mode: 0=prefix, 1=suffix, 2=prefix-suffix
	public OOVWordProb(Map<String, Map<Integer, Integer>> lexicon, Map<UniGram, Integer> uniGrams, 
				int maxAffixLength, int Treshold, int minWordFreq, int mode, boolean debug)
	{
		super(debug);
		
		this.mode = mode;
		this.UPsuffixTree = new AffixTree(uniGrams, Treshold, maxAffixLength);
		this.UPprefixTree = new AffixTree(uniGrams, Treshold, maxAffixLength);
		this.LOWsuffixTree = new AffixTree(uniGrams, Treshold, maxAffixLength);
		this.LOWprefixTree = new AffixTree(uniGrams, Treshold, maxAffixLength);
		this.CARprefixTree = new AffixTree(uniGrams, Treshold, maxAffixLength);
		
		for (Entry<String, Map<Integer, Integer>> wordEntry: lexicon.entrySet()) 
		{
			String word = wordEntry.getKey();
			int wordFreq = 0;
			
			// prosesnya adalah memanggil addWord
			// untuk tiap word pake prefix dan suffix-nya.
			if (word.length() == 0 || word.equals("<STARTTAG>") || word.equals("<ENDTAG>"))
			{
				continue;
			}
			
			// dapetin jumlah kata
			for (Entry<Integer, Integer> tagEntry: wordEntry.getValue().entrySet())
			{
				wordFreq += tagEntry.getValue();
			}
			
			// hanya untuk jumlah kata tertentu saja : memperkecil pohon
			// dengan membuang kata yang Freq-nya kecil/tidak begitu pengaruh
			// membantu sistem prunning
			AffixTree suffixtree = null;
			AffixTree prefixtree = null;

			if (cardinalPattern.matcher(word).matches()) 
			{
				prefixtree = this.CARprefixTree;
			}
			else 
			{
				boolean isUpper = Character.isUpperCase(word.charAt(0));
				
				if (mode == 1 || mode == 2)
					suffixtree = isUpper ? this.UPsuffixTree : this.LOWsuffixTree;
				if (mode == 0 || mode == 2)
					prefixtree = isUpper ? this.UPprefixTree : this.LOWprefixTree;
			}
			
			if (wordFreq > minWordFreq)
			{
				if (suffixtree != null)
					suffixtree.addWord(word, wordEntry.getValue());
				if (prefixtree != null)
					prefixtree.addWord(this.reverseWord(word), wordEntry.getValue());
			}

		}
		
		//Prunning Tree
		if (this.UPsuffixTree != null)
			this.UPsuffixTree.Prunning();
		if (this.UPprefixTree != null)
			this.UPprefixTree.Prunning();
		if (this.LOWsuffixTree != null)
			this.LOWsuffixTree.Prunning();
		if (this.LOWprefixTree != null)
			this.LOWprefixTree.Prunning();
		if (this.CARprefixTree != null)
			this.CARprefixTree.Prunning();
	}

	//Properti
	private int mode;
	private AffixTree UPsuffixTree;
	private AffixTree UPprefixTree;
	private AffixTree LOWsuffixTree;
	private AffixTree LOWprefixTree;
	private AffixTree CARprefixTree;
	private final static Pattern cardinalPattern =
		Pattern.compile("^([0-9]+)|([0-9]+\\.)|([0-9.,:-]+[0-9]+)|([a-zA-Z]{1,5}+[.,:-]+[0-9]+)|([0-9]+[a-zA-Z]{1,3})$");
	
	//method
	public AffixTree getUPSuffixTree()
	{
		return this.UPsuffixTree;
	}
	
	public AffixTree getUPPrefixTree()
	{
		return this.UPprefixTree;
	}
	
	public AffixTree getLOWSuffixTree()
	{
		return this.LOWsuffixTree;
	}
	
	public AffixTree getLOWPrefixTree()
	{
		return this.LOWprefixTree;
	}
	
	public AffixTree getCARPrefixTree()
	{
		return this.CARprefixTree;
	}
	
	public Map<Integer, Double> tagProbs(String word) 
	{
		//combine prefixTree and suffixTree
		//...
		//sementara suffix dulu
		AffixTree suffixtree = null;
		AffixTree prefixtree = null;
		Map<Integer, Double> preProbVec = null;
		Map<Integer, Double> sufProbVec = null;

		if (this.cardinalPattern.matcher(word).matches()) 
		{
			prefixtree = this.CARprefixTree;
			preProbVec = prefixtree.affixTagProbs(word);
		}
		else 
		{
			boolean isUpper = Character.isUpperCase(word.charAt(0));
			
			if (mode == 1 || mode == 2) {
				suffixtree = isUpper ? this.UPsuffixTree : this.LOWsuffixTree;
				sufProbVec = suffixtree.affixTagProbs(word);
			}
			if (mode == 0 || mode == 2) {
				prefixtree = isUpper ? this.UPprefixTree : this.LOWprefixTree;
				preProbVec = prefixtree.affixTagProbs(this.reverseWord(word));
			}
		}
		

		if (suffixtree != null && prefixtree != null)
		{
			return this.CombinePreSuff(preProbVec, sufProbVec);
		}
		else if (suffixtree == null)
		{
			return this.ChangeIntoLog(preProbVec);
		}
		else
		{
			return this.ChangeIntoLog(sufProbVec);
		} 
	}
	
	protected Map<Integer, Double> CombinePreSuff(Map<Integer, Double> prefix, Map<Integer, Double> suffix)
	{
		// Combinenya di-tambah
		Map<Integer, Double> combine = new HashMap<Integer, Double>();
		for (Entry<Integer, Double> e1: prefix.entrySet())
		{
			combine.put(e1.getKey(), e1.getValue());
		}
		for (Entry<Integer, Double> e2: suffix.entrySet())
		{
			if (!combine.containsKey(e2.getKey()))
				combine.put(e2.getKey(), 0.0);
			combine.put(e2.getKey(), combine.get(e2.getKey())+e2.getValue());
		}
		return this.Normalizing(combine);
	}
	
	private Map<Integer, Double> Normalizing(Map<Integer, Double> v)
	{
		double total = 0;
		for (Entry<Integer, Double> entry: v.entrySet())
		{
			total += entry.getValue();
		}
		for (Entry<Integer, Double> entry: v.entrySet())
		{
			v.put(entry.getKey(), Math.log((double)entry.getValue()/(double)total));	
		}
		return v;
	}
	
	private Map<Integer, Double> ChangeIntoLog(Map<Integer, Double> v)
	{
		for (Entry<Integer, Double> entry: v.entrySet())
		{
			v.put(entry.getKey(), Math.log(entry.getValue()));	
		}
		return v;
	}
	
	private String reverseWord(String str) 
	{
		char[] reverseChars = new char[str.length()];
		
		for (int i = 0; i < str.length(); ++i) 
		{
			reverseChars[i] = str.charAt(str.length() - (1 + i));
		}
		
		return new String(reverseChars);
	}
}

