
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

public class AffixTree {
	
	//INNER CLASS as Tree Node
	private class node 
	{
		
		// Property
		private final Map<Character, node> childs;
		private final Map<Integer, Integer> tagFreq;
		private int totalTagFreq;
		private double IG;
		private boolean deleted;
		
		//constructor
		public node () 
		{
			this.childs = new HashMap<Character, node>();
			this.tagFreq = new HashMap<Integer, Integer>();
			this.totalTagFreq = 0;
			this.deleted = false;
		}
		
		//method
		public boolean getDeleted()
		{
			return this.deleted;
		}
		
		public void setDeleted(boolean n)
		{
			this.deleted = n;
		}
		
		public double getIG()
		{
			return this.IG;
		}
		
		public void setIG(double n)
		{
			this.IG = n;
		}
		
		public Map<Integer, Integer> getTagFreq()
		{
			return this.tagFreq;
		}
		
		public int getTotalTagFreq()
		{
			return this.totalTagFreq;
		}
		
		public Map<Character, node> getChilds()
		{
			return this.childs;
		}
		
		public void addAffix(String reverseAffix, Map<Integer, Integer> tagFreqs) 
		{
		
		// process to input one affix into the tree
		// example for action : reveseSUffix : noitca
		// tagFreqs : tagFreqsnya 'action'
		// tagFreqs => tagFreq dari word, bukan keseluruhan
		
			// Add the tag frequencies to the current state/node.
			for (Entry<Integer, Integer> entry: tagFreqs.entrySet()) 
			{
				Integer tag = entry.getKey();
				int tagFrequency = entry.getValue();
			
				if (!this.tagFreq.containsKey(tag))
				{
					this.tagFreq.put(tag, tagFrequency);
				}
				else
				{
					this.tagFreq.put(tag, this.tagFreq.get(tag) + tagFrequency);
				}

				this.totalTagFreq += tagFrequency;
			}
			
			// If the suffix is fully processed, we reached the final state for
			// this suffix.
			if (reverseAffix.length() == 0) 
			{
				return;
			}

			// Add transition.
			Character transitionChar = reverseAffix.charAt(0);
			
			if (!this.childs.containsKey(transitionChar)) 
			{
				this.childs.put(transitionChar, new node());
			}
			
			this.childs.get(transitionChar).addAffix(reverseAffix.substring(1), tagFreqs);
		}
		
		public Map<Integer, Double> affixTagProbs(String reverseAffix, Map<Integer, Double> tagProbs) 
		{
		
			// return TagProbs of particular affix
			// search in the tree
			return new HashMap<Integer, Double>();			
		}
		
		//method yang lain Prunning, dll di main class
		
	}
	//AKHIR INNER CLASS
	
	//property of main class
	private Map<UniGram, Integer> uniGrams;
	private final node Root;
	private int maxAffixLength;
	private int Treshold;
	
	//constructor of main class
	public AffixTree(Map<UniGram, Integer> uniGrams, int Treshold, int maxAffixLength) 
	{
		this.Root = new node();
		this.maxAffixLength = maxAffixLength;
		this.Treshold = Treshold;
		this.uniGrams = new HashMap<UniGram, Integer>(uniGrams);
		
		// set Tag Fred in ROOT
		// Character in ROOT = # , abstractly
		/*
		for (Entry<UniGram, Integer> uniGramFreq: this.uniGrams.entrySet())
		{
			this.Root.tagFreq.put(uniGramFreq.getKey().t1(), uniGramFreq.getValue());
			this.Root.totalTagFreq += uniGramFreq.getValue();
		}
		*/
	}
	
	public node getRoot()
	{
		return this.Root;
	}
	
	public void addWord(String word, Map<Integer, Integer> tagFreqs) 
	{
		// tagFreqs => tagFreq dari word, bukan keseluruhan
		String reverseWord = reverseWord(word);
	
		if (reverseWord.length() > this.maxAffixLength) 
		{
			reverseWord = reverseWord.substring(0, this.maxAffixLength);
		}
		
		//please put the reverse Affix
		this.Root.addAffix(reverseWord, tagFreqs);
	}
	
	// buat mengambil tagProbs dengan masukan sebuah Kata
	public Map<Integer, Double> affixTagProbs(String word) 
	{
		String reverseWord = reverseWord(word);
		
		if (reverseWord.length() > this.maxAffixLength)
		{
			reverseWord = reverseWord.substring(0, this.maxAffixLength);
		}
					
		//Invoking affixTagProbsRecc
		//...
		//System.out.println("+++++++"+this.Root.getTotalTagFreq());
		//if frequency at ROOT = 0
		if (this.Root.getTotalTagFreq() == 0)
		{
			System.out.println("Error: no tree constructed, you should decrease parameter \"minWordFreq\"");
			System.exit(1);
		}
		
		return affixTagProbsRecc(reverseWord, this.Root);
	}
	
