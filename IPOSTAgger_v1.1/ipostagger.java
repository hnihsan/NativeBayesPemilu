
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
 
 import NLP_ITB.POSTagger.HMM.Train.MainTrainer;
 import NLP_ITB.POSTagger.HMM.Decoder.MainTagger;
 
 import java.io.BufferedReader;
 import java.io.FileReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 
 public class ipostagger
 {
	public static void main(String args[])
	{		
		if (args[0].equals("train"))
		{
			MainTrainer.Train("./resource/Corpus.crp");
			//MainTrainer.Train("UI-1M-tagged.txt");
		}
		else
		{	
		
			/************************
			 * JIKA INGIN MEMBUAT KONFIGURASI MENJADI STATIC, SILAKAN BERMAIN DENGAN KODE DIBAWAH
			 ************************/
			 
			// Mengambil konfigurasi IPOSTAGGER dari Argumen
			// misal :
			// > java ipostagger [namafile corpus-nya] 1 1 0 1 > hasiltag.hsl
			// maka, isi variabel konfigurasi adalah
			// lm = 1, affix = 1, pass2 = 0, lex = 1
			
			int lm 		= Integer.parseInt(args[1]);
			int affix	= Integer.parseInt(args[2]);
			int pass2	= Integer.parseInt(args[3]);
			int lex		= Integer.parseInt(args[4]);
			
			MainTagger mt = new MainTagger("./resource/Lexicon.trn", "./resource/Ngram.trn", lm,
						  3, 3,
						  0, affix,
						  false, 0.2,
						  pass2, 500.0,
						  lex);
			
			//MainTagger mt = new MainTagger("Lexicon.trn", "Ngram.trn", 0);//0:Bigram, 1:Trigram
			
			
			
			/************************
			 * JIKA HANYA FOKUS DI HASIL, SILAKAN BERMAIN DENGAN KODE DIBAWAH
			 * hasil tagging berada di ArrayList ret.
			 ************************/
			
			
			//hasil tagging ada di ArrayList
			ArrayList<String> ret = mt.taggingFile(args[0]);
			
			int i = 0;
			for (i = 0; i < ret.size(); i++)
			{
				System.out.print(ret.get(i)+" ");
				if (ret.get(i).equals("./.")) {
					System.out.print("\n");
				}
			}
			System.out.println("\n");
		}
	}
 }