	// reccursive affixTagProbs
	private Map<Integer, Double> affixTagProbsRecc(String reverseAffix, node n)
	{
		Character transitionChar = null;
		boolean childExist = false;
		boolean childExistAndCharInDeleted = false;
		boolean isDeletedAreaExist = false;
		int jumFreqDeleted = 0;
		Map<Integer, Integer>tagFreqDeleted = new HashMap<Integer, Integer>();
		
		//System.out.println("=="+reverseAffix);
		if (reverseAffix.length() != 0)
		{
			transitionChar = reverseAffix.charAt(0);
			
			for (Entry<Character, node> entry: n.getChilds().entrySet())
			{
				node temp = entry.getValue();
				Character c = entry.getKey();
				if (temp.getDeleted() == false) 
					childExist = true;
				if (temp.getDeleted() == true && transitionChar.equals(c))
					childExistAndCharInDeleted = true;
				if (temp.getDeleted() == true) { 
					isDeletedAreaExist = true;
					jumFreqDeleted += temp.getTotalTagFreq();
			
					// Summation of all deleted nodes(probability vector)
					for (Entry<Integer, Integer> e: temp.getTagFreq().entrySet()) {
						if (!tagFreqDeleted.containsKey(e.getKey())) {
							tagFreqDeleted.put(e.getKey(), 0);
						}	
						tagFreqDeleted.put(e.getKey(), tagFreqDeleted.get(e.getKey())+e.getValue());
					}
				}
			}
		}

		if (!childExist || reverseAffix.length() == 0) //leaf node || all childs are deleted !
		{
			//return the probability vector
			//System.out.println("--- : "+n.getTotalTagFreq());
			//System.out.println("-1-");
			//System.out.println("tc : "+transitionChar);
			return this.changeToTagProb(n.getTagFreq(), n.getTotalTagFreq());
		}
		else // child exist
		{
			if (!n.getChilds().containsKey(transitionChar) || childExistAndCharInDeleted)
			{
				//cek apakah ada daerah deleted / tidak ?
				if (isDeletedAreaExist) {
					// kembalikan vector deleted area
					//System.out.println("-2-");
					return this.changeToTagProb(tagFreqDeleted, jumFreqDeleted);
				} else {
					// kembalikan vector default
					// sementara kembalikan vector yang ada di node ini			
					//System.out.println("-3-");
					return this.changeToTagProb(n.getTagFreq(), n.getTotalTagFreq());
				}
			}
			else
			{
				//System.out.println("--- : "+n.getTotalTagFreq());
				//System.out.println("-4-");
				//System.out.println("tc : "+transitionChar);
				return affixTagProbsRecc(reverseAffix.substring(1), n.getChilds().get(transitionChar));
			}
		}
	}
	
	//tambahin method untuk Prunning
	public void Prunning()
	{
		// asumsi in the root : IM = ~
		PrunningRecc(this.Root, Double.POSITIVE_INFINITY);
	}
	
	private void PrunningRecc(node n, double IMparent)
	{
		double IM = this.computeIM(n.getTagFreq(), n.getTotalTagFreq());
		boolean childDeletedAll = true;
		
		//untuk tiap anak
		for (Entry<Character, node> entry: n.getChilds().entrySet())
		{
			PrunningRecc(entry.getValue(), IM);
			childDeletedAll = childDeletedAll && entry.getValue().getDeleted();
		}
		
		// compute information gain
		double IG = n.getTotalTagFreq()*(IMparent - IM);
		n.setIG(IG);
		
		if (IG < this.Treshold)
		{
			//hitung anak yang masih ada/belum di delete
			if (n.getChilds().size() == 0 || childDeletedAll) //LEAF node | children have been deleted
			{
				n.setDeleted(true);
			}
		}
	}
	
	
	//method untuk menghitung Information Measure
	private double computeIM(Map<Integer, Integer> tagFreq, int totalFreq)
	{
		double i = 0;
		
		for (Entry<Integer, Integer> entry: tagFreq.entrySet())
		{
			double temp = entry.getValue()/(double)totalFreq;
			//i += (temp) * Math.log(temp);
			i += (temp) * this.log2(temp);
		}
		
		return -1*i;
	}
	
	//method untuk mengembalikan Tag-Prob dari Tag-Freq
	private Map<Integer, Double> changeToTagProb(Map<Integer, Integer> tagFreq, int totalFreq)
	{
		Map<Integer, Double> TagProb = new HashMap<Integer, Double>();
		
		for (Entry<Integer, Integer> entry: tagFreq.entrySet())
		{
			double prob = entry.getValue()/(double)totalFreq;
			TagProb.put(entry.getKey(), prob);
			//System.out.println("======="+entry.getKey()+" "+prob);
		}		
		
		return TagProb;
	}
	
	//utility Log based 2
	private double log2(double x) 
	{
		double hasil = Math.log(x)/Math.log(2);
		return hasil;
	}
	
	//method buat tampilin affix tree
	public void viewAffixTree()
	{
		System.out.println("#");
		viewAffixTreeRecc(this.Root, 3);	
	}
	
	private void viewAffixTreeRecc(node n, int indentasi)
	{
		int i;
		
		for (Entry<Character, node> entry: n.getChilds().entrySet()) 
		{
			if (!entry.getValue().getDeleted())
			{
				// write indentasi
				for (i=1; i<=indentasi; i++)
				{
					System.out.print(" ");
				}
			
				// write node
				System.out.print(entry.getKey()+"|"+entry.getValue().getIG()+"|"+entry.getValue().getDeleted());
				for (Entry<Integer, Integer> e: entry.getValue().getTagFreq().entrySet()) {
					System.out.print("+"+e.getKey()+":"+e.getValue()+"|");
				}
				System.out.println("="+entry.getValue().getTotalTagFreq()+"");
			}
			
			// reccurens
			viewAffixTreeRecc(entry.getValue(), 3+indentasi);
		}
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

